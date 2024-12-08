package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CdpPreloadScripts {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "/media/cc/backup/_trash/selenium/chromedriver_linux64/chromedriver_copy");
        options.setBinary("/media/cc/backup/_trash/selenium/chrome-linux/chrome");
        options.addExtensions(new File("/media/cc/backup/_trash/selenium/ublock/ublock.crx"));
        options.addArguments("--user-data-dir=/media/cc/backup/_trash/selenium/Default");
        options.addArguments("--headless");

        ChromeDriver driver = new ChromeDriver(options);

        Map<String, Object> params = new HashMap<>();
        params.put("source", "console.log('This script will be executed on every page load'); window.varFromPreloadScript = 'I am a variable from Preload';");
        driver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);

        WebDriverUtils.cdpPreloadStealthJS(driver);

        driver.navigate().to("https://www.spiegel.de");
        Object result = driver.executeScript("return window.varFromPreloadScript;");
        System.out.println("Value of varFromPreloadScript: " + result);

        result = driver.executeScript("return window.chrome;");
        System.out.println("window.chrome: " + result);
        Thread.sleep(1000);
        // driver.quit();
    }


}
