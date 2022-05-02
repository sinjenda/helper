package com.sin.helper.roulete;

import com.sin.helper.machinelearning.Gene;

public class Bet implements Gene {
     BetSequence.pole p;
     int bet;

    public BetSequence.pole getP() {
        return p;
    }

    public void setP(BetSequence.pole p) {
        this.p = p;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public Bet(BetSequence.pole p, int bet) {
        this.p = p;
        this.bet = bet;
    }
}
