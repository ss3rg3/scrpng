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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CdpExtractXhrRequests {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = WebDriverUtils.defaultOptions(false);
//        options.addExtensions(new File("/media/cc/backup/_trash/selenium/ublock/ublock.crx"));
//        options.addArguments("--user-data-dir=/media/cc/backup/_trash/selenium/Default");
        ChromeDriver driver = new ChromeDriver(options);

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        Map<String, RequestWillBeSent> requests = WebDriverUtils.addListenerToRecordXhrRequests(devTools);
        Map<String, ResponseReceived> responses = WebDriverUtils.addListenerToRecordXhrResponses(devTools);

        Instant start = Instant.now();
        driver.navigate().to("http://seokratie.de");

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
        private final String requestUrl;
        private final int status;
        private final Map<String, List<String>> headersMap;

        /**
         * Properly correlated request and response
         */
        public XhrRequest(RequestWillBeSent requestWillBeSent, ResponseReceived responseReceived) {
            this.requestUrl = requestWillBeSent.getRequest().getUrl();
            this.status = responseReceived.getResponse().getStatus();

            // Convert headers to proper format
            Map<String, List<String>> headersMap = new HashMap<>();
            Map<String, Object> headersMapObject = responseReceived.getResponse().getHeaders().toJson();
            for (Map.Entry<String, Object> entry : headersMapObject.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    headersMap.put(key, List.of((String) value));
                } else if (value instanceof List) {
                    List<String> valueList = ((List<?>) value).stream()
                            .map(Object::toString)
                            .collect(Collectors.toList());
                    headersMap.put(key, valueList);
                } else {
                    headersMap.put(key, List.of(value.toString()));
                }
            }
            this.headersMap = headersMap;
        }

        /**
         * Request sent but no response received
         */
        public XhrRequest(RequestWillBeSent requestWillBeSent) {
            this.requestUrl = requestWillBeSent.getRequest().getUrl();
            this.status = 0;
            this.headersMap = new HashMap<>();
        }

        public String getRequestUrl() {
            return requestUrl;
        }

        public int getStatus() {
            return status;
        }

        public Map<String, List<String>> getHeadersMap() {
            return headersMap;
        }
    }

}
