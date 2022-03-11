/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import pkg3hge.Draw;

/**
 *
 * @author Konstantinos Meliras, Konstantinos Kontovas, Stamatis Asterios
 */
public class R3screen extends javax.swing.JFrame {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    static String[] monthName = {"Ιανουάριος", "Φεβρουάριος", "Μάρτιος", "Απρίλιος", "Μάιος", "Ιούνιος",
        "Ιούλιος", "Αύγουστος", "Σεπτέμβριος", "Οκτώβριος", "Νοέμβριος", "Δεκέμβριος"};          //month list to display to table 1
    private static int yearForView;                                                              //variable to select which year to view
    static int monthForView;                                                             //variable to select which year to view
    static int totalDraws = 0;                                                                   //variable to save how many draws we have retrieve and to display to R3results
    static int jackpots = 0;                                                                     //variable to save how many jackpots we have and to display to R3results
    static String prizesFormated;                                                                //variable to format the total money of prizes to display to R3results
    private static List<Draw> availabeDraws = new ArrayList<>();                                 //list to save the available years
    

    /**
     * Creates new form Menu
     */
    public R3screen() {
        initComponents();
        
    }
    
    //method to create Entity Manager and Factory to use for Database connection
    private static void createEMFandEM() {
        emf = Persistence.createEntityManagerFactory("3hGEPU");
        em = emf.createEntityManager();
    }
    
    //method to get all draws from the database
    private static void getDraws(){
        createEMFandEM();                                                   //call method to create manager and factory
        em.getTransaction().begin();                                        //database connection
        Query query = em.createNamedQuery("Draw.findAll", Draw.class);      //get all records from database    
        availabeDraws = query.getResultList();                           //save draw results to draw list
        em.close();                                                         //close connections from database
        emf.close();                                                                 
     }
    
    //method to display the available years to the user selection
    private static ArrayList showAvailableYears() {
        ArrayList<Integer> years = new ArrayList<>();                       //list to save the available years
        int year;                                                           //variable for the year
        for (Draw draw : availabeDraws) {                                   //iterate over the draw list
            year = draw.getDrawidtime().getYear() + 1900;                   //convert year to display it
            if (!years.contains(year)) {                                    //if list years hasn't the current draw year the add it to the list 
                years.add(year);
            }
        }
        return years;                                                       //return the list of available years 
    }
    
    //method to display the available months of selected year
    private static ArrayList showAvailableMonths(int year) {                //take input the selected year
        int yearSelected = year - 1900;                                     //covert year to compare it 
        yearForView = yearSelected;                                         //save the selected year to use it in showdraw method
        ArrayList<Integer> months = new ArrayList<>();                      //create list for the available months
        for (Draw draw : availabeDraws) {                                   //iterate over the draw list
            int month = draw.getDrawidtime().getMonth();                    //get each draw month
            if (yearSelected == draw.getDrawidtime().getYear()) {           //if selected year is the year of draw
                if (!months.contains(month)) {                              //and does't exist in the list months
                    months.add(month);                                      //save it to the list
                }
            }
        }
        return months;                                                      //return the list of available months 
    }
    
