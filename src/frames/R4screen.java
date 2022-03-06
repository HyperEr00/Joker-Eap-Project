/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pkg3hge.Draw;
import pkg3hge.NumberJoker;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.security.LtvTimestamp;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hyperer
 */
public class R4screen extends javax.swing.JFrame {

    static EntityManagerFactory emf;
    static EntityManager em;

    /**
     * Creates new form Menu
     */
    public R4screen() {
        initComponents();

    }

    private static void createEMFandEM() {
        emf = Persistence.createEntityManagerFactory("3hGEPU");
        em = emf.createEntityManager();
    }

    private void showStatistics(List<Draw> draws) {
        int numberOccurences;
        int jokerOccurences;
        int numberDelays;
        int jokerDelays;
        ArrayList<NumberJoker> numbers = new ArrayList<>();
        ArrayList<NumberJoker> jokers = new ArrayList<>();

        for (int i = 1; i <= 45; i++) {
            numberDelays = 0;
            numberOccurences = 0;
            jokerDelays = 0;
            jokerOccurences = 0;

            for (Draw draw : draws) {
                if ((i == draw.getFirstnumber()) || (i == draw.getSecondnumber()) || (i == draw.getThirdnumber()) || (i == draw.getFourthnumber()) || (i == draw.getFifthnumber())) {
                    numberOccurences += 1;
                    numberDelays = 0;
                } else {
                    numberDelays += 1;
                }
                if (i <= 20) {
                    if (i == draw.getJoker()) {
                        jokerOccurences += 1;
                        jokerDelays = 0;
                    } else {
                        jokerDelays += 1;
                    }
                }
            }
            NumberJoker joker = new NumberJoker(i, jokerOccurences, jokerDelays);
            NumberJoker number = new NumberJoker(i, numberOccurences, numberDelays);
            numbers.add(number);
            jokers.add(joker);
        }
        drawTable1(numbers);
        drawTable2(jokers);
    }

    private void getAllStats() {
        createEMFandEM();
        em.getTransaction().begin();
        Query query = em.createNamedQuery("Draw.findAll", Draw.class);
        List<Draw> draws = query.getResultList();
        em.close();
        emf.close();
        showStatistics(draws);
    }

    private void getRangeStats(Date start, Date end) {
        List<Draw> selectedDraws = new ArrayList<>();
        System.out.println(start);
        System.out.println(end);

        createEMFandEM();
        em.getTransaction().begin();
        Query query = em.createNamedQuery("Draw.findAll", Draw.class);
        List<Draw> draws = query.getResultList();
        em.close();
        emf.close();
        for (Draw draw : draws) {
            if ((draw.getDrawidtime().before(end) && draw.getDrawidtime().after(start)) || draw.getDrawidtime().equals(start) || draw.getDrawidtime().equals(end)) {
                selectedDraws.add(draw);
                System.out.println(draw.getDrawid());
            }
        }
        if (selectedDraws.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Δεν υπάρχουν δεδομένα για στατιστικά σ' αυτό το εύρος ημερομηνιών");
        }

        showStatistics(selectedDraws);
    }

