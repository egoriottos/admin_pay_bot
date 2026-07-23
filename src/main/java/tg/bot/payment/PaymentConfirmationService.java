package tg.bot.payment;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.bot.localization.Language;
import tg.bot.subscription.Subscription;
import tg.bot.subscription.SubscriptionRepository;
import tg.bot.subscription.SubscriptionStatus;
import tg.bot.tariff.Tariff;
import tg.bot.tariff.TariffRepository;

@Service
@RequiredArgsConstructor
public class PaymentConfirmationService {
  private final SubscriptionRepository subscriptionRepository;
  private final PaymentRepository paymentRepository;
  private final TariffRepository tariffRepository;

  /**
   * Помечает Payment оплаченным и создаёт активную подписку одной транзакцией. Telegram API сюда
   * намеренно не суётся - если он упадёт, оплата и подписка в БД всё равно должны остаться
   * сохранёнными.
   */
  @Transactional
  public PaidResult confirm(Payment payment) {
    Long tariffId = Long.parseLong(payment.getOrderId());
    Tariff tariff = tariffRepository.findById(tariffId).orElseThrow();

    payment.setStatus(PaymentStatus.PAID);
    payment.setPaidAt(LocalDateTime.now());

    LocalDateTime now = LocalDateTime.now();
    Subscription subscription =
        Subscription.builder()
            .user(payment.getUser())
            .channel(tariff.getChannel())
            .tariff(tariff)
            .status(SubscriptionStatus.ACTIVE)
            .startDate(now)
            .expireDate(now.plusMonths(tariff.getMonths()))
            .reminderSent(false)
            .build();

    payment.setSubscription(subscription);
    subscriptionRepository.save(subscription);
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
