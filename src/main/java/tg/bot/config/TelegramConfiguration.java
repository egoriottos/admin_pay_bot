package tg.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tg.bot.telegram.BotService;

@Configuration
public class TelegramConfiguration {
  @Bean
  public TelegramBotsApi telegramBotsApi(BotService botService) throws Exception {
    TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
    api.registerBot(botService);
    return api;
  }
}
