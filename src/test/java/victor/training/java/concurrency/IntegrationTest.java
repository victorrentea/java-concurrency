package victor.training.java.concurrency;

import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(properties = "remote.api.base.url=http://localhost:${wiremock.server.port}",
    webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
public class IntegrationTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @ValueSource(strings = {
      "threads",
      "thread-pools",
      "futures",
      "callbacks",
      "virtual-threads",
      "virtual-callbacks",
      "structured-concurrency"})
  @ParameterizedTest(name = "{0}")
  void drink(String uri) throws Exception {
    ResponseEntity<String> response = restTemplate.getForEntity("/" + uri, String.class);
    assertThat(response.getStatusCode().value()).isEqualTo(200);
    assertThat(response.getBody()).isEqualTo("""
        {"beer":{"type":"blond"},"vodka":{"type":"deadly"}}""");
  }

}
