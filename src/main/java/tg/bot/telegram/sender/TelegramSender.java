package tg.bot.telegram.sender;

import tg.bot.telegram.client.TelegramBotClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class TelegramSender {
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

    public void editMessage(Long chatId,
                            Integer messageId,
                            String text,
                            InlineKeyboardMarkup keyboard) {

        EditMessageText edit = new EditMessageText();

        edit.setChatId(chatId.toString());
        edit.setMessageId(messageId);
        edit.setText(text);
        edit.setReplyMarkup(keyboard);
        edit.setParseMode("Markdown");
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
}
