package com.sin.helper.jobsHandling;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ShutDowner extends CopyOnWriteArrayList<Closeable> implements Runnable{
    private static final ShutDowner shutDowner=new ShutDowner();
    private final CopyOnWriteArrayList<Closeable>closingActions=new CopyOnWriteArrayList<>();
    private ShutDowner() {

    }
    public static void putBeforeCloseAction(Closeable closeable){shutDowner.closingActions.add(closeable);}
    public static void putBlockingAction(Closeable closeable){
        shutDowner.add(closeable);
    }
    public static void closed(Closeable closeable){
        shutDowner.remove(closeable);
    }

    @Override
    public void run() {
        while (true){
            if (size()==0)
                System.exit(0);
            try {
                Thread.sleep(15000);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public static void close() {
        try {
            for (Closeable closeable : shutDowner) {
                closeable.close();
            }
            shutDowner.closingActions.forEach(a-> {
                try {
                    a.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            new Thread(shutDowner).start();
        }
        catch (IOException e) {
            Logger.getGlobal().severe("error in close: " + e);
        }
    }
}
