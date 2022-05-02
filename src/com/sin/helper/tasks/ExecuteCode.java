package com.sin.helper.tasks;

import com.sin.helper.RuntimeCompilerExample;
import com.sin.helper.Task;

import java.util.UUID;

public class ExecuteCode extends Task {

    int progress=0;

    public ExecuteCode(UUID taskId, String name, String[] params) {
        super(taskId, name, params);
    }

    @Override
    public void run() throws Exception {
        RuntimeCompilerExample.execute(params[0]);
        finish("done");
    }

    @Override
    public double getProgress() {
        return progress;
    }
}
