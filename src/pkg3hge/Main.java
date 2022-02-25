/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3hge;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Hyperer
 */
public class Main {

    static EntityManagerFactory emf;
    static EntityManager em;

    private static void createEMFandEM() {
        emf = Persistence.createEntityManagerFactory("3hGEPU");
        em = emf.createEntityManager();
    }
    
    private static void insertDraw(Draw draw){
        createEMFandEM();
        em.getTransaction().begin();
        em.persist(draw);
        em.getTransaction().commit();
        em.close();
        emf.close();
        System.out.println("done");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {

        Draw draw = Controller.callDrawId("2395");

        insertDraw(draw);
    }
}
