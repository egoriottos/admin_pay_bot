package tg.bot.user;

import tg.bot.localization.Language;
import tg.bot.localization.LanguageResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    public User getOrCreate(org.telegram.telegrambots.meta.api.objects.User tgUser) {
        User user = repository.findByTelegramId(tgUser.getId())
                .orElseGet(User::new);

        user.setTelegramId(tgUser.getId());
        user.setUsername(tgUser.getUserName());
        user.setFirstName(tgUser.getFirstName());
        user.setLastName(tgUser.getLastName());
        user.setLanguage(LanguageResolver.resolve(tgUser.getLanguageCode()));

        return repository.save(user);
    }

    public void setLanguage(Long telegramId, Language lang) {
        User user = repository.findByTelegramId(telegramId).
                orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setLanguage(lang);
        repository.save(user);
    }

    public User findByTelegramId(Long telegramId) {
        return repository.findByTelegramId(telegramId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
