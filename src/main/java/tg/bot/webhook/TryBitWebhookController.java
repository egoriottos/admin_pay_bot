package tg.bot.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
import org.telegram.telegrambots.meta.api.objects.ChatInviteLink;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.payment.Payment;
import tg.bot.payment.PaymentConfirmationService;
import tg.bot.payment.PaymentRepository;
import tg.bot.payment.PaymentStatus;
import tg.bot.subscription.SubscriptionService;
import tg.bot.telegram.callback.interfaces.TelegramBotClient;
import tg.bot.telegram.keyboard.KeyboardFactory;
import tg.bot.telegram.message.MessageFactory;
import tg.bot.telegram.sender.TelegramSender;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TryBitWebhookController {
  @Value("${trybit.secret-key}")
  private String secretKey;

  private final PaymentRepository paymentRepository;
  private final MessageFactory messageFactory;
  private final TelegramSender sender;
  private final ObjectMapper objectMapper;
  private final TelegramBotClient telegramClient;
  private final PaymentConfirmationService paymentConfirmationService;
  private final KeyboardFactory keyboardFactory;

  public static final String ALGORITHM = "HmacSHA256";

  @PostMapping("/trybit/webhook")
  public ResponseEntity<Void> handle(@RequestBody String rawBody) {
    log.info("Trybit webhook received: {}", rawBody);
    try {
      return processWebhook(rawBody);
    } catch (Exception e) {
      log.error("Ошибка обработки Trybit webhook: {}", rawBody, e);
      return ResponseEntity.ok().build();
    }
  }

  private ResponseEntity<Void> processWebhook(String rawBody) throws Exception {
    JsonNode json = objectMapper.readTree(rawBody);

    String status = textOrNull(json, "status");
    String invoiceId = textOrNull(json, "invoice_id");
    String token = textOrNull(json, "token");

    if (!"success".equalsIgnoreCase(status)) {
      log.info("Webhook status != success ({}), invoiceId={}, игнорируем", status, invoiceId);
      return ResponseEntity.ok().build();
    }

    if (!isValidToken(token)) {
      log.warn("Невалидная подпись токена для invoiceId={}", invoiceId);
      return ResponseEntity.status(403).build();
    }

    Payment payment =
        paymentRepository
            .findByInvoiceId(invoiceId)
            .orElseThrow(() -> new IllegalStateException("Unknown invoice: " + invoiceId));

    if (payment.getStatus() == PaymentStatus.PAID) {
      log.info("invoiceId={} уже был обработан ранее, пропускаем", invoiceId);
      return ResponseEntity.ok().build();
    }

    JsonNode invoiceInfo = json.get("invoice_info");
    String innerStatus = invoiceInfo != null ? textOrNull(invoiceInfo, "status") : null;
    if (innerStatus != null
        && !"paid".equalsIgnoreCase(innerStatus)
        && !"overpaid".equalsIgnoreCase(innerStatus)) {
      log.info(
          "invoiceId={}: invoice_info.status={} - не полная оплата, ждём следующий постбэк",
          invoiceId,
          innerStatus);
      return ResponseEntity.ok().build();
    }

    PaymentConfirmationService.PaidResult result = paymentConfirmationService.confirm(payment);

    try {
      String inviteLink = createTempInviteLink(result.channelChatId());
      sender.editMessage(
          result.chatId(),
          result.messageId(),
          messageFactory.paymentSuccess(result.language(), result.channelName()),
          keyboardFactory.channelAccess(result.language(), inviteLink));
      log.info("invoiceId={}: инвайт создан и сообщение обновлено успешно", invoiceId);
    } catch (TelegramApiException e) {
      log.error(
          "invoiceId={}: не удалось создать инвайт или обновить сообщение, chatId={}",
          invoiceId,
          result.channelChatId(),
          e);
      try {
        sender.sendMessage(
            result.telegramUserId(),
            "Оплата подтверждена, но не удалось выдать ссылку на канал автоматически. "
                + "Напишите в поддержку — мы выдадим доступ вручную.",
            null);
      } catch (Exception inner) {
        log.error("invoiceId={}: даже фолбэк-сообщение не отправилось", invoiceId, inner);
      }
    }

    return ResponseEntity.ok().build();
  }

  private String textOrNull(JsonNode node, String field) {
    JsonNode v = node.get(field);
    return (v == null || v.isNull()) ? null : v.asText();
  }

  private String createTempInviteLink(String chatId) throws TelegramApiException {
    CreateChatInviteLink createInvite = SubscriptionService.createInvite(chatId);
    ChatInviteLink link = telegramClient.execute(createInvite);
    return link.getInviteLink();
  }

  /**
   * Trybit подписывает постбэк JWT (HS256). Подпись - HMAC-SHA256 от "header.payload", ключ -
   * SECRET KEY проекта. Сторонняя JWT-библиотека не нужна, делаем всё вручную.
   */
  private boolean isValidToken(String token) {
    if (token == null || token.isBlank()) {
      return false;
    }
    String[] parts = token.split("\\.");
    if (parts.length != 3) {
      return false;
    }
    try {
      byte[] expectedSig = hmacSha256(parts[0] + "." + parts[1], secretKey);
      byte[] actualSig = base64UrlDecode(parts[2]);
      if (!MessageDigest.isEqual(expectedSig, actualSig)) {
        return false;
      }

      String payloadJson = new String(base64UrlDecode(parts[1]), StandardCharsets.UTF_8);
      JsonNode payload = objectMapper.readTree(payloadJson);
      if (payload.has("exp")) {
        long exp = payload.get("exp").asLong();
        if (Instant.now().getEpochSecond() >= exp) {
          return false;
        }
      }
      return true;
    } catch (Exception e) {
      log.error("Ошибка валидации токена постбэка", e);
      return false;
    }
  }

  private byte[] hmacSha256(String data, String secret) throws Exception {
    Mac mac = Mac.getInstance(ALGORITHM);
    mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM));
    return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
  }

  private byte[] base64UrlDecode(String s) {
    String padded = s;
    int mod = s.length() % 4;
    if (mod != 0) {
      padded += "=".repeat(4 - mod);
    }
    return Base64.getUrlDecoder().decode(padded);
  }
}
