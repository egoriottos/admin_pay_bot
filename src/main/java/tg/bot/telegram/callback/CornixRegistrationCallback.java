package tg.bot.telegram.callback;

import tg.bot.telegram.keyboard.KeyboardFactory;
import tg.bot.telegram.message.MessageFactory;
import tg.bot.telegram.sender.TelegramSender;
import tg.bot.user.User;
import tg.bot.user.UserService;
import tg.bot.utils.Callback;
import tg.bot.utils.CornixGuideSelectHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component("REGISTER_CORNIX")
@RequiredArgsConstructor
public class CornixRegistrationCallback implements CallbackCommand{
    private final UserService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;
    private final CornixGuideSelectHelper cornixGuideSelectHelper;
    private final TelegramSender sender;


    @Override
    public void handle(CallbackQuery query) {
        User user = userService.findByTelegramId(query.getFrom().getId());
        sender.editMessage(
                query.getMessage().getChatId(),
                query.getMessage().getMessageId(),
                messageFactory.cornixRegistration(user.getLanguage()),
                keyboardFactory.back(user.getLanguage(), Callback.MAIN_MENU));

        sender.sendDocument(query.getMessage().getChatId(), cornixGuideSelectHelper.getGuide(user.getLanguage()),
                "registration_guide.pdf");
    }
}
