package tg.bot.channel;

import tg.bot.tariff.Tariff;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;

    public List<Channel> getAll() {
        return channelRepository.findAll();
    }
}
