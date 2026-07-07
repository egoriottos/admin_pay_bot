package tg.bot.telegram.client;

import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.Serializable;

@Setter
@Component
public class TelegramBotClientImpl implements TelegramBotClient{
    private TelegramLongPollingBot bot;

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> void execute(Method method) throws TelegramApiException {
        bot.execute(method);
    }
}
