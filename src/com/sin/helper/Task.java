package com.sin.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public abstract class Task implements RunnableWithException{
    public Connection conn;
    public UUID getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    protected Task(UUID taskId, String name, String[] params) {
        this.taskId = taskId;
        this.name = name;
        this.params = params;
        try{
            conn=TaskManager.getConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void submitResult(String result) {
        try {
            Statement st = conn.createStatement();
            st.execute("update tasks set result=\"" + result + "\" where uuid=\"" + taskId+"\"");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public final void finish(String result){
        try {
            submitResult(result);
            conn.createStatement().execute("update tasks set error=false,progress=100.0 where uuid=\""+taskId+"\"");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public abstract void run() throws Exception;
    public abstract double getProgress();
    private final UUID taskId;
    private final String name;
    protected final String[] params;
}
