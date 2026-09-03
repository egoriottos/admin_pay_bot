package tg.bot.subscription;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.bot.channel.Channel;
import tg.bot.user.User;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
  List<Subscription> findByUserAndStatus(User user, SubscriptionStatus status);

  @EntityGraph(attributePaths = {"user", "channel"})
  List<Subscription> findByExpireDateBeforeAndStatus(
      LocalDateTime dateTime, SubscriptionStatus status);

  @EntityGraph(attributePaths = {"user", "channel"})
  List<Subscription> findByExpireDateBetweenAndReminderSentFalseAndStatus(
      LocalDateTime from, LocalDateTime to, SubscriptionStatus status);

  Optional<Subscription> findByUserAndChannel(User user, Channel channel);
}
