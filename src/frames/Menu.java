/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;


import frames.R2screen;
import javax.swing.JOptionPane;
import pkg3hge.Controller;
import pkg3hge.Controller;
import pkg3hge.Draw;
import pkg3hge.Draw;
import pkg3hge.Prizecategory;



/**
 *
 * @author Hyperer
 */
public class Menu extends javax.swing.JFrame {

    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();
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
        jButtonManageData = new javax.swing.JButton();
        jLabelselect = new javax.swing.JLabel();
        jButtonExit = new javax.swing.JButton();
        jButtonViewData = new javax.swing.JButton();
        jButtonViewStatistics = new javax.swing.JButton();
        jComboBoxGame = new javax.swing.JComboBox<>();
        jLabelOpap = new javax.swing.JLabel();
        jLabelEap = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setName("frameMenu"); // NOI18N
        setUndecorated(true);
        setResizable(false);

        jPanelMenu.setBackground(new java.awt.Color(204, 204, 204));
        jPanelMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelMenu.setPreferredSize(new java.awt.Dimension(1200, 800));
        jPanelMenu.setRequestFocusEnabled(false);

        menuTitle.setBackground(new java.awt.Color(204, 204, 204));
        menuTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        menuTitle.setText("ΑΝΑΛΥΣΗ ΑΠΟΤΕΛΕΣΜΑΤΩΝ ΠΑΙΧΝΙΔΙΩΝ ΟΠΑΠ ");

        jButtonManageData.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButtonManageData.setText("Διαχείριση δεδομένων");
        jButtonManageData.setToolTipText("");
        jButtonManageData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonManageDataActionPerformed(evt);
            }
        });

        jLabelselect.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabelselect.setText("Επιλογή παιχνιδιού :");

        jButtonExit.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButtonExit.setText("Έξοδος");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        jButtonViewData.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButtonViewData.setText("Προβολή στατιστικών δεδομένων ΤΖΟΚΕΡ και εκτύπωση σε αρχείο pdf");

        jButtonViewStatistics.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButtonViewStatistics.setText("Προβολή δεδομένων ανά μήνα για συγκεκριμένο έτος");
        jButtonViewStatistics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewStatisticsActionPerformed(evt);
            }
        });

        jComboBoxGame.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jComboBoxGame.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TZOKER", "PRO-PO", "LOTTO", "PROTO", "SUPER 3", "EXTRA 5", "KINO", "PAME STOIXIMA", "POWERSPIN" }));
        jComboBoxGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGameActionPerformed(evt);
            }
        });

        jLabelOpap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/images/opap.logo_20.png"))); // NOI18N

        jLabelEap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/images/eap_featured.png"))); // NOI18N

        javax.swing.GroupLayout jPanelMenuLayout = new javax.swing.GroupLayout(jPanelMenu);
        jPanelMenu.setLayout(jPanelMenuLayout);
        jPanelMenuLayout.setHorizontalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMenuLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonExit)
                .addGap(45, 45, 45))
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabelOpap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelEap)
                .addGap(59, 59, 59))
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(394, 394, 394)
                .addComponent(jLabelselect)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxGame, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMenuLayout.createSequentialGroup()
                .addContainerGap(321, Short.MAX_VALUE)
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMenuLayout.createSequentialGroup()
                        .addComponent(menuTitle)
                        .addGap(269, 269, 269))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMenuLayout.createSequentialGroup()
                        .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonViewData, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonViewStatistics, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonManageData, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(252, 252, 252))))
        );
        jPanelMenuLayout.setVerticalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelOpap)
                    .addComponent(jLabelEap))
                .addGap(80, 80, 80)
                .addComponent(menuTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelselect)
                    .addComponent(jComboBoxGame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addComponent(jButtonManageData)
                .addGap(36, 36, 36)
                .addComponent(jButtonViewStatistics)
                .addGap(43, 43, 43)
                .addComponent(jButtonViewData)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 221, Short.MAX_VALUE)
                .addComponent(jButtonExit)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButtonExitActionPerformed

    private void jComboBoxGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGameActionPerformed
           
    }//GEN-LAST:event_jComboBoxGameActionPerformed

    private void jButtonManageDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonManageDataActionPerformed
                String gameSelected = jComboBoxGame.getSelectedItem().toString();
        if("TZOKER".equals(gameSelected)){
            new R2screen().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Το συγκεκριμένο παιχνίδι δεν υποστηρίζεται ακόμα, παρακαλώ επιλέξτε κάποιο άλλο. ");
        }
    }//GEN-LAST:event_jButtonManageDataActionPerformed

    private void jButtonViewStatisticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewStatisticsActionPerformed
                String gameSelected = jComboBoxGame.getSelectedItem().toString();
        if("TZOKER".equals(gameSelected)){
            new R3screen().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Το συγκεκριμένο παιχνίδι δεν υποστηρίζεται ακόμα, παρακαλώ επιλέξτε κάποιο άλλο. ");
        }
    }//GEN-LAST:event_jButtonViewStatisticsActionPerformed

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
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonManageData;
    private javax.swing.JButton jButtonViewData;
    private javax.swing.JButton jButtonViewStatistics;
    private javax.swing.JComboBox<String> jComboBoxGame;
    private javax.swing.JLabel jLabelEap;
    private javax.swing.JLabel jLabelOpap;
    private javax.swing.JLabel jLabelselect;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JLabel menuTitle;
    // End of variables declaration//GEN-END:variables
}
