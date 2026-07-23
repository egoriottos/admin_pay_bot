package tg.bot.telegram.callback;

import tg.bot.subscription.Subscription;
import tg.bot.subscription.SubscriptionService;
import tg.bot.telegram.keyboard.KeyboardFactory;
import tg.bot.telegram.message.MessageFactory;
import tg.bot.telegram.sender.TelegramSender;
import tg.bot.user.User;
import tg.bot.user.UserService;
import tg.bot.utils.CallbackData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import java.util.List;

@Component("MY_SUBS")
@RequiredArgsConstructor
public class MySubscriptionsCallback implements CallbackCommand{
    private final UserService userService;
    private final MessageFactory messageFactory;
    private final KeyboardFactory keyboardFactory;
    private final TelegramSender sender;
    private final SubscriptionService subscriptionService;
    @Override
    @Transactional
    public void handle(CallbackQuery query) {
        User user = userService.findByTelegramId(query.getFrom().getId());

        List<Subscription> subscriptions =
                subscriptionService.getActiveSubscriptions(user);

        sender.editMessage(
                query.getMessage().getChatId(),
                query.getMessage().getMessageId(),
                messageFactory.mySubscriptions(
                        user.getLanguage(),
                        subscriptions
                ),
                keyboardFactory.back(
                        user.getLanguage(),
                        CallbackData.MAIN_MENU
                )
        );
    }
}
