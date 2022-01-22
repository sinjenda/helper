package com.sin.helper.jobsHandling;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Queue<T> {

    ConcurrentLinkedQueue<T>queue=new ConcurrentLinkedQueue<>();

    public void add(T t){
        queue.add(t);
    }
    public T next(){
        return queue.poll();
    }


    private boolean remove(int i){
        //noinspection SuspiciousMethodCalls
        return queue.remove(i);
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
