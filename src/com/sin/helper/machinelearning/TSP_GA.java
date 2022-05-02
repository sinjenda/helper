package com.sin.helper.machinelearning;

import me.tongfei.progressbar.ProgressBar;

public class TSP_GA {
    public static void main(String[] args) {



        for (int x = 0; x != 10; x++) {
            // Initialize population
            AbstractPopulation<Tour> pop = new AbstractPopulation<>(50, true,new Tour());
            System.out.println("Initial distance: " + pop.getFittest().getDistance());

            // Evolve population for 100 generations
            pop = GA.evolvePopulation(pop);
            ProgressBar progressBar = new ProgressBar("calculating", 1000);
            for (int i = 0; i < 1000; i++) {
                progressBar.step();
                pop = GA.evolvePopulation(pop);
                progressBar.setExtraMessage("distance: " + pop.getFittest().getDistance());
            }
            progressBar.close();
            pop.getFittest().paint("path solution "+x);
            // Print final results
            System.out.println("Finished");
            System.out.println("Final distance: " + pop.getFittest().getDistance());
            System.out.println("Solution:");
            System.out.println(pop.getFittest());
        }
    }
}
