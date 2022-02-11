/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3hge;

/**
 *
 * @author Hyperer
 */
class PrizeCategories {
    public int id;
    public double divident;
    public int winners;
    public double distributed;
    public double jackpot;

    public PrizeCategories(int id, double divident, int winners, double distributed, double jackpot) {
        this.id = id;
        this.divident = divident;
        this.winners = winners;
        this.distributed = distributed;
        this.jackpot = jackpot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDivident() {
        return divident;
    }

    public void setDivident(double divident) {
        this.divident = divident;
    }

    public int getWinners() {
        return winners;
    }

    public void setWinners(int winners) {
        this.winners = winners;
    }

    public double getDistributed() {
        return distributed;
    }

    public void setDistributed(double distributed) {
        this.distributed = distributed;
    }

    public double getJackpot() {
        return jackpot;
    }

    public void setJackpot(double jackpot) {
        this.jackpot = jackpot;
    }

   
    
}


