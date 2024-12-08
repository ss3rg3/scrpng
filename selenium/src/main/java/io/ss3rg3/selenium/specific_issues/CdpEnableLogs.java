package io.ss3rg3.selenium.specific_issues;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v124.runtime.Runtime;

import java.io.File;

public class CdpEnableLogs {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "/media/cc/backup/_trash/selenium/chromedriver_linux64/chromedriver_copy");
        options.setBinary("/media/cc/backup/_trash/selenium/chrome-linux/chrome");
        options.addExtensions(new File("/media/cc/backup/_trash/selenium/ublock/ublock.crx"));
        options.addArguments("--user-data-dir=/media/cc/backup/_trash/selenium/Default");

        ChromeDriver driver = new ChromeDriver(options);
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Runtime.enable());

        devTools.addListener(
                Runtime.consoleAPICalled(),
                event -> System.out.println(event.getArgs().get(0).getValue().orElse("")));

        driver.navigate().to("https://www.spiegel.de");
        driver.executeScript("console.log('Hello from the page')");
        Thread.sleep(1000);
        devTools.close();
        // driver.quit();
    }


}
