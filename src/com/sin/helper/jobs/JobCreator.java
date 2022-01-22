package com.sin.helper.jobs;

import com.sin.helper.jobsHandling.Job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Objects;

public class JobCreator {
    public static Job backup(File[]directories){
        return new Job(Job.type.local, Job.thread.New, () -> list(directories),"backup");
    }

    //starts a database server
    public static Job databaseServer(){
        return new Job(Job.type.internet, Job.thread.New,()-> new DatabaseServer().run(),"database-server");
    }


    private static void list(File[] directories) {
        try {
            String def="G:/"+ (Calendar.getInstance().getTime().toString().replace(" ","").replace(":",""));
            for (File file : directories) {
                String directory = def + "/" + file.getName();
                System.out.println(new File(directory).exists());
                System.out.println( new File(directory).mkdirs());
                for (File f : Objects.requireNonNull(file.listFiles())) {
                    if (f.isDirectory()){
                        list(directories);
                        continue;
                    }
                    FileOutputStream out = new FileOutputStream(directory + "/" + f.getName());
                    FileInputStream in = new FileInputStream(f.getPath());
                    while (in.available()!=-1) {
                        out.write(in.read());
                    }
                    out.close();
                    in.close();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
