package com.sin.helper.gameEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class GameWindow extends JComponent implements AutoCloseable,Runnable{

    private final ArrayList<UIComponent>components=new ArrayList<>();
    private final BufferedImage i;
    private Thread myThread=null;
    private final Graphics2D g;

    public GameWindow(int height, int width, String gameName) {
        JFrame frame = new JFrame(gameName);
        frame.add(this);
        i = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g=i.createGraphics();
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    public void addComponent(UIComponent component){
        component.prepare(g.create(component.location.x,component.location.y,component.width,component.height));
        components.add(component);
        GraphicsGenerator.registerComponent(component,g);
        if (myThread==null){
            myThread=new Thread(this);
            myThread.start();
        }
    }



    @Override
    public void paint(Graphics g) {
        g.drawImage(i, 0, 0, this);
    }

    @Override
    public void close() throws Exception {
        components.forEach(UIComponent::destroy);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            paint(getGraphics());
        }
    }
}
