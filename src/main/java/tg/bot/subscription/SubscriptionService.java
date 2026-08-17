package tg.bot.subscription;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
import tg.bot.channel.Channel;
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
    User user = payment.getUser();
    Channel channel = tariff.getChannel();

    Subscription subscription =
            subscriptionRepository
                    .findByUserAndChannel(user, channel)
                    .orElseGet(() -> Subscription.builder().user(user).channel(channel).build());

    subscription.setTariff(tariff);
    subscription.setStatus(SubscriptionStatus.ACTIVE);
    subscription.setStartDate(creationDate);
    subscription.setExpireDate(expirationDate.plusMonths(tariff.getMonths()));
    subscription.setReminderSent(reminderSent);

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

  public void saveAllSubscriptions(List<Subscription> subscriptions) {
    subscriptionRepository.saveAll(subscriptions);
  }

  public void saveSubscription(Subscription subscription) {
    subscriptionRepository.save(subscription);
  }

  public List<Subscription> findByExpireDateBeforeAndStatus(
      LocalDateTime dateTime, SubscriptionStatus status) {
    return subscriptionRepository.findByExpireDateBeforeAndStatus(dateTime, status);
  }

  public List<Subscription> findByExpireDateBetweenAndReminderSentFalseAndStatus(
      LocalDateTime from, LocalDateTime to, SubscriptionStatus status) {
    return subscriptionRepository.findByExpireDateBetweenAndReminderSentFalseAndStatus(
        from, to, status);
  }
}
