package com.sin.helper.gameEngine.physics;

import java.util.ArrayList;

public class Environment {
    private final ArrayList<Item>items=new ArrayList<>();
    private final stopWatch watch=new stopWatch();

    public void addItem(Item item){
        items.add(item);
    }

    public Frame getFrame(long time){
        Frame f=getFrame();
        while (f.getTime()!=time) {
            for (Item i : f.getItemArrayList()) {

            }
        }
        return null;
    }

    public Frame getFrame(){
        return new Frame(items,watch.getTimeInMillis());
    }

    private static final class stopWatch{
        private long timeInMillis=0;
        private boolean running=false;
        private Thread runThread;
        public void step(){
            timeInMillis++;
        }
        public void xSteps(int stepCount){
            timeInMillis+=stepCount;
        }

        public long getTimeInMillis() {
            return timeInMillis;
        }

        public void run(){
            if (runThread==null) {
                runThread = new Thread(() -> {
                    while (true){
                        if (running){
                            timeInMillis++;
                        }
                    }
                });
            }
            running=true;
        }
        public void stop(){
            running=false;
        }
    }
}
