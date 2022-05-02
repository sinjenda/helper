package com.sin.helper.machinelearning;

import java.util.ArrayList;
import java.util.Collections;

public abstract class AbstractIndividual implements Cloneable{
    public AbstractIndividual(int size) {
        list=new ArrayList<>(size);
        for (int i=0;i!=size;i++){
            list.add(null);
        }
    }

    protected ArrayList<Gene>list;
    protected static final double mutationRate = 0.015;
    public abstract<T extends AbstractIndividual> T newIndividual();
    public abstract void generateIndividual();
    public abstract double getFitness();
    public abstract<T extends AbstractIndividual> T crossover(T parent2);
    public abstract void mutate();
    public Gene getGene(int index){
        return list.get(index);
    }
    public <T extends Gene> void saveGene(int index,T gene){
        list.set(index,gene);
    }
    protected int individualSize(){
        return list.size();
    }
    protected void shuffle(){
        Collections.shuffle(list);
    }
    protected <T extends Gene> boolean containsGene(T gene){
        return list.contains(gene);
    }

    @Override
    public AbstractIndividual clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (AbstractIndividual) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
