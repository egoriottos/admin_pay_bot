package tg.bot.subscription;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
import tg.bot.payment.Payment;
import tg.bot.tariff.Tariff;
import tg.bot.user.User;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
  private final SubscriptionRepository subscriptionRepository;

  public Subscription createSubscription(
      Payment payment,
      Tariff tariff,
      LocalDateTime creationDate,
      LocalDateTime expirationDate,
      boolean reminderSent) {
    Subscription subscription =
        Subscription.builder()
            .user(payment.getUser())
            .channel(tariff.getChannel())
            .tariff(tariff)
            .status(SubscriptionStatus.ACTIVE)
            .startDate(creationDate)
            .expireDate(expirationDate.plusMonths(tariff.getMonths()))
            .reminderSent(reminderSent)
            .build();
    subscriptionRepository.save(subscription);
    return subscription;
  }

  public List<Subscription> getActiveSubscriptions(User user) {
    List<Subscription> userSubs =
        subscriptionRepository.findByUserAndStatus(user, SubscriptionStatus.ACTIVE);
    if (userSubs.isEmpty()) {
      return List.of();
    } else {
      return userSubs;
    }
  }

  public static CreateChatInviteLink createInvite(String chatId) {
    CreateChatInviteLink createInvite = new CreateChatInviteLink();
    createInvite.setChatId(chatId);
    createInvite.setMemberLimit(1);
    createInvite.setExpireDate((int) (System.currentTimeMillis() / 1000 + 3600));
    return createInvite;
  }
}
