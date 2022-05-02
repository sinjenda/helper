package com.sin.helper.machinelearning;


import org.math.plot.Plot2DPanel;

import javax.swing.*;

public class Tour extends AbstractIndividual {



    // Holds our tour of cities
    //private ArrayList<City> tour = new ArrayList<City>();
    // Cache
    private double fitness = 0;
    private int distance = 0;

    // Constructs a blank tour
    public Tour(){
        super(TourManager.numberOfCities());
    }

    @Override
    public <T extends AbstractIndividual> T newIndividual() {
        return (T) new Tour();
    }



    public void paint(String title){
        Plot2DPanel plot = new Plot2DPanel();
        double[] x=new double[individualSize()];
        double[] y=new double[individualSize()];
        for (int i=0;i!=individualSize();i++){
            x[i]=((City)getGene(i)).x;
            y[i]=((City)getGene(i)).y;
        }
        plot.addLinePlot("path",x,y);
        JFrame frame = new JFrame(title);
        frame.setContentPane(plot);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        double[] x = {2,3};
        double[] y = {2,3};

        // create your PlotPanel (you can use it as a JPanel)
        Plot2DPanel plot = new Plot2DPanel();

        // add a line plot to the PlotPanel
        plot.addLinePlot("my plot", x, y);

        // put the PlotPanel in a JFrame, as a JPanel
        JFrame frame = new JFrame("a plot panel");
        frame.setContentPane(plot);
        frame.pack();
        frame.setVisible(true);
    }

    // Creates a random individual
    public void generateIndividual() {
        // Loop through all our destination cities and add them to our tour
        for (int cityIndex = 0; cityIndex < TourManager.numberOfCities(); cityIndex++) {
            setCity(cityIndex, TourManager.getCity(cityIndex));
        }
        // Randomly reorder the tour
        shuffle();
    }

    @Override
    public <T extends AbstractIndividual> T crossover(T parent2) {
        // Create new child tour
        Tour child = new Tour();
        Tour parent=(Tour) parent2;

        // Get start and end sub tour positions for parent1's tour
        int startPos = (int) (Math.random() * individualSize());
        int endPos = (int) (Math.random() * individualSize());

        // Loop and add the sub tour from parent1 to our child
        for (int i = 0; i < child.individualSize(); i++) {
            // If our start position is less than the end position
            if (i > startPos && i < endPos) {
                child.setCity(i, getCity(i));
            } // If our start position is larger
            else if (startPos > endPos) {
                if (!(i < startPos && i > endPos)) {
                    child.setCity(i, getCity(i));
                }
            }
        }
        // Loop through parent2's city tour
        for (int i = 0; i < parent.individualSize(); i++) {
            // If child doesn't have the city add it
            if (!child.containsCity(parent.getCity(i))) {
                // Loop to find a spare position in the child's tour
                for (int ii = 0; ii < child.individualSize(); ii++) {
                    // Spare position found, add city
                    if (child.getCity(ii) == null) {
                        child.setCity(ii, parent.getCity(i));
                        break;
                    }
                }
            }
        }
        return (T) child;
    }

    // Gets a city from the tour
    public City getCity(int tourPosition) {
        return (City) getGene(tourPosition);
    }

    // Sets a city in a certain position within a tour
    public void setCity(int tourPosition, City city) {
        saveGene(tourPosition, city);
        // If the tours been altered we need to reset the fitness and distance
        fitness = 0;
        distance = 0;
    }

    // Gets the tours fitness
    @Override
    public double getFitness() {
        if (fitness == 0) {
            fitness = 1/(double)getDistance();
        }
        return fitness;
    }

    // Gets the total distance of the tour
    public int getDistance(){
        if (distance == 0) {
            int tourDistance = 0;
            // Loop through our tour's cities
            for (int cityIndex=0; cityIndex < individualSize(); cityIndex++) {
                // Get city we're travelling from
                City fromCity = getCity(cityIndex);
                // City we're travelling to
                City destinationCity;
                // Check we're not on our tour's last city, if we are set our
                // tour's final destination city to our starting city
                if(cityIndex+1 < individualSize()){
                    destinationCity = getCity(cityIndex+1);
                }
                else{
                    destinationCity = getCity(0);
                }
                // Get the distance between the two cities
                tourDistance += fromCity.distanceTo(destinationCity);
            }
            distance = tourDistance;
        }
        return distance;
    }

    // Get number of cities on our tour


    // Check if the tour contains a city
    public boolean containsCity(City city){
        return containsGene(city);
    }

    @Override
    public String toString() {
        StringBuilder geneString = new StringBuilder("|");
        for (int i = 0; i < individualSize(); i++) {
            geneString.append(getCity(i)).append("|");
        }
        return geneString.toString();
    }
    @Override
    public void mutate() {
        // Loop through individual cities
        for(int tourPos1=0; tourPos1 < individualSize(); tourPos1++){
            // Apply mutation rate
            if(Math.random() < mutationRate){
                // Get a second random position in the individual
                int tourPos2 = (int) (individualSize() * Math.random());

                // Get the cities at target position in individual
                City city1 = getCity(tourPos1);
                City city2 = getCity(tourPos2);

                // Swap them around
                setCity(tourPos2, city1);
                setCity(tourPos1, city2);
            }
        }
    }
}
