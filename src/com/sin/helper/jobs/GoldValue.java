package com.sin.helper.jobs;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sin.helper.commandLine.Gui;
import com.sin.helper.jobsHandling.Job;
import com.sin.helper.jobsHandling.ShutDowner;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class GoldValue implements Runnable, Closeable {

    private final AtomicDouble min=new AtomicDouble(999999999),max=new AtomicDouble(0),current=new AtomicDouble(0);
    private boolean b=true;

    private GoldValue() {
    }

    @Override
    public void run() {
        try {
            JTextArea area=new JTextArea();
            JScrollPane pane=new JScrollPane(area);
            Gui.addTab("gold value",pane);
            WebClient client = new WebClient();
            ShutDowner.putBeforeCloseAction(this);
            while (b) {
                HtmlPage page = client.getPage("https://www.kitco.com/gold-price-today-usa/");
                HtmlPage kurz=client.getPage("https://www.kurzy.cz/kurzy-men/prevodnik-men/USD-CZK/");
                HtmlElement div= (HtmlElement) kurz.getElementsByTagName("span").get(1);
                double division= Double.parseDouble(div.getTextContent());
                HtmlElement element = (HtmlElement) page.getByXPath("//*[contains(@class, 'table-price--body-table--overview-detail')]").get(0);
                area.setText(area.getText()+element.getElementsByTagName("tr").get(2).getElementsByTagName("td").get(1).getTextContent()+" usd\n");
                current.setValue(Double.parseDouble(element.getElementsByTagName("tr").get(2).getElementsByTagName("td").get(1).getTextContent())*division);
                if (current.getValue()<min.getValue())
                    min.setValue(current.getValue());
                if (current.getValue()>max.getValue())
                    max.setValue(current.getValue());
                area.setText(area.getText()+current+" czk\n");
                Thread.sleep(60000);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Job getInstance(){
        return new Job(Job.type.internet, Job.thread.New,new GoldValue(),"gold value");
    }


    @Override
    public void close() {
        b=false;
        try {
            ArrayList<Byte>before=new ArrayList<>();
            if (new File("gold value.txt").exists()) {
                for (byte b: new FileInputStream("gold value.txt").readAllBytes()){
                    before.add(b);
                }
            }
            Calendar calendar=Calendar.getInstance();
            String ret= calendar.get(Calendar.DAY_OF_MONTH)+":"+calendar.get(Calendar.MONTH)+":"+calendar.get(Calendar.YEAR)+" min: "+min+" max: "+max;
            for (int i:ret.toCharArray()){
                before.add((byte)i);
            }
            FileOutputStream out=new FileOutputStream("gold value.txt");
            for (byte b:before) {
                out.write(b);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    private static class AtomicDouble{
        double value;

        public AtomicDouble(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
