/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import pkg3hge.Controller;
import pkg3hge.Draw;
import pkg3hge.Prizecategory;

/**
 *
 * @author Konstantinos Meliras, Konstantinos Kontovas, Stamatis Asterios
 */
public class R2screen extends javax.swing.JFrame {
    private static EntityManagerFactory emf;        
    private static EntityManager em;
    private static Collection<Draw> drawsMain = new ArrayList<Draw>();     //list to save and retrieve draws    
       
    /**
     * Creates new form Menu
     */

    public R2screen() {
        initComponents();
        try {
            jDateChooserDrawStartRange.setDate(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2022"));   //set a default date to the start range date field
            jDateChooserDrawEndRange.setDate(new SimpleDateFormat("dd-MM-yyyy").parse("15-01-2022"));     //set a default date to the end range  date field
            jDateChooserDrawStartRangeDel.setDate(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2022"));   //set a default date to the start range date delete field
            jDateChooseDrawEndRangeDel.setDate(new SimpleDateFormat("dd-MM-yyyy").parse("15-01-2022"));     //set a default date to the end range date delete field
        } catch (ParseException ex) {
            System.out.println(ex);
        }
    }
    
    //method to create Entity Manager and Factory to use for Database connection
    private static void createEMFandEM() {               
        emf = Persistence.createEntityManagerFactory("3hGEPU");
        em = emf.createEntityManager();
    }
    
    //method to show each draw
    private void showDraw() {           
        Draw draw = Controller.callDrawId(jTextFieldDrawSelection.getText());   //call the api to get the draw with the current typed drawid 
        drawsMain.add(draw);                                                    //save draw to list          
        drawTable1();                                                           //call nethod drawtable to show draw in table 1
        showWinnings(draw.getDrawid());                                         //call method showWinnings to show the Prize categories for the draw
    }
    
    //methos to show a range of draws
    private void showRange(String dateRange) {  
        drawsMain = Controller.callDateRange(dateRange);                        //call the api to get the draw list and append it to drawsMain
        drawTable1();                                                           //call nethod drawtable to show draw in table 1
        String value = jTable1.getModel().getValueAt(0, 1).toString();          //get the first draw number
        showWinnings(Integer.valueOf(value));                                   //call method showWinnings to show the Prize categories for the first draw
    }
    
    //method to show each draw to table 1
    private void drawTable1() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");      //format date to greek 
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();   //create table 1
        model.setRowCount(0);                                           //set the initial row number
        int no = 1;                                                     //set the stard of table index
        for (Draw draw : drawsMain) {                                   //itarate drawMain list
            Object rowData[] = new Object[9];                           //number of table columns - 9
            rowData[0] = no;                                            //index number
            rowData[1] = draw.getDrawid();                              //draw id 
            rowData[2] = draw.getFirstnumber();                         //first winning number 
            rowData[3] = draw.getSecondnumber();                        //second winning number
            rowData[4] = draw.getThirdnumber();                         //third winning number   
            rowData[5] = draw.getFourthnumber();                        //fourth winning number
            rowData[6] = draw.getFifthnumber();                         //fifth winning number
            rowData[7] = draw.getJoker();                               //joker number
            rowData[8] = sdf.format(draw.getDrawidtime());              //date of draw
            model.addRow(rowData);                                      //insert row to the table 1
            no += 1;                                                    //point index number to next
        }
    }
    
    //method to show the prize categories winnings to table 2 by getting the draw id number
    private void showWinnings(int number) {
        String[] catTzoker = {"5+1", "5", "4+1", "4", "3+1", "3", "2+1", "1+1"};        //create array for each prize category name 
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();               //create table 2 
        model.setRowCount(0);                                                           //set the initial row number
        int cat = 0;                                                                    //variable for category number
        Object rowData[] = new Object[3];                                               //number of table columns - 3
        for (Draw draw : drawsMain) {                                                   //itarate drawMain list
            if (draw.getDrawid() == number) {                                           //check if drawid match with given number
                Collection<Prizecategory> prizeCategories = draw.getPrizecategoryCollection();
                for (int i = 1; i <= 8; i++) {
                    for (Prizecategory pr : prizeCategories) {
                        if (pr.getIdcategory() == i) {
                            rowData[0] = catTzoker[cat];                                 //first column get category name from the array we created
                            rowData[1] = pr.getWinners();                                //second column get the prize category winners
                            if (pr.getWinners() == 0) {                                  //check if there are winners 
                                rowData[2] = "ΤΖΑΚΠΟΤ";                                  //if no print jackpot
                            } else {                                                     //else print the dividents
                                rowData[2] = pr.getDivident();
                            }
                            cat += 1;                                                    //change category for next loop
                            model.addRow(rowData);                                       //add the row to the table
                        }
                    }
                }
            }
        }
    }

    //method to insert draw to database
    private static int insertDraw(Draw draw) {
        int recordsWritted = 0;                                                          //variable to record how many draws have writted to databes 
        createEMFandEM();                                                                //call method to create manager and factory
        em.getTransaction().begin();                                                     //database connection
        Query query = em.createNamedQuery("Draw.findByDrawid", Draw.class);              //if exist call query with the parameter of drawid  
        query.setParameter("drawid", draw.getDrawid());                                  //get the result to object draw    
        if (query.getResultList().isEmpty()) {                                           //check if resultlist is empty which means draw not exist       
            em.persist(draw);                                                            //if no exist write the draw to database
            recordsWritted += 1;                                                         //increace the number of draws writed 
        }
        em.getTransaction().commit();                                                    //save changes to database
        em.close();                                 
        emf.close();                                                                     //close connections from database
        return recordsWritted;                                                           //return the number of total draws writted
    }
    
    //method to delete draw from database
    private static int deleteDraw(int drawId) {
        int recordsDeleted = 0;                                                         //variable to record how many draws have deleted to databes
        createEMFandEM();                                                               //call method to create manager and factory
        em.getTransaction().begin();                                                    //database connection
        Query query = em.createNamedQuery("Draw.findByDrawid", Draw.class);             //if exist call query with the parameter of drawid  
        query.setParameter("drawid", drawId);                                           //get the result to object draw    
        if (!query.getResultList().isEmpty()) {                                         //check if resultlist is empty which means draw not exist
            Draw draw = (Draw) query.getSingleResult();
            em.remove(draw);                                                            //remove draw from database
            recordsDeleted += 1;                                                        //increace the number of draws deleted
        }
        em.getTransaction().commit();                                                   //save changes to database
        em.close();                                                                     
        emf.close();                                                                    //close connections from database
        return recordsDeleted;                                                          //return the number of total draws deleted 
    }
    
    //method to delete draws from database between range of date
    private static int deleteDrawRange(Date start, Date end) {      
        int recordsDeleted = 0;                                                         //variable to record how many draws have deleted to databes
        Date drawDate = null;

        createEMFandEM();                                                               //call method to create manager and factory
        em.getTransaction().begin();                                                    //database connection
        Query query = em.createNamedQuery("Draw.findAll", Draw.class);                  //query database to get all draws
        List<Draw> draws = query.getResultList();           

        for (Draw draw : draws) {                                                       //iterate over draw list to check if each draw date is between the select date
                drawDate = draw.getDrawidtime();
            if ((drawDate.before(end) && drawDate.after(start)) || drawDate.equals(start) || drawDate.equals(end)) {
                em.remove(draw);                                                        //if it is delete the current draw and increase the counter
                recordsDeleted += 1;
            }
        }
        em.getTransaction().commit();                                                   //save changes to database
        em.close();                                         
        emf.close();                                                                    //close connections from database
        return recordsDeleted;                                                          //return the number of total draws deleted 
    }
    
    private void showAvailableData() {
        drawsMain.clear();
        createEMFandEM();
        em.getTransaction().begin();
        Query query = em.createNamedQuery("Draw.findAll", Draw.class);
        drawsMain = query.getResultList();
        drawTable1();
        em.close();
        emf.close();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelR2 = new javax.swing.JPanel();
        menuTitle = new javax.swing.JLabel();
        jButtonExit = new javax.swing.JButton();
        jLabelInsertDraw = new javax.swing.JLabel();
        jTextFieldDrawSelection = new javax.swing.JTextField();
        jPanelR2Tables = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButtonSaveStatistics = new javax.swing.JButton();
        jButtonLoadStatistics = new javax.swing.JButton();
        dataStatus = new javax.swing.JLabel();
        dataStatus1 = new javax.swing.JLabel();
        jButtonViewStatistics = new javax.swing.JButton();
        jLabelInsertDrawRange = new javax.swing.JLabel();
        jLabelInsertDrawRange1 = new javax.swing.JLabel();
        jButtonViewStatisticsRange = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabelInsertDrawForDel = new javax.swing.JLabel();
        jTextFieldDrawForDel = new javax.swing.JTextField();
        jButtonDeleteStatistics = new javax.swing.JButton();
        jLabelInsertDrawRange3 = new javax.swing.JLabel();
        jLabelInsertDrawRangeDel = new javax.swing.JLabel();
        jButtonDeleteStatisticsRange = new javax.swing.JButton();
        menuTitle1 = new javax.swing.JLabel();
        jDateChooserDrawStartRangeDel = new com.toedter.calendar.JDateChooser();
        jDateChooseDrawEndRangeDel = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jDateChooserDrawEndRange = new com.toedter.calendar.JDateChooser();
        jDateChooserDrawStartRange = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setName("frameR2"); // NOI18N
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1200, 800));

