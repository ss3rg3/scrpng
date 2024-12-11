package io.ss3rg3.selenium;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.module.LogInspector;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v124.network.Network;
import org.openqa.selenium.devtools.v124.network.model.RequestWillBeSent;
import org.openqa.selenium.devtools.v124.network.model.Response;
import org.openqa.selenium.devtools.v124.network.model.ResponseReceived;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class WebDriverUtils {

    /**
     * `driver.get()` returns after `readyState == complete`
     */
    public static String getHtmlAfterDuration(int durationInMs, ChromeDriver driver) throws InterruptedException {
        Thread.sleep(durationInMs); // Wait for page being loaded before getting the HTML
        return driver.getPageSource();
    }

    public static void BiDiEnableLogging(WebDriver driver) {
        try (LogInspector logInspector = new LogInspector(driver)) {
            logInspector.onConsoleEntry(logEntry -> System.out.println(logEntry.getText()));
        }
    }

    public static void BiDiPreloadStealthJS(WebDriver driver) {
        throw new IllegalStateException("Don't use. Development is suspended ( https://github.com/ariya/phantomjs/issues/15344 ) and it doesn't do anything");
//        try {
//            String code = Files.readString(Paths.get("/media/cc/backup/_trash/selenium/stealth.min.js"));
//            try (Script script = new Script(driver)) {
//                script.addPreloadScript("() => { " + code + " }");
//            }
//        } catch (IOException e) {
//            throw new IllegalStateException(e);
//        }
    }

    public static void cdpPreloadStealthJS(ChromeDriver driver) {
        throw new IllegalStateException("Don't use. Development is suspended ( https://github.com/ariya/phantomjs/issues/15344 ) and it doesn't do anything");
//        try {
//            String code = Files.readString(Paths.get("/media/cc/backup/_trash/selenium/stealth.min.js"));
//            Map<String, Object> params = new HashMap<>();
//            params.put("source", code);
//            driver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);
//        } catch (IOException e) {
//            throw new IllegalStateException(e);
//        }
    }

    public static void registerProxyCredentials(String username, String password, ChromeDriver driver) {
        driver.register(UsernameAndPassword.of(username, password));
    }

    public static void takeScreenshot(ChromeDriver driver, String path) {
        try {
            // Set initial window size
            driver.manage().window().setSize(new Dimension(1300, 1080));

            // Get original screenshot bytes
            byte[] originalBytes = driver.getScreenshotAs(org.openqa.selenium.OutputType.BYTES);

            // Convert bytes to BufferedImage
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalBytes));

            // Create resized image
            BufferedImage resizedImage = new BufferedImage(975, 810, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();

            // Set rendering hints for better quality
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw resized image
            g.drawImage(originalImage, 0, 0, 975, 810, null);
            g.dispose();

            // Prepare output stream for compressed image
            ByteArrayOutputStream compressedBytes = new ByteArrayOutputStream();

            // Get JPEG writer
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();

            // Set compression quality (0.0 - 1.0, lower = more compression)
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(0.7f);  // Adjust this value as needed, lower = lower quality, smaller file

            // Write compressed image
            try (ImageOutputStream output = ImageIO.createImageOutputStream(compressedBytes)) {
                writer.setOutput(output);
                writer.write(null, new IIOImage(resizedImage, null, null), writeParam);
            }

            // Save compressed image
            Files.write(Paths.get(path), compressedBytes.toByteArray());

            // Maximize window back
            driver.manage().window().maximize();

            // Clean up
            writer.dispose();
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
