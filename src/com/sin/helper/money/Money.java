package com.sin.helper.money;

import com.sin.helper.jobsHandling.Job;

import java.io.*;
import java.util.Calendar;

public class Money {
    private Money() {
    }
    public static Job getJob(){
        return new Job(Job.type.local, Job.thread.New,()->{
            Calendar current=Calendar.getInstance();
            try {
                FileInputStream inputStream=new FileInputStream("cash.dat");
                ObjectInputStream in=new ObjectInputStream(inputStream);
                int cash=new DataInputStream(inputStream).readInt();
                Calendar before= (Calendar) in.readObject();
                if (current.get(Calendar.YEAR)==before.get(Calendar.YEAR)){
                    int i=before.get(Calendar.WEEK_OF_YEAR);
                    int i1=current.get(Calendar.WEEK_OF_YEAR);
                    i=i1-i;
                    for (;i!=0;i--){
                        cash+=90;
                        main(new String[]{String.valueOf(cash)});
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"cash");
    }
    public static Job getJob(int money){
        return new Job(Job.type.local, Job.thread.current,()->main(new String[]{String.valueOf(money)}),"cash");
    }

    public static void main(String[] args) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("cash.dat");
            ObjectOutputStream fos = new ObjectOutputStream(fileOutputStream);
            new DataOutputStream(fileOutputStream).writeInt(Integer.parseInt(args[0]));
            fos.writeObject(Calendar.getInstance());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
