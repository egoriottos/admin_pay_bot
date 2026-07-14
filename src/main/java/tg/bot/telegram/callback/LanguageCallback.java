package tg.bot.telegram.callback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tg.bot.localization.Language;
import tg.bot.telegram.keyboard.KeyboardFactory;
import tg.bot.telegram.message.MessageFactory;
import tg.bot.telegram.sender.TelegramSender;
import tg.bot.user.UserService;
import tg.bot.utils.CallbackData;
import static tg.bot.utils.CallbackData.*;

@Component("LANG")
@RequiredArgsConstructor
public class LanguageCallback implements CallbackCommand {
  private final UserService userService;
  private final TelegramSender sender;
  private final MessageFactory messageFactory;
  private final KeyboardFactory keyboardFactory;

  @Override
  public void handle(CallbackQuery query) {

    Long telegramId = query.getFrom().getId();

    Language language = Language.valueOf(query.getData().replace(LANG_, ""));

    userService.setLanguage(telegramId, language);
    sender.editMessage(
        query.getMessage().getChatId(),
        query.getMessage().getMessageId(),
        messageFactory.mainMenu(language),
        keyboardFactory.mainMenu(language));
  }
}
