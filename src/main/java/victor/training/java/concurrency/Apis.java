package victor.training.java.concurrency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import victor.training.java.concurrency.domain.Beer;
import victor.training.java.concurrency.domain.Preferences;
import victor.training.java.concurrency.domain.Vodka;

@Slf4j
@RequiredArgsConstructor
@Service
public class Apis {
  private final RestClient restClient;
  private final RestTemplate rest;

  public Preferences fetchPreferences() {
    return restClient.get().uri("/preferences").retrieve().body(Preferences.class);
//    return rest.getForObject("/preferences", Preferences.class);
  }

  public Beer fetchBeer(Preferences pref) {
    return restClient.get().uri("/beer/" + pref.favoriteBeerType()).retrieve().body(Beer.class);
//    return rest.getForObject("/beer/" + pref.favoriteBeerType(), Beer.class);
  }

  public Vodka fetchVodka() {
    return restClient.get().uri("/vodka").retrieve().body(Vodka.class);
//    return rest.getForObject("/vodka", Vodka.class);
  }

}
