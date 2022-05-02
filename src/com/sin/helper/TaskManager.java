package com.sin.helper;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManager implements Runnable{
    public Connection conn=DriverManager.getConnection("jdbc:mysql://remotemysql.com/e7gUOavUR9","e7gUOavUR9","xkvOD4ttmV");
    private final ArrayList<UUID>ids=new ArrayList<>();
    private final ArrayList<TaskProgressController>progressControllers=new ArrayList<>();
    private static final TaskManager manager;

    static {
        TaskManager manager1;
        try {
            manager1 = new TaskManager();
        } catch (SQLException e) {
            manager1 =null;
            e.printStackTrace();
        }
        manager = manager1;
    }
    public static Connection getConnection(){
        return manager.conn;
    }
    private final AtomicInteger updateTime=new AtomicInteger(-1);

    public TaskManager() throws SQLException {
    }

    public static void addTask(Task task) {
        manager.ids.add(task.getTaskId());
        manager.progressControllers.add(new TaskProgressController(task));
    }

    @SuppressWarnings("BusyWait")
    public static void updateAll(int autoUpdate){
        if (autoUpdate>0){
            if (manager.updateTime.get()==-1) {
                manager.updateTime.set(autoUpdate);
                new Thread(() -> {
                    while (manager.updateTime.get() != -1) {
                        updateAll(0);
                        try {
                            Thread.sleep(manager.updateTime.get() * 1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            else {
                manager.updateTime.set(autoUpdate);
            }
        }
        else {
            for(TaskProgressController ctrl: manager.progressControllers){
                ctrl.update();
            }
            manager.run();
        }
    }


    @Override
    public void run() {
        try {
            PreparedStatement ps=conn.prepareStatement("select * from tasks");

            String comm="update tasks set uuid=uuid() where uuid=\"uuid\"";
            conn.createStatement().execute(comm);
            ResultSet set=ps.executeQuery();
            while (set.next()){
                Calendar calendar=Calendar.getInstance();
                if (!ids.contains(UUID.fromString(set.getString("uuid")))){
                    ps=conn.prepareStatement("update tasks set time=? where uuid='"+set.getString("uuid")+"'");
                    ps.setTimestamp(1,Timestamp.from(Instant.now()));
                    ps.execute();
                    Class<? extends Task> clazz=ClassLoader.getSystemClassLoader().loadClass(set.getString("name")).asSubclass(Task.class);
                    if (clazz.getName().equals("com.sin.helper.tasks.ExecuteTask")){
                        String pr=set.getString("params");
                        int last=pr.lastIndexOf('}');
                        int length = pr.substring(last).split(",").length;
                        String[] ret=new String[length];
                        ret[0]=pr.substring(0,last+1);
                        //noinspection ManualArrayCopy
                        for (int i = 1; i!= length; i++){
                            ret[i]=pr.substring(last+2).split(",")[i-1];
                        }
                        addTask(clazz.getConstructor(UUID.class,String.class,String[].class).newInstance(UUID.fromString(set.getString("uuid")),set.getString("name"),ret));
                        continue;
                    }
                    addTask(clazz.getConstructor(UUID.class,String.class,String[].class).newInstance(UUID.fromString(set.getString("uuid")),set.getString("name"),set.getString("params").split(",")));
                }
                else {
                    if (calendar.after(set.getTimestamp("time").toInstant().plusSeconds(604800))){
                        conn.createStatement().execute("delete from tasks where uuid='"+set.getString("uuid")+"'");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        updateAll(1);
    }
}
