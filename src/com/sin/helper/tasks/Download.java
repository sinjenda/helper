package com.sin.helper.tasks;

import com.sin.helper.Task;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class Download extends Task {
    double progress=0;

    public Download(UUID taskId, String name, String[] params) {
        super(taskId, name, params);
    }

    @Override
    public void run() throws Exception{
            HttpURLConnection conn= (HttpURLConnection) new URL(params[0]).openConnection();
            FileOutputStream fos;
            long downloaded=0;
            String filename=params[1];
            if (new File(filename).exists()){
                conn.setRequestProperty("Range","bytes="+new File(filename).length()+"-");
                downloaded=new File(filename).length();
            }
            conn.setDoInput(true);
            conn.setDoOutput(true);
            long fileSize=conn.getContentLengthLong();
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            fos=(downloaded==0)? new FileOutputStream(filename): new FileOutputStream(filename,true);
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            long totalRead=0;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fos.write(dataBuffer, 0, bytesRead);
                totalRead+=bytesRead;
                progress=((double) totalRead/(double)fileSize)*100;
            }
            fos.close();
            finish("done");
    }

    @Override
    public double getProgress() {
        return progress;
    }
}
