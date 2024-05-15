package io.ss3rg3.selenium;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.module.LogInspector;
import org.openqa.selenium.bidi.module.Script;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v124.network.Network;
import org.openqa.selenium.devtools.v124.network.model.RequestId;
import org.openqa.selenium.devtools.v124.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v124.network.model.Response;
import org.openqa.selenium.devtools.v124.network.model.ResponseReceived;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WebDriverUtils {

    /**
     * `driver.get()` returns after `readyState == complete`
     */
    public static String getHtmlAfterDuration(int durationInMs, ChromeDriver driver) throws InterruptedException {
        Thread.sleep(durationInMs); // Wait for page being loaded before getting the HTML
        return driver.getPageSource();
    }

    public static ChromeOptions defaultOptionsWithBiDi(boolean shouldUseHeadless) {
        System.setProperty("webdriver.chrome.driver", "/home/cc/Desktop/_trash/selenium/chromedriver_linux64/chromedriver_copy");

        ChromeOptions options = new ChromeOptions();
        options.setBinary("/home/cc/Desktop/_trash/selenium/chrome-linux/chrome");
        options.setCapability("webSocketUrl", true);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", null);
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.addArguments("--user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
        options.addArguments("--disable-infobars");
        if (shouldUseHeadless) {
            options.addArguments("--headless");
        }
        return options;
    }

    public static ChromeOptions defaultOptions(boolean shouldUseHeadless) {
        System.setProperty("webdriver.chrome.driver", "/home/cc/Desktop/_trash/selenium/chromedriver_linux64/chromedriver_copy");

        ChromeOptions options = new ChromeOptions();
        options.setBinary("/home/cc/Desktop/_trash/selenium/chrome-linux/chrome");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", null);
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.addArguments("--user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
        options.addArguments("--disable-infobars");
        if (shouldUseHeadless) {
            options.addArguments("--headless");
        }
        return options;
    }

    public static void BiDiEnableLogging(WebDriver driver) {
        try (LogInspector logInspector = new LogInspector(driver)) {
            logInspector.onConsoleEntry(logEntry -> System.out.println(logEntry.getText()));
        }
    }

    public static void BiDiPreloadStealthJS(WebDriver driver) {
        try {
            String code = Files.readString(Paths.get("/home/cc/Desktop/_trash/stealth/stealth.min.js"));
            try (Script script = new Script(driver)) {
                script.addPreloadScript("() => { " + code + " }");
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void cdpPreloadStealthJS(ChromeDriver driver) {
        try {
            String code = Files.readString(Paths.get("/home/cc/Desktop/_trash/selenium/stealth.min.js"));
            Map<String, Object> params = new HashMap<>();
            params.put("source", code);
            driver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }



    public static void takeScreenshot(ChromeDriver driver, String path) {
        try {
            driver.manage().window().setSize(new Dimension(1300, 1080));
            Files.write(Paths.get(path), driver.getScreenshotAs(org.openqa.selenium.OutputType.BYTES));
            driver.manage().window().maximize();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * JSON.stringify the `window` object and cast into a Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> extractJsVariables(ChromeDriver driver) {
        return (Map<String, Object>) driver.executeScript("""
                    let result = {}
                    Object.getOwnPropertyNames(window).forEach(key => {
                        const value = window[key];
                        switch (typeof value) {
                           case 'undefined':
                               result[key] = null;
                               break;
                           case 'function':
                               result[key] = null;
                               break;
                           default:
                               try {
                                   result[key] = JSON.parse(JSON.stringify(value));
                               } catch(err) {
                                   // Some values have circular references which throw errors
                               }
                               break;
                        }
                      });
                      return result;
                """);
    }

    /**
     * This adds a listener to record the HTTP URL of each request. Note that you need an open DevTools session.
     * Afterward, you should close the session with `devTools.close()` before navigating to another page.
     */
    public static Map<String, RequestWillBeSent> addListenerToRecordXhrRequests(DevTools devToolsWithOpenSession) {
        devToolsWithOpenSession.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        Map<String, RequestWillBeSent> requestWillBeSents = new LinkedHashMap<>();
        devToolsWithOpenSession.addListener(Network.requestWillBeSent(), requestWillBeSent -> {
            if (requestWillBeSent.getRequest().getUrl().startsWith("http")) {
                requestWillBeSents.put(requestWillBeSent.getRequestId().toString(), requestWillBeSent);
            }
        });
        return requestWillBeSents;
    }

    public static Map<String, ResponseReceived> addListenerToRecordXhrResponses(DevTools devToolsWithOpenSession) {
        devToolsWithOpenSession.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        Map<String, ResponseReceived> responsesReceived = new LinkedHashMap<>();

        devToolsWithOpenSession.addListener(Network.responseReceived(), response -> {
            Response receivedResponse = response.getResponse();
            if (receivedResponse.getUrl().startsWith("http")) {
                responsesReceived.put(response.getRequestId().toJson(), response);
            }
        });
        return responsesReceived;
    }


}
