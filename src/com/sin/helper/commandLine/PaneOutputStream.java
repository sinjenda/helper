package com.sin.helper.commandLine;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class PaneOutputStream extends OutputStream {
    private final JTextPane pane;

    public PaneOutputStream(JTextPane pane) {
        this.pane = pane;
    }

    @Override
    public void write(int b) {
        pane.setText(pane.getText()+(char)b);
    }

    public PrintStream getPrintStream(){
        return new PrintStream(this);
    }
}
