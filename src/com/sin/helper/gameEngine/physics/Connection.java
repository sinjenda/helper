package com.sin.helper.gameEngine.physics;

public class Connection {
    private Item up,upRight,right,downRight,down,leftDown,left,leftUp;

    public Item getUp() {
        return up;
    }

    public Item getUpRight() {
        return upRight;
    }

    public Item getRight() {
        return right;
    }

    public Item getDownRight() {
        return downRight;
    }

    public Item getDown() {
        return down;
    }

    public Item getLeftDown() {
        return leftDown;
    }

    public Item getLeft() {
        return left;
    }

    public Item getLeftUp() {
        return leftUp;
    }

    public void setConnection(Direction direction, Item item){
        switch (direction){
            case up -> {
                up=item;
            }
            case upRight -> {
                upRight=item;
            }
            case right -> {
                right=item;
            }
            case downRight -> {
                downRight=item;
            }
            case down -> {
                down=item;
            }
            case leftDown -> {
                leftDown=item;
            }
            case left -> {
                left=item;
            }
            case leftUp -> {
                leftUp=item;
            }
        }
    }

    public enum Direction{
        up,upRight,right,downRight,down,leftDown,left,leftUp;
    }
}
