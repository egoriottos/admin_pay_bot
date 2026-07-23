package tg.bot.subscription;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.bot.user.User;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
  private final SubscriptionRepository subscriptionRepository;

  public List<Subscription> getActiveSubscriptions(User user) {
    List<Subscription> userSubs =
        subscriptionRepository.findByUserAndStatus(user, SubscriptionStatus.ACTIVE);
    if (userSubs.isEmpty()) {
      return List.of();
    } else {
      return userSubs;
    }
  }
}
