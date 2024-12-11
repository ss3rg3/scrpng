package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.ChromeOptionsBuilder;
import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This will execute a script before the loads. The variable in the `window` object will be available after the page load.
 */
public class CdpPreloadScripts {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptionsBuilder().build();
        ChromeDriver driver = new ChromeDriver(options);

        Map<String, Object> params = new HashMap<>();
        params.put("source", "console.log('This script will be executed before the page load'); window.varFromPreloadScript = 'I am a variable from Preload';");
        driver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);

        driver.navigate().to("https://www.example.com");
        Object result = driver.executeScript("return window.varFromPreloadScript;");
        System.out.println("Value of varFromPreloadScript: " + result);

        result = driver.executeScript("return window.chrome;");
        System.out.println("window.chrome: " + result);
        Thread.sleep(1000);
        driver.quit();
    }


}
