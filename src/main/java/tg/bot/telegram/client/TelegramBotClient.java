package tg.bot.telegram.client;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.Serializable;

public interface TelegramBotClient {
    <T extends Serializable, Method extends BotApiMethod<T>> void execute(Method method)
        throws TelegramApiException;
}
