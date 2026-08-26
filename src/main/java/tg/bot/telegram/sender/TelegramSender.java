package tg.bot.telegram.sender;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tg.bot.telegram.callback.interfaces.TelegramBotClient;

@Component
@RequiredArgsConstructor
public class TelegramSender {
  private static final String MARKDOWN = "Markdown";
  private static final String HTML = "HTML";
  private final TelegramBotClient telegramClient;

  public void sendMessage(Long chatId, String text, InlineKeyboardMarkup keyboard) {
    SendMessage message = new SendMessage();

    message.setChatId(chatId.toString());
    message.setText(text);
    message.setReplyMarkup(keyboard);

    try {
      telegramClient.execute(message);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  public void editMessage(
          Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard) {
    editMessage(chatId, messageId, text, keyboard, MARKDOWN);
  }

  public void editMessageHtml(
          Long chatId, Integer messageId, String text, InlineKeyboardMarkup keyboard) {
    editMessage(chatId, messageId, text, keyboard, HTML);
  }

  private void editMessage(
          Long chatId,
          Integer messageId,
          String text,
          InlineKeyboardMarkup keyboard,
          String parseMode) {

    EditMessageText edit = new EditMessageText();

    edit.setChatId(chatId.toString());
    edit.setMessageId(messageId);
    edit.setText(text);
    edit.setReplyMarkup(keyboard);
    edit.setParseMode(parseMode);
    try {
      telegramClient.execute(edit);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendDocument(Long chatId, InputStream file, String fileName) {
    SendDocument document = new SendDocument();
    document.setChatId(chatId.toString());
    document.setDocument(new InputFile(file, fileName));
    try {
      telegramClient.execute(document);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  public void sendPhotos(Long chatId, List<InputStream> images) {
    SendMediaGroup mediaGroup = new SendMediaGroup();
    mediaGroup.setChatId(chatId.toString());
    List<InputMedia> media = new ArrayList<>();

    for (int i = 0; i < images.size(); i++) {
      InputMediaPhoto photo = new InputMediaPhoto();
      photo.setMedia(images.get(i), "image_" + (i + 1) + ".jpg");
      media.add(photo);
    }
    mediaGroup.setMedias(media);

    try {
      telegramClient.execute(mediaGroup);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }
}
