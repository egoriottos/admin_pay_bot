package tg.bot.telegram.dispatcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import tg.bot.telegram.callback.CallbackHandler;
import tg.bot.telegram.command.StartCommand;

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {
  private static final String START = "/start";
  private final StartCommand startCommand;
  private final CallbackHandler callbackHandler;

  public void dispatch(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String text = update.getMessage().getText();

      if (text.equals(START)) {
        startCommand.execute(update.getMessage());
      }

    } else if (update.hasCallbackQuery()) {
      callbackHandler.handle(update.getCallbackQuery());
    }
  }
}
