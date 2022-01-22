package com.sin.helper.jobs;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class Schedule {
    public Schedule() {
        try {
            WebClient client=new WebClient(BrowserVersion.getDefault(), null, -1);
            WebDriver driver=new ChromeDriver();
            //new WebDriverWait(driver, 10);
            driver.get("https://www.skolaonline.cz/Aktuality.aspx");
            driver.findElement(By.id("JmenoUzivatele")).sendKeys("sinjan");
            driver.findElement(By.id("HesloUzivatele")).sendKeys("prvni123");
            driver.findElement(By.id("dnn_ctr994_SOLLogin_btnODeslat")).click();
            WebElement tomorrow=driver.findElement(By.xpath("//*[@id=\"ctl00_main_boxKalendar_ctl00_ctl04\"]/tbody/tr[4]"));
            List<WebElement>elements=tomorrow.findElements(By.xpath("./child::*"));
            elements.remove(0);
            elements.remove(1);
            System.out.println(driver.getPageSource());
            for (WebElement element:elements){
                System.out.println(element.findElement(By.tagName("table")));

            }
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Schedule();
    }
}
