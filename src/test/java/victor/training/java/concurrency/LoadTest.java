package victor.training.java.concurrency;

import base.GatlingEngine;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.Simulation;

import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static java.time.Duration.ofSeconds;

public class LoadTest extends Simulation {
  public static void main(String[] args) {
    GatlingEngine.startClass(LoadTest.class);
  }

  {
    PopulationBuilder p = Stream.of(
            "threads",
            "thread-pools",
            "futures",
            "callbacks",
            "virtual-threads",
            "virtual-callbacks",
            "structured-concurrency"
        )
        .map(scenarioName -> scenario(scenarioName)
            .exec(http(scenarioName).get("/" + scenarioName))
            .injectClosed(constantConcurrentUsers(50).during(ofSeconds(10))))
        .reduce(PopulationBuilder::andThen).orElseThrow();

    String host = "http://localhost:8080";
    setUp(p)
        .protocols(http.baseUrl(host))
        .assertions(global().successfulRequests().percent().gt(99.0));
  }
}
