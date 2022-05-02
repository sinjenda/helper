package com.sin.helper.gameEngine.physics;

import java.util.ArrayList;

public record Frame(ArrayList<Item> itemArrayList, long time) {
    public ArrayList<Item> getItemArrayList() {
        return itemArrayList;
    }

    public long getTime() {
        return time;
    }

}
