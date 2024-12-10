package io.ss3rg3.selenium.specific_issues;

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

public class ExceptionsAndTimeouts {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = WebDriverUtils.defaultOptions(false);
        ChromeDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));

        Instant start = Instant.now();
        try {
            System.out.println("Navigating");
            driver.navigate().to("https://rosequartz.ws");
        } catch (Exception e) {
            System.out.println("Failed to navigate");
            // browsing can be continued: driver.navigate().to("http://seokratie.de");
            // throw new IllegalStateException("Failed to navigate", e);
        }

        Thread.sleep(100);
        Instant elapsedTime = Instant.now().minusMillis(start.toEpochMilli());
        System.out.println("Took: " + elapsedTime.toEpochMilli() + "ms");
        driver.quit();
    }

}
