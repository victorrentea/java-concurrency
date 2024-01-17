package victor.training.java.concurrency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.java.concurrency.domain.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static victor.training.java.concurrency.SpringApp.executor;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ThreadPools {
  private final Apis apis;

  @GetMapping("/thread-pools")
  public DillyMutable threadPools() throws InterruptedException {
    DillyMutable dilly = new DillyMutable();
    Preferences pref = apis.fetchPreferences();

    var done = new CountDownLatch(2); // i await for 2 responses
    executor.execute(() -> {
      dilly.setBeer(apis.fetchBeer(pref));
      done.countDown();
    });
    executor.execute(() -> {
      dilly.setVodka(apis.fetchVodka());
      done.countDown();
    });
    done.await();
    log.info("Returning: {}", dilly);
    return dilly;
  }
  // === ANCIENT ===
  // ✅ Reuses expensive threads
  // ❌ Mutates state: risk of race bugs / deadlocks protecting the mutations
}

