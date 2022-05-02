package com.sin.helper.roulete;

import com.sin.helper.machinelearning.*;
import me.tongfei.progressbar.ProgressBar;

import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class BetSequence extends AbstractIndividual implements BiConsumer<BetSequence.pole, Integer> {
    double fitness = 0;
    private final List<Integer> redNumbers = Arrays.asList(1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 30, 32, 34, 36);
    private final List<Integer> blackNumbers = Arrays.asList(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 29, 31, 33, 35);
    private final List<Integer> slope1 = Arrays.asList(1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31, 34);
    private final List<Integer> slope2 = Arrays.asList(2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35);
    private final List<Integer> slope3 = Arrays.asList(3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36);
    private final List<Integer> pair=Arrays.asList(1,3,5,7,9,11,13,15,17,19,21,23,25,27,29,31,33,35);
    private final List<Integer> manque=Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18);
    int number = 0;
    int win = 0;
    AtomicInteger expense = new AtomicInteger();

    @Override
    public void accept(pole pole, Integer integer) {
        switch (pole) {
            case zero -> {
                if (number == 0) {
                    win += integer * 36;
                }
            }
            case one -> {
                if (number == 1) {
                    win += integer * 36;
                }
            }
            case two -> {
                if (number == 2) {
                    win += integer * 36;
                }
            }
            case three -> {
                if (number == 3) {
                    win += integer * 36;
                }
            }
            case four -> {
                if (number == 4) {
                    win += integer * 36;
                }
            }
            case five -> {
                if (number == 5) {
                    win += integer * 36;
                }
            }
            case six -> {
                if (number == 6) {
                    win += integer * 36;
                }
            }
            case seven -> {
                if (number == 7) {
                    win += integer * 36;
                }
            }
            case eight -> {
                if (number == 8) {
                    win += integer * 36;
                }
            }
            case nine -> {
                if (number == 9) {
                    win += integer * 36;
                }
            }
            case ten -> {
                if (number == 10) {
                    win += integer * 36;
                }
            }
            case eleven -> {
                if (number == 11) {
                    win += integer * 36;
                }
            }
            case twelve -> {
                if (number == 12) {
                    win += integer * 36;
                }
            }
            case thirteen -> {
                if (number == 13) {
                    win += integer * 36;
                }
            }
            case fourteen -> {
                if (number == 14) {
                    win += integer * 36;
                }
            }
            case fifteen -> {
                if (number == 15) {
                    win += integer * 36;
                }
            }
            case sixteen -> {
                if (number == 16) {
                    win += integer * 36;
                }
            }
            case seventeen -> {
                if (number == 17) {
                    win += integer * 36;
                }
            }
            case eighteen -> {
                if (number == 18) {
                    win += integer * 36;
                }
            }
            case nineteen -> {
                if (number == 19) {
                    win += integer * 36;
                }
            }
            case twenty -> {
                if (number == 20) {
                    win += integer * 36;
                }
            }
            case twenty_one -> {
                if (number == 21) {
                    win += integer * 36;
                }
            }
            case twenty_two -> {
                if (number == 22) {
                    win += integer * 36;
                }
            }
            case twenty_three -> {
                if (number == 23) {
                    win += integer * 36;
                }
            }
            case twenty_four -> {
                if (number == 24) {
                    win += integer * 36;
                }
            }
            case twenty_five -> {
                if (number == 25) {
                    win += integer * 36;
                }
            }
            case twenty_six -> {
                if (number == 26) {
                    win += integer * 36;
                }
            }
            case twenty_seven -> {
                if (number == 27) {
                    win += integer * 36;
                }
            }
            case twenty_eight -> {
                if (number == 28) {
                    win += integer * 36;
                }
            }
            case twenty_nine -> {
                if (number == 29) {
                    win += integer * 36;
                }
            }
            case thirty -> {
                if (number == 30) {
                    win += integer * 36;
                }
            }
            case thirty_one -> {
                if (number == 31) {
                    win += integer * 36;
                }
            }
            case thirty_two -> {
                if (number == 32) {
                    win += integer * 36;
                }
            }
            case thirty_three -> {
                if (number == 33) {
                    win += integer * 36;
                }
            }
            case thirty_four -> {
                if (number == 34) {
                    win += integer * 36;
                }
            }
            case thirty_five -> {
                if (number == 35) {
                    win += integer * 36;
                }
            }
            case thirty_six -> {
                if (number == 36) {
                    win += integer * 36;
                }
            }
            case red -> {
                if (redNumbers.contains(number)) {
                    win += integer * 2;
                }
            }
            case black -> {
                if (blackNumbers.contains(number)) {
                    win += integer * 2;
                }
            }
            case slope1 -> {
                if (slope1.contains(number)) {
                    win += 3 * integer;
                }
            }
            case slope2 -> {
                if (slope2.contains(number)) {
                    win += 3 * integer;
                }
            }
            case slope3 -> {
                if (slope3.contains(number)) {
                    win += 3 * integer;
                }
            }
            case first_twelve -> {
                if (Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).contains(number)) {
                    win += 3 * integer;
                }
            }
            case second_twelve -> {
                if (Arrays.asList(13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24).contains(number)){
                    win+=3*integer;
                }
            }
            case third_twelve -> {
                if (Arrays.asList(25,26,27,28,29,30,31,32,33,34,35,36).contains(number))
                    win+=3*integer;
            }
            case impair -> {
                if (!pair.contains(number)){
                    win+=2*integer;
                }
            }
            case pair -> {
                if (pair.contains(number)){
                    win+=2*integer;
                }
            }
            case passe -> {
                if (!manque.contains(number))
                    win+=2*integer;
            }
            case manque -> {
                if (manque.contains(number))
                    win+=integer*2;
            }
        }
    }

    public enum pole {
        zero, one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen, seventeen, eighteen, nineteen,
        twenty, twenty_one, twenty_two, twenty_three, twenty_four, twenty_five, twenty_six, twenty_seven, twenty_eight, twenty_nine,
        thirty, thirty_one, thirty_two, thirty_three, thirty_four, thirty_five, thirty_six, red, black, slope1, slope2, slope3, first_twelve,
        second_twelve, third_twelve, impair, pair, passe, manque
    }

    public BetSequence() {
        super(pole.values().length);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T extends AbstractIndividual> T newIndividual() {
        return (T) new BetSequence();
    }

    @Override
    public void generateIndividual() {
        for(int i=0;i!=pole.values().length;i++){
            saveGene(i,new Bet(pole.values()[i],ThreadLocalRandom.current().nextInt(0,1000)));
        }
    }

    public double getFitness() {
        if (fitness == 0) {
            for (int i=0;i!=individualSize();i++) expense.set(expense.get() + ((Bet) getGene(i)).bet);
            expense.set(expense.get() * 100);
            for (int i1 = 0; i1 != 100; i1++) {
                number = ThreadLocalRandom.current().nextInt(0, 36);
                for (int i=0;i!=individualSize();i++){
                    accept(((Bet)getGene(i)).p,((Bet) getGene(i)).bet);
                }
            }
            fitness=win-expense.get();
        }
        return fitness;
    }

    @Override
    public <T extends AbstractIndividual> T crossover(T parent2) {
        BetSequence child=new BetSequence();
        for (int i=0;i!=pole.values().length;i++) {
            if (ThreadLocalRandom.current().nextInt(0, 1) == 0) {
                child.saveGene(i,getGene(i));
            }
            else {
                child.saveGene(i, parent2.getGene(i));
            }
        }
        return (T) child;
    }

    @Override
    public void mutate() {
        int num=ThreadLocalRandom.current().nextInt(0,individualSize());
        ((Bet)getGene(num)).setBet(ThreadLocalRandom.current().nextInt(0,1000));
    }

    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        for (Gene g:list){
            Bet b=(Bet) g;
            builder.append(b.p.toString()).append(": ").append(b.bet).append(", ");
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        AbstractPopulation<BetSequence> pop = new AbstractPopulation<>(50, true,new BetSequence());
        ProgressBar progressBar = new ProgressBar("calculating", 1000000);
        for (int i = 0; i < 1000000; i++) {
            progressBar.step();
            pop = GA.evolvePopulation(pop);
            progressBar.setExtraMessage("best: " + pop.getFittest().getFitness());
        }
        System.out.println("\nbest: "+pop.getFittest());
    }
}
