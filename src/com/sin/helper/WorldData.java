package com.sin.helper;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class WorldData implements Runnable{
    public static void main(String[] args) {
         new Thread(new WorldData()).start();
    }

    @Override
    public void run() {
        WebClient client=new WebClient();
        client.setThrowExceptionOnScriptError(false);
        try {
            HtmlPage page=client.getPage("https://www.worldometers.info/cz/");
            HtmlElement e= (HtmlElement) page.getByXPath("/html/body/div/div[4]/table/tbody/tr/td[3]/table/tbody/tr/td/div/table/tbody/tr[24]/td[1]/div").get(0);
            while (e.getTextContent().contains("Načítání dat...")&&!e.getTextContent().equals("$")){

                e= (HtmlElement) page.getByXPath("/html/body/div/div[4]/table/tbody/tr/td[3]/table/tbody/tr/td/div/table/tbody/tr[24]/td[1]/div").get(0);
            }
            for (int i=0;i!=10;i++) {
                e= (HtmlElement) page.getByXPath("/html/body/div/div[4]/table/tbody/tr/td[3]/table/tbody/tr/td/div/table/tbody/tr[24]/td[1]/div").get(0);
                System.out.println(e.getTextContent());
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
