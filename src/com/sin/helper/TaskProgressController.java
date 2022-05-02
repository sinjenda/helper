package com.sin.helper;


import java.sql.*;

public class TaskProgressController implements Runnable,Thread.UncaughtExceptionHandler{
    private final Task task;
    private final Thread taskThread;

    public TaskProgressController(Task task) {
        this.task = task;
        taskThread=new Thread(() -> {
            try {
                task.run();
            } catch (Exception e) {
                throw new TaskException(e,task);
            }
        });
        taskThread.setDaemon(true);
        taskThread.setUncaughtExceptionHandler(this);
        taskThread.start();
        new Thread(this).start();
    }

    public void update(){
        run();
    }

    @Override
    public void run() {
        try {
            Connection conn = TaskManager.getConnection();
            if (taskThread.isAlive()) {
                if (task.getProgress()<99) {
                    conn.createStatement().execute("update tasks set progress=" + task.getProgress() + " where uuid=\"" + task.getTaskId()+"\"");
                }
            }
            else {
                ResultSet set=conn.createStatement().executeQuery("select * from tasks where uuid=\""+task.getTaskId()+"\"");
                set.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof TaskException exception) {
            try {
                Statement st=exception.task.conn.createStatement();
                st.execute("update tasks set result=\""+e.getCause().getLocalizedMessage()+"\" where uuid=\""+exception.task.getTaskId()+"\"");
                st.execute("update tasks set error=true where uuid=\""+exception.task.getTaskId()+"\"");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        else {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t,e);
        }
    }
}
