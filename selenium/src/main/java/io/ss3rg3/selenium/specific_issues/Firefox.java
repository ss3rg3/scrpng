package io.ss3rg3.selenium.specific_issues;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;

public class Firefox {

    /**
     * <h3 style='color:orange'>You need the apt version</h3>
     * See here: https://askubuntu.com/questions/1399383/how-to-install-firefox-as-a-traditional-deb-package-without-snap-in-ubuntu-22/1404401#1404401
     */
    public static void main(String[] args) throws Exception {

        System.setProperty("webdriver.gecko.driver", "/home/cc/Downloads/geckodriver");
        FirefoxProfile profile = new FirefoxProfile(new File("/home/cc/Desktop/_trash/firefox"));

        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);
         // options.addArguments("--headless");
        WebDriver driver = new FirefoxDriver(options);
        driver.get("https://www.cyberport.de");

        Thread.sleep(20000);

        // driver.quit();

    }

}
