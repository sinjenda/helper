package com.sin.helper.commandLine;

import com.sin.helper.jobs.Downloader;
import com.sin.helper.jobsHandling.Job;
import com.sin.helper.jobsHandling.JobExecutor;
import com.sin.helper.jobsHandling.ShutDowner;

import java.io.*;

public class CommandLine extends Thread implements Closeable {
    BufferedReader scnr=new BufferedReader(new InputStreamReader(Gui.getInputStream()));
    @SuppressWarnings("JavaReflectionInvocation")
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            System.out.print("hello to commandline client everything working good ");
           main: while (true) {
                String line=null;
                while (line == null) {
                    line = scnr.readLine();
                    Thread.sleep(5000);
                }
                switch (line) {
                    case "shutdown":
                        ShutDowner.close();
                        break main;
                    case "create":
                        System.out.println("class name: ");
                        Class<?> c=Class.forName(getNextInput());
                        System.out.println("method name: ");
                        JobExecutor.add((Job) c.getDeclaredMethod(getNextInput()).invoke(null, (Object) null));
                        break ;
                    case "help":
                        System.out.println("shutdown\ncreate\nhelp");
                        break ;
                    case "download":
                        System.out.println("what to download");
                        String s=getNextInput();
                        Downloader.downloadApp(s);
                        break ;
                    case "kill":
                        System.out.println("job id");
                        JobExecutor.kill(Long.parseLong(getNextInput()));
                        break ;
                    case "running":
                        StringBuilder builder=new StringBuilder();
                        builder.append("running: \n");
                        JobExecutor.getRunning().forEach(a->builder.append(a.getName()).append(": ").append(a.getId()).append("\n"));
                        System.out.println(builder);
                        break ;
                    default:
                        System.out.println("default");

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Job getJob(){
        return new Job(Job.type.local, Job.thread.New,()->{},"command line");
    }

    @Override
    public void close() throws IOException {
        scnr.close();
    }
    private String getNextInput() throws IOException {
        String ret=null;
        while (ret==null){
            ret=scnr.readLine();
        }
        return ret;
    }
}
