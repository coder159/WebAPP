package com.example.ui.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class BaseUiTest {
    protected WebDriver driver;
    protected String baseUrl = System.getProperty("baseUrl", "http://localhost:5173");

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new org.openqa.selenium.Dimension(1280, 900));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }
}
