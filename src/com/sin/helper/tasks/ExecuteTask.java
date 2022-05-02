package com.sin.helper.tasks;

import com.sin.helper.RuntimeCompilerExample;
import com.sin.helper.Task;
import com.sin.helper.TaskManager;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

public class ExecuteTask extends Task {

    int progress=0;

    public ExecuteTask(UUID taskId, String name, String[] params) {
        super(taskId, name, params);
    }

    @Override
    public void run() throws Exception {
        Class<? extends Task>clazz=RuntimeCompilerExample.toClass(params[0]).asSubclass(Task.class);
        StringBuilder p= new StringBuilder();
        for (int i=1;i!=params.length;i++){
            p.append(params[i]).append(",");
        }
        p.deleteCharAt(p.length()-1);
        String[] p1=Arrays.copyOfRange(params,1,params.length);
        UUID taskId=UUID.randomUUID();
        String comm="insert into tasks values('"+clazz.getName()+"','"+p+"','"+taskId+"',?,null,null,null)";
        PreparedStatement statement= TaskManager.getConnection().prepareStatement(comm);
        statement.setTimestamp(1, Timestamp.from(Instant.now()));
        statement.execute();
        TaskManager.addTask(clazz.getConstructor(UUID.class,String.class,String[].class).newInstance(taskId,clazz.getName(),p1));
        finish("started");
    }

    @Override
    public double getProgress() {
        return progress;
    }
}
