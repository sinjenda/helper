package com.sin.helper.gameEngine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameWindowTest {


    @SuppressWarnings({"InfiniteLoopStatement"})
    @org.junit.jupiter.api.Test
    public void GameWindow() throws InterruptedException {
        GameWindow window=new GameWindow(1000,1000,"test");
        Rect r=new Rect(50,50,new Point(0,0),Color.YELLOW);
        window.addComponent(r);
        while (true){

        }
    }


}
