package com.sin.helper.tasks;

import com.sin.helper.Task;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class Ls extends Task {
    int progress=0;

    public Ls(UUID taskId, String name, String[] params) {
        super(taskId, name, params);
    }

    @Override
    public void run() throws Exception {
        StringBuilder builder=new StringBuilder();
        for (String s: Objects.requireNonNull(new File(params[0]).list())){
            builder.append(s).append(" ");
        }
        finish(builder.toString());
        progress=100;
    }

    @Override
    public double getProgress() {
        return progress;
    }
}
