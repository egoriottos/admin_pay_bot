package tg.bot.telegram.callback.channels;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tg.bot.tariff.Tariff;
import tg.bot.tariff.TariffService;
import tg.bot.telegram.callback.CallbackCommand;
import tg.bot.telegram.keyboard.KeyboardFactory;
import tg.bot.telegram.message.MessageFactory;
import tg.bot.telegram.sender.TelegramSender;
import tg.bot.user.User;
import tg.bot.user.UserService;
import tg.bot.utils.CallbackData;

@Component("TRADE_BE")
@RequiredArgsConstructor
public class TradeBeCallback implements CallbackCommand {
  private final UserService userService;
  private final TariffService tariffService;
  private final MessageFactory messageFactory;
  private final KeyboardFactory keyboardFactory;
  private final TelegramSender sender;

  @Override
  public void handle(CallbackQuery query) {
    User user = userService.findByTelegramId(query.getFrom().getId());
    List<Tariff> tariffs = tariffService.getByChannelCode(CallbackData.TRADE_BE);
    sender.editMessage(
        query.getMessage().getChatId(),
        query.getMessage().getMessageId(),
        messageFactory.tariffs(user.getLanguage()),
        keyboardFactory.tariffs(user.getLanguage(), tariffs));
  }
}
