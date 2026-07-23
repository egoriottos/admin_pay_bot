package tg.bot.telegram.callback;

import static tg.bot.utils.CallbackData.BUY_TARIFF;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tg.bot.payment.Payment;
import tg.bot.payment.PaymentRepository;
import tg.bot.payment.PaymentStatus;
import tg.bot.payment.TryBitPaymentService;
import tg.bot.tariff.Tariff;
import tg.bot.tariff.TariffRepository;
import tg.bot.telegram.callback.interfaces.CallbackCommand;
import tg.bot.telegram.keyboard.KeyboardFactory;
import tg.bot.telegram.message.MessageFactory;
import tg.bot.telegram.sender.TelegramSender;
import tg.bot.user.User;
import tg.bot.user.UserService;

@Component("BUY_TARIFF_HANDLER")
@RequiredArgsConstructor
public class BuyTariffCallback implements CallbackCommand {
  private final UserService userService;
  private final TariffRepository tariffRepository;
  private final PaymentRepository paymentRepository;
  private final TryBitPaymentService tryBitPaymentService;
  private final MessageFactory messageFactory;
  private final KeyboardFactory keyboardFactory;
  private final TelegramSender sender;

  @Override
  @Transactional
  public void handle(CallbackQuery query) {
    User user = userService.findByTelegramId(query.getFrom().getId());
    Long tariffId = Long.parseLong(query.getData().substring(BUY_TARIFF.length()));
    Tariff tariff =
        tariffRepository
            .findById(tariffId)
            .orElseThrow(() -> new IllegalArgumentException("Tariff not found: " + tariffId));

    TryBitPaymentService.TrybitInvoice invoice =
        tryBitPaymentService.createInvoice(tariff.getPrice());

    Payment payment =
        Payment.builder()
            .invoiceId(invoice.invoiceId())
            .user(user)
            .amount(tariff.getPrice())
            .orderId(tariffId.toString())
            .status(PaymentStatus.CREATED)
            .createdAt(LocalDateTime.now())
            .chatId(query.getMessage().getChatId())
            .messageId(query.getMessage().getMessageId())
            .build();
    paymentRepository.save(payment);

    sender.editMessage(
        query.getMessage().getChatId(),
        query.getMessage().getMessageId(),
        messageFactory.payInvoice(user.getLanguage(), tariff),
        keyboardFactory.payInvoice(user.getLanguage(), invoice.payUrl()));
  }
}
