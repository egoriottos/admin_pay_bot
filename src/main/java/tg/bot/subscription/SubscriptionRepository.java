package tg.bot.subscription;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.bot.user.User;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
  List<Subscription> findByUserAndStatus(User user, SubscriptionStatus status);

  List<Subscription> findByExpireDateBeforeAndStatus(
      LocalDateTime dateTime, SubscriptionStatus status);

  List<Subscription> findByExpireDateBetweenAndReminderSentFalseAndStatus(
      LocalDateTime from, LocalDateTime to, SubscriptionStatus status);
}
