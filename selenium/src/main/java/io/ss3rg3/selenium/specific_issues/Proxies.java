package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.ChromeOptionsBuilder;
import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * This will open a browser with a proxy server. For Basic Auth you must use `driver.register(UsernameAndPassword.of(username, password))`.
 * All other solution are crap.
 */
class Proxies {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptionsBuilder()
                .proxyServer("107.172.163.27:6543")
                .build();
        ChromeDriver driver = new ChromeDriver(options);
        WebDriverUtils.registerProxyCredentials("username", "password", driver);

        driver.get("https://whatismyipaddress.com/");
        // driver.get("https://api64.ipify.org/?format=json");

        Thread.sleep(10000);

        driver.quit();
    }

}
