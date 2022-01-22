package com.sin.helper.jobsHandling;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class JobExecutor extends Thread implements Closeable {
    Queue<ModdedJob> queue=new Queue<>();
    HashMap<Long,Object>returns=new HashMap<>();
    CopyOnWriteArrayList<Thread> running=new CopyOnWriteArrayList<>();
    public static JobExecutor executor;
    private long clearerId;
    InternetTester tester=new InternetTester();
    private final Logger logger=Logger.getLogger("executor");
    private boolean closed=false;

    @Override
    public void run() {
        clearerId= add(getClearer());
        ShutDowner.putBlockingAction(this);
        boolean b=false;
        while (!b){
            ModdedJob job=queue.next();
            if (job==null){
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (closed){
                    b=true;
                }
                continue;
            }
            logger.info("executing: "+job);
            if (job.type().equals(Job.type.internet)){
                if (tester.isAvailable()){
                    if (job.thread().equals(Job.thread.current)){
                        job.runnable().run();
                    }
                    if (job.thread().equals(Job.thread.New)){
                        Thread t= job.runnable;
                        t.setName(job.name());
                        running.add(t);
                        t.start();
                    }
                }
                else {
                    add(new Job(job.type,job.thread, job.runnable, job.name()));
                }
            }
            if (job.type().equals(Job.type.local)){
                if (job.thread().equals(Job.thread.current)){
                    job.runnable().run();
                }
                if (job.thread().equals(Job.thread.New)){
                    Thread t= job.runnable;
                    t.setName(job.name());
                    running.add(t);
                    t.start();
                }
            }
            logger.info("job: "+job+" executed successfully");
            queue.queue.remove(job);
        }
        ShutDowner.closed(this);
    }
    public static long add(Job job){
        Thread t=new Thread(job.runnable());
        executor.queue.add(new ModdedJob(job.type(),job.thread(),t,job.name()));
        return t.getId();
    }

    static {
        executor=new JobExecutor();
    }

    public static void waitForFinish(long id){
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (executor.running.stream().filter(a->a.getId()==id).toArray().length>0||executor.queue.queue.stream().filter(a->a.runnable.getId()==id).toArray().length>0)
            try {
                sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    public static void Return(Object o){
        executor.returns.put(Thread.currentThread().getId(),o);
    }

    public static ArrayList<Object> getReturn(long id){
        ArrayList<Object>returns=new ArrayList<>();
        for (long id2:executor.returns.keySet().stream().filter(a-> a ==id).toList()){
            returns.add(executor.returns.get(id2));
            executor.returns.remove(id2);
        }
        if (returns.size()==0){
            try {
                Thread.sleep(2000);
                return getReturn(id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return returns;
    }



    public JobExecutor() {
        start();
    }

    @Override
    public void close() {
        closed=true;
        kill(clearerId);
    }

    public static void kill(long jobName){
        for (Object o: executor.running.stream().filter(a->a.getId()==jobName).toArray()){
            Thread t=(Thread)o;
            t.interrupt();
            System.out.println(t.isAlive());
        }
    }

    private Job getClearer(){
        return new Job(Job.type.local, Job.thread.New,()->{
            //noinspection InfiniteLoopStatement
            while (true){
                running.removeIf(t -> !t.isAlive());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"clearer");
    }

    private record ModdedJob(com.sin.helper.jobsHandling.Job.type type, Job.thread thread, Thread runnable, String name){
        @Override
        public String toString() {
            return name;
        }
    }

    public static CopyOnWriteArrayList<Thread> getRunning(){
        return executor.running;
    }

}
