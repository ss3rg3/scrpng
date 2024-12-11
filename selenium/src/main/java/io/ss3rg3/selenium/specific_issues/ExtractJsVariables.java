package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.ChromeOptionsBuilder;
import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Instant;
import java.util.Map;

/**
 * This will extract JavaScript variables from a page. Note that single variables are available as Map&gt;String, Object>
 * which allows for to iterate over them to find nested keys.
 */
public class ExtractJsVariables {

    public static void main(String[] args) {

        ChromeOptions options = new ChromeOptionsBuilder().build();
        ChromeDriver driver = new ChromeDriver(options);

        Instant start = Instant.now();
        driver.get("https://www.seokratie.de");
        Instant elapsedTime = Instant.now().minusMillis(start.toEpochMilli());
        System.out.println("Took: " + elapsedTime.toEpochMilli() + "ms");

        Map<String, Object> jsVars = WebDriverUtils.extractJsVariables(driver);
        jsVars.forEach((key, value) -> System.out.println(key + " : " + value));
        String borlabsCookieConfig = findValueByNestedKey(jsVars, "borlabsCookieConfig");
        System.out.println(borlabsCookieConfig);
        String borlabsCookieConfigContentBlockers = findValueByNestedKey(jsVars, "borlabsCookieConfig.contentBlockers.default.description");
        System.out.println(borlabsCookieConfigContentBlockers);

        driver.quit();
    }

    public static String findValueByNestedKey(Map<String, Object> map, String nestedKey) {
        String[] keys = nestedKey.split("\\.");
        Object currentValue = map;

        for (String key : keys) {
            if (!(currentValue instanceof Map)) {
                return null; // Not a Map, so we cannot proceed further
            }
            @SuppressWarnings("unchecked") // Cast is safe because we check instanceof before
            Map<String, Object> currentMap = (Map<String, Object>) currentValue;
            currentValue = currentMap.get(key);
            if (currentValue == null) {
                return null; // Key not found
            }
        }

        return String.valueOf(currentValue);
    }

}
