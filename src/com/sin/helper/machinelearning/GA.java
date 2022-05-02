package com.sin.helper.machinelearning;

public class GA {
    /* GA parameters */

    private static final int tournamentSize = 5;
    private static final boolean elitism = true;

    // Evolves a population over one generation
    public static<T extends AbstractIndividual> AbstractPopulation<T> evolvePopulation(AbstractPopulation<T> pop) {
        AbstractPopulation<T> newPopulation = new AbstractPopulation<>(pop.populationSize(), false,pop.getIndividual(0));

        // Keep our best individual if elitism is enabled
        int elitismOffset = 0;
        if (elitism) {
            newPopulation.saveIndividual(0, pop.getFittest());
            elitismOffset = 1;
        }

        // Crossover population
        // Loop over the new population's size and create individuals from
        // Current population
        for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
            // Select parents
            AbstractIndividual parent1 = tournamentSelection(pop);
            AbstractIndividual parent2 = tournamentSelection(pop);
            // Crossover parents
            AbstractIndividual child = parent1.crossover(parent2);
            // Add child to new population
            //noinspection unchecked
            newPopulation.saveIndividual(i, (T) child);
        }

        // Mutate the new population a bit to add some new genetic material
        for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
            newPopulation.getIndividual(i).mutate();
        }

        return newPopulation;
    }

    // Applies crossover to a set of parents and creates offspring


    // Mutate a individual using swap mutation


    // Selects candidate tour for crossover
    private static<T extends AbstractIndividual> AbstractIndividual tournamentSelection(AbstractPopulation<T> pop) {
        // Create a tournament population
        AbstractPopulation<T> tournament = new AbstractPopulation<T>(tournamentSize, false,pop.getIndividual(0));
        // For each place in the tournament get a random candidate tour and
        // add it
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.populationSize());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        // Get the fittest tour
        return tournament.getFittest();
    }
}
