package com.sin.helper.gameEngine.physics;

public class Force {
    private final int x,y;
    private final double angle;
    private final int size;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public int getSize() {
        return size;
    }

    public Force(int x, int y, double angle, int size) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.size = size;
    }
}
