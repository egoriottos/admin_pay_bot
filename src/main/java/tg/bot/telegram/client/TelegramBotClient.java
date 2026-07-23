package tg.bot.telegram.client;

import java.io.Serializable;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.CreateChatInviteLink;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.objects.ChatInviteLink;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface TelegramBotClient {
  <T extends Serializable, Method extends BotApiMethod<T>> void execute(Method method)
      throws TelegramApiException;

  void execute(SendDocument document) throws TelegramApiException;

  void execute(SendMediaGroup photo) throws TelegramApiException;

  ChatMember getChatMember(GetChatMember request) throws TelegramApiException;

  ChatInviteLink execute(CreateChatInviteLink link) throws TelegramApiException;
}
