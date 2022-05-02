package com.sin.helper.gameEngine;

import java.awt.*;

public class Label extends UIComponent{
    private String text;

    public Label(Point location,String text) {
        //super((int) (1.5*text.toCharArray().length), 20,location);
        super(1000,1000,location);
        this.text=text;
    }

    @Override
    public void prepare(Graphics g) {
        super.prepare(g);
        setFont(g.getFont().deriveFont(30f));
    }

    @Override
    public void paint() {
        drawText(0,0,text);
    }

    public Label(Point location) {
        super(20, 10,location);
    }

    public void setText(String text){
        drawText(location.x, location.y,text);
        this.text=text;
    }

    public void changeColor(Color color) {
        setColor(color);
    }
}
