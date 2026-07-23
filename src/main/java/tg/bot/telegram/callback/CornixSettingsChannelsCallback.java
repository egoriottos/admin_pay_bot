package tg.bot.telegram.callback;

import static tg.bot.utils.CallbackData.*;

import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import tg.bot.telegram.callback.interfaces.CallbackCommand;
import tg.bot.telegram.sender.TelegramSender;
import tg.bot.user.User;
import tg.bot.user.UserService;
import tg.bot.utils.CornixSettingUtils;

@Component("CORNIX_SETTINGS_CHANNEL")
@RequiredArgsConstructor
public class CornixSettingsChannelsCallback implements CallbackCommand {
  private final UserService userService;
  private final CornixSettingUtils cornixSettingUtils;
  private final TelegramSender telegramSender;

  @Override
  public void handle(CallbackQuery query) {
    User user = userService.findByTelegramId(query.getFrom().getId());

    String channelCode = query.getData().replace(CORNIX_SETTINGS_WITHOUT_CODE, "");

    List<InputStream> images = cornixSettingUtils.getImages(channelCode);

    telegramSender.sendPhotos(query.getMessage().getChatId(), images);
  }
}
