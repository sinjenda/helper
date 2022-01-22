package com.sin.helper.commandLine;

import com.sin.helper.jobsHandling.Queue;

import java.io.*;

public class PaneInputStream extends InputStream {
    private final Queue<Integer>integerQueue=new Queue<>();
    FileOutputStream fos;

    public PaneInputStream(){
        try {
            fos=new FileOutputStream("C:/Users/uzivatel/Desktop/log.log");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int read() {
        Integer i=integerQueue.next();
        if (i==null)
            return -1;
        return i;
    }
    public void add(int i){
        integerQueue.add(i);
    }

}
