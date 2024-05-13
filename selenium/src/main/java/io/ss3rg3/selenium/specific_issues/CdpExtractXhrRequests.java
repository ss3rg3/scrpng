package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;

import java.time.Instant;
import java.util.Set;

public class CdpExtractXhrRequests {

    public static void main(String[] args) throws Exception {

        ChromeDriver driver = new ChromeDriver(WebDriverUtils.defaultOptionsWithBiDi(false));
        WebDriverUtils.BiDiEnableLogging(driver);
        WebDriverUtils.BiDiPreloadStealthJS(driver);

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        Set<String> requestUrls = WebDriverUtils.addListenerToRecordXhrRequests(devTools);

        Instant start = Instant.now();
        driver.navigate().to("https://www.seokratie.de");

        requestUrls.forEach(System.out::println);
        Instant elapsedTime = Instant.now().minusMillis(start.toEpochMilli());
        System.out.println("Took: " + elapsedTime.toEpochMilli() + "ms");
        devTools.close();

        start = Instant.now();
        devTools.createSession();
        requestUrls = WebDriverUtils.addListenerToRecordXhrRequests(devTools);
        driver.navigate().to("https://www.otto.de");

        requestUrls.forEach(System.out::println);
        elapsedTime = Instant.now().minusMillis(start.toEpochMilli());
        System.out.println("Took: " + elapsedTime.toEpochMilli() + "ms");

        driver.quit();
    }


}
