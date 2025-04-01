
/**
* 
* 
* This file contains the class of StockReport which have stock details with 
* there operations like search,remove,print,etc.
* 
* 
*/

package components.report;

import javax.swing.*;
import javax.swing.table.*;
import com.toedter.calendar.*;
import components.report.generate.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import config.*;
import partial.*;
import partial.interfaces.*;

/**
 * This class contains the details of stock with there operations like
 * search,remove,print,etc.
 */
public class StockReport extends JPanel implements FontInterface, ActionListener {

    JButton search, remove, print, cancel, exit;
    JLabel heading, fromDate, toDate, totalWeightLabel, totalBagsLabel, totalAmountLabel, totalRowsLabel;
    JPanel topPanel, basePanel, buttonPanel, tablePanel, mainPanel;
    JTextField totalRowsField, totalWeightField, totalBagsField, totalAmountField;
    JDateChooser fromDateChooser, toDateChooser;
    JTable viewTable;
    DefaultTableModel tableModel;
    JScrollPane scrollPane;
    TableRowSorter<TableModel> sorter;
    JMenuItem homeMenuItem;
    Integer sr_no = 0;
    Double total = 0d;
    String fromDateForReportPrint = null, toDateForReportPrint = null;

    /**
     * Constructor that creates the report layout with data.
     * 
     * @param homeMenu a reference of home menu item.
     */
    public StockReport(JMenuItem homeMenu) {
        homeMenuItem = homeMenu;
        // set grid bag layout to the panel.
        setLayout(new FlowLayout(FlowLayout.CENTER));

        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        mainPanel.setPreferredSize(new Dimension(1258, 600));
        mainPanel.setBorder(border);
        mainPanel.setBackground(Color.white);

        // create an top panel with heading and from-to dates.
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        topPanel.setPreferredSize(new Dimension(1120, 45));
        topPanel.setBorder(border);
        topPanel.setAlignmentY(CENTER_ALIGNMENT);

        // heading of report
        heading = new JLabel("Inventory Report");
        heading.setBorder(emptyBorderLeft);
        heading.setFont(headingFont);

        // date selectors
        fromDateChooser = new JDateChooser();
        // set max selected date to current date.
        fromDateChooser.setMaxSelectableDate(calendar.getTime());
        fromDateChooser.setDate(calendar.getTime());
        fromDateChooser.setFont(labelFont);
        fromDateChooser.setPreferredSize(new Dimension(250, 30));

        // label for from date
        fromDate = new JLabel("From :");
        fromDate.setFont(new Font("cambria", 10, 22));
        fromDate.setHorizontalAlignment(SwingConstants.CENTER);
        fromDate.setPreferredSize(new Dimension(130, 20));

        // date selector
        toDateChooser = new JDateChooser();
        // set max selected date to current date.
        toDateChooser.setMaxSelectableDate(calendar.getTime());
        toDateChooser.setFont(labelFont);
        toDateChooser.setDate(calendar.getTime());
        toDateChooser.setPreferredSize(new Dimension(250, 30));

        // label for to date
        toDate = new JLabel("To :");
        toDate.setFont(new Font("cambria", 10, 22));
        toDate.setPreferredSize(new Dimension(130, 20));
        toDate.setHorizontalAlignment(SwingConstants.CENTER);

        // add an components to the top panel.
        topPanel.add(heading);
        topPanel.add(fromDate);
        topPanel.add(fromDateChooser);
        topPanel.add(toDate);
        topPanel.add(toDateChooser);
        topPanel.setBackground(Color.white);

        // add the top panel.
        mainPanel.add(topPanel);

        // create a table model object by overriding the method isCellEditable() to set
        // all cell uneditable.
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // add an columns to the table model.
        tableModel.addColumn("Sr.No.");
        tableModel.addColumn("size");
        tableModel.addColumn("guage");
        tableModel.addColumn("rate");
        tableModel.addColumn("total bags");
        tableModel.addColumn("total weight");
        tableModel.addColumn("total Amount");

        // add an table model to JTable.
        viewTable = new JTable(tableModel);
        viewTable.setRowHeight(30);
        viewTable.setFont(labelFont);

        // create scroll pane with table to add scrollbar functionality.
        scrollPane = new JScrollPane(viewTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setPreferredSize(new Dimension(1120, 450));

        // add a table row sorter to the table.
        sorter = new TableRowSorter<TableModel>(tableModel);
        viewTable.setRowSorter(sorter);

        // get table header to modify it.
        JTableHeader header = viewTable.getTableHeader();
        header.setFont(labelFont);

        // create table panel to add table.
        tablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        tablePanel.setPreferredSize(new Dimension(1250, 455));
        tablePanel.setBackground(Color.white);
        tablePanel.add(scrollPane);

        // this method creates an buttons.
        allocateToButtons();

        // add table panel to main panel.
        tablePanel.add(buttonPanel);
        mainPanel.add(tablePanel);

        // this create the base panel .
        allocateToBasePanel();
        mainPanel.add(basePanel);
        add(mainPanel);
        setBackground(Color.white);
        // set the data to JTable.
        setTableData();
        setVisible(true);
    }

    /**
     * This method are allocate an memory or create an buttons for the report.
     */
    private void allocateToButtons() {
        // create button panel with grid layout.
        buttonPanel = new JPanel(new GridLayout(10, 1, 5, 5));
        buttonPanel.setPreferredSize(new Dimension(95, 450));
        buttonPanel.setBackground(Color.white);

        search = new JButton("Search");
        search.setBackground(new Color(180, 56, 252));
        search.setForeground(Color.white);
        search.setFont(buttonFont);

        remove = new JButton("Remove");
        remove.setBackground(new Color(252, 122, 122));
        remove.setForeground(Color.white);
        remove.setFont(buttonFont);

        print = new JButton("Print");
        print.setBackground(new Color(155, 173, 252));
        print.setForeground(Color.white);
        print.setFont(buttonFont);

        cancel = new JButton("Cancel");
        cancel.setFont(buttonFont);
        cancel.setBackground(new Color(100, 177, 77));
        cancel.setForeground(Color.white);

        exit = new JButton("Exit");
        exit.setFont(buttonFont);
        exit.setForeground(Color.white);
        exit.setBackground(new Color(252, 143, 41));

        search.addActionListener(this);
        remove.addActionListener(this);
        print.addActionListener(this);
        cancel.addActionListener(this);
        exit.addActionListener(this);

        buttonPanel.add(search);
        buttonPanel.add(remove);
        buttonPanel.add(print);
        buttonPanel.add(cancel);
        buttonPanel.add(exit);
    }

    /**
     * This method create an base panel.
     */
    private void allocateToBasePanel() {
        // set flow layout to panel.
        basePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        basePanel.setPreferredSize(new Dimension(1120, 50));
        basePanel.setBackground(Color.white);
        basePanel.setBorder(border);

        totalRowsLabel = new JLabel("Total Entries :");
        totalRowsLabel.setFont(labelFont);
        totalRowsField = new JTextField(10);
        totalRowsField.setEnabled(false);
        totalRowsField.setDisabledTextColor(Color.darkGray);
        totalRowsField.setFont(labelFont);

        totalAmountLabel = new JLabel("Total Amount :");
        totalAmountLabel.setFont(labelFont);
        totalAmountField = new JTextField(10);
        totalAmountField.setEnabled(false);
        totalAmountField.setDisabledTextColor(Color.darkGray);
        totalAmountField.setFont(labelFont);

        totalWeightLabel = new JLabel("Total Weight :");
        totalWeightLabel.setFont(labelFont);
        totalWeightField = new JTextField(10);
        totalWeightField.setEnabled(false);
        totalWeightField.setDisabledTextColor(Color.darkGray);
        totalWeightField.setFont(labelFont);

        totalBagsLabel = new JLabel("Total Bags :");
        totalBagsLabel.setFont(labelFont);
        totalBagsField = new JTextField(10);
        totalBagsField.setFont(labelFont);
        totalBagsField.setEnabled(false);
        totalBagsField.setDisabledTextColor(Color.darkGray);

        // add components to base panel.
        basePanel.add(totalRowsLabel);
        basePanel.add(totalRowsField);
        basePanel.add(totalWeightLabel);
        basePanel.add(totalWeightField);
        basePanel.add(totalBagsLabel);
        basePanel.add(totalBagsField);
        basePanel.add(totalAmountLabel);
        basePanel.add(totalAmountField);

    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // Checks the source is exit or not.
        if (source.equals(exit)) {
            boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to go from here..");
            // true then exit from stock report.
            if (res) {
                homeMenuItem.doClick();
            }
        }

        // checks the source is cancel or not.
        if (source.equals(cancel)) {
            // reset the table and dates to current date.
            fromDateChooser.setDate(calendar.getTime());
            toDateChooser.setDate(calendar.getTime());
            fromDateForReportPrint = null;
            toDateForReportPrint = null;
            // set data to table.
            setTableData();
        }

        // checks the source is search or not.
        if (source.equals(search)) {
            try {
                // get formatted dates
                String fromDateVal = format.format(fromDateChooser.getDate());
                String toDateVal = format.format(toDateChooser.getDate());
                // checks the dates are selected by user or not.
                if (fromDateVal.isBlank() && toDateVal.isBlank()) {
                    DialogWindow.showMessageDialog(this, "Please! choose the dates..");
                } else {
                    fromDateForReportPrint = fromDateVal;
                    toDateForReportPrint = toDateVal;
                    // search records in DB between these dates.
                    searchByDates(fromDateVal, toDateVal);
                }
            } catch (Exception exc) {
                DialogWindow.showErrorDialog(this, "Invalid date selected!");
            }
        }

        // Checks the source is remove or not.
        if (source.equals(remove)) {
            try {
                // get formatted selected dates.
                String fromDateVal = format.format(fromDateChooser.getDate());
                String toDateVal = format.format(toDateChooser.getDate());
                // checks values are null or not.
                if (fromDateVal.isBlank() && toDateVal.isBlank()) {
                    DialogWindow.showMessageDialog(this, "Please! choose the dates..");
                } else {
                    // get confirmation to delete the data from DB.
                    boolean res = DialogWindow.showConfirmDialog(this,
                            "Are you sure to remove the data between " + fromDateVal + " To " + toDateVal);
                    // result true then data will removed.
                    if (res) {
                        // remove the data between these dates.
                        String query = "DELETE FROM stocks WHERE entry_month BETWEEN '" + fromDateVal + "' AND '"
                                + toDateVal + "'";
                        boolean res1 = DBConnection.execute(query);
                        if (res1) {
                            DialogWindow.showMessageDialog(this, "Data removed successfully");
                        } else {
                            DialogWindow.showMessageDialog(this, "Records does not remove!");
                        }
                    }
                }
                // reset the report.
                cancel.doClick();
            } catch (Exception exc) {
                DialogWindow.showErrorDialog(this, "Connection error,try again or invalid dates!");
            }
        }

        // Checks the source is print or not.
        if (source.equals(print)) {
            boolean res = DialogWindow.showConfirmDialog(this, "Are you print this report ?");
            if (res) {
                // A directory path where pdf file will store/save.
                String path = "C:\\kk_enterprises\\inventory_reports";

                // headers/fields of pdf table.
                java.util.List<String> headers = Arrays.asList("Sr.No.", "Size", "Guage", "Rate", "T Bags",
                        "T Weight", "T Amount");

                // array list to hold multiple list of records.
                java.util.List<java.util.List<String>> data = new ArrayList<java.util.List<String>>();

                // declare a list to hold a record.
                java.util.List<String> list = null;

                // get number of rows in table model.
                int rows = tableModel.getRowCount();
                // check table model not empty.
                if (rows > 0) {
                    int cnt = sorter.getViewRowCount();
                    // loop each record/row of table model.
                    for (int j = 0; j < cnt; j++) {
                        int i = sorter.convertRowIndexToModel(j);

                        // get values of each cell of row.
                        String sr_no = Integer.toString(j + 1);
                        String size = tableModel.getValueAt(i, 1).toString();
                        String guage = tableModel.getValueAt(i, 2).toString();
                        String rate = tableModel.getValueAt(i, 3).toString();
                        String tBags = tableModel.getValueAt(i, 4).toString();
                        String tWeight = tableModel.getValueAt(i, 5).toString();
                        String tAmt = Double.toString(Double.parseDouble(rate) * Double.parseDouble(tWeight));

                        // create a list to hold a record.
                        list = new ArrayList<String>();
                        list.add(sr_no);
                        list.add(size);
                        list.add(guage);
                        list.add(rate);
                        list.add(tBags);
                        list.add(tWeight);
                        list.add(tAmt);

                        // add list in to main list.
                        data.add(list);
                    }

                    // create a instance of PDFMaker to generate a pdf file with given data.
                    PDFMaker generatePdf = new PDFMaker("Inventory Report", null, headers, data, path, true,
                            fromDateForReportPrint, toDateForReportPrint, true, "T Amount");
                    // call method to generate actual pdf file at given path.
                    String result = generatePdf.generatePDF();
                    // check result not null means pdf are successfully created.
                    if (result != null) {
                        DialogWindow.showMessageDialog(this, "PDF Generated at:\n" + result);
                    } else {
                        DialogWindow.showWarningDialog(this, "PDF Cannot created at:\n" + path);
                    }
                } else {
                    // execute when table model is empty.
                    DialogWindow.showErrorDialog(this, "No Record found to print!");
                }
            }
        }
    }

    /**
     * This method get the data from DB and send it for set into the JTable.
     */
    private void setTableData() {
        try {
            // Get data from DB.
            String query = "SELECT rate,guage,size,SUM(weight) AS total_weight,SUM(bag) AS total_bag FROM stocks GROUP BY rate,guage,size ORDER BY size ASC";
            ResultSet res = DBConnection.executeQuery(query);
            // set resulted data to JTable.
            setResultedData(res);
        } catch (Exception exc) {
            DialogWindow.showErrorDialog(this, "Connection error, try again!");
        }
    }

    /**
     * This method search the record into DB between fromDate to toDate.
     * 
     * @param fromDate starting date
     * @param toDate   ending date
     */
    private void searchByDates(String fromDate, String toDate) {
        try {
            // Get the data from DB.
            String query = "SELECT rate,guage,size,SUM(weight) AS total_weight,SUM(bag) AS total_bag FROM stocks WHERE entry_month BETWEEN '"
                    + fromDate + "' AND '" + toDate + "' GROUP BY rate,guage,size ORDER BY size ASC LIMIT 100";
            ResultSet res = DBConnection.executeQuery(query);
            // set data to JTable.
            setResultedData(res);
        } catch (Exception e) {
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }
    }

    /**
     * This method set the ResultSet data to JTable.
     * 
     * @param res a object of java.sql.ResultSet.
     */
    private void setResultedData(ResultSet res) {
        try {
            // remove old rows from table model.
            while (tableModel.getRowCount() > 0) {
                tableModel.removeRow(0);
            }
            sr_no = 1;
            total = 0d;
            int tWeight = 0;
            int tBags = 0;

            // Set the records to the JTable one by one.
            while (res.next()) {
                String size = res.getString("size");
                Double guage = res.getDouble("guage");
                Double rate = res.getDouble("rate");
                int totalWeight = res.getInt("total_weight");
                int totalBags = res.getInt("total_bag");
                Double totalAmount = totalWeight * rate;
                total = total + totalAmount;
                tWeight = tWeight + totalWeight;
                tBags = tBags + totalBags;
                // set the data to one by one.
                Object data[] = new Object[] { sr_no++, size, guage, rate, totalBags, totalWeight, totalAmount };
                tableModel.addRow(data);
            }

            // set the total of all data to base panel components.
            totalRowsField.setText(Integer.toString(--sr_no));
            totalWeightField.setText(Integer.toString(tWeight));
            totalBagsField.setText(Integer.toString(tBags));
            totalAmountField.setText(Double.toString(total));

        } catch (Exception e) {
            DialogWindow.showErrorDialog(this, "Sorry Invalid response received ,try again!");
        }
    }
}
/**
 * This component end here...
 */