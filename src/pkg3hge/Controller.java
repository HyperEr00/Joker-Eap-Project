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
 * @author Hyperer
 */
public class Controller {
    
    public static ArrayList callDateRange(String callparameter) {
        int currentPage = 0;
        int totalPages = 0;
        String responseString = "";
        ArrayList<Draw> draws = new ArrayList<>();

        do {
            String urlToCall = "https://api.opap.gr/draws/v3.0/5104/" + callparameter + "?page=" + currentPage;
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

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);

            totalPages = jsonObject.get("totalPages").getAsInt();

            JsonArray contentArray = jsonObject.getAsJsonArray("content");
            for (JsonElement je : contentArray) {
                draws.add(parseDraw(je.getAsJsonObject()));
            }

        } while (currentPage <= totalPages);

        return draws;
    }

    public static Draw callDrawId(String callparameter) {
        String responseString = "";

        String urlToCall = "https://api.opap.gr/draws/v3.0/5104/" + callparameter;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urlToCall).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                responseString = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);

        return parseDraw(jsonObject);
    }

    public static Draw parseDraw(JsonObject jsonObject) {
        Draw newDraw = new Draw();
        ArrayList<Integer> winningNumbers = new ArrayList<>();
        Collection<Prizecategory> prizecategorys = new ArrayList<>();
        int bonus = 0;
        Integer drawId = jsonObject.get("drawId").getAsInt();
        long drawTimeUnix = jsonObject.get("drawTime").getAsLong();

        JsonArray numbersList = jsonObject.getAsJsonObject("winningNumbers").getAsJsonArray("list");
        for (JsonElement je : numbersList) {
            winningNumbers.add(je.getAsInt());
        }

        JsonArray bonusList = jsonObject.getAsJsonObject("winningNumbers").getAsJsonArray("bonus");
        for (JsonElement je : bonusList) {
            bonus = je.getAsInt();
        }

        newDraw.setDrawid(drawId);
        newDraw.setDrawidtime(drawTimeUnix);
        newDraw.setFirstnumber(winningNumbers.get(0));
        newDraw.setSecondnumber(winningNumbers.get(1));
        newDraw.setThirdnumber(winningNumbers.get(2));
        newDraw.setFourthnumber(winningNumbers.get(3));
        newDraw.setFifthnumber(winningNumbers.get(4));
        newDraw.setJoker(bonus);

        JsonArray pzArray = jsonObject.getAsJsonArray("prizeCategories");
        for (JsonElement je : pzArray) {
            int idCategory = je.getAsJsonObject().get("id").getAsInt();
            double divident = je.getAsJsonObject().get("divident").getAsDouble();
            int winners = je.getAsJsonObject().get("winners").getAsInt();
            double distributed = je.getAsJsonObject().get("distributed").getAsDouble();
            double jackpot = je.getAsJsonObject().get("jackpot").getAsDouble();

            Prizecategory anPc = new Prizecategory(idCategory, distributed, divident, jackpot, winners, newDraw);
            prizecategorys.add(anPc);
        }

        newDraw.setPrizecategoryCollection(prizecategorys);
        return newDraw;
    }

}
