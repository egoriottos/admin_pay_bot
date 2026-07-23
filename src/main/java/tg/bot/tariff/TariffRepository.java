package tg.bot.tariff;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
  List<Tariff> findByChannelCode(String code);
  @Query("""
        select t
        from Tariff t
        join fetch t.channel
        where t.id = :id
    """)
  Optional<Tariff> findByIdWithChannel(Long id);
}
