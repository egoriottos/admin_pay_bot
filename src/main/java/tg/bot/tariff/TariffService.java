package tg.bot.tariff;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffService {
    private final TariffRepository tariffRepository;

    public List<Tariff> getByChannelCode(String code) {
       return tariffRepository.findByChannelCode(code);
    }
}
