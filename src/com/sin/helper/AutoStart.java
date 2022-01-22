package com.sin.helper;

import com.sin.helper.jobsHandling.Job;
import com.sin.helper.jobsHandling.JobExecutor;
import com.sin.helper.jobsHandling.Queue;

public class AutoStart extends Thread {
    private final Queue<starter>starterQueue=new Queue<>();
    public static AutoStart autoStarter;

    private AutoStart() {

    }
    static {
        autoStarter=new AutoStart();
        autoStarter.start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true){
            starter starter=starterQueue.next();
            if (starter==null){
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                if (starter.condition()){
                    JobExecutor.add(starter.getJob());
                }
                else {
                    starterQueue.add(starter);
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void add(starter starter){
        starterQueue.add(starter);
    }

    public interface starter{
        Job getJob();
        boolean condition();
    }
}
