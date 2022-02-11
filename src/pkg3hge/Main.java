/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3hge;

import java.util.ArrayList;
import java.sql.SQLException;

/**
 *
 * @author Hyperer
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
            
        
        
          
            
            
            
            
        
        
        
        
        
        
        
        
        
        
//          PreparedStatement stmt=connection.prepareStatement("select * from GAMES ");
//                ResultSet rs=stmt.executeQuery();
//                if(rs.next())
//                {
//                   System.out.println("ID : "+rs.getInt(1) +" "+" DRAWID :"+rs.getString(2));
//                }
//                else
//                {
//                   System.out.println("No word matching in database");
//                }
//        System.exit(0);
          
           // Controller.callParameter("2395");
            //Content content =  Controller.callParameter("draw-id/2084/2227"); 
            //Content content =  Controller.callDateRange("draw-date/2021-11-05/2022-02-05"); 
               
            ArrayList<Content> contents = Controller.callDateRange("draw-date/2022-01-01/2022-02-05");
            
//                 if (content != null) {
//                     int jackpotsum =0;
//                     int totalGames = 0;
//            for (Game g : content.getGames()) {
//                System.out.println(g.toString());
//                System.out.println("dianemomena " +g.distributedMoney());
//                
//                if(g.jackpots()){
//                    jackpotsum += 1;
//                }
//                totalGames +=1;        
//            }
//                     System.out.println("total jackpots " +jackpotsum);
//                     System.out.println("total Games " +totalGames);
//                     System.out.println("total pages " +content.getTotalPages());
//        }
         if (contents != null) {
             int jackpotsum =0;
             int totalGames = 0;
             for (Content c : contents){
                 for (Game g : c.getGames()) {
                     System.out.println("-----------------------");
                     System.out.println(g.toString());
                     System.out.println(g.getDrawTime());
                     System.out.println("dianemomena " +g.distributedMoney());
                     
                     System.out.println(g.getDrawId());
               
                     
                     
                     if(g.jackpots()){
                          jackpotsum += 1;
                     }
                     totalGames +=1;
                 }
             }
             System.out.println("total jackpots " +jackpotsum);
             System.out.println("total Games " +totalGames);
             
         }    
         
             
             
          //Controller.insertTable();
             
             
             
             
             
             
             
             
             
    }
    

}
