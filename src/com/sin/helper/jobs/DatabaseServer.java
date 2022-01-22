package com.sin.helper.jobs;

import com.sin.helper.jobsHandling.JobExecutor;
import com.sin.helper.jobsHandling.Logger;
import com.sin.helper.jobsHandling.ShutDowner;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DatabaseServer implements Runnable, Closeable {
    ArrayList<Book>books=new ArrayList<>();

    public DatabaseServer() {
        if (!Thread.currentThread().getStackTrace()[1].getClassName().equals(getClass().getName())){
            ShutDowner.putBeforeCloseAction(this);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public synchronized void run() {
        try {
            if (new File("books.sav").exists()){
                DataInputStream in=new DataInputStream(new FileInputStream("books.sav"));
                for (int i=in.readInt();i!=0;i--){
                    books.add(new Book.BookInputStream(in).readBook());
                }
            }
            ServerSocket ss=new ServerSocket(2459);
            Logger logger=Logger.getDefaultLogger();
            logger.log("server ready waiting for connection", Logger.recordLevel.info);
            while (true) {
                Socket s= ss.accept();
                logger.log("socket accepted :"+s, Logger.recordLevel.info);
                Book.BookOutputStream out=new Book.BookOutputStream(s.getOutputStream());
                Book.BookInputStream in=new Book.BookInputStream(s.getInputStream());
                switch (in.readUTF()){
                    case "add":
                        books.add(in.readBook());
                        break;
                    case "get":
                        String param=Book.BookOutputStream.stringToHtml(in.readUTF());
                        out.writeBook(books.stream().filter(a->a.nazev().equals(param)||a.autor().equals(param)).toList().get(0));
                        break;
                    case "remove":
                        param=in.readUTF();
                        books.remove(books.stream().filter(a->a.nazev().equals(param)||a.autor().equals(param)).toList().get(0));
                        break;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JobExecutor.add(JobCreator.databaseServer());
    }

    @Override
    public void close() throws IOException {
        DataOutputStream out=new DataOutputStream(new FileOutputStream("books.sav"));
        out.writeInt(books.size());
        Book.BookOutputStream bookOutputStream=new Book.BookOutputStream(out);
        for (Book book:books){
            bookOutputStream.writeBook(book);
        }
    }
}
