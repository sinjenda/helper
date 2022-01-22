package com.sin.helper.jobsHandling;

import java.net.InetAddress;

public class InternetTester extends Thread {
    public boolean isAvailable() {
        return available;
    }

    private boolean available = false;

    public InternetTester() {
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                available = InetAddress.getByName("8.8.8.8").isReachable(100);
                if (available)
                    NetServer.startExecuting();
                else
                    NetServer.stopExecuting();
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
