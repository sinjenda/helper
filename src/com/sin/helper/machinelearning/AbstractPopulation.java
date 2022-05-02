package com.sin.helper.machinelearning;

import java.util.ArrayList;

public class AbstractPopulation<T extends AbstractIndividual>{
    ArrayList<T>list;

    public AbstractPopulation(int size,boolean initialize,T instance) {
        list=new ArrayList<>(size);
        for (int i=0;i!=size;i++){
            list.add(null);
        }
        if (initialize){
            for (int i=0;i<populationSize();i++){
                T individual=instance.newIndividual();
                individual.generateIndividual();
                saveIndividual(i,individual);
            }
        }
    }
    public int populationSize(){
        return list.size();
    }
    public void saveIndividual(int index,T individual){
        list.set(index,individual);
    }
    public T getIndividual(int index){
        return list.get(index);
    }
    public T getFittest(){
        T fittest=list.get(0);
        for (T t:list){
            if (fittest.getFitness()<=t.getFitness()){
                fittest=t;
            }
        }
        return fittest;
    }
}
