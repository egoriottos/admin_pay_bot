package tg.bot.localization;

import org.springframework.stereotype.Component;

@Component
public class LanguageResolver {
  public static Language resolve(String telegramLanguageCode) {
    if (telegramLanguageCode == null) {
      return Language.EN;
    }
    return telegramLanguageCode.toLowerCase().startsWith("ru") ? Language.RU : Language.EN;
  }
}
