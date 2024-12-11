package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.ChromeOptionsBuilder;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

/**
 * This adds an extension to the browser session via .crx file which is configured in the profile.
 * You can add configuration by running the driver and editing the extension settings manually as you would do in a normal browser.
 */
public class AddChromeExtension {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptionsBuilder().build();
        options.addExtensions(new File("/media/cc/backup/_trash/selenium/ublock/ublock.crx"));
        options.addArguments("--user-data-dir=/media/cc/backup/_trash/selenium/Default");
        ChromeDriver driver = new ChromeDriver(options);

        driver.navigate().to("https://spiegel.de/");
        Object result = driver.executeScript("return window.chrome;");
        System.out.println("window.chrome: " + result);

        Thread.sleep(10000);
        driver.quit();
    }


}