    private void drawTable1(ArrayList<NumberJoker> numbers) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        for (NumberJoker number : numbers) {
            Object rowData[] = new Object[3];
            rowData[0] = number.getNumber();
            rowData[1] = number.getOccurrence();
            rowData[2] = number.getDelays();
            model.addRow(rowData);
        }
    }

    private void drawTable2(ArrayList<NumberJoker> joker) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        for (NumberJoker number : joker) {
            if (number.getNumber() <= 20) {
                Object rowData[] = new Object[3];
                rowData[0] = number.getNumber();
                rowData[1] = number.getOccurrence();
                rowData[2] = number.getDelays();
                model.addRow(rowData);
            }
        }
    }

    private void printPdf() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("JokerStats-"+System.currentTimeMillis()+".pdf"));
            document.open();
            PdfPTable pdftable = new PdfPTable(jTable1.getColumnCount());
            Paragraph header1 = new Paragraph("JOKER STATISTICS");
            header1.setAlignment(Element.ALIGN_CENTER);
            document.add(header1);
            document.add(new Paragraph("Number Statistics:"));
            document.add(Chunk.NEWLINE);
            pdftable.addCell("Numbers");
            pdftable.addCell("Occurrences");
            pdftable.addCell("Delays");
            for (int rows = 0; rows < jTable1.getRowCount(); rows++) {
                for (int cols = 0; cols < jTable1.getColumnCount(); cols++) {
                    pdftable.addCell(jTable1.getModel().getValueAt(rows, cols).toString());
                }
            }
            document.add(pdftable);
            document.add(Chunk.NEXTPAGE);
            PdfPTable pdftable2 = new PdfPTable(jTable2.getColumnCount());
            document.add(new Paragraph("Joker Numbers Statistics:"));
            document.add(Chunk.NEWLINE);
            pdftable2.addCell("Numbers");
            pdftable2.addCell("Occurrences");
            pdftable2.addCell("Delays");
            for (int rows = 0; rows < jTable2.getRowCount(); rows++) {
                for (int cols = 0; cols < jTable2.getColumnCount(); cols++) {
                    pdftable2.addCell(jTable2.getModel().getValueAt(rows, cols).toString());
                }
            }
            document.add(pdftable2);
            document.close();
        } catch (DocumentException ex) {
            Logger.getLogger(R4screen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(R4screen.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        jButtonExit = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabelInsertDraw = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabelInsertDraw1 = new javax.swing.JLabel();
        jLabelInsertDrawRange = new javax.swing.JLabel();
        jTextFieldDrawStartRange = new javax.swing.JTextField();
        jLabelInsertDrawRange1 = new javax.swing.JLabel();
        jTextFieldDrawEndRange = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
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
        menuTitle.setText("Προβολή στατιστικών δεδομένων  ΤΖΟΚΕΡ ");

        jButtonExit.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButtonExit.setText("Κεντρικό μενού");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelInsertDraw.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabelInsertDraw.setText("Στατιστικά Αριθμών");

        jTable1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Αριθμός", "Εμφανίσεις", "Καθυστερήσεις"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setToolTipText("");
        jTable1.setRowHeight(26);
        jTable1.setSelectionBackground(new java.awt.Color(51, 153, 255));
        jScrollPane2.setViewportView(jTable1);

        jTable2.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Αριθμός", "Εμφανίσεις", "Καθυστερήσεις"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setToolTipText("");
        jTable2.setRowHeight(26);
        jTable2.setSelectionBackground(new java.awt.Color(51, 153, 255));
        jScrollPane3.setViewportView(jTable2);

        jLabelInsertDraw1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabelInsertDraw1.setText("Στατιστικά Joker");

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

        jLabelInsertDrawRange1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabelInsertDrawRange1.setText("έως");

        jTextFieldDrawEndRange.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTextFieldDrawEndRange.setText("30-01-2022");
        jTextFieldDrawEndRange.setMinimumSize(new java.awt.Dimension(12, 3));
        jTextFieldDrawEndRange.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldDrawEndRangeMouseClicked(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton1.setText("Συνολικά Στατιστικά");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton2.setText("Στατιστικά Εύρους");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton3.setText("Εκτύπωση pdf");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabelInsertDraw)
                                .addGap(75, 75, 75)
                                .addComponent(jLabelInsertDraw1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(280, 280, 280)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextFieldDrawStartRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelInsertDrawRange1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldDrawEndRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabelInsertDrawRange))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jButton1)
                        .addGap(67, 67, 67)
                        .addComponent(jButton2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addComponent(jButton3)))
                .addContainerGap(513, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelInsertDraw)
                    .addComponent(jLabelInsertDraw1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelInsertDrawRange)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDrawStartRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDrawEndRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelInsertDrawRange1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3))
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/images/tzoker-logo_1_30.png"))); // NOI18N

        javax.swing.GroupLayout jPanelMenuLayout = new javax.swing.GroupLayout(jPanelMenu);
        jPanelMenu.setLayout(jPanelMenuLayout);
        jPanelMenuLayout.setHorizontalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMenuLayout.createSequentialGroup()
                .addContainerGap(996, Short.MAX_VALUE)
                .addComponent(jButtonExit)
                .addGap(40, 40, 40))
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMenuLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(155, 155, 155)
                .addComponent(menuTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelMenuLayout.setVerticalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelMenuLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(menuTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonExit)
                .addGap(20, 20, 20))
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
        this.dispose();
        new Menu().setVisible(true);
    }//GEN-LAST:event_jButtonExitActionPerformed

    private void jTextFieldDrawStartRangeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDrawStartRangeMouseClicked
        jTextFieldDrawStartRange.setText("");
    }//GEN-LAST:event_jTextFieldDrawStartRangeMouseClicked

    private void jTextFieldDrawEndRangeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldDrawEndRangeMouseClicked
        jTextFieldDrawEndRange.setText("");
    }//GEN-LAST:event_jTextFieldDrawEndRangeMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        getAllStats();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();
        model1.setRowCount(0);
        DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();
        model2.setRowCount(0);
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
        getRangeStats(start, end);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        printPdf();
    }//GEN-LAST:event_jButton3ActionPerformed

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
                new R4screen().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelInsertDraw;
    private javax.swing.JLabel jLabelInsertDraw1;
    private javax.swing.JLabel jLabelInsertDrawRange;
    private javax.swing.JLabel jLabelInsertDrawRange1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextFieldDrawEndRange;
    private javax.swing.JTextField jTextFieldDrawStartRange;
    private javax.swing.JLabel menuTitle;
    // End of variables declaration//GEN-END:variables

}
