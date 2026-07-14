package tg.bot.telegram.callback;

import tg.bot.subscription.CheckReferralSubscriptionService;
import tg.bot.telegram.keyboard.KeyboardFactory;
import tg.bot.telegram.message.MessageFactory;
import tg.bot.telegram.sender.TelegramSender;
import tg.bot.user.User;
import tg.bot.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component("REFERRAL")
@RequiredArgsConstructor
public class ReferralCallback implements CallbackCommand{
    private final UserService userService;
    private final CheckReferralSubscriptionService checkSubscriptionService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;
    private final TelegramSender sender;
    @Override
    public void handle(CallbackQuery query) {
        User user = userService.findByTelegramId(
                query.getFrom().getId()
        );
        boolean subscribed = checkSubscriptionService
                .isUserSubscribed(user.getTelegramId());
        if (subscribed) {
            sender.editMessage(
                    query.getMessage().getChatId(),
                    query.getMessage().getMessageId(),
                    messageFactory.referralSuccess(user.getLanguage()),
                    keyboardFactory.referralSuccess(user.getLanguage())
            );
        } else {
            sender.editMessage(
                    query.getMessage().getChatId(),
                    query.getMessage().getMessageId(),
                    messageFactory.referralDenied(user.getLanguage()),
                    keyboardFactory.referralDenied(user.getLanguage())
            );
        }
    }
}
