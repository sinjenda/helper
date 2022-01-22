package com.sin.helper.commandLine;

import com.sin.helper.jobsHandling.Job;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;

public class Gui {


    private JTextPane out;
    private JTextPane line;
    private JTextField textField1;
    private JPanel panel;
    private JTabbedPane tabbedPane;
    private final PaneInputStream in = new PaneInputStream();
    private final JFrame frame;
    private final PrintStream defaultErrStream=System.err;
    private final PrintStream defaultOutputStream=System.out;
    static final Gui gui=new Gui();
    private final HashMap<String,Integer>tabsMap=new HashMap<>();

    private Gui() {
        JFrame frame = new JFrame("helper");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println(textField1.getText());
                    writeString(textField1.getText());
                    textField1.setText("");
                }
            }
        });
        frame.pack();
        this.frame=frame;
    }

    public static void addTab(String tabName,Component panel){
        gui.tabbedPane.addTab(tabName,panel);
        gui.tabsMap.put(tabName,gui.tabsMap.size()+2);
        gui.frame.pack();
    }

    public static void removeTab(String tabName){
        int index=gui.tabsMap.get(tabName);
        gui.tabbedPane.removeTabAt(index);
        gui.tabsMap.remove(tabName);
        gui.frame.pack();
        gui.tabsMap.forEach((a,b)->{if (b>index){gui.tabsMap.replace(a,b-1);}});
    }

    private void redirect(){
        System.setOut(new PaneOutputStream(line).getPrintStream());
        System.setErr(new PaneOutputStream(out).getPrintStream());
    }

    public void writeString(String s){
        for (int c : s.toCharArray()) {
            in.add(c);
        }
        in.add('\n');
    }
    public static InputStream getInputStream(){
        return gui.in;
    }
    public static void setVisible(boolean b){
        if (b){
            gui.redirect();
        }
        else {
            System.setOut(gui.defaultOutputStream);
            System.setErr(gui.defaultErrStream);
        }
        gui.frame.setVisible(b);
    }

    public static Job getJob(){
        return new Job(Job.type.local, Job.thread.New,()->{
            setVisible(true);
        },"gui");
    }
}