    //method to display the list of available years to table 1
    private void drawTable1(ArrayList<Integer> years) {                     //take input the list of available years
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);                                               //crete table of 1 column
        for (Integer year : years) {                                        //iterate over years list
            Object rowData[] = new Object[1];                               
            rowData[0] = year;                                              //add value 
            model.addRow(rowData);                                          //add row to the table
        }
    }
    
    //method to display the list of available months to table 2
    private void drawTable2(ArrayList<Integer> months) {                    //take input the list of available months
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);                                               //crete table of 1 column
        for (Integer month : months) {                                      //iterate over months list
            Object rowData[] = new Object[1];                   
            rowData[0] = monthName[month];                                  //add value
            model.addRow(rowData);                                          //add row to the table
        }
    }
    
    //method to calculate total money distributed for all prize categories and display all data to R3result screen
    private void showDraws() {
        totalDraws = 0;
        jackpots = 0;
        int totalPrizes = 0;                                                //variable for total money
        
        for (Draw draw : availabeDraws) {                                   //iterate over list of draws
            if (yearForView == draw.getDrawidtime().getYear()) {            //check if the draw year is the selected
                if (monthForView == draw.getDrawidtime().getMonth()) {      //check if the draw month is the selected
                   totalDraws += 1;                                         //increase number of draws
                   jackpots += draw.getjackpots();                          //increase number of draws
                   totalPrizes += draw.getdistributedMoney();               //add distributed money
                }
            }
        }
        prizesFormated = String.format("%,d", totalPrizes);                 //convert total money to string
        new R3results().setVisible(true);                                   //create R3result sceen to display all the results
    }
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelMenu = new javax.swing.JPanel();
        menuTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelInsertDraw = new javax.swing.JLabel();
        jLabelInsertDraw1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButtonExit = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setName("frameMenu"); // NOI18N
        setUndecorated(true);
        setResizable(false);

        jPanelMenu.setBackground(new java.awt.Color(204, 204, 204));
        jPanelMenu.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanelMenu.setAutoscrolls(true);
        jPanelMenu.setPreferredSize(new java.awt.Dimension(1200, 800));
        jPanelMenu.setRequestFocusEnabled(false);

        menuTitle.setBackground(new java.awt.Color(204, 204, 204));
        menuTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        menuTitle.setText("Προβολή δεδομένων ΤΖΟΚΕΡ ανά μήνα για συγκεκριμένο έτος");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelInsertDraw.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabelInsertDraw.setText("Επιλογή Έτους");

        jLabelInsertDraw1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabelInsertDraw1.setText("Επιλογή Μήνα");

        jTable1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Έτος"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setToolTipText("");
        jTable1.setRowHeight(26);
        jTable1.setSelectionBackground(new java.awt.Color(51, 153, 255));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton1.setText("Εμφάνιση Δεδομένων");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Έτος"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setToolTipText("");
        jTable2.setRowHeight(26);
        jTable2.setSelectionBackground(new java.awt.Color(51, 153, 255));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable2);

        jButtonExit.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButtonExit.setText("Κεντρικό μενού");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jLabelInsertDraw)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(107, 107, 107)
                                .addComponent(jLabelInsertDraw1))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(jButton1)))
                .addContainerGap(572, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonExit)
                .addGap(23, 23, 23))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelInsertDraw)
                    .addComponent(jLabelInsertDraw1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(101, 101, 101)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jButtonExit)
                .addGap(17, 17, 17))
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/images/tzoker-logo_1_30.png"))); // NOI18N

        javax.swing.GroupLayout jPanelMenuLayout = new javax.swing.GroupLayout(jPanelMenu);
        jPanelMenu.setLayout(jPanelMenuLayout);
        jPanelMenuLayout.setHorizontalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 60, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMenuLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(menuTitle)
                .addGap(219, 219, 219))
        );
        jPanelMenuLayout.setVerticalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMenuLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(menuTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMenu, 808, 808, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        this.dispose();                     //close this screen and return to main menu
        new Menu().setVisible(true);
    }//GEN-LAST:event_jButtonExitActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        getDraws();                                                                     //call method getDraws to get all the draws from database
        ArrayList<Integer> years = showAvailableYears();                                //call method showAvailableYears to get all the available years of the draws
        if(years.isEmpty()){                                                            //if the years list is empty display report message
            JOptionPane.showMessageDialog(this, "Δεν υπάρχουν δεδομένα για εμφάνιση.");
        }else {
            drawTable1(years);                                                          //else call method drawTable1 to display the list of available years to table 1
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        JTable source = (JTable) evt.getSource();                                           //get the selected year when user click a year from table 1
        int column = 0;
        int row = source.rowAtPoint(evt.getPoint());                                        
        String yearSelected = jTable1.getModel().getValueAt(row, column).toString();
        ArrayList<Integer> months = showAvailableMonths(Integer.parseInt(yearSelected));    //call method showAvailableMonths to get all the available months
        drawTable2(months);                                                                 //call method drawTable2 to display the list of available months to table 2
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        JTable source = (JTable) evt.getSource();                                           //get the selected month when user click a month from table 2
        int column = 0;
        int row = source.rowAtPoint(evt.getPoint());
        String monthSelected = jTable2.getModel().getValueAt(row, column).toString();
        for(int i = 0; i < monthName.length; i++) {                                         //if table2 text match with the name of monthname list
            if(monthName[i].equals(monthSelected)){                                         
                monthForView = i;                                                           //set variable montforview the number of monthname list index
            }
        }
        showDraws();                                //call method showDraw to calculate and display the statistics to R3results screen
    }//GEN-LAST:event_jTable2MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new R3screen().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelInsertDraw;
    private javax.swing.JLabel jLabelInsertDraw1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel menuTitle;
    // End of variables declaration//GEN-END:variables

}
