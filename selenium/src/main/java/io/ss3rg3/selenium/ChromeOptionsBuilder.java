package io.ss3rg3.selenium;

import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;

public class ChromeOptionsBuilder {

    private final ChromeOptions options;

    public ChromeOptionsBuilder() {
        System.setProperty("webdriver.chrome.driver", "/media/cc/backup/_trash/selenium/chromedriver_linux64/chromedriver_copy");

        this.options = new ChromeOptions();
        this.options.setBinary("/media/cc/backup/_trash/selenium/chrome-linux/chrome");
        this.options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        this.options.setExperimentalOption("useAutomationExtension", null);
        this.options.addArguments("--no-sandbox");
        this.options.addArguments("--disable-dev-shm-usage");
        this.options.addArguments("--disable-blink-features=AutomationControlled");
        this.options.addArguments("--start-maximized");
        this.options.addArguments("--remote-allow-origins=*");
        this.options.addArguments("--user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
        this.options.addArguments("--disable-infobars");
    }

    public ChromeOptionsBuilder overrideBinary(String binaryPath) {
        this.options.setBinary(binaryPath);
        return this;
    }

    public ChromeOptionsBuilder enableHeadless() {
        this.options.addArguments("--headless");
        return this;
    }

    public ChromeOptionsBuilder enableBidi() {
        this.options.setCapability("webSocketUrl", true);
        return this;
    }

    public ChromeOptionsBuilder proxyServer(String proxyIpColonPort) {
        this.options.addArguments("--proxy-server=http://" + proxyIpColonPort);
        return this;
    }

    public ChromeOptions build() {
        return this.options;
    }

}
