package com.sin.helper.pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Pong extends JFrame {
    public rect r1;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        setBackground(Color.BLACK);
    }

    public Pong(){
        setSize(800,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        rect r=new rect(50,300,new Dimension(800,600));
        r1=r;
        setBackground(Color.BLACK);
        r.setBackground(Color.BLACK);
        setResizable(false);
        //r.setSize(800,600);
        add(r);
        //r.setLocation(50,300);
        //pack();
        //add(new rect(300,300));
        addKeyListener(r);
        setVisible(true);
    }

    private static class rect extends JPanel implements KeyListener {
        int x,x1;
        int y,y1;
        Dimension d;
        final int width=15;
        final int height=90;

        public rect(int x,int y,Dimension screenSize) {
            super();
            d=screenSize;
            this.x=x;
            this.y=y;
            setSize(800,600);
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public void paint(Graphics g){
            g.setColor(Color.BLACK);
            g.fillRect(x-width,0,width,y);
            g.fillRect(x-width,y+height,width,d.height);
            g.setColor(Color.GREEN);
            g.fillRect(x-width,y-height,width,height);
            g.fillRect(d.width-x-width,y-height,width,height);
            setBackground(Color.BLACK);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            switch (e.getKeyChar()) {
                case 'w' -> y -= 5;
                case 's' -> y += 5;
            }
            System.out.println("clicked");
            repaint();
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

        private class ball {
            private int speedX,speedY;
            private int x,y;

            public int getSpeedx() {
                return speedX;
            }


            public void paint(Graphics g){
                g.setColor(Color.white);
                g.fillOval(x,y,25,25);
            }

            public int getSpeedy() {
                return speedY;
            }

            public void setSpeedy(int speedy) {
                this.speedY = speedy;
            }

            public ball(int speedX, int speedY, int x, int y) {
                this.speedX = speedX;
                this.speedY = speedY;
                this.x = x;
                this.y = y;
            }
        }
    }

    public static void main(String[] args) {
        Pong p= new Pong();
    }
}
