package tg.bot.telegram.keyboard;

import tg.bot.channel.Channel;
import tg.bot.localization.Language;
import tg.bot.localization.LocalizationService;
import tg.bot.tariff.Tariff;
import tg.bot.utils.Callback;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KeyboardFactory {
    @Value("${telegram.free_channel}")
    private String FREE_CHANNEL;
    private final LocalizationService localizationService;

    /**
     * Выбор языка.
     * Здесь язык еще не выбран, поэтому кнопки всегда отображаются
     * каждая на своем языке.
     */
    public InlineKeyboardMarkup languageKeyboard() {
        return keyboard(List.of(
                row(button(Language.RU, "button.language", "LANG_RU")),
                row(button(Language.EN, "button.language", "LANG_EN"))
        ));
    }

    /**
     * Главное меню.
     */
    public InlineKeyboardMarkup mainMenu(Language language) {
        return keyboard(mainMenuRows(language));
    }

    private List<List<InlineKeyboardButton>> mainMenuRows(Language language) {
        return List.of(
                row(button(language, "button.elite.trend", Callback.ELITE_TREND)),
                row(button(language, "button.cti.pro", Callback.CTI_PRO)),
                row(button(language, "button.trade.b.and.e", Callback.TRADE_BE)),
                row(button(language, "button.referral", Callback.REFERRAL)),
                row(button(language, "button.my.subs", Callback.MY_SUBS)),
                row(button(language, "button.registration.cornix", Callback.REGISTER_CORNIX)),
                row(button(language, "button.cornix.settings", Callback.CORNIX_SETTINGS)),
                row(button(language, "button.help", Callback.HELP)),
                row(urlButton(localizationService.get("button.cti_subscribe", language), FREE_CHANNEL))
        );
    }

    /**
     * Кнопка "Назад".
     */
    public InlineKeyboardMarkup back(Language language, String callback) {
        return keyboard(List.of(
                row(button(language, "button.back", callback))
        ));
    }


    /**
     * Создает локализованную кнопку.
     */
    private InlineKeyboardButton button(Language language, String messageKey, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(localizationService.get(messageKey, language))
                .callbackData(callbackData)
                .build();
    }

    /**
     * Создает кнопку-ссылку
     */
    private InlineKeyboardButton urlButton(String text, String url) {
        return InlineKeyboardButton.builder()
                .text(text)
                .url(url)
                .build();
    }

    /**
     * Создает строку кнопок.
     */
    @SafeVarargs
    private final List<InlineKeyboardButton> row(InlineKeyboardButton... buttons) {
        return List.of(buttons);
    }

    /**
     * Создает клавиатуру.
     */
    private InlineKeyboardMarkup keyboard(List<List<InlineKeyboardButton>> rows) {
        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    /**
     * Создает кнопки каналов в настройках корникса.
     */
    public InlineKeyboardMarkup channelsCornixSettings(Language language, List<Channel> channels) {
        List<List<InlineKeyboardButton>> rows = channels.stream()
                .map(channel -> row(
                        channelButton(language, channel)
                ))
                .collect(Collectors.toList());
        rows.add(row(button(language, "button.back", Callback.MAIN_MENU)));
        return keyboard(rows);
    }

    /**
     * Создает кнопки тарифов.
     */
    public InlineKeyboardMarkup tariffs(Language language, List<Tariff> tariffs) {
        List<List<InlineKeyboardButton>> rows = tariffs.stream()
                .map(tariff -> row(
                        tariffButton(language, tariff)
                ))
                .collect(Collectors.toList());
        rows.add(row(button(language, "button.back", Callback.MAIN_MENU)));
        return keyboard(rows);
    }

    private InlineKeyboardButton channelButton(Language language, Channel channel) {
        return InlineKeyboardButton.builder()
                .text(channelText(language,channel))
                .callbackData("CORNIX_SETTINGS")
                .build();
    }

    private InlineKeyboardButton tariffButton(Language language, Tariff tariff) {
        return InlineKeyboardButton.builder()
                .text(tariffText(language, tariff))
                .callbackData("BUY_TARIFF_" + tariff.getId())
                .build();
    }

    private String channelText(Language language, Channel channel) {
        String key = switch (channel.getCode()) {
            case "ELITE_TREND" -> "button.elite.trend";
            case "CTI_PRO" -> "button.cti.pro";
            case "TRADE_BE" -> "button.trade.b.and.e";
            default -> throw new IllegalArgumentException("Unsupported channel code: " + channel.getCode());
        };
        return localizationService.get(key,language, channel.getName());
    }

    private String tariffText(Language language, Tariff tariff) {
        String key = switch (tariff.getMonths()) {
            case 1 -> "tariff.month_1";
            case 3 -> "tariff.month_3";
            case 6 -> "tariff.month_6";
            case 12 -> "tariff.year_1";
            default -> throw new IllegalArgumentException("Unsupported tariff period: " + tariff.getMonths());
        };
        return localizationService.get(key, language, tariff.getPrice());
    }
}