        jPanelR2.setBackground(new java.awt.Color(204, 204, 204));
        jPanelR2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelR2.setPreferredSize(new java.awt.Dimension(1200, 800));

        menuTitle.setBackground(new java.awt.Color(204, 204, 204));
        menuTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        menuTitle.setText("Διαχείριση δεδομένων ΤΖΟΚΕΡ");

        jButtonExit.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButtonExit.setText("Κεντρικό μενού");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        jLabelInsertDraw.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabelInsertDraw.setText("Εισάγετε τον αριθμό της κλήρωσης : ");

        jTextFieldDrawSelection.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextFieldDrawSelection.setText("π.χ 2395");
        jTextFieldDrawSelection.setMinimumSize(new java.awt.Dimension(12, 3));
        jTextFieldDrawSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDrawSelectionMouseClicked(evt);
            }
        });

        jPanelR2Tables.setBackground(new java.awt.Color(204, 204, 204));
        jPanelR2Tables.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelR2Tables.setRequestFocusEnabled(false);

        jTable1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Κλήρωση", "1ος Αριθμός", "2ος Αριθμός", "3ος Αριθμός", "4ος Αριθμός", "5ος Αριθμός", "ΤΖΟΚΕΡ", "Ημερομηνία"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(20);
        jTable1.setSelectionBackground(new java.awt.Color(51, 153, 255));
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(30);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(30);
            jTable1.getColumnModel().getColumn(1).setMinWidth(65);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(65);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(65);
            jTable1.getColumnModel().getColumn(2).setMinWidth(80);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(80);
            jTable1.getColumnModel().getColumn(3).setMinWidth(80);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(80);
            jTable1.getColumnModel().getColumn(4).setMinWidth(80);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(4).setMaxWidth(80);
            jTable1.getColumnModel().getColumn(5).setMinWidth(80);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(5).setMaxWidth(80);
            jTable1.getColumnModel().getColumn(6).setMinWidth(80);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(6).setMaxWidth(80);
            jTable1.getColumnModel().getColumn(7).setMinWidth(60);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(60);
            jTable1.getColumnModel().getColumn(7).setMaxWidth(60);
        }

        jTable2.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Επιτυχίες", "Κέρδη"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setRowHeight(24);
        jTable2.setSelectionBackground(new java.awt.Color(51, 153, 255));
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(60);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(60);
        }

        jButtonSaveStatistics.setBackground(new java.awt.Color(146, 202, 142));
        jButtonSaveStatistics.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButtonSaveStatistics.setText("Αποθήκευση δεδομένων");
        jButtonSaveStatistics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveStatisticsActionPerformed(evt);
            }
        });

        jButtonLoadStatistics.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButtonLoadStatistics.setText("Προβολή αποθηκευμένων δεδομένων");
        jButtonLoadStatistics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadStatisticsActionPerformed(evt);
            }
        });

        dataStatus.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        dataStatus.setForeground(new java.awt.Color(255, 0, 51));
        dataStatus.setText("ONLINE");

        dataStatus1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        dataStatus1.setText("Πηγή δεδομένων:");

        javax.swing.GroupLayout jPanelR2TablesLayout = new javax.swing.GroupLayout(jPanelR2Tables);
        jPanelR2Tables.setLayout(jPanelR2TablesLayout);
        jPanelR2TablesLayout.setHorizontalGroup(
            jPanelR2TablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelR2TablesLayout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(jButtonSaveStatistics)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonLoadStatistics)
                .addGap(98, 98, 98))
            .addGroup(jPanelR2TablesLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 664, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE))
            .addGroup(jPanelR2TablesLayout.createSequentialGroup()
                .addGap(188, 188, 188)
                .addComponent(dataStatus1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dataStatus)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelR2TablesLayout.setVerticalGroup(
            jPanelR2TablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelR2TablesLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanelR2TablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataStatus)
                    .addComponent(dataStatus1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelR2TablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelR2TablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSaveStatistics)
                    .addComponent(jButtonLoadStatistics))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jButtonViewStatistics.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButtonViewStatistics.setText("Προβολή κλήρωσης");
        jButtonViewStatistics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewStatisticsActionPerformed(evt);
            }
        });

        jLabelInsertDrawRange.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabelInsertDrawRange.setText("Εισάγετε τον εύρος ημερομηνιών : ");

        jLabelInsertDrawRange1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabelInsertDrawRange1.setText("έως");

        jButtonViewStatisticsRange.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButtonViewStatisticsRange.setText("Προβολή κληρώσεων");
        jButtonViewStatisticsRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewStatisticsRangeActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelInsertDrawForDel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabelInsertDrawForDel.setText("Εισάγετε τον αριθμό της κλήρωσης : ");

        jTextFieldDrawForDel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextFieldDrawForDel.setText("π.χ 2395");
        jTextFieldDrawForDel.setMinimumSize(new java.awt.Dimension(12, 3));
        jTextFieldDrawForDel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDrawForDelMouseClicked(evt);
            }
        });

        jButtonDeleteStatistics.setBackground(new java.awt.Color(204, 133, 133));
        jButtonDeleteStatistics.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButtonDeleteStatistics.setForeground(new java.awt.Color(255, 255, 255));
        jButtonDeleteStatistics.setText("Διαγραφή κλήρωσης");
        jButtonDeleteStatistics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteStatisticsActionPerformed(evt);
            }
        });

        jLabelInsertDrawRange3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabelInsertDrawRange3.setText("έως");

        jLabelInsertDrawRangeDel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabelInsertDrawRangeDel.setText("Εισάγετε τον εύρος ημερομηνιών : ");

        jButtonDeleteStatisticsRange.setBackground(new java.awt.Color(204, 133, 133));
        jButtonDeleteStatisticsRange.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButtonDeleteStatisticsRange.setForeground(new java.awt.Color(255, 255, 255));
        jButtonDeleteStatisticsRange.setText("Διαγραφή κληρώσεων");
        jButtonDeleteStatisticsRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteStatisticsRangeActionPerformed(evt);
            }
        });

        menuTitle1.setBackground(new java.awt.Color(204, 204, 204));
        menuTitle1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        menuTitle1.setText("Διαγραφή δεδομένων");

        jDateChooserDrawStartRangeDel.setDateFormatString("dd-MM-yyyy");
        jDateChooserDrawStartRangeDel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jDateChooserDrawStartRangeDel.setPreferredSize(new java.awt.Dimension(140, 30));

        jDateChooseDrawEndRangeDel.setDateFormatString("dd-MM-yyyy");
        jDateChooseDrawEndRangeDel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jDateChooseDrawEndRangeDel.setPreferredSize(new java.awt.Dimension(140, 30));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabelInsertDrawForDel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldDrawForDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jLabelInsertDrawRangeDel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooserDrawStartRangeDel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelInsertDrawRange3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooseDrawEndRangeDel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(426, 426, 426)
                .addComponent(menuTitle1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addComponent(jButtonDeleteStatistics)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonDeleteStatisticsRange)
                .addGap(108, 108, 108))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooserDrawStartRangeDel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelInsertDrawRange3)
                            .addComponent(jDateChooseDrawEndRangeDel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDeleteStatisticsRange))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelInsertDrawForDel)
                            .addComponent(jTextFieldDrawForDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelInsertDrawRangeDel))
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDeleteStatistics)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/images/tzoker-logo_1_30.png"))); // NOI18N

        jDateChooserDrawEndRange.setDateFormatString("dd-MM-yyyy");
        jDateChooserDrawEndRange.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jDateChooserDrawEndRange.setPreferredSize(new java.awt.Dimension(140, 30));

        jDateChooserDrawStartRange.setDateFormatString("dd-MM-yyyy");
        jDateChooserDrawStartRange.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jDateChooserDrawStartRange.setPreferredSize(new java.awt.Dimension(140, 30));

        javax.swing.GroupLayout jPanelR2Layout = new javax.swing.GroupLayout(jPanelR2);
        jPanelR2.setLayout(jPanelR2Layout);
        jPanelR2Layout.setHorizontalGroup(
            jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelR2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonExit)
                .addGap(40, 40, 40))
            .addGroup(jPanelR2Layout.createSequentialGroup()
                .addGroup(jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelR2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(254, 254, 254)
                        .addComponent(menuTitle))
                    .addGroup(jPanelR2Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanelR2Tables, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelR2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelInsertDraw)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldDrawSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(jLabelInsertDrawRange)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooserDrawStartRange, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelInsertDrawRange1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooserDrawEndRange, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
            .addGroup(jPanelR2Layout.createSequentialGroup()
                .addGap(217, 217, 217)
                .addComponent(jButtonViewStatistics)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonViewStatisticsRange)
                .addGap(149, 149, 149))
        );
        jPanelR2Layout.setVerticalGroup(
            jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelR2Layout.createSequentialGroup()
                .addGroup(jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelR2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(menuTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelInsertDraw)
                        .addComponent(jTextFieldDrawSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelInsertDrawRange))
                    .addGroup(jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jDateChooserDrawStartRange, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jDateChooserDrawEndRange, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelInsertDrawRange1, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addGap(18, 18, 18)
                .addGroup(jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonViewStatistics)
                    .addComponent(jButtonViewStatisticsRange))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelR2Tables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonExit)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelR2, 1213, 1213, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelR2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    
    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        this.dispose();                                 //close this screen and return to main menu
        new Menu().setVisible(true);
    }//GEN-LAST:event_jButtonExitActionPerformed

    private void jTextFieldDrawSelectionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDrawSelectionMouseClicked
        jTextFieldDrawSelection.setText("");            //clear the text to selected text field
    }//GEN-LAST:event_jTextFieldDrawSelectionMouseClicked
    
    //show the single draw statistics
    private void jButtonViewStatisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewStatisticsActionPerformed
        dataStatus.setText("ONLINE");                                                           //show where the source of data
        drawsMain.clear();                                                                      //clear drawsmain list
        try {
            int drawSelected = Integer.parseInt(jTextFieldDrawSelection.getText());             //get drawid number for textfield
            if (drawSelected > 0) {                                                             //if is above 0 then call showdraw method
                try {
                    showDraw();                                                                     
                } catch (NullPointerException e) {                                              //if draw doesn't exist we get exception and print warning message
                    JOptionPane.showMessageDialog(this, "Η συγκεκριμένη κλήρωση δεν υπάρχει."); //we clear table 1 and table 2
                    DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();
                    model1.setRowCount(0);
                    DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();
                    model2.setRowCount(0);
                }
            } else {                                                                                        
                JOptionPane.showMessageDialog(this, "Παρακαλούμε εισάγετε έγκυρo αριθμός κλήρωσης."); //if typed drawid number is bellow 0 print warning message
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Παρακαλούμε εισάγετε έγκυρo αριθμός κλήρωσης.");     //if typed text isn't number print warning message
        }
    }//GEN-LAST:event_jButtonViewStatisticsActionPerformed
    
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        JTable source = (JTable) evt.getSource();                   
        int column = 1;
        int row = source.rowAtPoint(evt.getPoint());
        String value = jTable1.getModel().getValueAt(row, column).toString();   //get the draw number when mouse click in certain row of table 1
        showWinnings(Integer.valueOf(value));                                   //call method showinnings with drawid number and display prize category winnings
    }//GEN-LAST:event_jTable1MouseClicked

    //show a range of draw by date statistics
    private void jButtonViewStatisticsRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewStatisticsRangeActionPerformed
        dataStatus.setText("ONLINE");                                                                  //show where the source of data
        drawsMain.clear();                                                                             //clear drawsmain list
        SimpleDateFormat opap = new SimpleDateFormat("yyyy-MM-dd");                                    //opap-format request to call api

        try {
            if ((jDateChooserDrawStartRange.getDate() != null) && (jDateChooserDrawEndRange.getDate() != null)) {
                String startDateString = opap.format(jDateChooserDrawStartRange.getDate());           //get date from startRange chooser and conver it
                String endDateString = opap.format(jDateChooserDrawEndRange.getDate());               //get date from endRange chooser and conver it
                try {
                    showRange("draw-date/" + startDateString + "/" + endDateString);                  //call method showRange to get the draws from api call
                } catch (NullPointerException | ArrayIndexOutOfBoundsException ex) {                  //if not results found or error show warning
                    JOptionPane.showMessageDialog(this, "Δεν υπάρχουν κληρώσεις ή το εύρος ξεπερνάει το τρίμηνο");
                    DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();                //clear table 1
                    model1.setRowCount(0);
                    DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();                //clear table 2
                    model2.setRowCount(0);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Εισάγετε την ημερομηνία στη μορφή 15-01-2022 .");      //if there is error in parsing then display message
            }
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Εισάγετε την ημερομηνία στη μορφή 15-01-2022 .");      //if there is error in parsing then display message
        }
    }//GEN-LAST:event_jButtonViewStatisticsRangeActionPerformed
        
    private void jButtonSaveStatisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveStatisticsActionPerformed
        int records = 0;                
        for (Draw draw : drawsMain) {                   //iterate over drawsmain list       
            records += insertDraw(draw);                //calls insertDraw method for each draw and get result 
        }
        if (records > 0) {                                                                              //if number os saved draws is above 0 display the current number
            JOptionPane.showMessageDialog(this, "Αποθηκεύτηκαν " + records + " κληρώσεις. ");
        } else {                                                                                        //else print no result message
            JOptionPane.showMessageDialog(this, "Δεν αποθηκεύτηκε καμία κλήρωση διότι υπήρχαν ήδη στη βάση δεδομένων.");
        }
    }//GEN-LAST:event_jButtonSaveStatisticsActionPerformed

    private void jTextFieldDrawForDelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDrawForDelMouseClicked
        jTextFieldDrawForDel.setText("");           //clear the text to select text field
    }//GEN-LAST:event_jTextFieldDrawForDelMouseClicked

    private void jButtonDeleteStatisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteStatisticsActionPerformed
        int response = JOptionPane.showConfirmDialog(null, "Θέλετε να προχωρήσετε σε διαγραφή;", "Επιβεβαίωση", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); //ask confirmation for delete
        if (response == JOptionPane.YES_OPTION) {       //if user answer yes proceed                    
            try {
                int drawSelected = Integer.parseInt(jTextFieldDrawForDel.getText());                   //get drawid number from textfield
                if (drawSelected > 0) {                                                                //if is above 0 then call showdraw method
                    try {
                        int records = deleteDraw(drawSelected);                                         //call deletedraw method and if draw is deleted (1)
                        if (records > 0) {          
                            JOptionPane.showMessageDialog(this, "Η κλήρωση " + drawSelected + " διαγράφηκε επιτυχώς.");         //print confirmation
                        } else {
                            JOptionPane.showMessageDialog(this, "Δεν βρέθηκε η κλήρωση στη βάση δεδομένων. ");                  //if not deleted show info to user
                        }
                    } catch (NullPointerException e) {                                                 //if draw doesn't exist we get exception and print warning message
                        JOptionPane.showMessageDialog(this, "Η συγκεκριμένη κλήρωση δεν υπάρχει.");    //we clear table 1 and table 2
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Παρακαλούμε εισάγετε αριθμό κληρωσης σε μορφή '3295' "); //if typed drawid number is bellow 0 print warning message
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Παρακαλούμε εισάγετε αριθμό κληρωσης σε μορφή '3295' ");     //if typed text isn't number print warning message
            }
        }
    }//GEN-LAST:event_jButtonDeleteStatisticsActionPerformed

    private void jButtonDeleteStatisticsRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteStatisticsRangeActionPerformed
            SimpleDateFormat test = new SimpleDateFormat("dd-MM-yyyy");                                         //date format to check user input

            try { 
            String startDateCheck = test.format(jDateChooserDrawStartRangeDel.getDate());                       //check start date
            String endDateCheck = test.format(jDateChooseDrawEndRangeDel.getDate());                            //check end date
            Date start = jDateChooserDrawStartRangeDel.getDate();                                               //get date from start field or daletion range
            Date end = jDateChooseDrawEndRangeDel.getDate();                                                    //get date from end field or daletion range

            int response = JOptionPane.showConfirmDialog(null, "Θέλετε να προχωρήσετε σε διαγραφή;", "Επιβεβαίωση", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); //ask confirmation for delete
            if (response == JOptionPane.YES_OPTION) {                                                           //if user answer yes proceed        
                int records = deleteDrawRange(start, end);                                                      //call deletedraw method and if draw is deleted 
                if (records > 0) {                                          
                    JOptionPane.showMessageDialog(this, "Διαγράφηκαν " + records + " κληρώσεις επιτυχώς.");     //print confirmation and how many draws deleted
                } else {
                    JOptionPane.showMessageDialog(this, "Δεν βρέθηκε καμία κλήρωση στη βάση δεδομένων. ");      //if none deleted show info to user
                }
            }
        } catch (NumberFormatException | NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Εισάγετε την ημερομηνία στη μορφή 15-01-2022 .");      //if there is error in parsing then display message
            System.out.println("error");
        }
        
        
        
        
        
    }//GEN-LAST:event_jButtonDeleteStatisticsRangeActionPerformed

    private void jButtonLoadStatisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadStatisticsActionPerformed
        dataStatus.setText("DATABASE");           //show where the source of data
        dataStatus.setForeground(Color.BLUE);     //change color of label
        showAvailableData();                      //show data of database
    }//GEN-LAST:event_jButtonLoadStatisticsActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {                                                                                   //catch exceptions
            System.out.println(ex);
        } catch (InstantiationException ex) {
            System.out.println(ex);
        } catch (IllegalAccessException ex) {
            System.out.println(ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            System.out.println(ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new R2screen().setVisible(true);

            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dataStatus;
    private javax.swing.JLabel dataStatus1;
    private javax.swing.JButton jButtonDeleteStatistics;
    private javax.swing.JButton jButtonDeleteStatisticsRange;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonLoadStatistics;
    private javax.swing.JButton jButtonSaveStatistics;
    private javax.swing.JButton jButtonViewStatistics;
    private javax.swing.JButton jButtonViewStatisticsRange;
    private com.toedter.calendar.JDateChooser jDateChooseDrawEndRangeDel;
    private com.toedter.calendar.JDateChooser jDateChooserDrawEndRange;
    private com.toedter.calendar.JDateChooser jDateChooserDrawStartRange;
    private com.toedter.calendar.JDateChooser jDateChooserDrawStartRangeDel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelInsertDraw;
    private javax.swing.JLabel jLabelInsertDrawForDel;
    private javax.swing.JLabel jLabelInsertDrawRange;
    private javax.swing.JLabel jLabelInsertDrawRange1;
    private javax.swing.JLabel jLabelInsertDrawRange3;
    private javax.swing.JLabel jLabelInsertDrawRangeDel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelR2;
    private javax.swing.JPanel jPanelR2Tables;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextFieldDrawForDel;
    private javax.swing.JTextField jTextFieldDrawSelection;
    private javax.swing.JLabel menuTitle;
    private javax.swing.JLabel menuTitle1;
    // End of variables declaration//GEN-END:variables
}
