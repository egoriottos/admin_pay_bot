package tg.bot.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.telegram.client.TelegramBotClient;

@Service
@RequiredArgsConstructor
public class CheckReferralSubscriptionService {
  @Value("${chat.public-chat-id}")
  private String freeChatId;

  private final TelegramBotClient telegramClient;

  public boolean isUserSubscribed(Long userId) {
    GetChatMember request = GetChatMember.builder().chatId(freeChatId).userId(userId).build();
    try {
      ChatMember member = telegramClient.getChatMember(request);
      return switch (member.getStatus()) {
        case "member", "administrator", "creator" -> true;
        default -> false;
      };
    } catch (TelegramApiException e) {
      return false;
    }
  }
}
