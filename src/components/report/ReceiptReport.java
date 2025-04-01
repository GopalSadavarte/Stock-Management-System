/**
 * 
 * 
 * This file contains the class which show the receipt data in the table format.
 * 
 */
package components.report;

import java.awt.*;
import java.awt.event.*;
import components.report.generate.*;
import components.report.partial.*;
import config.*;
import java.sql.*;
import java.util.*;
import partial.*;
import javax.swing.*;
import com.toedter.calendar.*;

/**
 * The ReceiptReport class have to design a table with receipt data.
 */
public class ReceiptReport extends ReceiptReportView implements ActionListener {

    // instance variables.
    JPanel mainPanel, buttonPanel, headingPanel, datePanel;
    JLabel headingLabel, fromDateLabel, toDateLabel;
    JDateChooser fromDateChooser, toDateChooser;
    JButton print, exit, search, cancel;
    JMenuItem homeMenuItem, receiptReport;

    /**
     * This constructor create a receipt report.
     * 
     * @param homeMenu the reference of home menu.
     * @param current  the reference of current menu.
     */
    public ReceiptReport(JMenuItem homeMenu, JMenuItem current) {
        // set the menus for navigation.
        receiptReport = current;
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

        headingLabel = new JLabel("Receipt Report");
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
                // directory path where a pdf file will store/save.
                String path = "C:\\kk_enterprises\\receipt_reports";

                // headers/fields of pdf table.
                java.util.List<String> headers = Arrays.asList("Sr.No.", "Description", "Amount", "Date");

                // create a array list to hold multiple list of records.
                java.util.List<java.util.List<String>> data = new ArrayList<java.util.List<String>>();

                // declare a list to hold a single record.
                java.util.List<String> list = null;

                // get number of rows in table model.
                int rows = tableModel.getRowCount();
                // check table model not empty.
                if (rows > 0) {
                    int cnt = sorter.getViewRowCount();
                    // loop each row/record of table model.
                    for (int j = 0; j < cnt; j++) {
                        int i = sorter.convertRowIndexToModel(j);
                        // get the values of each cell of row.
                        String sr_no = Integer.toString(j + 1);
                        String desc = tableModel.getValueAt(i, 1).toString();
                        String amount = tableModel.getValueAt(i, 2).toString();
                        String date = tableModel.getValueAt(i, 5).toString();

                        // reverse a date string.
                        String d[] = date.split("\\-");
                        date = d[2] + "-" + d[1] + "-" + d[0];

                        // create a list to hold a record.
                        list = new ArrayList<String>();
                        list.add(sr_no);
                        list.add(desc);
                        list.add(amount);
                        list.add(date);

                        // add list in to another list/main list.
                        data.add(list);
                    }

                    // create a instance of PDFMaker with given data to generate a pdf file.
                    PDFMaker pdfMaker = new PDFMaker("Receipt Report", null, headers, data, path, true, null, null,
                            true,
                            "Amount");
                    // call method of PDFMaker to generate actual pdf using given data.
                    String result = pdfMaker.generatePDF();
                    // check res no null means it return a path where pdf are save.
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

        // check source is exit button or not.
        if (source.equals(exit)) {
            boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to exit from here..");
            if (res) {
                homeMenuItem.doClick();
            }
        }

        // check source is cancel button or not
        if (source.equals(cancel)) {
            receiptReport.doClick();
        }

        if (source.equals(search)) {
            String from = format.format(fromDateChooser.getDate());
            String to = format.format(toDateChooser.getDate());

            if (from != null && to != null) {
                try {
                    String query = "SELECT * FROM vouchers WHERE status = 'Received' AND date BETWEEN '" + from
                            + "' AND '"
                            + to + "'";
                    ResultSet resultSet = DBConnection.executeQuery(query);
                    int sr_no = 1;
                    while (tableModel.getRowCount() > 0) {
                        tableModel.removeRow(0);
                    }
                    while (resultSet.next()) {
                        // extract data from result set.
                        String desc = resultSet.getString("description");
                        double amount = resultSet.getDouble("amount");
                        String state = resultSet.getString("payment_state");
                        String status = resultSet.getString("status");
                        String date = resultSet.getString("date");

                        // add data into the table model.
                        tableModel.addRow(new Object[] { sr_no++, desc, amount, state, status, date });
                    }

                } catch (Exception exc) {
                    DialogWindow.showErrorDialog(this, "Connection error,try again!");
                }
            }
        }
    }
}
/**
 * This component end here...
 */