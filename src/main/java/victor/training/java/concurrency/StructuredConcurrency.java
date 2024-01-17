package victor.training.java.concurrency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.java.concurrency.domain.Dilly;
import victor.training.java.concurrency.domain.Preferences;

import java.util.concurrent.StructuredTaskScope.ShutdownOnFailure;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StructuredConcurrency {
  private final Apis apis;

  @GetMapping("/structured-concurrency")
  public Dilly structuredConcurrency() throws Exception {
    Preferences pref = apis.fetchPreferences();

    try (var scope = new ShutdownOnFailure()) {
      var beerTask = scope.fork(() -> apis.fetchBeer(pref)); // +1 child virtual thread
      var vodkaTask = scope.fork(apis::fetchVodka); // +1 child virtual thread

      scope.join().throwIfFailed(); // throw exception if any subtasks failed

      return new Dilly(beerTask.get(), vodkaTask.get());
    }
  }
  // === POST-MODERN ===
  // ✅ If client aborts request => subtasks are cancelled
  // ✅ If one subtask fails => the other task is interrupted
  // ✅ JFR profiler can link children threads with parent thread

}

