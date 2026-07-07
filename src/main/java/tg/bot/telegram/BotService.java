package tg.bot.telegram;

import tg.bot.config.BotProperties;
import tg.bot.telegram.client.TelegramBotClientImpl;
import tg.bot.telegram.dispatcher.UpdateDispatcher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class BotService extends TelegramLongPollingBot {
    private final UpdateDispatcher dispatcher;
    private final BotProperties botProperties;

    public BotService(BotProperties botProperties, UpdateDispatcher dispatcher, TelegramBotClientImpl client) {
        super(botProperties.token());
        this.botProperties = botProperties;
        this.dispatcher = dispatcher;
        client.setBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        dispatcher.dispatch(update);
    }

    @Override
    public String getBotUsername() {
        return botProperties.username();
    }
}
