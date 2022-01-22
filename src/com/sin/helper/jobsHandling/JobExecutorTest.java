package com.sin.helper.jobsHandling;

import com.sin.helper.jobs.Book;
import com.sin.helper.jobs.DatabaseClient;
import com.sin.helper.jobs.JobCreator;
import org.junit.jupiter.api.Test;

class JobExecutorTest implements Runnable {

    @Test
    void test1() throws InterruptedException {
        for (int i=0;i!=10000;i++)
            JobExecutor.add(new Job(Job.type.local, Job.thread.New,this,"name"));
        Thread.sleep(1000);
    }

    @Test
    void test2() throws InterruptedException {
        JobExecutor.add(JobCreator.databaseServer());
        Thread.sleep(5000);
        long id=JobExecutor.add(new DatabaseClient("localhost","add",null,new Book("marek","jan",2006,null)).asJob());
        JobExecutor.waitForFinish(id);
        System.out.println(JobExecutor.getReturn(id));
        id=JobExecutor.add(new DatabaseClient("localhost","get","jan",null).asJob());
        JobExecutor.waitForFinish(id);
        System.out.println(JobExecutor.getReturn(id));
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}