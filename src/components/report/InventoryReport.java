/**
 * 
 * 
 * This file contains the class which show the employee data in the table format.
 * 
 */
package components.report;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import components.report.generate.PDFMaker;
import components.report.partial.*;
import config.*;
import partial.*;
import javax.swing.*;
import com.toedter.calendar.*;

/**
 * The EmployeeReport class have to design a table with employee data.
 */
public class InventoryReport extends InventoryReportView implements ActionListener {

    // instance variables.
    JPanel mainPanel, buttonPanel, headingPanel, datePanel;
    JLabel headingLabel, searchLabel, fromDateLabel, toDateLabel;
    JDateChooser fromDateChooser, toDateChooser;
    JTextField searchField;
    JButton print, exit, search, cancel;
    JMenuItem homeMenuItem, inventoryReportMenu;

    /**
     * This constructor create a employee report.
     * 
     * @param homeMenu the reference of home menu.
     * @param current  the reference of current menu.
     */
    public InventoryReport(JMenuItem homeMenu, JMenuItem current) {
        // set the menus for navigation.
        inventoryReportMenu = current;
        homeMenuItem = homeMenu;
        // GUI start here..
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.white);

        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.setPreferredSize(new Dimension(1250, 600));
        mainPanel.setBorder(border);
        mainPanel.setBackground(Color.white);

        headingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        headingPanel.setPreferredSize(new Dimension(1240, 70));
        headingPanel.setBorder(border);
        headingPanel.setBackground(Color.white);

        headingLabel = new JLabel("Stock Report");
        headingLabel.setFont(headingFont);
        headingLabel.setForeground(Color.darkGray);

        headingPanel.add(headingLabel);

        datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        datePanel.setPreferredSize(new Dimension(980, 50));
        datePanel.setBackground(Color.white);

        fromDateLabel = new JLabel("From :");
        fromDateLabel.setPreferredSize(new Dimension(80, 30));
        fromDateLabel.setVerticalAlignment(SwingConstants.CENTER);
        fromDateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fromDateLabel.setFont(new Font("cambria", 10, 22));
        fromDateLabel.setForeground(Color.darkGray);

        fromDateChooser = new JDateChooser(calendar.getTime());
        fromDateChooser.setMaxSelectableDate(calendar.getTime());
        fromDateChooser.setFont(labelFont);
        fromDateChooser.setPreferredSize(new Dimension(250, 30));

        toDateLabel = new JLabel("To :");
        toDateLabel.setPreferredSize(new Dimension(80, 30));
        toDateLabel.setVerticalAlignment(SwingConstants.CENTER);
        toDateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        toDateLabel.setFont(new Font("cambria", 10, 22));
        toDateLabel.setForeground(Color.darkGray);

        toDateChooser = new JDateChooser(calendar.getTime());
        toDateChooser.setMaxSelectableDate(calendar.getTime());
        toDateChooser.setFont(labelFont);
        toDateChooser.setPreferredSize(new Dimension(250, 30));

        search = new JButton("Search");
        search.setFont(buttonFont);
        search.setBackground(purple);
        search.setForeground(Color.white);
        search.setPreferredSize(new Dimension(100, 30));
        search.addActionListener(this);

        cancel = new JButton("Cancel");
        cancel.setFont(buttonFont);
        cancel.setBackground(orange);
        cancel.setForeground(Color.white);
        cancel.setPreferredSize(new Dimension(100, 30));
        cancel.addActionListener(this);

        datePanel.add(fromDateLabel);
        datePanel.add(fromDateChooser);
        datePanel.add(toDateLabel);
        datePanel.add(toDateChooser);
        datePanel.add(search);
        datePanel.add(cancel);

        headingPanel.add(datePanel);

        mainPanel.add(headingPanel);

        scrollPane.setPreferredSize(new Dimension(1225, 470));
        scrollPane.getViewport().setBackground(Color.white);
        mainPanel.add(scrollPane);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setPreferredSize(new Dimension(1225, 40));
        buttonPanel.setBorder(border);
        buttonPanel.setBackground(Color.white);

        print = new JButton("Print");
        print.setForeground(Color.white);
        print.setBackground(lightBlue);
        print.setFont(buttonFont);
        print.setPreferredSize(new Dimension(100, 30));
        print.addActionListener(this);

        exit = new JButton("Exit");
        exit.setBackground(red);
        exit.setForeground(Color.white);
        exit.setFont(buttonFont);
        exit.setPreferredSize(new Dimension(100, 30));
        exit.addActionListener(this);

