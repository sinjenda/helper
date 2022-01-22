package com.sin.helper.commandLine;


import com.sin.helper.jobsHandling.Job;
import com.sin.helper.jobsHandling.ShutDowner;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;

public class VoiceControl implements Closeable,Runnable {



    private VoiceControl() {

    }
    public static Job newInstance(){
        return new Job(Job.type.local, Job.thread.New,new VoiceControl(),"voice");
    }
    private boolean b=true;

    @Override
    public void run() {
        ShutDowner.putBeforeCloseAction(this);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("C:\\Users\\uzivatel\\IdeaProjects\\my_helper\\venv\\Scripts\\python.exe","C:/Users/uzivatel/IdeaProjects/my_helper/src/com/sin/helper/commandLine/speechRecognition.py");
            BufferedReader in= new BufferedReader(new InputStreamReader(builder.start().getInputStream()));
            while (b){
                String line=in.readLine();
                if (line!=null)
                    Gui.gui.writeString(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void close(){
        b=false;
    }
}
