package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.time.Instant;

public class TakeScreenshot {

    public static void main(String[] args) {

        ChromeDriver driver = new ChromeDriver(WebDriverUtils.defaultOptionsWithBiDi(false));
        WebDriverUtils.BiDiEnableLogging(driver);
        WebDriverUtils.BiDiPreloadStealthJS(driver);

        Instant start = Instant.now();
//        driver.get("https://www.example.com");
        driver.get("https://www.techflex.de");

        Instant elapsedTime = Instant.now().minusMillis(start.toEpochMilli());
        System.out.println("Took: " + elapsedTime.toEpochMilli() + "ms");
        // todo wait for readyState==complete is useless. driver.get() does it by default
        Wait<ChromeDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(3))
                .pollingEvery(Duration.ofMillis(100));
        wait.until(d -> d.executeScript("return document.readyState").equals("complete"));
        elapsedTime = Instant.now().minusMillis(start.toEpochMilli());
        System.out.println("Took: " + elapsedTime.toEpochMilli() + "ms");
        WebDriverUtils.takeScreenshot(driver, "/tmp/test.png");

        driver.quit();
    }

}
