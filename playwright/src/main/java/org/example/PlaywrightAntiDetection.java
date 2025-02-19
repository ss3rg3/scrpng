package org.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.Page.WaitForLoadStateOptions;
import com.microsoft.playwright.options.Geolocation;
import com.microsoft.playwright.options.LoadState;

import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlaywrightAntiDetection {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Playwright playwright = Playwright.create();
        String extensionPath = "/home/cc/coldleads/profiles/1/Default/Extensions/ublock"; // uBlock Origin example

        BrowserContext context = playwright.chromium().launchPersistentContext(
                Paths.get("/home/cc/coldleads/profiles/1"),
                new BrowserType.LaunchPersistentContextOptions()
                        .setExecutablePath(Paths.get("/var/coldleads/chrome"))
                        .setArgs(List.of(
                                "--disable-extensions-except=" + extensionPath, // Mandatory, without this extension won't load
                                "--load-extension=" + extensionPath, // Not mandatory, but we add it anyway
                                "--disable-infobars",
                                "--headless=new",
                                "--disable-blink-features=AutomationControlled" // Prevents detection as an automated browser
                        ))
                        .setHeadless(true)
                        .setAcceptDownloads(false)
                        .setGeolocation(new Geolocation(40.72058285077201, -74.00126652986674)));

        Page page = context.newPage();

        Set<String> xhrRequests = new LinkedHashSet<>();
        page.onRequest(request -> xhrRequests.add(request.url()));
        page.onResponse(response -> xhrRequests.add(response.url()));

        String url = "chrome://extensions";
        Response hpResp = page.navigate(url); // This must be wrapped with try-catch. This is where errors are thrown.

        try {
            WaitForLoadStateOptions TIMEOUT_5000MS = new WaitForLoadStateOptions().setTimeout(1000);
            page.waitForLoadState(LoadState.NETWORKIDLE, TIMEOUT_5000MS);
        } catch (Exception e) {
            System.out.println("TIMEOUT"); // Timeout must be caught or
        }

        Map<String, Object> jsVars = extractJsVariables(page); // After scripts are loaded

        xhrRequests.forEach(System.out::println);

        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("/tmp").resolve(url.replaceAll("\\W", "") + ".png"))
                .setFullPage(false));

        System.out.println("Headers");
        hpResp.headersArray().forEach(httpHeader -> System.out.println(httpHeader.name + " - " + httpHeader.value));
        System.out.println("-------");
        String html = page.innerHTML("html");
        System.out.println("HTML length: " + html.length());
        System.out.println("HOMEPAGE: " + hpResp.status() + " - " + hpResp.url() + " - Headers: " + hpResp.headers().size());


        // Close browser
        context.close();
        playwright.close();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> extractJsVariables(Page page) {
        return (Map<String, Object>) page.evaluate("""
                    () => {
                        let result = {};
                        Object.getOwnPropertyNames(window).forEach(key => {
                            const value = window[key];
                            switch (typeof value) {
                                case 'undefined':
                                case 'function':
                                    result[key] = null;
                                    break;
                                default:
                                    try {
                                        result[key] = JSON.parse(JSON.stringify(value));
                                    } catch (err) {
                                        // Ignore circular references
                                    }
                                    break;
                            }
                        });
                        return result;
                    }
                """);
    }

    private static HttpHeaders createRealisticHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        headers.add("Accept-Language", "en-US,en;q=0.9");
        headers.add("Accept-Encoding", "gzip, deflate, br");
        headers.add("DNT", "1");
        headers.add("Upgrade-Insecure-Requests", "1");
        headers.add("Sec-Fetch-Dest", "document");
        headers.add("Sec-Fetch-Mode", "navigate");
        headers.add("Sec-Fetch-Site", "none");
        headers.add("Sec-Fetch-User", "?1");
        headers.add("Cache-Control", "max-age=0");
        headers.add("Pragma", "no-cache");
        return headers;
    }

    static class HttpHeaders extends java.util.HashMap<String, String> {
        public void add(String name, String value) {
            this.put(name, value);
        }
    }
}
