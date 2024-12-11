package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.ChromeOptionsBuilder;
import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

class BiDiLogging {

    /**
     * This will print the console output when executing JavaScript on a page.
     */
    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptionsBuilder()
                .enableBidi()
                .build();
        ChromeDriver driver = new ChromeDriver(options);
        WebDriverUtils.BiDiEnableLogging(driver);

        driver.get("http://example.com/");

        driver.executeScript("console.log('After page load')");
        driver.executeScript("console.log('Chrome:', window.chrome)");
        driver.executeScript("console.log('Navigator.userAgent:', navigator.userAgent)");
        Thread.sleep(1000); // executeScript is async, wait for it to finish

        driver.quit();

    }


}
