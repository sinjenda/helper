package com.sin.helper.jobs;

import com.sin.helper.jobsHandling.Job;
import com.sin.helper.jobsHandling.JobExecutor;
import jdk.internal.reflect.CallerSensitive;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;

public class DatabaseClient implements Runnable{
    String command,param,address;
    Book object;

    public DatabaseClient(String address,String command, String param, Book object) {
        this.address=address;
        this.command = command;
        this.param = param;
        this.object = object;
    }

    public @Nullable Book make() {
        try {
            Socket s=new Socket(address,3306);
            Book.BookOutputStream outputStream=new Book.BookOutputStream(s.getOutputStream());
            Book.BookInputStream inputStream=new Book.BookInputStream(s.getInputStream());
            outputStream.writeUTF(command);
            switch (command){
                case "add":
                    outputStream.writeBook(object);
                    return null;
                case "get":
                    outputStream.writeUTF(param);
                    outputStream.flush();
                    return inputStream.readBook();
                case "remove":
                    outputStream.writeUTF(param);
                    return null;
                default:
                    return null;
            }
        }catch (Exception e){
            throw new UnsupportedOperationException();
        }
    }
    public Job asJob(){
        return new Job(Job.type.internet, Job.thread.New,this,"database-client");
    }

    @Override
    @CallerSensitive
    public void run() {
        JobExecutor.Return(make());
    }
}
