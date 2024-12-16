package io.ss3rg3.selenium;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

class BasicExample {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptionsBuilder()
                .build();
        ChromeDriver driver = new ChromeDriver(options);


        driver.get("https://tls.peet.ws/api/all");

        Thread.sleep(120_000);
        driver.quit();

    }


}
