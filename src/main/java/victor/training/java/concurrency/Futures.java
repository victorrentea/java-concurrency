package victor.training.java.concurrency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.java.concurrency.domain.Beer;
import victor.training.java.concurrency.domain.Dilly;
import victor.training.java.concurrency.domain.Preferences;
import victor.training.java.concurrency.domain.Vodka;

import java.net.http.HttpClient;
import java.util.concurrent.*;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static victor.training.java.concurrency.SpringApp.executor;

@RestController
@Slf4j
@RequiredArgsConstructor
public class Futures {
  private final Apis apis;

  @GetMapping("/futures")
  public Dilly futures() throws ExecutionException, InterruptedException {
    Preferences pref = apis.fetchPreferences();
    Future<Beer> beerFuture = executor.submit(() -> apis.fetchBeer(pref));
    Future<Vodka> vodkaFuture = executor.submit(apis::fetchVodka);
    Dilly dilly = new Dilly(beerFuture.get(), vodkaFuture.get());
    log.info("Returning: {}", dilly);
    return dilly;
  }
  // === MIDDLE AGES ===
  // ✅ Functional-style: async ops return values, don't change state
  // ❌ Blocks the HTTP thread
}

