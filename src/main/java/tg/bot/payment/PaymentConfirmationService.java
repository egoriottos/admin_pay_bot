package tg.bot.payment;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.bot.localization.Language;
import tg.bot.subscription.Subscription;
import tg.bot.subscription.SubscriptionService;
import tg.bot.tariff.Tariff;
import tg.bot.tariff.TariffService;

@Service
@RequiredArgsConstructor
public class PaymentConfirmationService {
  private final SubscriptionService subscriptionService;
  private final PaymentRepository paymentRepository;
  private final TariffService tariffService;

  /**
   * Помечает Payment оплаченным и создаёт активную подписку одной транзакцией. Telegram API сюда
   * намеренно не суётся - если он упадёт, оплата и подписка в БД всё равно должны остаться
   * сохранёнными.
   */
  @Transactional
  public PaidResult confirm(Payment payment) {
    Long tariffId = Long.parseLong(payment.getOrderId());
    Tariff tariff = tariffService.getTariffByTariffId(tariffId);
    LocalDateTime now = LocalDateTime.now();

    payment.setStatus(PaymentStatus.PAID);
    payment.setPaidAt(LocalDateTime.now());

    Subscription subscription =
        subscriptionService.createSubscription(
            payment, tariff, now, now.plusMonths(tariff.getMonths()), false);

    payment.setSubscription(subscription);
    paymentRepository.save(payment);

    return new PaidResult(
        payment.getChatId(),
        payment.getMessageId(),
        payment.getUser().getTelegramId(),
        payment.getUser().getLanguage(),
        tariff.getChannel().getTelegramChatId(),
        tariff.getChannel().getName());
  }

  public record PaidResult(
      Long chatId,
      Integer messageId,
      Long telegramUserId,
      Language language,
      String channelChatId,
      String channelName) {}
}
