package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Instant;
import java.util.Map;

public class ExtractJsVariables {

    public static void main(String[] args) {

        ChromeDriver driver = new ChromeDriver(WebDriverUtils.defaultOptionsWithBiDi(false));
        WebDriverUtils.BiDiEnableLogging(driver);
        WebDriverUtils.BiDiPreloadStealthJS(driver);

        Instant start = Instant.now();
        driver.get("https://www.seokratie.de");
        Map<String, Object> jsVars = WebDriverUtils.extractJsVariables(driver);
        Instant elapsedTime = Instant.now().minusMillis(start.toEpochMilli());
        System.out.println("Took: " + elapsedTime.toEpochMilli() + "ms");
//        jsVars.forEach((key, value) -> System.out.println(key + " : " + value));
        String borlabsCookieConfig = findValueByNestedKey(jsVars, "borlabsCookieConfig");
//        System.out.println(borlabsCookieConfig);
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
            // Cast is safe because we check instanceof before
            @SuppressWarnings("unchecked")
            Map<String, Object> currentMap = (Map<String, Object>) currentValue;
            currentValue = currentMap.get(key);
            if (currentValue == null) {
                return null; // Key not found
            }
        }

        return String.valueOf(currentValue);
    }

}
