package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

public class AddChromeExtension {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = WebDriverUtils.defaultOptions(false);
        options.addExtensions(new File("/home/cc/Desktop/_trash/selenium/ublock/ublock.crx"));
        options.addArguments("--user-data-dir=/home/cc/Desktop/_trash/selenium/Default");
//        options.addArguments("--headless");
        ChromeDriver driver = new ChromeDriver(options);
        WebDriverUtils.cdpPreloadStealthJS(driver);

        driver.navigate().to("https://www.spiegel.de");
        Object result = driver.executeScript("return window.chrome;");
        System.out.println("window.chrome: " + result);

        // driver.quit();
    }


}
