package tg.bot.subscription;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
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

  public static CreateChatInviteLink createInvite(String chatId) {
    CreateChatInviteLink createInvite = new CreateChatInviteLink();
    createInvite.setChatId(chatId);
    createInvite.setMemberLimit(1);
    createInvite.setExpireDate((int) (System.currentTimeMillis() / 1000 + 3600));
    return createInvite;
  }
}
