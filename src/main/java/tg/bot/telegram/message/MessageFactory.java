package tg.bot.telegram.message;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tg.bot.localization.Language;
import tg.bot.localization.LocalizationService;
import tg.bot.subscription.Subscription;
import tg.bot.tariff.Tariff;

@Component
@RequiredArgsConstructor
public class MessageFactory {
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
  private final LocalizationService loc;

  public String chooseLanguage() {
    return loc.get("language.choose", Language.EN) + "/" + loc.get("language.choose", Language.RU);
  }

  public String mainMenu(Language language) {
    return loc.get("main.menu", language);
  }

  public String help(Language language) {
    return loc.get("help.info", language);
  }

  public String tariffs(Language language) {
    return loc.get("tariffs.info", language);
  }

  public String cornixRegistration(Language language) {
    return loc.get("cornix.registration", language);
  }

  public String cornixSettings(Language language) {
    return loc.get("cornix.settings", language);
  }

  public String referralSuccess(Language language) {
    return loc.get("referral.success", language);
  }

  public String referralDenied(Language language) {
    return loc.get("referral.denied", language);
  }

  public String payInvoice(Language language, Tariff tariff) {
    return loc.get("button.payment_instruction", language, tariff);
  }

  public String paymentSuccess(Language language, String channelName) {
    return loc.get("payment.success", language, channelName);
  }

  public String mySubscriptions(Language language, List<Subscription> subscriptions) {

    if (subscriptions.isEmpty()) {
      return loc.get("my.subscriptions.empty", language);
    }

    return subscriptions.stream()
        .map(
            subscription ->
                loc.get(
                    "my.subscription",
                    language,
                    subscription.getChannel().getName(),
                    subscription.getExpireDate().format(DATE_FORMAT)))
        .collect(Collectors.joining("\n\n"));
  }

  public String subscriptionsExpired(Language language, List<String> channelNames) {
    String list = channelNames.stream().map(name -> "• " + name).collect(Collectors.joining("\n"));
    return loc.get("subscriptions.expired", language, list);
  }

  public String subscriptionsExpiringSoon(Language language, List<Subscription> subscriptions) {
    String list =
        subscriptions.stream()
            .map(s -> "• " + s.getChannel().getName() + " — " + s.getExpireDate().toLocalDate())
            .collect(Collectors.joining("\n"));
    return loc.get("subscriptions.expiring_soon", language, list);
  }
}
