package victor.training.java.concurrency;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class SpringApp {
  public static final ExecutorService executor = Executors.newFixedThreadPool(200);

  public static void main(String[] args) {
    SpringApplication.run(SpringApp.class, args);
  }

//  @Bean
//  public RestClient restClient(@Value("${remote.api.base.url}") String baseUrl) {
//    return RestClient.builder()
//        .baseUrl(baseUrl) // wiremock port (run StartWiremock.java)
//        .build();
//  }

  @Bean
  public RestClient restClient(RestTemplate rest, @Value("${remote.api.base.url}") String baseUrl) {
    return RestClient.builder(rest).baseUrl(baseUrl).build();
  }

  @Bean
  public RestTemplate rest(@Value("${remote.api.base.url}") String baseUrl) {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(100);
    connectionManager.setDefaultMaxPerRoute(100);

    CloseableHttpClient httpClient = HttpClientBuilder.create()
        .setConnectionManager(connectionManager)
        .build();
    ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
    return new RestTemplateBuilder()
        .requestFactory(()->factory)
        .rootUri(baseUrl)
        .build();
  }
}
