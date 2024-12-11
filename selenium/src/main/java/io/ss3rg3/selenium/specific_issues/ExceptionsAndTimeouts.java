package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.ChromeOptionsBuilder;
import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v124.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v124.network.model.ResponseReceived;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This shows how to handle exceptions and timeouts. `pageLoadTimeout` is the overall timeout. Simple errors like
 * `nx_domains` will throw directly but TCP connect issues need a timeout.
 */
public class ExceptionsAndTimeouts {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptionsBuilder().build();
        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));

        Instant start = Instant.now();
        try {
            System.out.println("Navigating");
            driver.navigate().to("https://rosequartz.ws"); // this will trigger `pageLoadTimeout`. nx_domains will also be caught.
        } catch (Exception e) {
            throw new IllegalStateException("Failed to navigate", e);
            // browsing can be continued: driver.navigate().to("http://seokratie.de");
        }

        Thread.sleep(1000);
        Instant elapsedTime = Instant.now().minusMillis(start.toEpochMilli());
        System.out.println("Took: " + elapsedTime.toEpochMilli() + "ms");
        driver.quit();
    }

}
