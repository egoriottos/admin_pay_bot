package tg.bot.subscription;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.bot.user.User;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
  List<Subscription> findByUserAndStatus(User user, SubscriptionStatus status);

  @EntityGraph(attributePaths = "user")
  List<Subscription> findByExpireDateBeforeAndStatus(
      LocalDateTime dateTime, SubscriptionStatus status);

  @EntityGraph(attributePaths = "user")
  List<Subscription> findByExpireDateBetweenAndReminderSentFalseAndStatus(
      LocalDateTime from, LocalDateTime to, SubscriptionStatus status);
}
