package tg.bot.telegram.callback;

import tg.bot.utils.CallbackData;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import static tg.bot.utils.CallbackData.*;

@Component
public class CallbackHandler {
  private final Map<String, CallbackCommand> callbacks;

  public CallbackHandler(Map<String, CallbackCommand> callbacks) {
    this.callbacks = callbacks;
  }

  public void handle(CallbackQuery query) {
    String data = query.getData();
    if (data.startsWith(LANG_)) {
      callbacks.get(LANG).handle(query);
      return;
    }
    if (data.startsWith(CORNIX_SETTINGS_WITHOUT_CODE)) {
      callbacks.get(CORNIX_SETTINGS_CHANNEL).handle(query);
      return;
    }
    CallbackCommand callback = callbacks.get(data);
    if (callback != null) {
      callback.handle(query);
    }
  }
}
