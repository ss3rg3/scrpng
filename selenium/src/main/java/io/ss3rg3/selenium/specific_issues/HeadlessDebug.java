package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.ChromeOptionsBuilder;
import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

class HeadlessDebug {

    /**
     * Will load the page and save the HTML to /tmp/result.html after some time.
     */
    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptionsBuilder()
                .enableHeadless()
                .build();
        ChromeDriver driver = new ChromeDriver(options);

        driver.get("http://example.com/");

        String html = WebDriverUtils.getHtmlAfterDuration(1000, driver);
        Files.writeString(
                Paths.get("/tmp/result.html"),
                html,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        driver.quit();

    }


}
