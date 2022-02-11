/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3hge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.html.parser.DTDConstants;

/**
 *
 * @author Hyperer
 */
public class Content {
    @SerializedName("content")
    @Expose
    public List<Game> games = new ArrayList<Game>();
    @SerializedName("totalPages")
    @Expose
    private int totalPages;

    public Content() {
       
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
    
    @Override
    public String toString() {
        return games.toString();
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    
    
    
}

