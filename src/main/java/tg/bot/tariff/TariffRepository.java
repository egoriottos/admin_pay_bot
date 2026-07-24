package tg.bot.tariff;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
  List<Tariff> findByChannelCode(String code);

  Tariff findTariffById(Long id);
}
