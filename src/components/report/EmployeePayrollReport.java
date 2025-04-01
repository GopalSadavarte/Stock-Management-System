/**
 * 
 * 
 * This file contains the class which show the employee payroll data in the table format.
 * 
 */
package components.report;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import components.report.generate.*;
import components.report.partial.*;
import partial.*;
import javax.swing.*;

/**
 * The EmployeePayrollReport class have to design a table with employee data.
 */
public class EmployeePayrollReport extends EmployeePayrollView implements ActionListener {

    // instance variables.
    JPanel mainPanel, buttonPanel, headingPanel;
    JLabel headingLabel, searchLabel;
    JTextField searchField;
    JButton print, exit, search, cancel;
    JMenuItem homeMenuItem, empReportMenu;

    /**
     * This constructor create a employee report.
     * 
     * @param homeMenu the reference of home menu.
     * @param current  the reference of current menu.
     */
    public EmployeePayrollReport(JMenuItem homeMenu, JMenuItem current) {
        // set the menus for navigation.
        empReportMenu = current;
        homeMenuItem = homeMenu;
        // GUI start here..
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.white);

        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.setPreferredSize(new Dimension(1250, 600));
        mainPanel.setBorder(border);
        mainPanel.setBackground(Color.white);

        headingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        headingPanel.setPreferredSize(new Dimension(1240, 70));
        headingPanel.setBorder(border);

        headingPanel.setBackground(Color.white);

        headingLabel = new JLabel("Employee Payroll Report");
        headingLabel.setFont(headingFont);
        headingLabel.setForeground(Color.darkGray);

        headingPanel.add(headingLabel);

        searchLabel = new JLabel("Search By Name :");
        searchLabel.setFont(labelFont);
        searchLabel.setPreferredSize(new Dimension(150, 20));
        searchLabel.setHorizontalAlignment(SwingConstants.CENTER);
        searchLabel.setForeground(Color.darkGray);
        searchLabel.setVerticalAlignment(SwingConstants.CENTER);

        headingPanel.add(searchLabel);

        searchField = new JTextField(30);
        searchField.setFont(labelFont);

        headingPanel.add(searchField);

        search = new JButton("Search");
        search.setForeground(Color.white);
        search.setBackground(purple);
        search.setPreferredSize(new Dimension(100, 30));
        search.setFont(buttonFont);
        search.addActionListener(this);

        cancel = new JButton("Cancel");
        cancel.setForeground(Color.white);
        cancel.setBackground(orange);
        cancel.setPreferredSize(new Dimension(100, 30));
        cancel.setFont(buttonFont);
        cancel.addActionListener(this);

        headingPanel.add(search);
        headingPanel.add(cancel);

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
                // path where a pdf will store/save.
                String path = "C:\\kk_enterprises\\payroll_reports";

                // list to hold the table headings.
                java.util.List<String> headers = Arrays.asList("Sr.No.", "Name", "Basic", "Advance", "Deposit",
                        "Pending",
                        "Overtime Hours", "Rate", "Amount", "Month");

                // list to hold a record.
                java.util.List<String> list = null;

                // list to hold multiple list/records.
                java.util.List<java.util.List<String>> data = new ArrayList<java.util.List<String>>();

                // get row count of table.
                int rows = tableModel.getRowCount();
                // check table not empty.
                if (rows > 0) {
                     int cnt = sorter.getViewRowCount();
                    // loop each row of table.
                    for (int j = 0; j < cnt; j++) {
                        int i = sorter.convertRowIndexToModel(j);
                        // get data of each cell.
                        String sr_no = Integer.toString(j + 1);
                        String name = tableModel.getValueAt(i, 2).toString();
                        String basic = tableModel.getValueAt(i, 3).toString();
                        String advance = tableModel.getValueAt(i, 4).toString();
                        String deposit = tableModel.getValueAt(i, 5).toString();
                        String pending = tableModel.getValueAt(i, 6).toString();
                        String hours = tableModel.getValueAt(i, 7).toString();
                        String rate = tableModel.getValueAt(i, 8).toString();
                        String payableAmount = tableModel.getValueAt(i, 9).toString();
                        String month = tableModel.getValueAt(i, 10).toString();

                        // create a list to hold the record.
                        list = new ArrayList<String>();
                        list.add(sr_no);
                        list.add(name);
                        list.add(basic);
                        list.add(advance);
                        list.add(deposit);
                        list.add(pending);
                        list.add(hours);
                        list.add(rate);
                        list.add(payableAmount);
                        list.add(month);

                        // add list into another list.
                        data.add(list);
                    }

                    // create instance of PDFMaker with data to create a pdf file.
                    PDFMaker pdfMaker = new PDFMaker("Employee Payroll Report", null, headers, data, path, false, null,
                            null, true, "Amount");
                    // call method to generate pdf.
                    String generatedPath = pdfMaker.generatePDF();
                    // check the resulted path not empty.
                    if (generatedPath != null) {
                        DialogWindow.showMessageDialog(this, "Employee Payroll Report Generated at :\n" + generatedPath);
                    } else {
                        DialogWindow.showWarningDialog(this, "Report could not generate at :\n" + path);
                    }
                } else {
                    // execute when no records found.
                    DialogWindow.showErrorDialog(this, "Records not found for PDF!");
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
            empReportMenu.doClick();
        }

        // check source is search button or not.
        if (source.equals(search)) {
            // get the value from text field for searching it into the employee report
            // table.
            String searchVal = searchField.getText().trim().toLowerCase();
            // check the value not should be empty or null
            if (searchVal.length() > 0) {
                // loop over the table employee records.
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    // get the name value of cell.
                    String name = tableModel.getValueAt(i, 2).toString().toLowerCase();
                    // compare the value,if not included then this row will be removed from table.
                    if (!name.contains(searchVal))
                        // remove the row.
                        tableModel.removeRow(i--);
                }
            } else {
                // execute when the search field value is null or empty.
                DialogWindow.showMessageDialog(this, "Enter value in the search field!");
                searchField.requestFocus();
            }
        }
    }
}
/**
 * This component end here...
 */