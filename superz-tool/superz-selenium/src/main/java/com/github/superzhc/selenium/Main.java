package com.github.superzhc.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverInfo;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author superz
 * @create 2022/2/16 18:15
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", "D:\\code\\SuperzHadoop\\superz-tool\\superz-selenium\\webdriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.baidu.com");
        System.out.println(driver.getTitle());

        Thread.sleep(1000 * 60 * 10);

        driver.quit();
    }
}
