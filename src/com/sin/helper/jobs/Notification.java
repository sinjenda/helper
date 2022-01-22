package com.sin.helper.jobs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Notification implements ActionListener {
    public static void main(String[] args) throws IOException, AWTException {
        if (!Marks.available(123)){
            System.exit(5);
        }
        else {
            ServerSocket ss=new ServerSocket(123);
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("System tray icon demo");
            tray.add(trayIcon);
            PopupMenu menu=new PopupMenu();
            menu.add(new MenuItem("close"));
            menu.addActionListener(new Notification());
            trayIcon.setPopupMenu(menu);
            //noinspection InfiniteLoopStatement
            while (true){
                Socket s=ss.accept();
                DataInputStream in=new DataInputStream(s.getInputStream());
                String title=in.readUTF();
                String message=in.readUTF();
                Scanner scanner=new Scanner(message);
                Exception e=null;
                String m1="",m2="",m3="";
                do {
                    try{
                        m1=scanner.nextLine();
                        m2=scanner.nextLine();
                        m3=scanner.nextLine();
                    }
                    catch (Exception e1){
                        e=e1;
                    }
                    trayIcon.displayMessage(title, m1+"\n"+m2+"\n"+m3, TrayIcon.MessageType.INFO);
                }while (e==null&&scanner.hasNextLine());

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
