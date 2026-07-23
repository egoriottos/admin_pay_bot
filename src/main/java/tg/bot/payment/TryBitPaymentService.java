package tg.bot.payment;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TryBitPaymentService {
  @Value("${trybit.api-token}")
  private String apiToken;

  @Value("${trybit.shop-id}")
  private String shopId;

  private static final String BASE_URL = "https://api.trybit.com/v2";

  private final RestTemplate restTemplate;

  /**
   * amountUsd — сумма в USD (fiat), Trybit сам сконвертирует её в криптовалюту, которую выберет
   * плательщик на странице оплаты.
   */
  public TrybitInvoice createInvoice(BigDecimal amountUsd) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Token " + apiToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, Object> body = new HashMap<>();
    body.put("shop_id", shopId);
    body.put("amount", amountUsd);
    body.put("currency", "USD");

    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
    var response =
        restTemplate.postForEntity(
            BASE_URL + "/invoice/create", request, TrybitInvoiceResponse.class);

    var result = Objects.requireNonNull(response.getBody()).result();

    // В ответе uuid приходит с префиксом "INV-", а в постбэке invoice_id — уже без него.
    // Срезаем префикс сразу, чтобы потом искать Payment по одному и тому же значению.
    String invoiceId = result.uuid().replace("INV-", "");

    return new TrybitInvoice(invoiceId, result.link());
  }

  public record TrybitInvoice(String invoiceId, String payUrl) {}

  public record TrybitInvoiceResponse(String status, Result result) {
    public record Result(String uuid, String link) {}
  }
}
