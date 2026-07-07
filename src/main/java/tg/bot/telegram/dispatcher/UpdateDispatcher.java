package tg.bot.telegram.dispatcher;

import tg.bot.telegram.callback.CallbackHandler;
import tg.bot.telegram.command.StartCommand;
import tg.bot.user.User;
import tg.bot.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {
    private final StartCommand startCommand;
    private final CallbackHandler callbackHandler;

    public void dispatch(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();

            if (text.equals("/start")) {
                startCommand.execute(update.getMessage());
            }

        } else if (update.hasCallbackQuery()) {
            callbackHandler.handle(update.getCallbackQuery());
        }
    }
}
