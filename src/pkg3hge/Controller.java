/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3hge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//sql
import java.sql.*;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
//sql

/**
 *
 * @author Hyperer
 */
public class Controller {

    private ArrayList<Game> games = new ArrayList<Game>();

    public static ArrayList callDateRange(String callparameter) {
        int page = 0;
        int counter = 0;
        String responseString = "";
        Content counterContents = new Content();
        ArrayList<Content> contents = new ArrayList<>();

        do {
            String urlToCall = "https://api.opap.gr/draws/v3.0/5104/" + callparameter + "?page=" + page;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(urlToCall).build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    responseString = response.body().string();
                    page += 1;
                    counter += 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Content content = gson.fromJson(responseString, Content.class);
            counterContents = content;
            contents.add(content);
     

        } while (counter <= counterContents.getTotalPages());
        
        
        return contents;
    }
    
    public static Game callDrawId(String callparameter) {
        int page = 0;
        String responseString = "";
        ArrayList<Content> contents = new ArrayList<>();

        String urlToCall = "https://api.opap.gr/draws/v3.0/5104/" + callparameter + "?page=" + page;
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
        Game game = gson.fromJson(responseString, Game.class);
        return game;
    }
    
    public static Connection connect() {
        String connectionString = "jdbc:derby:joker;create=true";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex){
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null,ex);
        }
        return connection;
    }
    
    public static void createTable() {
        try {
            Connection connection = Controller.connect();
//            String createTableSql = "CREATE TABLE GAME"
//                    + "(DRAWID INTEGER NOT NULL PRIMARY KEY,"
//                    + "NUMBERS VARCHAR(20), "
//                    + "JOKER VARCHAR (5))";
                String insertSql = "INSERT INTO GAME VALUES(2,'10 4 5 22','19')";
            Statement statement = connection.createStatement();
//            statement.execute(createTableSql);
                statement.execute(insertSql);
            statement.close();
            connection.close();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        public static void insertTable() {
        try {
            ArrayList<Content> contents = Controller.callDateRange("draw-date/2021-12-01/2022-02-05");
            Connection connection = Controller.connect();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO GAME values(?,?,?,?)");
            for (Content c : contents) {
                for (Game g : c.getGames()) {
                    pstm.setInt(1, g.getDrawId());
                    pstm.setString(2, g.toString());
                   // pstm.setString(3, g.tzoker());
                    pstm.setString(4, g.getDrawTime());
                    pstm.executeUpdate();
                }
            }
            pstm.close();
            connection.close();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
