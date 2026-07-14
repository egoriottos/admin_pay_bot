package tg.bot.localization;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalizationService {
  private final MessageSource messageSource;

  public String get(String key, Language lang) {
    Locale locale = lang == Language.RU ? new Locale("ru") : Locale.ENGLISH;
    return messageSource.getMessage(key, null, locale);
  }

  public String get(String key, Language lang, Object... args) {
    Locale locale = lang == Language.RU ? new Locale("ru") : Locale.ENGLISH;
    return messageSource.getMessage(key, args, locale);
  }
}
