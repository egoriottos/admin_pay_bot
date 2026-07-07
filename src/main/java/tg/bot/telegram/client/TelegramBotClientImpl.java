package tg.bot.telegram.client;

import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
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

    @Override
    public void execute(SendDocument document) throws TelegramApiException {
        bot.execute(document);
    }

}
