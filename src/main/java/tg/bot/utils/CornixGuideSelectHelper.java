package tg.bot.utils;

import tg.bot.localization.Language;
import org.springframework.stereotype.Component;
import java.io.InputStream;

@Component
public class CornixGuideSelectHelper {
    public InputStream getGuide(Language language) {
        String fileName = language == Language.RU ? "registration_guide_ru.pdf" : "registration_guide_en.pdf";

        return getClass().getClassLoader().getResourceAsStream("registration_guide/" + fileName);
    }
}
