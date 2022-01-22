package com.sin.helper.jobsHandling;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NetServer extends Thread{
    private volatile boolean running=false;
    // TODO: 08.10.2021 use this as webserver 
    private static final NetServer server=new NetServer();
    private NetServer() {
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket=new ServerSocket(258);
            while (running) {
                Socket socket=serverSocket.accept();
                ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
                String command=in.readUTF();
                if (command.equals("job")){
                    JobExecutor.add((Job) in.readObject());
                }
                if (command.equals("stop")){
                    ShutDowner.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void startExecuting() {
        if (!server.running) {
            if (!server.isAlive()) {
                server.start();
                server.running = true;
            }
        }
    }

    public static void stopExecuting(){
        server.running=false;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
