package tg.bot.scheduled;

import static tg.bot.subscription.SubscriptionStatus.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.UnbanChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.subscription.Subscription;
import tg.bot.subscription.SubscriptionService;
import tg.bot.telegram.callback.interfaces.TelegramBotClient;
import tg.bot.telegram.message.MessageFactory;
import tg.bot.telegram.sender.TelegramSender;
import tg.bot.user.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckSubscriptionsAndKickUser {
  @Value("${subscription.reminder-days-before}")
  private int reminderDaysBefore;

  private final SubscriptionService subscriptionService;
  private final TelegramBotClient telegramClient;
  private final TelegramSender sender;
  private final MessageFactory messageFactory;

  /** Раз в час: кикаем пользователей из каналов, где подписка уже истекла. */
  @Scheduled(cron = "${subscription.cron.check-expired}")
  public void checkExpiredSubscriptions() {
    LocalDateTime now = LocalDateTime.now();
    List<Subscription> expired = subscriptionService.findByExpireDateBeforeAndStatus(now, ACTIVE);

    Map<User, List<Subscription>> byUser =
        expired.stream().collect(Collectors.groupingBy(Subscription::getUser));

    log.info("Найдено {} истёкших подписок у {} пользователей", expired.size(), byUser.size());

    byUser.forEach(
        (user, subscriptions) -> {
          try {
            processExpiredForUser(user, subscriptions);
          } catch (Exception e) {
            log.error(
                "Не удалось обработать истёкшие подписки пользователя telegramId={}",
                user.getTelegramId(),
                e);
          }
        });
  }

  /** Кикает пользователя из всех каналов разом и шлёт ОДНО сообщение со списком. */
  private void processExpiredForUser(User user, List<Subscription> subscriptions) {
    List<String> expiredChannelNames = new ArrayList<>();

    for (Subscription subscription : subscriptions) {
      try {
        kickFromChannel(subscription.getChannel().getTelegramChatId(), user.getTelegramId());
        subscription.setStatus(EXPIRED);
        subscriptionService.saveSubscription(subscription);
        expiredChannelNames.add(subscription.getChannel().getName());
      } catch (Exception e) {
        // сбой по одному каналу не должен мешать кикнуть из остальных
        log.error(
            "Не удалось кикнуть telegramId={} из канала id={}",
            user.getTelegramId(),
            subscription.getChannel().getId(),
            e);
      }
    }

    if (!expiredChannelNames.isEmpty()) {
      sender.sendMessage(
          user.getTelegramId(),
          messageFactory.subscriptionsExpired(user.getLanguage(), expiredChannelNames),
          null);
      log.info("telegramId={}: кикнут из каналов {}", user.getTelegramId(), expiredChannelNames);
    }
  }

  /** Раз в день (в полдень): одно напоминание со списком всех скоро истекающих подписок. */
  @Scheduled(cron = "${subscription.cron.reminder}")
  public void sendExpirationReminders() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime threshold = now.plusDays(reminderDaysBefore);

    List<Subscription> soonExpiring =
        subscriptionService.findByExpireDateBetweenAndReminderSentFalseAndStatus(
            now, threshold, ACTIVE);

    Map<User, List<Subscription>> byUser =
        soonExpiring.stream().collect(Collectors.groupingBy(Subscription::getUser));

    log.info(
        "Найдено {} подписок для напоминания у {} пользователей",
        soonExpiring.size(),
        byUser.size());

    byUser.forEach(
        (user, subscriptions) -> {
          try {
            sendReminderForUser(user, subscriptions);
          } catch (Exception e) {
            log.error("Не удалось отправить напоминание telegramId={}", user.getTelegramId(), e);
          }
        });
  }

  private void sendReminderForUser(User user, List<Subscription> subscriptions) {
    sender.sendMessage(
        user.getTelegramId(),
        messageFactory.subscriptionsExpiringSoon(user.getLanguage(), subscriptions),
        null);

    subscriptions.forEach(s -> s.setReminderSent(true));
    subscriptionService.saveAllSubscriptions(subscriptions);
  }

  /**
   * "Кик" без бана: banChatMember + сразу unbanChatMember(onlyIfBanned=true). Пользователь
   * удаляется из канала, НО не остаётся в чёрном списке - при повторной оплате сможет зайти обратно
   * по новой invite-ссылке.
   */
  private void kickFromChannel(String channelChatId, Long telegramUserId)
      throws TelegramApiException {
    BanChatMember ban = new BanChatMember();
    ban.setChatId(channelChatId);
    ban.setUserId(telegramUserId);
    telegramClient.execute(ban);

    UnbanChatMember unban = new UnbanChatMember();
    unban.setChatId(channelChatId);
    unban.setUserId(telegramUserId);
    unban.setOnlyIfBanned(true);
    telegramClient.execute(unban);
  }
}
