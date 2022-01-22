package com.sin.helper.jobs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Marks implements Runnable {

    @Override
    public void run() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("https://www.skolaonline.cz/Aktuality.aspx");
        try {
            Robot r = new Robot();
            r.keyPress(KeyEvent.VK_WINDOWS);
            r.keyPress(KeyEvent.VK_DOWN);
            r.keyRelease(KeyEvent.VK_DOWN);
            Thread.sleep(500);
            r.keyPress(KeyEvent.VK_DOWN);
            r.keyRelease(KeyEvent.VK_WINDOWS);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        driver.findElement(By.xpath("/html/body/form/div[3]/div[4]/div/div[1]/div[2]/div[1]/div[1]/div/div[1]/div/div/table/tbody/tr[1]/td/input")).sendKeys("sinjan");
        driver.findElement(By.xpath("/html/body/form/div[3]/div[4]/div/div[1]/div[2]/div[1]/div[1]/div/div[1]/div/div/table/tbody/tr[2]/td[1]/input")).sendKeys("prvni123");
        driver.findElement(By.xpath("/html/body/form/div[3]/div[4]/div/div[1]/div[2]/div[1]/div[1]/div/div[1]/div/div/table/tbody/tr[2]/td[2]/a")).click();
        driver.get("https://aplikace.skolaonline.cz/SOL/App/Hodnoceni/KZH001_HodnVypisStud.aspx");
        WebElement e = driver.findElementByXPath("/html/body/form/div[2]/div[2]/div/div[2]/div[2]/div/div[2]/div/div[1]/table[2]/tbody/tr/td/div/table/tbody");
        List<WebElement> elements = e.findElements(By.xpath("./*"));
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 3; i != elements.size(); i++) {
            WebElement element = elements.get(i);
            ArrayList<Integer> integers = new ArrayList<>();
            for (char c : element.getText().toCharArray()) {
                try {
                    integers.add(Integer.parseInt(String.valueOf(c)));
                } catch (NumberFormatException ignore) {

                }
            }
            int added = 0;
            for (int i1 : integers) {
                added += i1;
            }
            try {

                strings.add(element.findElement(By.xpath("./*")).getText() + ": " + Math.round(added / (double) integers.size()));
            } catch (ArithmeticException ignore) {
            }
        }
        driver.quit();
        display(strings);
    }

    private void display(ArrayList<String> strings) {
        StringBuilder ret = new StringBuilder();
        ret.append(strings.get(0));
        for (int i = 1; i != strings.size(); i++) {
            ret.append("\n").append(strings.get(i));
        }
        if (available(123)) {
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "C:\\Users\\uzivatel\\.jdks\\openjdk-16.0.1\\bin\\java.exe -javaagent:C:\\Users\\uzivatel\\AppData\\Local\\JetBrains\\Toolbox\\apps\\IDEA-C\\ch-0\\213.5744.223\\lib\\idea_rt.jar=37875:C:\\Users\\uzivatel\\AppData\\Local\\JetBrains\\Toolbox\\apps\\IDEA-C\\ch-0\\213.5744.223\\bin -classpath C:\\Users\\uzivatel\\IntelliJIDEAProjects\\helper\\out\\production\\helper; -Dfile.encoding=UTF-8 com.sin.helper.jobs.Notification");
            try {
                builder.inheritIO();
                builder.start();
                Thread.sleep(20000);
            } catch (IOException e) {
                throw new IllegalStateException("process cannot start", e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Socket s = new Socket("localhost", 123);
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeUTF("průměry");
            out.writeUTF(ret.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean available(int port) {
        if (port < 1 || port > 600000) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException ignored) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        new Marks().run();
    }

}
