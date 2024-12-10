package io.ss3rg3.selenium;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

class FullSetup {

    public static void main(String[] args) throws Exception {

        boolean shouldUseHeadless = false;

        ChromeDriver driver = new ChromeDriver(WebDriverUtils.defaultOptionsWithBiDi(shouldUseHeadless, "107.172.163.27:6543"));
        WebDriverUtils.BiDiEnableLogging(driver);
        driver.register(UsernameAndPassword.of("clproxyuser", "b3ff6bf2b78e07"));


//            driver.get("https://www.cyberport.de");
//        driver.get("https://ipapi.co/");
        driver.get("http://cyberport.de/");
//        driver.get("https://whatismyipaddress.com/");
//        driver.get("https://www.linkedin.com/company/techflex-inc-");
//            driver.get("https://4chan.org/");
//            driver.get("https://nowsecure.nl/");
//            driver.get("https://abrahamjuliot.github.io/creepjs/");
//            driver.get("https://bot.sannysoft.com/");


        String html = WebDriverUtils.getHtmlAfterDuration(10000, driver);
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
