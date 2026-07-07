package tg.bot.telegram.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackCommand {
    void handle(CallbackQuery query);
}
