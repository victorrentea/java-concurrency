package victor.training.java.concurrency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.java.concurrency.domain.DillyMutable;
import victor.training.java.concurrency.domain.Preferences;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class Threads {
  private final Apis apis;

  @GetMapping("/threads")
  public DillyMutable threads() throws InterruptedException {
    DillyMutable dilly = new DillyMutable();
    Preferences pref = apis.fetchPreferences();
    Thread t1 = new Thread(() -> dilly.setBeer(apis.fetchBeer(pref)));
    Thread t2 = new Thread(() -> dilly.setVodka(apis.fetchVodka()));
    t1.start();
    t2.start();

    t1.join();
    t2.join();
    log.info("Returning: {}", dilly);
    return dilly;
  }
  // === PREHISTORIC ===
  // ❌ Wasteful: creates new threads for each request
  // ❌ Mutates state: risk of race bugs / deadlocks protecting the mutations
}

