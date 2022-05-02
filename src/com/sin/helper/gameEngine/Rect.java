package com.sin.helper.gameEngine;

import java.awt.*;

public class Rect extends UIComponent{
    Color color;

    @Override
    public void prepare(Graphics g) {
        super.prepare(g);
        setColor(color);
    }

    public Rect(int width, int height, Point location, Color color) {
        super(width, height, location);
        this.color=color;
    }

    @Override
    public void paint() {
        drawRect(0,0,width,height);
    }
}
