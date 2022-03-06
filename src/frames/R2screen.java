/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

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
 * @author Hyperer
 */
public class R2screen extends javax.swing.JFrame {

    /**
     * Creates new form Menu
     */
    static EntityManagerFactory emf;
    static EntityManager em;
    Collection<Draw> drawsMain = new ArrayList<Draw>();
    private static boolean sortPrizeC = false;

    public R2screen() {
        initComponents();
    }
    
    private static void createEMFandEM() {
        emf = Persistence.createEntityManagerFactory("3hGEPU");
        em = emf.createEntityManager();
    }

    private void showDraw() {           
        Draw draw = Controller.callDrawId(jTextFieldDrawSelection.getText());
        drawsMain.add(draw);                            
        drawTable1();   
        showWinnings(draw.getDrawid());
    }

    private void showRange(String dateRange) {
        drawsMain = Controller.callDateRange(dateRange);
        drawTable1();
        String value = jTable1.getModel().getValueAt(0, 1).toString();
        showWinnings(Integer.valueOf(value));
    }

    private void showWinnings(int number) {
        String[] catTzoker = {"5+1", "5", "4+1", "4", "3+1", "3", "2+1", "1+1"};
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        int cat = 0;
        Object rowData[] = new Object[3];
        for (Draw draw : drawsMain) {
            if (draw.getDrawid() == number) {
                Collection<Prizecategory> prizeCategories = draw.getPrizecategoryCollection();
                for (Prizecategory pr : prizeCategories) {
                    System.out.println(pr.getIdcategory());
                    rowData[0] = catTzoker[cat];
                    rowData[1] = pr.getWinners();
                    if (pr.getDivident() == 0) {
                        rowData[2] = "ΤΖΑΚΠΟΤ";
                    } else {
                        rowData[2] = pr.getDivident();
                    }
                    cat += 1;
                    model.addRow(rowData);
                }
            }
        }sortPrizeC = false;
    }

    private static boolean findDraw(int drawID) {
        Query query = em.createNamedQuery("Draw.findAll", Draw.class);
        List<Draw> draws = query.getResultList();
        for (Draw draw : draws) {
            if (draw.getDrawid().equals(drawID)) {
                return true;
            }
        }
        return false;
    }

    private static int insertDraw(Draw draw) {
        int recordsWritted = 0;
        createEMFandEM();
        em.getTransaction().begin();
        if (!findDraw(draw.getDrawid())) {
            em.persist(draw);
            recordsWritted += 1;
        }
        em.getTransaction().commit();
        em.close();
        emf.close();
        return recordsWritted;
    }
    
