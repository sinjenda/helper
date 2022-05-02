package com.sin.helper.gameEngine.physics;

import java.util.ArrayList;

public class Item {
    private int x,y;
    private int speed;
    private final ArrayList<Force>forces=new ArrayList<>();
    private final Material material;
    private final Connection connection=new Connection();


    public double getMass(){
        return material.getDensity()/100000;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }



    public void setX(int x) {
        this.x = x;
    }
    public void setConnection(Connection.Direction direction, Item item){
        connection.setConnection(direction,item);
    }

    public void setY(int y) {
        this.y = y;
    }



    public Item(int x, int y, Material material) {
        this.x = x;
        this.y = y;
        this.material = material;
    }


    public Material getMaterial() {
        return material;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
