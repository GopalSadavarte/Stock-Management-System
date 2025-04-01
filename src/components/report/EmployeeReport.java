/**
 * 
 * 
 * This file contains the class which show the employee data in the table format.
 * 
 */
package components.report;

import java.awt.*;
import java.awt.event.*;
import components.report.partial.*;
import partial.*;
import java.util.*;
import components.report.generate.*;
import javax.swing.*;

/**
 * The EmployeeReport class have to design a table with employee data.
 */
public class EmployeeReport extends EmployeeView implements ActionListener {

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
    public EmployeeReport(JMenuItem homeMenu, JMenuItem current) {
        // set the menus for navigation.
        empReportMenu = current;
        homeMenuItem = homeMenu;
        // GUI start here..
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.white);
        // create employee table with employee data with joining and leaving dates.
        createEmployeeView(true);

        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.setPreferredSize(new Dimension(1250, 600));
        mainPanel.setBorder(border);
        mainPanel.setBackground(Color.white);

        headingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        headingPanel.setPreferredSize(new Dimension(1240, 70));
        headingPanel.setBorder(border);

        headingPanel.setBackground(Color.white);

        headingLabel = new JLabel("Employee Report");
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

        searchField = new JTextField(40);
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
                // directory path to store the created pdf.
                String path = "C:\\kk_enterprises\\employee_reports";

                // name of the fields of table of pdf.
                java.util.List<String> header = Arrays.asList("Sr.No.", "Name", "Email", "Mobile", "Address",
                        "Joining", "Leaving");

                // A array list to hold multiple record in array list format.
                java.util.List<java.util.List<String>> data = new ArrayList<java.util.List<String>>();

                // list to hold a single record.
                java.util.List<String> list = null;

                // get the count of rows of table model.
                int rows = tableModel.getRowCount();
                // check the table model not empty.
                if (rows > 0) {
                    int cnt = sorter.getViewRowCount();
                    // loop record of table model.
                    for (int j = 0; j < cnt; j++) {
                        int i = sorter.convertRowIndexToModel(j);
                        // get the values of each record each cell.
                        String sr_no = Integer.toString(j + 1);
                        String name = tableModel.getValueAt(i, 2).toString();
                        String email = tableModel.getValueAt(i, 3).toString();
                        String mobile = tableModel.getValueAt(i, 4).toString();
                        String address = tableModel.getValueAt(i, 6).toString();
                        String joining = tableModel.getValueAt(i, 8).toString();
                        String leaving = tableModel.getValueAt(i, 9).toString();

                        // create a list to hold a record.
                        list = new ArrayList<String>();
                        list.add(sr_no);
                        list.add(name);
                        list.add(email);
                        list.add(mobile);
                        list.add(address);
                        list.add(joining);
                        list.add(leaving);

                        // add list to another list.
                        data.add(list);
                    }

                    // create a instance of PDFMaker to generate pdf from given data.
                    PDFMaker pdfMaker = new PDFMaker("Employee Report", header, data, path, false);
                    // call method to generate pdf and it return the path where pdf are save/store.
                    String generatedPath = pdfMaker.generatePDF();
                    // check a path not be null,if path are return then pdf are successfully
                    // created.
                    if (generatedPath != null) {
                        DialogWindow.showMessageDialog(this, "Employee report generated at:\n" + generatedPath);
                    } else {
                        DialogWindow.showWarningDialog(this, "Employee report cannot created at:\n" + path);
                    }
                } else {
                    // execute when record not found.
                    DialogWindow.showErrorDialog(this, "No record found to print!");
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