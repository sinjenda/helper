package com.sin.helper.jobsHandling;

public record Job(com.sin.helper.jobsHandling.Job.type type, thread thread, Runnable runnable, String name){

    public enum type {
        internet, local
    }
    public enum thread{
        current,New
    }

    @Override
    public String toString() {
        return name;
    }
}
