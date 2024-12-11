package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.ChromeOptionsBuilder;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Uses Brave instead of Chromium. All you need is the right Brave version matching the ChromeDriver and maybe
 * a profile.
 */
class Brave {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptionsBuilder()
                .overrideBinary("/media/cc/backup/_trash/selenium/brave/brave-browser/brave-browser")
                .build();
        options.addArguments("--user-data-dir=/media/cc/backup/_trash/selenium/brave/brave-profile");
        ChromeDriver driver = new ChromeDriver(options);

        driver.get("http://example.com/");

        Thread.sleep(10_000);
        driver.quit();

    }


}
