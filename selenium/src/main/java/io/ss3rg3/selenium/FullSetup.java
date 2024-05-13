package io.ss3rg3.selenium;

import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

class FullSetup {

    public static void main(String[] args) throws Exception {

        boolean shouldUseHeadless = false;
        ChromeDriver driver = new ChromeDriver(WebDriverUtils.defaultOptionsWithBiDi(shouldUseHeadless));
        WebDriverUtils.BiDiEnableLogging(driver);
        WebDriverUtils.BiDiPreloadStealthJS(driver);


//            driver.get("https://www.cyberport.de");
        driver.get("https://www.example.com");
//            driver.get("https://4chan.org/");
//            driver.get("https://nowsecure.nl/");
//            driver.get("https://abrahamjuliot.github.io/creepjs/");
//            driver.get("https://bot.sannysoft.com/");


        String html = WebDriverUtils.getHtmlAfterDuration(1000, driver);
        Files.writeString(
                Paths.get("/tmp/result.html"),
                html,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);


        driver.executeScript("console.log('After page load')");
        driver.executeScript("console.log('Chrome:', window.chrome)");
        driver.executeScript("console.log('Navigator.userAgent:', navigator.userAgent)");
        Thread.sleep(1000); // executeScript is async, wait for it to finish

        driver.quit();

    }



}
