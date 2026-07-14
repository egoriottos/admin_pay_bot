package tg.bot.channel;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelService {
  private final ChannelRepository channelRepository;

  public List<Channel> getAll() {
    return channelRepository.findAll();
  }
}
