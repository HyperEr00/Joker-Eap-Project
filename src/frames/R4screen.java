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
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
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
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;
import pkg3hge.Prizecategory;

/**
 *
 * @author Konstantinos Meliras, Konstantinos Kontovas, Stamatis Asterios
 */
public class R4screen extends javax.swing.JFrame {

    static EntityManagerFactory emf;
    static EntityManager em;
    static ArrayList<NumberJoker> numbers = new ArrayList<>();          //list to use for statistics for the numbers
    static ArrayList<NumberJoker> jokers = new ArrayList<>();           //list to use for statistics for the jokers
    static List<Draw> selectedDraws = new ArrayList<>();                //list to save the selected draws
        
    /**
     * Creates new form Menu
     */
    public R4screen() {
        initComponents();
        try {
            jDateChooserDrawStartRange.setDate(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2022"));   //set a default date to the start range date field
            jDateChooserDrawEndRange.setDate(new SimpleDateFormat("dd-MM-yyyy").parse("15-01-2022"));     //set a default date to the end range  date field
        } catch (ParseException ex) {
              System.out.println(ex);
        }
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
        selectedDraws = query.getResultList();                    //save draw results to draw list
        em.close();                                                         //close connections from database
        emf.close();                                                                 
     }
    
    //method to calculate statistics for each number 
    private void showStatistics(List<Draw> draws) {          //get draw list to check
        int numberOccurrences;                               //variable for how many times was drawn a number 
        int jokerOccurrences;                                //variable for how many times was drawn a number for joker
        int numberDelays;                                    //variable for how many draws a number wasn't drawn
        int jokerDelays;                                     //variable for how many draws a number wasn't drawn for joker
        int jokerLimit = 0;
        numbers.clear();                                     //clear number list
        jokers.clear();                                      //clear joker list

        for (int i = 1; i <= 45; i++) {                      //loop for the numbers and jokers
            numberDelays = 0;
            numberOccurrences = 0;
            jokerDelays = 0;
            jokerOccurrences = 0;

            for (Draw draw : draws) {                        //check if number was draws 1st,2nd,3r,4th or 5th in each draw
                if ((i == draw.getFirstnumber()) || (i == draw.getSecondnumber()) || (i == draw.getThirdnumber()) || (i == draw.getFourthnumber()) || (i == draw.getFifthnumber())) {
                    numberOccurrences += 1;                  //if number was drawn increase the occurences
                    numberDelays = 0;                        //and reset the delays
                } else {
                    numberDelays += 1;                       //if number wasn't drawn increase the delays   
                } //the we check in same loop for joker number statistics
                if (i <= 20) {                               //if number is in valid range for joker 1-20 check 
                    if (i == draw.getJoker()) {              //if number is draws as a joker number
                        jokerOccurrences += 1;               //if number was drawn increase the occurences of joker number  
                        jokerDelays = 0;                     //and reset the delays of joker number
                    } else {
                        jokerDelays += 1;                    //if number wasn't drawn increase the delays of joker number
                    }
                }
            }
            NumberJoker number = new NumberJoker(i, numberOccurrences, numberDelays);        //create object numberjoker for the lucky number
            numbers.add(number);                                                             //add object number to numbers list
            if (jokerLimit < 20) {                                                           //add only the first 20 numbers 
                NumberJoker joker = new NumberJoker(i, jokerOccurrences, jokerDelays);       //create object numberjoker for the joker number
                jokers.add(joker);                                                           //add object joker to jokers list
                jokerLimit += 1;
            }
        }
        drawTable(numbers,jTable1);                                 //call method drawTable1 to display statistics for lucky numbers to table 1
        drawTable(jokers,jTable2);                                  //call method drawTable2 to display statistics for joker numbers to table 2
    }
    
    //method to get statistics for selected range
    private void getRangeStats(Date start, Date end) {        //get input the starting date and ending date
        selectedDraws.clear();                                //clear selectedDraws list
        createEMFandEM();                                     //call method to create manager and factory
        em.getTransaction().begin();                          //database connection
        Query query = em.createNamedQuery("Draw.findAll", Draw.class);  //get all records from database
        List<Draw> draws = query.getResultList();             //save draw results to temporary list
        em.close();
        emf.close();                                          //close connections from database
        for (Draw draw : draws) {                             //itarate over draw list to check if draw date is between start and end date or equal
            if ((draw.getDrawidtime().before(end) && draw.getDrawidtime().after(start)) || draw.getDrawidtime().equals(start) || draw.getDrawidtime().equals(end)) {
                selectedDraws.add(draw);                      //if it si then added to the selectedDraws list
            }
        }
        if (selectedDraws.isEmpty()) {                        //if selectedDraw list is empty this mean no draw was selected and display message
            JOptionPane.showMessageDialog(this, "Δεν υπάρχουν δεδομένα για στατιστικά σ' αυτό το εύρος ημερομηνιών");
            DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();            //clear table 1
            model1.setRowCount(0);
            DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();            //clear table 2
            model2.setRowCount(0);
        }else {
          showStatistics(selectedDraws);                        //call method to calculate and show the statistics for the selected draws
        }
    }
    
    //method to display the number statistics to the tables
    private void drawTable(ArrayList<NumberJoker> numbers, JTable ajtable) {  //get source of arraylist and destination table
        DefaultTableModel model = (DefaultTableModel) ajtable.getModel();
        model.setRowCount(0);
        for (NumberJoker number : numbers) {
            Object rowData[] = new Object[3];                                 //create 3 column table
            rowData[0] = number.getNumber();                                  //first column add number value
            rowData[1] = number.getOccurrence();                              //second column add cocurrence value
            rowData[2] = number.getDelays();                                  //third column add delay value
            model.addRow(rowData);                                            //add row to the table
        }
    }
  
    //method to print the statistics to pdf file
    private void printPdf() {
        try {
            BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);   //use certain font that can display right the greek letters
            baseFont.setSubset(true); Font font = new Font(baseFont, 12, Font.BOLD);                                    //set the size and type of font
            Document document = new Document();                                                                         //create the document
            PdfWriter.getInstance(document, new FileOutputStream("JokerStats-"+System.currentTimeMillis()+".pdf"));     //with certain filename that contain the date signature   
            document.open();                                                                                            //open document
            PdfPTable pdftable = new PdfPTable(jTable1.getColumnCount());                                               //create pdf table that will be writted to the document
            Paragraph header1 = new Paragraph("ΣΤΑΤΙΣΤΙΚΑ JOKER",font);                                                 //create header
            header1.setAlignment(Element.ALIGN_CENTER);                                                                 //center the header
            document.add(header1);                                                                                      //add header to document
            document.add(new Paragraph("Στατιστικά αριθμών:",font));                                                    //add paragraph
            document.add(Chunk.NEWLINE);                                                                                //add space line 
            for (int i = 0; i < jTable1.getColumnCount(); i++) {                                                        //loop the first row of jtable to get the titles of the cells
                pdftable.addCell(new Phrase(jTable1.getColumnName(i),font));                                            //add them to the pdf table
            }
            for (int rows = 0; rows < jTable1.getRowCount(); rows++) {                                                  //double loop the rest rows of jtable to get the data of all cells
                for (int cols = 0; cols < jTable1.getColumnCount(); cols++) {
                    pdftable.addCell(jTable1.getModel().getValueAt(rows, cols).toString());                             //add them to the pdf table
                }
            }
            document.add(pdftable);                                                                                     //add the pdftable to the document 
            document.add(Chunk.NEWLINE);                                                                                //do the same for the second jtable
            PdfPTable pdftable2 = new PdfPTable(jTable2.getColumnCount());
            document.add(new Paragraph("Στατιστικά αριθμών Joker:",font));
            document.add(Chunk.NEWLINE);
            for (int i = 0; i < jTable2.getColumnCount(); i++) {
                pdftable.addCell(new Phrase(jTable2.getColumnName(i),font));
            }
            for (int rows = 0; rows < jTable2.getRowCount(); rows++) {
                for (int cols = 0; cols < jTable2.getColumnCount(); cols++) {
                    pdftable2.addCell(jTable2.getModel().getValueAt(rows, cols).toString());
                }
            }
            document.add(pdftable2);                                            
            document.close();                                                                                            //write the data and close the document
        } catch (DocumentException | IOException ex) {                                                                                 //catch possible exceptions
            System.out.println(ex);
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
        jLabelInsertDrawRange1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jDateChooserDrawEndRange = new com.toedter.calendar.JDateChooser();
        jDateChooserDrawStartRange = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButtonNumbersFreq = new javax.swing.JButton();
        jButtonJokerFreq = new javax.swing.JButton();
        jButtonPrizesAverage = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setName("frameMenu"); // NOI18N
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1200, 800));
        setResizable(false);

