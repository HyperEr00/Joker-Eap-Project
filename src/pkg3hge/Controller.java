/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3hge;

import com.google.gson.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author Konstantinos Meliras, Konstantinos Kontovas, Stamatis Asterios
 */
public class Controller {

    //Method to get json file by calling api for certain range (limit 3 months).
    public static ArrayList callDateRange(String callparameter) {
        int currentPage = 0;    //json file page
        int totalPages = 0;     //json file total pages
        String responseString = "";  //string variable to save the jsnon file
        ArrayList<Draw> draws = new ArrayList<>(); //Array to save the draws

        do {
            String urlToCall = "https://api.opap.gr/draws/v3.0/5104/" + callparameter + "?page=" + currentPage; //api call with parameters the range of draws
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(urlToCall).build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    responseString = response.body().string();
                    currentPage += 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create(); //create json object form json file to parse the data
            JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);

            totalPages = jsonObject.get("totalPages").getAsInt();   //get total pages of json file 

            JsonArray contentArray = jsonObject.getAsJsonArray("content");  //get draws form jsnon array 
            for (JsonElement je : contentArray) {
                draws.add(parseDraw(je.getAsJsonObject()));   //add each draw to Draw arraylist with method parseDraw
            }

        } while (currentPage <= totalPages);    //check if current page has exceeded the final page of json file

        return draws;  //reture draw arraylist to method call
    }

    public static Draw callDrawId(String callparameter) {
        String responseString = "";     //string variable to save the jsnon file

        String urlToCall = "https://api.opap.gr/draws/v3.0/5104/" + callparameter;    //api call with parameter the draw id
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urlToCall).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                responseString = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();     //create json object form json file to parse the data
        JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);

        return parseDraw(jsonObject);       //return current draw to method call
    }

    public static Draw parseDraw(JsonObject jsonObject) {
        Draw newDraw = new Draw();      //variable to parse draw
        ArrayList<Integer> winningNumbers = new ArrayList<>();  //array list to parse th winning numbers
        Collection<Prizecategory> prizecategories = new ArrayList<>(); //array list type collection to parse prize categories
        int bonus = 0;  //variable to parse joker number
        Integer drawId = jsonObject.get("drawId").getAsInt();   //variable to parse draw id number
        long drawTimeUnix = jsonObject.get("drawTime").getAsLong();  //variable to parse draw date in unix time

        JsonArray numbersList = jsonObject.getAsJsonObject("winningNumbers").getAsJsonArray("list");    //create json array for winning numbers 
        for (JsonElement je : numbersList) {                                                            //iterating json array to get the winning numbers
            winningNumbers.add(je.getAsInt());                                                          //add winning numbers to arraylist 
        }

        JsonArray bonusList = jsonObject.getAsJsonObject("winningNumbers").getAsJsonArray("bonus");     //create json array for joker number
        for (JsonElement je : bonusList) {                                                              //iterating json array to get the joker number
            bonus = je.getAsInt();                                                                      //save joker number to variable 
        }

        //add variables to newdraw object 
        newDraw.setDrawid(drawId);
        newDraw.setDrawidtime(drawTimeUnix);
        newDraw.setFirstnumber(winningNumbers.get(0));
        newDraw.setSecondnumber(winningNumbers.get(1));
        newDraw.setThirdnumber(winningNumbers.get(2));
        newDraw.setFourthnumber(winningNumbers.get(3));
        newDraw.setFifthnumber(winningNumbers.get(4));
        newDraw.setJoker(bonus);

        JsonArray pzArray = jsonObject.getAsJsonArray("prizeCategories");                               //create json array for prize categories 
        for (JsonElement je : pzArray) {                                                                //iterating json array to get the prize categories variables
            int idCategory = je.getAsJsonObject().get("id").getAsInt();                                 //initate and save variables for prizecagory object  
            double divident = je.getAsJsonObject().get("divident").getAsDouble();
            int winners = je.getAsJsonObject().get("winners").getAsInt();
            double distributed = je.getAsJsonObject().get("distributed").getAsDouble();
            double jackpot = je.getAsJsonObject().get("jackpot").getAsDouble();

            Prizecategory anPc = new Prizecategory(idCategory, distributed, divident, jackpot, winners, newDraw);   //create prizecatebory object with the constractor 
            prizecategories.add(anPc);
        }

        newDraw.setPrizecategoryCollection(prizecategories);                                    //save the prizecategory object ot a arraylist of prize categories of the new draw object
        return newDraw;                                                                         //return the newdraw object  
    }
    

}
