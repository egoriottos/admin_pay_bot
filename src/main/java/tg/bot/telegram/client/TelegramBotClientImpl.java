package tg.bot.telegram.client;

import java.io.Serializable;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.ChatInviteLink;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Setter
@Component
public class TelegramBotClientImpl implements TelegramBotClient {
  private TelegramLongPollingBot bot;

  @Override
  public <T extends Serializable, Method extends BotApiMethod<T>> void execute(Method method) throws TelegramApiException {
    bot.execute(method);
  }

  @Override
  public void execute(SendDocument document) throws TelegramApiException {
    bot.execute(document);
  }

  @Override
  public void execute(SendMediaGroup mediaGroup) throws TelegramApiException {
    bot.execute(mediaGroup);
  }

  @Override
  public ChatMember getChatMember(GetChatMember request) throws TelegramApiException {
    return bot.execute(request);
  }

  @Override
  public ChatInviteLink execute(CreateChatInviteLink link) throws TelegramApiException {
    return bot.execute(link);
  }
}
