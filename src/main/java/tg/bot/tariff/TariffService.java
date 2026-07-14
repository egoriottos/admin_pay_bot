package tg.bot.tariff;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TariffService {
  private final TariffRepository tariffRepository;

  public List<Tariff> getByChannelCode(String code) {
    return tariffRepository.findByChannelCode(code);
  }
}
