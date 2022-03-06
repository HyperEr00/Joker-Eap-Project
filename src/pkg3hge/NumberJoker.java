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
public class NumberJoker {

    private int number;
    private int occurrence;
    private int delays;

    public NumberJoker(int number, int occurrence, int delays) {
        this.number = number;
        this.occurrence = occurrence;
        this.delays = delays;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }

    public int getDelays() {
        return delays;
    }

    public void setDelays(int delays) {
        this.delays = delays;
    }
}
