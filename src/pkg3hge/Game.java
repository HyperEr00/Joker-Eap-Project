/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3hge;

import com.google.gson.annotations.SerializedName;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
/**
 *
 * @author Hyperer
 */
public class Game {
    @SerializedName("drawId")
    private int drawId;
    @SerializedName("drawTime")
    private long drawTime;
    @SerializedName("winningNumbers")
    private WinningNumbers winningNumbers;
    @SerializedName("prizeCategories")
    private ArrayList<PrizeCategories> prizeCategories;

    public Game(int drawId, long drawTime, WinningNumbers winningNumbers, ArrayList<PrizeCategories> prizeCategories) {
        this.drawId = drawId;
        this.drawTime = drawTime;
        this.winningNumbers = winningNumbers;
        this.prizeCategories = prizeCategories;
    }

    public int getDrawId() {
        return drawId;
    }

    public void setDrawId(int drawId) {
        this.drawId = drawId;
    }

    public String getDrawTime() {
        long unixTime = this.drawTime / 1000;
        Date date = new Date(unixTime * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        String javaTime = sdf.format(date);

        return javaTime;
    }

    public WinningNumbers getWinningNumbers() {
        return winningNumbers;
    }

    public void setWinningNumbers(WinningNumbers winningNumbers) {
        this.winningNumbers = winningNumbers;
    }

    public ArrayList<PrizeCategories> getPrizeCategories() {
        return prizeCategories;
    }

    public void setPrizeCategories(ArrayList<PrizeCategories> prizeCategories) {
        this.prizeCategories = prizeCategories;
    }

    public Game() {
    }
    
    private class WinningNumbers {
        private int [] list = new int[5];
        private int [] bonus = new int[1];

        public WinningNumbers() {
        }

        public int[] getList() {
            return list;
        }

        public int[] getBonus() {
            return bonus;
        }

        public void setList(int[] list) {
            this.list = list;
        }

        public void setBonus(int[] bonus) {
            this.bonus = bonus;
        }
        
        @Override 
        public String toString(){
        String all = "";
        for(int i=0; i<list.length;i++){
            all= all +list[i] +" ";
        }
        return all + " " +bonus[0];
       // return Arrays.toString(list) + Arrays.toString(bonus);
        }
        
    }
    
     @Override
    public String toString() {
        return (getWinningNumbers().toString());
    }
    
    public int distributedMoney() {
        double totalDistributed = 0;
        for (PrizeCategories pc : prizeCategories) {
            if ((pc.getId() == 1) || (pc.getId() == 2)) {
                totalDistributed += pc.getDistributed() + pc.getJackpot();
            }
        }
        return (int) totalDistributed;
    }
    
    public boolean jackpots() {
        for (PrizeCategories pc : prizeCategories) {
            if ((pc.getId() == 1) && (pc.getWinners() == 0)) {
                return  true;
            }
        }
        return false;
    }
    
}
