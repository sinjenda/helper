package com.sin.helper.gameEngine;

import java.awt.*;
import java.util.HashMap;

public class GraphicsGenerator {
    private static final HashMap<UIComponent,Graphics2D> parent=new HashMap<>();
    public static Graphics generate(UIComponent component){
        return parent.get(component).create(component.location.x,component.location.y,component.width, component.height);
    }
    public static void registerComponent(UIComponent component,Graphics2D parentGraphics){
        parent.put(component,parentGraphics);
    }
}
