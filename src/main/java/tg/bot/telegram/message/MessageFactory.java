package tg.bot.telegram.message;

import tg.bot.localization.Language;
import tg.bot.localization.LocalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageFactory {
    private final LocalizationService loc;

    public String chooseLanguage() {
        return loc.get("language.choose", Language.EN)+"/"+loc.get("language.choose", Language.RU);
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
        return loc.get("cornix.settings",language);
    }
}
