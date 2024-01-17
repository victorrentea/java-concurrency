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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static victor.training.java.concurrency.SpringApp.executor;

@RestController
@Slf4j
@RequiredArgsConstructor
public class Callbacks {
  private final Apis apis;

  @GetMapping("/callbacks")
  public CompletableFuture<Dilly> callbacks() {
    CompletableFuture<Preferences> prefPromise = supplyAsync(apis::fetchPreferences, executor);
    CompletableFuture<Beer> beerPromise = prefPromise.thenApplyAsync(apis::fetchBeer, executor);
    CompletableFuture<Vodka> vodkaPromise = supplyAsync(apis::fetchVodka, executor);
    CompletableFuture<Dilly> dillyPromise = beerPromise.thenCombine(vodkaPromise, Dilly::new);
    dillyPromise.thenAccept(dilly -> log.info("Returning: {}", dilly));
    return dillyPromise;
  }
  // === RENAISSANCE ===
  // ✅ Does not block the HTTP thread
  // ❌ Hard to read
}

