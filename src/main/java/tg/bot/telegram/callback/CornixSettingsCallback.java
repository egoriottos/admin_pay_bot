package tg.bot.telegram.callback;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tg.bot.channel.Channel;
import tg.bot.channel.ChannelService;
import tg.bot.telegram.keyboard.KeyboardFactory;
import tg.bot.telegram.message.MessageFactory;
import tg.bot.telegram.sender.TelegramSender;
import tg.bot.user.User;
import tg.bot.user.UserService;

@Component("CORNIX_SETTINGS")
@RequiredArgsConstructor
public class CornixSettingsCallback implements CallbackCommand {
  private final UserService userService;
  private final ChannelService channelService;
  private final MessageFactory messageFactory;
  private final KeyboardFactory keyboardFactory;
  private final TelegramSender telegramSender;

  @Override
  public void handle(CallbackQuery query) {
    User user = userService.findByTelegramId(query.getFrom().getId());
    List<Channel> channels = channelService.getAll();
    telegramSender.editMessage(
        query.getMessage().getChatId(),
        query.getMessage().getMessageId(),
        messageFactory.cornixSettings(user.getLanguage()),
        keyboardFactory.channelsCornixSettings(user.getLanguage(), channels));
  }
}
