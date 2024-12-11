package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.ChromeOptionsBuilder;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v124.runtime.Runtime;

/**
 * This will enable the console logs in the browser and print them to the console.
 */
public class CdpEnableLogs {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptionsBuilder().build();
        ChromeDriver driver = new ChromeDriver(options);
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Runtime.enable());

        devTools.addListener(
                Runtime.consoleAPICalled(),
                event -> System.out.println(event.getArgs().get(0).getValue().orElse("")));

        driver.navigate().to("https://www.example.com/");
        driver.executeScript("console.log('Hello from the page')");
        Thread.sleep(1000);
        devTools.close();
        driver.quit();
    }


}
