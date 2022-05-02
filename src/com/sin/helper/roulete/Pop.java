package com.sin.helper.roulete;

public class Pop {
    BetSequence[] betSequences;

    public Pop(int populationSize,boolean initialize) {
        betSequences =new BetSequence[populationSize];
        if (initialize){
            for (int i=0;i<populationSize();i++){
                BetSequence b=new BetSequence();
                saveBet(i,b);
            }
        }
    }
    public void saveBet(int index, BetSequence betSequence){
        betSequences[index]= betSequence;
    }
    public int populationSize(){
        return betSequences.length;
    }

    public BetSequence getBet(int index){
        return betSequences[index];
    }

    public BetSequence getFittest(){
        BetSequence fittest= betSequences[0];
        for (int i = 1; i < populationSize(); i++) {
            if (fittest.getFitness() <= getBet(i).getFitness()) {
                fittest = getBet(i);
            }
        }
        return fittest;
    }
}
