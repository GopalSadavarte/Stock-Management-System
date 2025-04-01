/**
 * 
 * 
 * This file contains the class which show the employee data in the table format.
 * 
 */
package components.report;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import components.report.generate.PDFMaker;
import components.report.partial.*;
import partial.*;
import javax.swing.*;

/**
 * The EmployeeReport class have to design a table with employee data.
 */
public class LateArrivalReport extends LateArrivalEmployeeView implements ActionListener {

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
    public LateArrivalReport(JMenuItem homeMenu, JMenuItem current) {
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

        headingLabel = new JLabel("Late Arrival Report");
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

        searchField = new JTextField(35);
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
                // path to store a generated pdf.
                String path = "C:\\kk_enterprises\\late_arrival_reports";

                // headers/fields of table of pdf.
                java.util.List<String> headers = Arrays.asList("Sr.No.", "Name", "Hours", "Description", "Time",
                        "Date");

                // create a arraylist to hold multiple list of records.
                java.util.List<java.util.List<String>> data = new ArrayList<java.util.List<String>>();

                // declare list to hold a record.
                java.util.List<String> list = null;

                // get number of rows in table model.
                int rows = tableModel.getRowCount();
                // check table model not empty.
                if (rows > 0) {
                     int cnt = sorter.getViewRowCount();
                    // loop each record/row of table model.
                    for (int j = 0; j < cnt; j++) {
                        int i = sorter.convertRowIndexToModel(j);
                        // get each cell value of record/row.
                        String sr_no = Integer.toString(j + 1);
                        String name = tableModel.getValueAt(i, 1).toString();
                        String hours = tableModel.getValueAt(i, 2).toString();
                        String desc = tableModel.getValueAt(i, 3).toString();
                        String time = tableModel.getValueAt(i, 4).toString();
                        String d[] = tableModel.getValueAt(i, 5).toString().split("\\-");
                        String date = d[2] + "-" + d[1] + "-" + d[0];

                        // set description value to description ,if cell contains a value otherwise set
                        // to "-".
                        desc = desc.length() > 0 ? desc : "-";

                        // create a list to hold a record.
                        list = new ArrayList<String>();
                        list.add(sr_no);
                        list.add(name);
                        list.add(hours);
                        list.add(desc);
                        list.add(time);
                        list.add(date);

                        // add list into another/main data list.
                        data.add(list);
                    }

                    // create a instance of PDFMaker to generate a pdf.
                    PDFMaker pdfMaker = new PDFMaker("Late Arrival Report", headers, data, path, true);
                    // call method to generate actual pdf file.
                    String generatedPath = pdfMaker.generatePDF();
                    // check path not empty means pdf are created successfully.
                    if (generatedPath != null) {
                        DialogWindow.showMessageDialog(this, "PDF Generated at:\n" + generatedPath);
                    } else {
                        DialogWindow.showWarningDialog(this, "PDF Cannot created at:\n" + path);
                    }
                } else {
                    // execute when table model is empty.
                    DialogWindow.showErrorDialog(this, "Record not found for PDF!");
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
                    String name = tableModel.getValueAt(i, 1).toString().toLowerCase();
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