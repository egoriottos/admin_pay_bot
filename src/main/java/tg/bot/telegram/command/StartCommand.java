package tg.bot.telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import tg.bot.telegram.keyboard.KeyboardFactory;
import tg.bot.telegram.message.MessageFactory;
import tg.bot.telegram.sender.TelegramSender;
import tg.bot.user.User;
import tg.bot.user.UserService;

@Component
@RequiredArgsConstructor
public class StartCommand {
  private final KeyboardFactory keyboardFactory;
  private final MessageFactory messageFactory;
  private final TelegramSender sender;
  private final UserService userService;

  public void execute(Message message) {
    User user = userService.getOrCreate(message.getFrom());
    sender.sendMessage(
        message.getChatId(), messageFactory.chooseLanguage(), keyboardFactory.languageKeyboard());
  }
}
