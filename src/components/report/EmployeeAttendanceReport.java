/**
 * 
 * 
 * 
 * This file the class which contains the report of employee attendance with there total 
 * present days,total absent days,total half days and total overtime hours 
 * according to the selected month and also print the report as per 
 * request.
 * 
 * 
 * 
 * 
 */

package components.report;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.beans.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
import com.toedter.calendar.*;
import components.report.generate.*;
import config.*;
import partial.*;
import partial.interfaces.*;

/**
 * The class <code>EmployeeAttendanceReport</code> are create an attendance
 * report
 * monthly.
 */
public class EmployeeAttendanceReport extends JPanel implements FontInterface, PropertyChangeListener, ActionListener {

    // the panels to hold the components.
    JPanel mainPanel, headingPanel, datePanel, columnHeadingPanel, contentPanel, buttonPanel;
    // labels for displaying info.
    JLabel heading, dateLabel, srNoLabel, nameLabel, totalPresentDaysLabel, totalAbsentDaysLabel,
            totalOvertimeHoursLabel, totalHalfDaysLabel, totalPresentDaysFromJoiningDate;
    // buttons for operation like print and exit from window.
    JButton print, exit;
    // date selector.
    JDateChooser dateChooser;
    // scrolling panel for add scrollable component.
    JScrollPane scrollPane;
    // table for displaying data.
    JTable table;
    DefaultTableModel tableModel;
    TableRowSorter<TableModel> sorter;
    // menu fo navigation.
    JMenuItem homeMenuItem;
    // collection framework structures for storing and manipulating data.
    Map<Integer, Integer> empIdPresentHashMap, empIdAbsentHashMap, empIdHalfDayHashMap;
    Map<Integer, String> empIdNameHashMap;
    Map<Integer, Double> empIdOvertimeHashMap;
    // sr no for table rows.
    static int srNoForTable = 1;

    /**
     * The constructor which create a <strong>employee attendance report</strong>.
     * 
     * @param homeMenu is menu item reference of home menu.
     */
    public EmployeeAttendanceReport(JMenuItem homeMenu) {
        // set reference of menu item.
        homeMenuItem = homeMenu;
        // GUI start here..
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.white);

        mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.setPreferredSize(new Dimension(1250, 610));
        mainPanel.setBorder(border);
        mainPanel.setBackground(Color.white);

        headingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 5));
        headingPanel.setBackground(Color.white);
        headingPanel.setPreferredSize(new Dimension(1235, 60));
        headingPanel.setBorder(border);

        heading = new JLabel("Employee Attendance Report");
        heading.setForeground(Color.darkGray);
        heading.setFont(headingFont);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setVerticalAlignment(SwingConstants.CENTER);

        headingPanel.add(heading);

        datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        datePanel.setBackground(Color.white);
        datePanel.setPreferredSize(new Dimension(370, 40));

        dateLabel = new JLabel("Date :");
        dateLabel.setForeground(Color.darkGray);
        dateLabel.setFont(labelFont);
        dateLabel.setPreferredSize(new Dimension(80, 30));
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dateLabel.setVerticalAlignment(SwingConstants.CENTER);

        dateChooser = new JDateChooser(calendar.getTime());
        dateChooser.setPreferredSize(new Dimension(250, 30));
        dateChooser.addPropertyChangeListener("date", this);
        dateChooser.setFont(labelFont);
        dateChooser.setMaxSelectableDate(calendar.getTime());

        datePanel.add(dateLabel);
        datePanel.add(dateChooser);

        headingPanel.add(datePanel);

        mainPanel.add(headingPanel);

        // creating a table model with no one cell are editable.
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("Sr.No.");
        tableModel.addColumn("Name");
        tableModel.addColumn("Total Present Days");
        tableModel.addColumn("Total Absent Days");
        tableModel.addColumn("Total Half Days");
        tableModel.addColumn("Total Overtime Hours");

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(labelFont);

        sorter = new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(sorter);

        JTableHeader header = table.getTableHeader();
        header.setFont(labelFont);
        header.setBackground(Color.white);
        header.setForeground(purple);

        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setPreferredSize(new Dimension(1225, 470));

        mainPanel.add(scrollPane);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        buttonPanel.setPreferredSize(new Dimension(1230, 50));
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
        // set the key-value of id-empName of all employees.
        setEmpIdNameForCalculating();
        // call function once to set data of current month.
        propertyChange(null);

        setVisible(true);
        // GUI end here..
    }

    /**
     * This method are get the employee id and name from DB and set it into Hash
     * Map.
     */
    private void setEmpIdNameForCalculating() {
        // create hash map for id-name.
        empIdNameHashMap = new HashMap<Integer, String>();
        try {
            // get data from DB.
            String query = "SELECT id,name FROM employees";
            ResultSet resultSet = DBConnection.executeQuery(query);
            // set data one by one.
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                // put the key and value.
                empIdNameHashMap.put(id, name);
            }
        } catch (Exception e) {
            // execute when any exception occur in above code.
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }

    }

    public void propertyChange(PropertyChangeEvent e) {
        // get the selected date and convert it specified format.
        String date = format.format(dateChooser.getDate());
        // convert date string to string array.
        String d[] = date.split("\\-");
        // get month value.
        int month = Integer.parseInt(d[1].trim());
        // get year value.
        int year = Integer.parseInt(d[0].trim());
        try {
            // Get the attendance data of selected month and year.
            String query = "SELECT * FROM attendance WHERE extract(MONTH FROM attendance_date) = " + month
                    + " AND extract(YEAR FROM attendance_date) = " + year;
            ResultSet res = DBConnection.executeQuery(query);
            // create hash maps for holding data.
            empIdPresentHashMap = new HashMap<Integer, Integer>();
            empIdOvertimeHashMap = new HashMap<Integer, Double>();
            empIdAbsentHashMap = new HashMap<Integer, Integer>();
            empIdHalfDayHashMap = new HashMap<Integer, Integer>();
            // loop one by one record.
            while (res.next()) {
                // convert the status string to string[][].
                String status[][] = convertStringToArray(res.getString("status"));
                // set one by one id-status and id-hours in hash map.
                for (int i = 1; i < status.length; i++) {
                    // get id of employee.
                    int id = Integer.parseInt(status[i][0]);
                    // add key value into hash map.
                    switch (status[i][1].trim()) {
                        case "Present":
                            // check the key is already exist if yes then increment the existing value.
                            if (empIdPresentHashMap.containsKey(id)) {
                                empIdPresentHashMap.put(id, empIdPresentHashMap.get(id) + 1);
                            } else {
                                // add new pair.
                                empIdPresentHashMap.put(id, 1);
                            }
                            break;
                        case "Absent":
                            // check the key is already exist if yes then increment the existing value.
                            if (empIdAbsentHashMap.containsKey(id)) {
                                empIdAbsentHashMap.put(id, empIdAbsentHashMap.get(id) + 1);
                            } else {
                                // add new pair.
                                empIdAbsentHashMap.put(id, 1);
                            }
                            break;
                        case "HalfDay":
                            // check the key is already exist if yes then increment the existing value.
                            if (empIdHalfDayHashMap.containsKey(id)) {
                                empIdHalfDayHashMap.put(id, empIdHalfDayHashMap.get(id) + 1);
                            } else {
                                // add new pair.
                                empIdHalfDayHashMap.put(id, 1);
                            }
                            break;
                    }

                    if (!status[i][2].trim().equals("No")) {
                        // convert the string hours value to double value.
                        double hours = Double.parseDouble(status[i][2]);
                        // check the key is already exist if yes then increment the existing value.
                        if (empIdOvertimeHashMap.containsKey(id)) {
                            empIdOvertimeHashMap.put(id, empIdOvertimeHashMap.get(id) + hours);
                        } else {
                            // add new pair.
                            empIdOvertimeHashMap.put(id, hours);
                        }
                    }

                }
            }

            // remove all previous rows from table.
            if (tableModel != null) {
                while (tableModel.getRowCount() > 0) {
                    tableModel.removeRow(0);
                }
            }

            // check the hash map not should be null.
            if (empIdNameHashMap != null) {

                srNoForTable = 1;
                // loop on each pair of map.
                empIdNameHashMap.forEach((id, name) -> {

                    /*
                     * first it check the key means id of current employee are exist in map if yes
                     * then set the value of that particular id (present count,absent count ,half
                     * day count,etc.) otherwise set to 0.
                     */
                    int presentCount = empIdPresentHashMap.containsKey(id) ? empIdPresentHashMap.get(id) : 0;
                    int absentCount = empIdAbsentHashMap.containsKey(id) ? empIdAbsentHashMap.get(id) : 0;
                    int halfDayCount = empIdHalfDayHashMap.containsKey(id) ? empIdHalfDayHashMap.get(id) : 0;
                    ;
                    double overtimeHours = empIdOvertimeHashMap.containsKey(id) ? empIdOvertimeHashMap.get(id) : 0;

                    // add record in table model.
                    tableModel
                            .addRow(new Object[] { srNoForTable++, name.trim(), presentCount, absentCount, halfDayCount,
                                    overtimeHours });

                });
            }

        } catch (Exception exc) {
            // execute when any exception occur in above code.
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }
    }

    public void actionPerformed(ActionEvent e) {
        // get the source component.
        Object source = e.getSource();
        // check the source is exit button or not.
        if (source.equals(exit)) {
            // get the confirmation before exit.
            boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to exit from here...");
            // res is true then exit from current window.
            if (res) {
                homeMenuItem.doClick();
            }
        }

        // check the source is print button or not.
        if (source.equals(print)) {
            boolean res = DialogWindow.showConfirmDialog(this, "Are you print this report ?");
            if (res) {
                // path where pdf will store/save.
                String path = "C:\\kk_enterprises\\attendance_reports";

                // array list to hold the table headings.
                java.util.List<String> headers = Arrays.asList("Sr.No.", "Name", "Presents", "Absents", "Half",
                        "Overtime Hours", "Month");

                // list to hold the multiple list of strings/nested array lists.
                java.util.List<java.util.List<String>> data = new ArrayList<java.util.List<String>>();

                // list to hold single list of strings.
                java.util.List<String> list = null;

                // get the count of table rows.
                int rows = tableModel.getRowCount();
                // check the table not should be empty.
                if (rows > 0) {
                    int cnt = sorter.getViewRowCount();
                    // loop on each row.
                    for (int j = 0; j < cnt; j++) {
                        int i = sorter.convertRowIndexToModel(j);
                        // get the values of each cell.
                        String sr_no = Integer.toString(j + 1);
                        String name = tableModel.getValueAt(i, 1).toString();
                        String present = tableModel.getValueAt(i, 2).toString();
                        String absent = tableModel.getValueAt(i, 3).toString();
                        String half = tableModel.getValueAt(i, 4).toString();
                        String overtime = tableModel.getValueAt(i, 5).toString();
                        String month = format.format(dateChooser.getDate());
                        String[] m = month.split("\\-");
                        month = m[1] + "-" + m[0];

                        // create a list to hold the record.
                        list = new ArrayList<String>();
                        list.add(sr_no);
                        list.add(name);
                        list.add(present);
                        list.add(absent);
                        list.add(half);
                        list.add(overtime);
                        list.add(month);

                        // add list into another list or data.
                        data.add(list);
                    }

                    // create a instance of PDFMaker with data to generate PDF file.
                    PDFMaker pdfMaker = new PDFMaker("Employee Attendance Report", headers, data, path, false);

                    // call method of it and get path.
                    String result = pdfMaker.generatePDF();
                    // check the pdf are successfully created.
                    if (result != null) {
                        DialogWindow.showMessageDialog(this, "PDF Generated at:\n" + result);
                    } else {
                        DialogWindow.showWarningDialog(this, "PDF Cannot created at:\n " + path);
                    }
                } else {
                    // execute when no record found.
                    DialogWindow.showErrorDialog(this, "No Record found to print!");
                }
            }
        }
    }
}
/**
 * This component are end here...
 */