package tg.bot.telegram.callback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tg.bot.telegram.keyboard.KeyboardFactory;
import tg.bot.telegram.message.MessageFactory;
import tg.bot.telegram.sender.TelegramSender;
import tg.bot.user.User;
import tg.bot.user.UserService;

@Component("MAIN_MENU")
@RequiredArgsConstructor
public class MainMenuCallback implements CallbackCommand {

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
        messageFactory.mainMenu(user.getLanguage()),
        keyboardFactory.mainMenu(user.getLanguage()));
  }
}
