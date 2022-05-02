package com.sin.helper;

public class TaskException extends RuntimeException{
    Task task;

    public TaskException( Throwable cause,Task task) {
        super(cause);
        this.task=task;
    }
}
