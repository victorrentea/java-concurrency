package victor.training.java.concurrency;

import com.github.tomakehurst.wiremock.standalone.WireMockServerRunner;

import java.io.File;
import java.io.IOException;

public class StartWireMock {
  public static void main(String[] args) throws IOException {
    File rootFolder = new File(".", "src/test/resources");
    File mappingsFolder = new File(rootFolder, "mappings");
    System.out.println("*.json mappings stubs expected at (click it🎯) file://" + mappingsFolder.getAbsolutePath());

    WireMockServerRunner.main(
            "--port", "9999",
            "--root-dir", rootFolder.getAbsolutePath(),
            "--global-response-templating", // UUID
            "--async-response-enabled=true" // enable Wiremock to not bottleneck on heavy load
    );
  }
}