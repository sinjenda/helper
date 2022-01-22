package com.sin.helper;

import com.sin.helper.commandLine.CommandLine;
import com.sin.helper.commandLine.Gui;
import com.sin.helper.jobs.GoldValue;
import com.sin.helper.jobs.JobCreator;
import com.sin.helper.jobsHandling.Job;
import com.sin.helper.jobsHandling.JobExecutor;
import com.sin.helper.money.Money;

import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
        AutoStart.autoStarter.add(new AutoStart.starter() {
            Calendar previous=Calendar.getInstance();
            @Override
            public Job getJob() {
                return Money.getJob();
            }

            @Override
            public boolean condition() {
                boolean b=Calendar.getInstance().get(Calendar.DAY_OF_YEAR)!=previous.get(Calendar.DAY_OF_YEAR);
                previous=Calendar.getInstance();
                return b;
            }
        });
        JobExecutor.add(Money.getJob());
        new CommandLine().start();
        JobExecutor.add(JobCreator.databaseServer());
        Gui.setVisible(true);
        JobExecutor.add(GoldValue.getInstance());
    }



}
