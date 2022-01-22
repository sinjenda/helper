package com.sin.helper.jobs;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sin.helper.commandLine.Gui;
import com.sin.helper.jobsHandling.Job;
import com.sin.helper.jobsHandling.JobExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Downloader implements Runnable{

    String name;

    private Downloader(String name) {
        this.name = name;
    }

    public static void downloadApp(String name) {
        JobExecutor.add(new Job(Job.type.internet, Job.thread.New,new Downloader(name),"download"));
    }


    private static void saveFile(String url, String filename, long fileSize) {
        try  {
            HttpURLConnection conn= (HttpURLConnection) new URL(url).openConnection();
            FileOutputStream fos;
            long downloaded=0;
            if (new File(filename).exists()){
                conn.setRequestProperty("Range","bytes="+new File(filename).length()+"-");
                downloaded=new File(filename).length();

            }
            conn.setDoInput(true);
            conn.setDoOutput(true);
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            fos=(downloaded==0)? new FileOutputStream(filename): new FileOutputStream(filename,true);
            JTextArea area = new JTextArea();
            AtomicLong atomicLong=new AtomicLong(0);
            Gui.addTab("download", area);
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            AtomicLong all =new AtomicLong( 0);
            AtomicBoolean b=new AtomicBoolean(true);
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    while (b.get()) {
                        long i = all.get();
                        Thread.sleep(1000);
                        atomicLong.set((all.get()-i)*1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fos.write(dataBuffer, 0, bytesRead);
                all.addAndGet( dataBuffer.length);
                area.setText((double) all.get() / (double) 1000000 + "mb" + "/" + (double) fileSize / (double) 1000000 + "mb"+"   speed: "+atomicLong+"kbps");
            }
            b.set(false);
            Gui.removeTab("download");
            unzipFile(new File(filename));
        } catch (IOException e) {
            // handle exception
        }
    }
    public static void unzipFile(File file) throws IOException{
        byte[] buffer = new byte[1024];
        ZipInputStream zis;
        try {
            zis = new ZipInputStream(new FileInputStream(file), Charset.forName("IBM437"));
        }catch (Exception e){
            zis=new ZipInputStream(new FileInputStream(file));
    }
        ZipEntry zipEntry = zis.getNextEntry();
        System.out.println(file.getParent());
        File destDir=new File(file.getParent());
        //noinspection ResultOfMethodCallIgnored
        destDir.mkdir();
        while (zipEntry != null) {
            File newFile = new File(destDir.getPath()+ (zipEntry.getName()));
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }
    @SuppressWarnings("unused")
    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    @Override
    public void run() {
        try {
            WebClient webClient = new WebClient();
            HtmlPage page = webClient.getPage("https://steamunlocked.net/");
            HtmlInput searchBox = page.getHtmlElementById("s");
            searchBox.setValueAttribute(name);
            HtmlElement button = (HtmlElement) page.createElement("button");
            button.setAttribute("type", "submit");
            HtmlElement form = (HtmlElement) page.getElementById("searchformInput");
            form.appendChild(button);
            page = button.click();
            int i = 28;
            HtmlAnchor anchor = page.getAnchors().get(i);
            page = webClient.getPage(anchor.getHrefAttribute());
            //System.setProperty("webdriver.chrome.driver","C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
            DesiredCapabilities.chrome().setCapability("download_restrictions", 3);
            WebDriver driver = new ChromeDriver();
            new WebDriverWait(driver, 10);
            driver.get(page.getUrl().toString());
            WebElement element = driver.findElement(By.className("btn-download"));
            element.click();
            String fileSize = element.findElement(By.tagName("em")).getText();
            Scanner scnr = new Scanner(fileSize);
            scnr.next();
            double doubleSize = Double.parseDouble(scnr.next());
            long realSize;
            if (scnr.next().equalsIgnoreCase("gb")) {
                realSize = (long) (doubleSize * 1000000000);
            } else {
                realSize = (long) doubleSize * 1000000;
            }
            Thread.sleep(23000);
            Robot bot = new Robot();
            int mask = InputEvent.BUTTON1_DOWN_MASK;
            bot.mouseMove(300, 300);
            bot.mousePress(mask);
            bot.mouseRelease(mask);
            Thread.sleep(1000);
            driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[2]);
            driver.findElement(By.id("submitFree")).click();
            String downloadUrl = driver.findElement(By.linkText("here")).getAttribute("href");
            String filename = "D:/downloads/" + downloadUrl.split("filename=")[1];
            driver.quit();
            System.out.println("downloading: " + filename);
            if (new File(filename).exists()){
                saveFile(downloadUrl,filename+UUID.randomUUID(),realSize);
            }
            else {
                saveFile(downloadUrl, filename, realSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Downloader("prison architect").run();
    }
}