        jPanelMenu.setBackground(new java.awt.Color(204, 204, 204));
        jPanelMenu.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanelMenu.setAutoscrolls(true);
        jPanelMenu.setPreferredSize(new java.awt.Dimension(1200, 800));

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

        jLabelInsertDrawRange1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabelInsertDrawRange1.setText("έως");

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

        jDateChooserDrawEndRange.setDateFormatString("dd-MM-yyyy");
        jDateChooserDrawEndRange.setFont(new java.awt.Font("Arial", 0, 22)); // NOI18N

        jDateChooserDrawStartRange.setDateFormatString("dd-MM-yyyy");
        jDateChooserDrawStartRange.setFont(new java.awt.Font("Arial", 0, 22)); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setText("Εμφάνιση σε Γράφημα");

        jButtonNumbersFreq.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButtonNumbersFreq.setText("Συχότητα εμφάνισης αριθμών");
        jButtonNumbersFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNumbersFreqActionPerformed(evt);
            }
        });

        jButtonJokerFreq.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButtonJokerFreq.setText("Συχότητα εμφάνισης αριθμών Joker");
        jButtonJokerFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJokerFreqActionPerformed(evt);
            }
        });

        jButtonPrizesAverage.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButtonPrizesAverage.setText("Μέσος όρος κερδών ανά κατηγορία");
        jButtonPrizesAverage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrizesAverageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonJokerFreq, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonNumbersFreq, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonPrizesAverage, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(36, 36, 36)
                .addComponent(jButtonNumbersFreq, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jButtonJokerFreq, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jButtonPrizesAverage, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(284, 284, 284)
                        .addComponent(jLabelInsertDrawRange))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(jButton1)
                                .addGap(67, 67, 67)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jDateChooserDrawStartRange, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelInsertDrawRange1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jDateChooserDrawEndRange, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(94, 94, 94)))
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73)))
                .addGap(108, 108, 108))
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
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(40, 40, 40))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelInsertDraw)
                    .addComponent(jLabelInsertDraw1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelInsertDrawRange)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooserDrawStartRange, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelInsertDrawRange1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooserDrawEndRange, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(40, 40, 40))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
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
            .addComponent(jPanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
        this.dispose();                                       //close this screen and return to main menu
        new Menu().setVisible(true);
    }//GEN-LAST:event_jButtonExitActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        selectedDraws.clear();                                //clear selectedDraws list
        getDraws();                                           //call method to get all the draws from database
        showStatistics(selectedDraws);                        //call method to calculate and show the statistics
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DefaultTableModel model1 = (DefaultTableModel) jTable1.getModel();          //clear table 1
        model1.setRowCount(0);
        DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();          //clear table 2
        model2.setRowCount(0);
        try{
        if((jDateChooserDrawStartRange.getDate() != null) && (jDateChooserDrawEndRange.getDate() != null)) {
            getRangeStats(jDateChooserDrawStartRange.getDate(), jDateChooserDrawEndRange.getDate());    //call method getRangeStats to display the statistics
        }else{
            JOptionPane.showMessageDialog(this, "Εισάγετε την ημερομηνία στη μορφή 15-01-2022 .");      //if there is error in parsing then display message
        }}catch(NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Εισάγετε την ημερομηνία στη μορφή 15-01-2022 .");      //if there is error in parsing then display message
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        printPdf();                                                                 //call method printPd to save statistics to Pdf file
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButtonJokerFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJokerFreqActionPerformed
        if (!jokers.isEmpty()) {                                                    //if the jokers list is not empty
            Collections.sort(jokers);                                               //sort the list by the joker number occurrence 
            DefaultCategoryDataset datasetJokers = new DefaultCategoryDataset();    //create dataset for the barchart
            for (int i = 0; i < 5; i++) {
                datasetJokers.addValue(jokers.get(i).getOccurrence(),               //add value of occurence to the dataset of chart
                        "",
                        String.valueOf(jokers.get(i).getNumber()));                 //add value of number to the dataset of chart
            }
            BarChart barChart = new BarChart("Οι 5 συχνότεροι αριθμοί Joker", "Αριθμοί", "Εμφανίσεις", datasetJokers);    //create a new barchart to display the graph of the top 5 occurrency joker numbers
        }
    }//GEN-LAST:event_jButtonJokerFreqActionPerformed

    private void jButtonNumbersFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNumbersFreqActionPerformed
        if (!numbers.isEmpty()) {                                                    //if the numbars list is not empty
            Collections.sort(numbers);                                               //sort the list by the number occurrence
            DefaultCategoryDataset datasetNumbers = new DefaultCategoryDataset();    //create dataset for the barchart
            for (int i = 0; i < 5; i++) {
                datasetNumbers.addValue(numbers.get(i).getOccurrence(),              //add value of occurence to the dataset of chart
                        "",
                        String.valueOf(numbers.get(i).getNumber()));                 //add value of number to the dataset of chart
            }
            BarChart barChart = new BarChart("Οι 5 συχνότεροι αριθμοί", "Αριθμοί", "Εμφανίσεις", datasetNumbers);     //create a new barchart to display the graph of the top 5 occurrency numbers
        }
    }//GEN-LAST:event_jButtonNumbersFreqActionPerformed

    private void jButtonPrizesAverageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrizesAverageActionPerformed
        String[] catTzoker = {"5+1", "5", "4+1", "4", "3+1", "3", "2+1", "1+1"};        //create array for each prize category name 
        Map<Integer, Double> pz = new HashMap<>();                                      //map each category with total prizes
        double totalMoney;                                                              //variable to sum the prizes of each category
        double totalDraws;                                                              //variable to count the draws 

        for (int i = 1; i <= 8; i++) {                                                  //8 loops, one for each category
            totalMoney = 0;                                                             //reset after loop the variables
            totalDraws = 0;
            for (Draw d : selectedDraws) {                                              //iterate over the selected draws
                for (Prizecategory p : d.getPrizecategoryCollection()) {                //get the prizeCategory list from the draw and iterate over it
                    if (p.getIdcategory() == i) {                                       //if category number match with i do (category group for all draws)
                        if (i == 1 || i == 2) {                                         //for category 1 and 2 sum the total money with distributed and the price of jackpot
                            totalMoney = totalMoney + p.getDistributed() + p.getJackpot();
                        } else {
                            totalMoney = totalMoney + p.getDistributed();               //for rest category just add the distributed
                        }
                    }
                }
                totalDraws += 1;                                                        //increase draw counter
            }
            pz.put(i, (totalMoney / totalDraws));                                       //map the values of i = category and average prizes for the category in all selected draws
        }
        DefaultCategoryDataset datasetPrizes = new DefaultCategoryDataset();            //create dataset for the barchart
        for (Map.Entry<Integer, Double> me                                              //iterate over map 
                : pz.entrySet()) {
            datasetPrizes.addValue(Math.round(((me.getValue()) * 100.0) / 100.0),       //add value of average formated to 2 decimal to the dataset of chart
                    "",
                    catTzoker[(me.getKey())-1]);                                        //add category name base to catTzoker array to the dataset of chart
        }
        BarChart barChart = new BarChart("Μέσος όρος κερδών ανα κατηγορία", "Κατηγορίες", "Κέρδη", datasetPrizes);  //create a new barchart to display the graph of the prize category average
    }//GEN-LAST:event_jButtonPrizesAverageActionPerformed

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
    private javax.swing.JButton jButtonJokerFreq;
    private javax.swing.JButton jButtonNumbersFreq;
    private javax.swing.JButton jButtonPrizesAverage;
    private com.toedter.calendar.JDateChooser jDateChooserDrawEndRange;
    private com.toedter.calendar.JDateChooser jDateChooserDrawStartRange;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelInsertDraw;
    private javax.swing.JLabel jLabelInsertDraw1;
    private javax.swing.JLabel jLabelInsertDrawRange;
    private javax.swing.JLabel jLabelInsertDrawRange1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel menuTitle;
    // End of variables declaration//GEN-END:variables
    
    //class of barchart object
    public class BarChart extends JFrame{
        public BarChart(String title, String xString, String yString, CategoryDataset adataset) {       //constructor get title, title for x axis
            CategoryDataset dataset = adataset;                                                         //title for y axis and the dataset that will be used
            JFreeChart chart = ChartFactory.createBarChart(                     
                    title,
                    xString,
                    yString,
                    dataset,
                    PlotOrientation.VERTICAL,                                                           //se orientation of chart to vertical
                    true, true, false);
            
            ChartPanel chartPanel = new ChartPanel(chart);                                      //create chartpanel 
            setContentPane(chartPanel);                                                         
            CategoryItemRenderer renderer = ((CategoryPlot) chart.getPlot()).getRenderer();     //create renderer to adjust the chartpanel settings
            renderer.setSeriesPaint(0, Color.blue);                                             //change column color to blue
            renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());       //enable values show
            renderer.setBaseItemLabelsVisible(true);                                                  
            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,       //position of values
                    TextAnchor.BOTTOM_CENTER);
            renderer.setBasePositiveItemLabelPosition(position);

            JFrame frameBarchart = new JFrame("Γράφημα");                                              //create new Jfame
            frameBarchart.setDefaultCloseOperation(DISPOSE_ON_CLOSE);                                  //set the default close behaviour
            frameBarchart.add(new ChartPanel(chart));                                                  //add chartpanel to the frame
            frameBarchart.setLocationByPlatform(true);                                                 //set position for the frame
            frameBarchart.pack();
            frameBarchart.setVisible(true);                                                            //show the new frame
            RefineryUtilities.centerFrameOnScreen(frameBarchart);                                      //center the frame
        }
    }
}
