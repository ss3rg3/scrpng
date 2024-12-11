package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.ChromeOptionsBuilder;
import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

/**
 * This will take a screenshot and directly compress it via ImageIO.
 */
public class TakeScreenshot {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptionsBuilder().build();
        options.addExtensions(new File("/media/cc/backup/_trash/selenium/ublock/ublock.crx")); // without UBlock you will get cookie consent dialogs
        options.addArguments("--user-data-dir=/media/cc/backup/_trash/selenium/Default");
        ChromeDriver driver = new ChromeDriver(options);

        Instant start = Instant.now();
        driver.get("https://www.amazon.com");
        Instant elapsedTime = Instant.now().minusMillis(start.toEpochMilli());
        System.out.println("Took: " + elapsedTime.toEpochMilli() + "ms");

        /* !! WAITING FOR readyState==complete IS USELESS. driver.get() does it by default
        Wait<ChromeDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofMillis(100));
        wait.until(d -> d.executeScript("return document.readyState").equals("complete"));
        // --- NO NEED FOR WAITING, SEE ABOVE */

        WebDriverUtils.takeScreenshot(driver, "/tmp/test.png");
        elapsedTime = Instant.now().minusMillis(start.toEpochMilli());
        System.out.println("Took: " + elapsedTime.toEpochMilli() + "ms");
        Thread.sleep(10000);

        driver.quit();
    }

}
