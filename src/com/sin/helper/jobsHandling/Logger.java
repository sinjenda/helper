package com.sin.helper.jobsHandling;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;

public class Logger {
    private static final Logger defaultLogger=new LoggerBuilder().create();

    public static Logger getDefaultLogger() {
        return defaultLogger;
    }

    private final PrintStream printStream;
    private final int bufferSize;
    private final LogRecords records=new LogRecords();
    private final recordLevel maxRecordLevel;

    private Logger(PrintStream printStream,int bufferSize,recordLevel maxRecordLevel) {
        this.printStream = printStream;
        this.bufferSize=bufferSize;
        this.maxRecordLevel=maxRecordLevel;
    }

    public void log(String message,recordLevel level){
        records.log(message,level);
        if (maxRecordLevel.compareTo(level)<0) {
            printStream.println("from: " + Thread.currentThread().getStackTrace()[1].getMethodName() + "message: " + message);
        }
    }

    public static class LoggerBuilder{
        private PrintStream printStream=System.err;
        private int bufferSize=10;
        private recordLevel maxRecordLevel=recordLevel.warning;

        public void setPrintStream(PrintStream printStream) {
            this.printStream = printStream;
        }

        public void setBufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
        }

        public void setMaxRecordLevel(recordLevel maxRecordLevel) {
            this.maxRecordLevel = maxRecordLevel;
        }
        public Logger create(){
            return new Logger(printStream,bufferSize,maxRecordLevel);
        }
    }

    private class LogRecords{
        private int i=0;
        private final ArrayList<LogRecord> records=new ArrayList<>(bufferSize+1);


        private record LogRecord(String message, Calendar date, recordLevel level){

        }

        public LogRecords() {
            for (int i1=0;i1!=bufferSize+1;i1++){
                records.add(new LogRecord("",Calendar.getInstance(),recordLevel.error));
            }
        }

        public void log(String message, recordLevel level){
            records.add(i,new LogRecord(message,Calendar.getInstance(),level));
            if (i==bufferSize){
                i=0;
            }
            else {
                i++;
            }
        }
        public Object get(String get,int i){
            for (;i!=0;i--){
                this.i--;
                if (this.i==-1){
                    this.i=bufferSize;
                }
            }
            return switch (get) {
                case "message" -> records.get(this.i).message();
                case "date" ->  records.get(this.i).date();
                case "level" -> records.get(this.i).level();
                default -> null;
            };

        }
    }
    public enum recordLevel{
        error,warning,info

    }
}
