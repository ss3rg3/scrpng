package io.ss3rg3.selenium.specific_issues;

import io.ss3rg3.selenium.WebDriverUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v124.network.model.RequestId;
import org.openqa.selenium.devtools.v124.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v124.network.model.ResponseReceived;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CdpExtractXhrRequests {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = WebDriverUtils.defaultOptions(false);
//        options.addExtensions(new File("/home/cc/Desktop/_trash/selenium/ublock/ublock.crx"));
//        options.addArguments("--user-data-dir=/home/cc/Desktop/_trash/selenium/Default");
        ChromeDriver driver = new ChromeDriver(options);

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        Map<String, RequestWillBeSent> requests = WebDriverUtils.addListenerToRecordXhrRequests(devTools);
        Map<String, ResponseReceived> responses = WebDriverUtils.addListenerToRecordXhrResponses(devTools);

        Instant start = Instant.now();
        driver.navigate().to("https://www.seokratie.de");

        Thread.sleep(100);
        List<XhrRequest> xhrRequests = compileXhrRequests(requests, responses);
        xhrRequests.forEach(xhrRequest -> System.out.println(xhrRequest.getRequestUrl() + " : " + xhrRequest.getStatus()));
        Instant elapsedTime = Instant.now().minusMillis(start.toEpochMilli());
        System.out.println("Took: " + elapsedTime.toEpochMilli() + "ms");
        devTools.close();
        driver.quit();
    }

    private static List<XhrRequest> compileXhrRequests(Map<String, RequestWillBeSent> requests,
                                                       Map<String, ResponseReceived> responses) {
        List<XhrRequest> xhrRequests = new ArrayList<>();

        for (Map.Entry<String, RequestWillBeSent> entry : requests.entrySet()) {
            String requestId = entry.getKey();
            RequestWillBeSent requestWillBeSent = entry.getValue();
            ResponseReceived responseReceived = responses.get(requestId);
            if (responseReceived != null) {
                xhrRequests.add(new XhrRequest(requestWillBeSent, responseReceived));
            } else {
                xhrRequests.add(new XhrRequest(requestWillBeSent));
            }
        }
        return xhrRequests;
    }

    public static class XhrRequest {
        private String requestUrl;
        private int status;

        /**
         * Properly correlated request and response
         */
        public XhrRequest(RequestWillBeSent requestWillBeSent, ResponseReceived responseReceived) {
            this.requestUrl = requestWillBeSent.getRequest().getUrl();
            this.status = responseReceived.getResponse().getStatus();
        }

        /**
         * Request sent but no response received
         */
        public XhrRequest(RequestWillBeSent requestWillBeSent) {
            this.requestUrl = requestWillBeSent.getRequest().getUrl();
            this.status = 0;
        }

        public String getRequestUrl() {
            return requestUrl;
        }

        public int getStatus() {
            return status;
        }
    }

}
