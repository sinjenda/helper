package com.sin.helper.gameEngine;

import java.awt.*;

public abstract class UIComponent {

    private boolean ready = false;

    protected int width;
    protected int height;
    protected Color backgroundColor;
    private Graphics g;
    protected int layer = 0;

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    protected Point location;

    public void prepare(Graphics g) {
        this.g = g;
        g.clearRect(0,0,width,height);
        backgroundColor=((Graphics2D)g).getBackground();
        ready = true;
    }

    public final void destroy() {
        ready = false;
        g = null;
    }

    public final boolean isReady() {
        return ready;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        clear();
        ready=false;
        g=GraphicsGenerator.generate(this);
        ready=true;
        paint();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        clear();
        ready=false;
        g=GraphicsGenerator.generate(this);
        ready=true;
        paint();
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }



    protected final void draw3DRect(int x,int y,int width,int height){
        g.fill3DRect(x,y,width,height,true);
    }

    protected final void drawRect(int x, int y, int width, int height) {
        testReady();
        g.fillRect(x, y, width, height);
    }

    @SuppressWarnings({"ConstantConditions", "UnnecessaryLocalVariable"})
    protected final void drawRect(int x, int y, int width, int height, int angle) {
        double radiansAngle = Math.toRadians(-angle);
        int[] xs = new int[4];
        int[] ys = new int[4];
        xs[0] = x;
        ys[0] = y;
        int bx = x;
        int by = y+height;
        xs[1]=(int) ((bx-x) * Math.cos(radiansAngle) - (by-y) * Math.sin(radiansAngle)+x);
        ys[1]=(int) ((bx-x)*Math.sin(radiansAngle) + (by-y) * Math.cos(radiansAngle)+y);
        int cx=width+x;
        int cy=height+y;
        xs[2]=(int) ((cx-x) * Math.cos(radiansAngle) - (cy-y) * Math.sin(radiansAngle)+x);
        ys[2]=(int) ((cx-x)*Math.sin(radiansAngle) + (cy-y) * Math.cos(radiansAngle)+y);
        int dx=width+x;
        int dy=y;
        xs[3]=(int) ((dx-x) * Math.cos(radiansAngle) - (dy-y) * Math.sin(radiansAngle)+x);
        ys[3]=(int) ((dx-x)*Math.sin(radiansAngle) + (dy-y) * Math.cos(radiansAngle)+y);
        g.fillPolygon(xs,ys,4);
    }

    protected final void drawOval(int x, int y, int width, int height) {
        testReady();
        g.fillOval(x, y, width, height);
    }

    protected final void drawCircle(int x, int y, int r) {
        testReady();
        g.fillOval(x-r, y-r, r*2, r*2);
    }

    protected final void drawText(int x, int y, String text) {
        testReady();
        g.drawString(text, x, y + 23);
    }

    protected final void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        testReady();
        g.fillPolygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
    }

    protected final void clear(){
        testReady();
        g.clearRect(0,0,width,height);
    }

    protected final void setBackground(Color color){
        ((Graphics2D)g).setBackground(color);
    }

    public final void setColor(Color color) {
        testReady();
        g.setColor(color);
        paint();
    }

    public final void setFont(Font font){
        testReady();
        g.setFont(font);
        paint();
    }

    private void testReady() {
        if (!isReady()) {
            throw new IllegalStateException("not initialized you must first insert this object to window");
        }
    }

    public UIComponent(int width, int height, Point location) {
        this.width = width;
        this.height = height;
        this.location = location;
    }

    public abstract void paint();


}
