/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3hge;

/**
 *
 * @author Konstantinos Meliras, Konstantinos Kontovas, Stamatis Asterios
 */

//class to use to get statistics for the winning numbers and joker numbers. we store for each number 
//in certain range of draws the number of numbers'draws (occurrence) and the number of delays until last draw.
public class NumberJoker implements Comparable<NumberJoker>{

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

    @Override
    public int compareTo(NumberJoker t) {   //method to compare each numbers base to occurence
        if(this.occurrence > t.occurrence) { return -1;}
        else if(this.occurrence < t.occurrence) { return 1;}
        else {
            return 0;
        }
    }
}