    private void drawTable1() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        int no = 1;
        for (Draw draw : drawsMain) {
            Object rowData[] = new Object[9];
            rowData[0] = no;
            rowData[1] = draw.getDrawid();
            rowData[2] = draw.getFirstnumber();
            rowData[3] = draw.getSecondnumber();
            rowData[4] = draw.getThirdnumber();
            rowData[5] = draw.getFourthnumber();
            rowData[6] = draw.getFifthnumber();
            rowData[7] = draw.getJoker();
            rowData[8] = sdf.format(draw.getDrawidtime());
            model.addRow(rowData);
            no += 1;
        }
    }
    
    private void showAvailableData() {
        createEMFandEM();
        em.getTransaction().begin();
        //Query query = em.createNamedQuery("Draw.findAll",Draw.class);
        drawsMain.clear();
        Query query = em.createNativeQuery("SELECT DISTINCT * FROM DRAW INNER JOIN PRIZECATEGORY ON PRIZECATEGORY.DRAWID = DRAW.DRAWID ORDER BY PRIZECATEGORY.IDCATEGORY",Draw.class);
        drawsMain = query.getResultList();
   //     drawTable1();
        for (Draw draw : drawsMain) {
            Collection<Prizecategory> pz = draw.getPrizecategoryCollection();
            for (Prizecategory p : pz) {
                System.out.println(p.getIdcategory());
            }
        }
        em.close();
        emf.close();
    }

    private static int deleteDraw(int drawId) {
        int recordsDeleted = 0;
        createEMFandEM();
        em.getTransaction().begin();
        if (findDraw(drawId)) {
            Query query = em.createNamedQuery("Draw.findByDrawid", Draw.class);
            query.setParameter("drawid", drawId);
            Draw draw = (Draw) query.getSingleResult();
            em.remove(draw);
            recordsDeleted += 1;
        }
        em.getTransaction().commit();
        em.close();
        emf.close();
        return recordsDeleted;
    }

    private static int deleteDrawRange(Date start, Date end) {
        int recordsDeleted = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        Date drawDate = null;

        createEMFandEM();
        em.getTransaction().begin();
        Query query = em.createNamedQuery("Draw.findAll", Draw.class);
        List<Draw> draws = query.getResultList();

        for (Draw draw : draws) {
                drawDate = draw.getDrawidtime();
            if ((drawDate.before(end) && drawDate.after(start)) || drawDate.equals(start) || drawDate.equals(end)) {
                em.remove(draw);
                recordsDeleted += 1;
            }
        }
        em.getTransaction().commit();
        em.close();
        emf.close();
        return recordsDeleted;
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
        jButtonViewStatistics = new javax.swing.JButton();
        jLabelInsertDrawRange = new javax.swing.JLabel();
        jTextFieldDrawStartRange = new javax.swing.JTextField();
        jTextFieldDrawEndRange = new javax.swing.JTextField();
        jLabelInsertDrawRange1 = new javax.swing.JLabel();
        jButtonViewStatisticsRange = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabelInsertDrawForDel = new javax.swing.JLabel();
        jTextFieldDrawForDel = new javax.swing.JTextField();
        jButtonDeleteStatistics = new javax.swing.JButton();
        jTextFieldDrawStartRangeDel = new javax.swing.JTextField();
        jLabelInsertDrawRange3 = new javax.swing.JLabel();
        jTextFieldDrawEndRangeDel = new javax.swing.JTextField();
        jLabelInsertDrawRangeDel = new javax.swing.JLabel();
        jButtonDeleteStatisticsRange = new javax.swing.JButton();
        menuTitle1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

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
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 664, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
        );
        jPanelR2TablesLayout.setVerticalGroup(
            jPanelR2TablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelR2TablesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelR2TablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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

        jTextFieldDrawStartRange.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextFieldDrawStartRange.setText("15-01-2022");
        jTextFieldDrawStartRange.setMinimumSize(new java.awt.Dimension(12, 3));
        jTextFieldDrawStartRange.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDrawStartRangeMouseClicked(evt);
            }
        });

        jTextFieldDrawEndRange.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextFieldDrawEndRange.setText("30-01-2022");
        jTextFieldDrawEndRange.setMinimumSize(new java.awt.Dimension(12, 3));
        jTextFieldDrawEndRange.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDrawEndRangeMouseClicked(evt);
            }
        });

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

        jTextFieldDrawStartRangeDel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextFieldDrawStartRangeDel.setText("15-01-2022");
        jTextFieldDrawStartRangeDel.setMinimumSize(new java.awt.Dimension(12, 3));
        jTextFieldDrawStartRangeDel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDrawStartRangeDelMouseClicked(evt);
            }
        });

        jLabelInsertDrawRange3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabelInsertDrawRange3.setText("έως");

        jTextFieldDrawEndRangeDel.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextFieldDrawEndRangeDel.setText("30-01-2022");
        jTextFieldDrawEndRangeDel.setMinimumSize(new java.awt.Dimension(12, 3));
        jTextFieldDrawEndRangeDel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDrawEndRangeDelMouseClicked(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabelInsertDrawForDel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldDrawForDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(139, 139, 139)
                        .addComponent(jButtonDeleteStatistics)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonDeleteStatisticsRange)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelInsertDrawRangeDel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldDrawStartRangeDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelInsertDrawRange3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldDrawEndRangeDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(426, 426, 426)
                .addComponent(menuTitle1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelInsertDrawRangeDel)
                            .addComponent(jTextFieldDrawStartRangeDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldDrawEndRangeDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelInsertDrawRange3))
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDeleteStatisticsRange))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelInsertDrawForDel)
                            .addComponent(jTextFieldDrawForDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDeleteStatistics)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/images/tzoker-logo_1_30.png"))); // NOI18N

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
                        .addGroup(jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanelR2Layout.createSequentialGroup()
                                .addGap(100, 100, 100)
                                .addComponent(jLabelInsertDraw)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldDrawSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(57, 57, 57)
                                .addComponent(jLabelInsertDrawRange)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldDrawStartRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelInsertDrawRange1))
                            .addGroup(jPanelR2Layout.createSequentialGroup()
                                .addGap(217, 217, 217)
                                .addComponent(jButtonViewStatistics)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonViewStatisticsRange)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldDrawEndRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelR2Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanelR2Tables, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanelR2Layout.setVerticalGroup(
            jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelR2Layout.createSequentialGroup()
                .addGroup(jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelR2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(menuTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanelR2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelInsertDraw)
                    .addComponent(jTextFieldDrawSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelInsertDrawRange)
                    .addComponent(jTextFieldDrawStartRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDrawEndRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelInsertDrawRange1))
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
        this.dispose();
        new Menu().setVisible(true);
    }//GEN-LAST:event_jButtonExitActionPerformed

    private void jTextFieldDrawSelectionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDrawSelectionMouseClicked
        jTextFieldDrawSelection.setText("");
    }//GEN-LAST:event_jTextFieldDrawSelectionMouseClicked

    private void jButtonViewStatisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewStatisticsActionPerformed
        sortPrizeC = false;
        drawsMain.clear();
        try {
            int drawSelected = Integer.parseInt(jTextFieldDrawSelection.getText());
            if (drawSelected > 0) {
                try {
                    showDraw();
                } catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(this, "Η συγκεκριμένη κλήρωση δεν υπάρχει.");
                    DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();
                    model1.setRowCount(0);
                    DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();
                    model2.setRowCount(0);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Παρακαλούμε εισάγετε έγκυρo αριθμός κλήρωσης.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Παρακαλούμε εισάγετε έγκυρo αριθμός κλήρωσης.");
        }
    }//GEN-LAST:event_jButtonViewStatisticsActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        JTable source = (JTable) evt.getSource();
        int column = 1;
        int row = source.rowAtPoint(evt.getPoint());
        String value = jTable1.getModel().getValueAt(row, column).toString();
        showWinnings(Integer.valueOf(value));
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextFieldDrawStartRangeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDrawStartRangeMouseClicked
        jTextFieldDrawStartRange.setText("");
    }//GEN-LAST:event_jTextFieldDrawStartRangeMouseClicked

    private void jTextFieldDrawEndRangeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDrawEndRangeMouseClicked
        jTextFieldDrawEndRange.setText("");
    }//GEN-LAST:event_jTextFieldDrawEndRangeMouseClicked

    private void jButtonViewStatisticsRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewStatisticsRangeActionPerformed
        sortPrizeC = false;
        drawsMain.clear();
        String startDate = jTextFieldDrawStartRange.getText();
        String endDate = jTextFieldDrawEndRange.getText();
        Date start = new Date();
        Date end = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat opap = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            start = sdf.parse(startDate);
            end = sdf.parse(endDate);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Εισάγετε την ημερομηνία στη μορφή 15-01-2022 .");
        }
        try {
            String startToSting = opap.format(start);
            String endToString = opap.format(end);
            showRange("draw-date/" + startToSting + "/" + endToString);
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Δεν υπάρχουν κληρώσεις ή το εύρος ξεπερνάει το τρίμηνο");
            DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();
            model1.setRowCount(0);
            DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();
            model2.setRowCount(0);
        }
    }//GEN-LAST:event_jButtonViewStatisticsRangeActionPerformed

    private void jButtonSaveStatisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveStatisticsActionPerformed
        int records = 0;
        for (Draw draw : drawsMain) {
            records += insertDraw(draw);
        }
        if (records > 0) {
            JOptionPane.showMessageDialog(this, "Αποθηκεύτηκαν " + records + " κληρώσεις. ");
        } else {
            JOptionPane.showMessageDialog(this, "Δεν αποθηκεύτηκε καμία κλήρωση διότι υπήρχαν ήδη στη βάση δεδομένων.");
        }
    }//GEN-LAST:event_jButtonSaveStatisticsActionPerformed

    private void jTextFieldDrawForDelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDrawForDelMouseClicked
        jTextFieldDrawForDel.setText("");
    }//GEN-LAST:event_jTextFieldDrawForDelMouseClicked

    private void jButtonDeleteStatisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteStatisticsActionPerformed
        int response = JOptionPane.showConfirmDialog(null, "Θέλετε να προχωρήσετε σε διαγραφή;", "Επιβεβαίωση", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {

            int drawid = 0;
            try {
                drawid = Integer.parseInt(jTextFieldDrawForDel.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Παρακαλούμε εισάγετε αριθμό κληρωσης σε μορφή '3295' ");
            }
            int records = deleteDraw(drawid);
            if (records > 0) {
                JOptionPane.showMessageDialog(this, "Η κλήρωση " + drawid + " διαγράφηκε επιτυχώς.");
            } else {
                JOptionPane.showMessageDialog(this, "Δεν βρέθηκε η κλήρωση στη βάση δεδομένων. ");
            }
        }
    }//GEN-LAST:event_jButtonDeleteStatisticsActionPerformed

    private void jTextFieldDrawStartRangeDelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDrawStartRangeDelMouseClicked
        jTextFieldDrawStartRangeDel.setText("");
        jTextFieldDrawEndRangeDel.setText("");
    }//GEN-LAST:event_jTextFieldDrawStartRangeDelMouseClicked

    private void jTextFieldDrawEndRangeDelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDrawEndRangeDelMouseClicked
        jTextFieldDrawStartRangeDel.setText("");
        jTextFieldDrawEndRangeDel.setText("");
    }//GEN-LAST:event_jTextFieldDrawEndRangeDelMouseClicked

    private void jButtonDeleteStatisticsRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteStatisticsRangeActionPerformed
        String startDate = jTextFieldDrawStartRangeDel.getText();
        String endDate = jTextFieldDrawEndRangeDel.getText();
        Date start;
        Date end;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        try {
            start = sdf.parse(startDate);
            end = sdf.parse(endDate);
            int response = JOptionPane.showConfirmDialog(null, "Θέλετε να προχωρήσετε σε διαγραφή;", "Επιβεβαίωση", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {

                int records = deleteDrawRange(start, end);
                if (records > 0) {
                    JOptionPane.showMessageDialog(this, "Διαγράφηκαν " + records + " κληρώσεις επιτυχώς.");
                } else {
                    JOptionPane.showMessageDialog(this, "Δεν βρέθηκε καμία κλήρωση στη βάση δεδομένων. ");
                }
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Εισάγετε την ημερομηνία στη μορφή 15/01/2022 .");
        }
    }//GEN-LAST:event_jButtonDeleteStatisticsRangeActionPerformed

    private void jButtonLoadStatisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadStatisticsActionPerformed
        sortPrizeC = true;
        showAvailableData();
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(R2screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(R2screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(R2screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(R2screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JButton jButtonDeleteStatistics;
    private javax.swing.JButton jButtonDeleteStatisticsRange;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonLoadStatistics;
    private javax.swing.JButton jButtonSaveStatistics;
    private javax.swing.JButton jButtonViewStatistics;
    private javax.swing.JButton jButtonViewStatisticsRange;
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
    private javax.swing.JTextField jTextFieldDrawEndRange;
    private javax.swing.JTextField jTextFieldDrawEndRangeDel;
    private javax.swing.JTextField jTextFieldDrawForDel;
    private javax.swing.JTextField jTextFieldDrawSelection;
    private javax.swing.JTextField jTextFieldDrawStartRange;
    private javax.swing.JTextField jTextFieldDrawStartRangeDel;
    private javax.swing.JLabel menuTitle;
    private javax.swing.JLabel menuTitle1;
    // End of variables declaration//GEN-END:variables
}
