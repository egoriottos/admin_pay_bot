package tg.bot.utils;

import java.io.InputStream;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CornixSettingUtils {
  public List<InputStream> getImages(String channelCode) {
    String folder =
        switch (channelCode) {
          case "ELITE_TREND" -> "elite_trend";
          case "CTI_PRO" -> "cti_pro";
          case "TRADE_BE" -> "trade_be";
          default -> throw new IllegalArgumentException("Unknown channel:" + channelCode);
        };
    return List.of(
        resource(folder + "/1.jpg"), resource(folder + "/2.jpg"), resource(folder + "/3.jpg"));
  }

  private InputStream resource(String path) {
    InputStream is = getClass().getClassLoader().getResourceAsStream("cornix_settings/" + path);
    if (is == null) {
      throw new IllegalArgumentException("Resource not found: " + path);
    }
    return is;
  }
}
