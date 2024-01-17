package victor.training.java.concurrency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.java.concurrency.domain.Beer;
import victor.training.java.concurrency.domain.Dilly;
import victor.training.java.concurrency.domain.Preferences;
import victor.training.java.concurrency.domain.Vodka;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

@RestController
@Slf4j
@RequiredArgsConstructor
public class VirtualThreads {
  private final Apis apis;

  @GetMapping("/virtual-threads")
  public Dilly virtual() {
    log.info("Start on {}", Thread.currentThread());
    Preferences pref = apis.fetchPreferences();
    Beer beer = apis.fetchBeer(pref);
    Vodka vodka = apis.fetchVodka();
    Dilly dilly = new Dilly(beer, vodka);
    log.info("Return: {} on {}", dilly, Thread.currentThread());
    return dilly;
  }
  // === MODERN ===
  // ✅ Easy to read
  // ✅ Memory-efficient
  // ❌ Slow: sequential call of fetchBeer, fetchVodka (not in parallel) - see below for solution
  // ❌ Risks: thread pinning, monopolization, heavy thread locals
  // ❌ No better for CPU Intensive flows

  @GetMapping("/virtual-callbacks")
  public Dilly virtualCallbacks() {
    log.info("Start on {}", Thread.currentThread());
    Preferences pref = apis.fetchPreferences();
    CompletableFuture<Beer> beerFuture = supplyAsync(() -> apis.fetchBeer(pref), newVirtualThreadPerTaskExecutor());
    CompletableFuture<Vodka> vodkaFuture = supplyAsync(apis::fetchVodka, newVirtualThreadPerTaskExecutor());
    Dilly dilly = new Dilly(beerFuture.join(), vodkaFuture.join());
    log.info("Returning: {} on {}", dilly, Thread.currentThread());
    return dilly;
  }
  // ✅ Parallel calls wrapped in CompletableFutures
  // ❌ Harder to read


}

