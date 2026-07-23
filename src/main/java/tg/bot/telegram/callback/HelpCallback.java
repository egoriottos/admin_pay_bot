package tg.bot.telegram.callback;

import static tg.bot.utils.CallbackData.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tg.bot.telegram.callback.interfaces.CallbackCommand;
import tg.bot.telegram.keyboard.KeyboardFactory;
import tg.bot.telegram.message.MessageFactory;
import tg.bot.telegram.sender.TelegramSender;
import tg.bot.user.User;
import tg.bot.user.UserService;

@Component("HELP")
@RequiredArgsConstructor
public class HelpCallback implements CallbackCommand {
  private final UserService userService;
  private final MessageFactory messageFactory;
  private final KeyboardFactory keyboardFactory;
  private final TelegramSender sender;

  @Override
  public void handle(CallbackQuery query) {
    User user = userService.findByTelegramId(query.getFrom().getId());

    sender.editMessage(
        query.getMessage().getChatId(),
        query.getMessage().getMessageId(),
        messageFactory.help(user.getLanguage()),
        keyboardFactory.back(user.getLanguage(), MAIN_MENU));
  }
}