        buttonPanel.add(print);
        buttonPanel.add(exit);

        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
        // GUI end here..
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // check source is print button or not.
        if (source.equals(print)) {
            boolean res = DialogWindow.showConfirmDialog(this, "Are you print this report ?");
            if (res) {
                // a path where pdf will save.
                String path = "C:\\kk_enterprises\\stock_reports";

                // header/fields of the pdf table.
                java.util.List<String> headers = Arrays.asList("Sr.No.", "Size", "Guage", "Rate", "Bags",
                        "Weight", "Amount", "Date");

                // array list to hold multiple lists/records.
                java.util.List<java.util.List<String>> data = new ArrayList<java.util.List<String>>();

                // list to hold a single record.
                java.util.List<String> list = null;

                // get number of rows in table model.
                int rows = tableModel.getRowCount();
                // check table model not empty.
                if (rows > 0) {
                    int cnt = sorter.getViewRowCount();
                    // loop on each row of table model.
                    for (int j = 0; j < cnt; j++) {
                        int i = sorter.convertRowIndexToModel(j);
                        // get each cell value of row.
                        String sr_no = Integer.toString(j + 1);
                        String size = tableModel.getValueAt(i, 1).toString();
                        String guage = tableModel.getValueAt(i, 2).toString();
                        String rate = tableModel.getValueAt(i, 3).toString();
                        String tBags = tableModel.getValueAt(i, 4).toString();
                        String tWeight = tableModel.getValueAt(i, 5).toString();
                        String tAmt = Double.toString(Double.parseDouble(rate) * Double.parseDouble(tWeight));
                        String date = tableModel.getValueAt(i, 6).toString();
                        String d[] = date.split("\\-");
                        // reverse a date.
                        date = d[2] + "-" + d[1] + "-" + d[0];

                        // create a new list to hold record.
                        list = new ArrayList<String>();
                        list.add(sr_no);
                        list.add(size);
                        list.add(guage);
                        list.add(rate);
                        list.add(tBags);
                        list.add(tWeight);
                        list.add(tAmt);
                        list.add(date);

                        // add list into another list.
                        data.add(list);
                    }

                    // create a instance of PDFMaker to create a pdf with given data.
                    PDFMaker generatePdf = new PDFMaker("Stock Report", null, headers, data, path, true, null, null,
                            true,
                            "Amount");
                    // call method to generate actual pdf.
                    String result = generatePdf.generatePDF();
                    // check a pdf are created successfully or not,if path are return then pdf are
                    // generated.
                    if (result != null) {
                        DialogWindow.showMessageDialog(this, "PDF Generated at:\n" + result);
                    } else {
                        DialogWindow.showWarningDialog(this, "PDF Cannot created at:\n" + path);
                    }
                } else {
                    // execute when record not found.
                    DialogWindow.showErrorDialog(this, "No Record found to print!");
                }
            }
        }

        // check source is exit button or not.
        if (source.equals(exit)) {
            boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to exit from here..");
            if (res) {
                homeMenuItem.doClick();
            }
        }

        // check source is cancel button or not.
        if (source.equals(cancel)) {
            inventoryReportMenu.doClick();
        }

        // check source is search button or not.
        if (source.equals(search)) {
            String from = format.format(fromDateChooser.getDate());
            String to = format.format(toDateChooser.getDate());
            if (from != null && to != null) {
                try {
                    // query for getting data from DB.
                    String query = "SELECT * FROM stocks WHERE entry_month BETWEEN '" + from + "' AND '" + to + "'";
                    // execute query and get result set.
                    ResultSet resultSet = DBConnection.executeQuery(query);
                    int sr_no = 1;
                    while (tableModel.getRowCount() > 0) {
                        tableModel.removeRow(0);
                    }
                    // loop one by one record and set it into the table by rows.
                    while (resultSet.next()) {
                        // extract data from result set.
                        String size = resultSet.getString("size");
                        int guage = resultSet.getInt("guage");
                        double rate = resultSet.getDouble("rate");
                        double bags = resultSet.getDouble("bag");
                        double weight = resultSet.getDouble("weight");
                        String date = resultSet.getString("entry_month");

                        // add data into the table model.
                        tableModel.addRow(new Object[] { sr_no++, size, guage, rate, bags, weight, date });
                    }
                } catch (Exception exc) {
                    // execute when an exception occur in above code.
                    DialogWindow.showErrorDialog(this, "Connection error,try again!");
                }
            }
        }
    }
}
/**
 * This component end here...
 */
