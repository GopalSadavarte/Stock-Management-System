/**
 * 
 * 
 * This component are allow user to make a late arrival entry of a particular employee that means
 * the employee hours entry how many hours it could be late entered to the work.
 * 
 * 
 */

package components.utilities;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import com.toedter.calendar.*;
import components.utilities.support.*;
import config.*;
import partial.*;
import partial.interfaces.*;

/**
 * The class LateArrivalEntry are manage the late entry component.
 */
public class LateArrivalEntry extends JPanel implements FontInterface, ActionListener, ItemListener {

    // instance variables.
    JPanel headingPanel, contentPanel, formPanel, datePanel, mainPanel, buttonPanel, tablePanel;
    JLabel heading, dateLabel, timeLabel, hoursLabel, descLabel, empNameLabel, empIdLabel, totalEntriesLabel;
    JTextField hoursField, descField, empIdField, totalEntriesField;
    JComboBox<String> empNames;
    JDateChooser dateChooser;
    TimePicker timePicker;
    JButton add, exit;
    JTable table;
    DefaultTableModel tableModel;
    JScrollPane scrollPane;
    JMenuItem homeMenuItem;
    TableRowSorter<TableModel> sorter;
    HashMap<String, Integer> empNameIdHashMap;
    int sr_no = 1;

    /**
     * The constructor to make a late entry component.
     * 
     * @param homeMenu the menu reference of home component.
     */
    public LateArrivalEntry(JMenuItem homeMenu) {
        homeMenuItem = homeMenu;
        // GUI start here..
        setBackground(Color.white);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.setBackground(Color.white);
        mainPanel.setPreferredSize(new Dimension(800, 620));
        mainPanel.setBorder(border);

        headingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 60, 5));
        headingPanel.setBackground(Color.white);
        headingPanel.setPreferredSize(new Dimension(790, 50));
        headingPanel.setBorder(border);

        heading = new JLabel("Late Arrival Log");
        heading.setForeground(Color.darkGray);
        heading.setFont(headingFont);

        datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        datePanel.setBackground(Color.white);
        datePanel.setPreferredSize(new Dimension(380, 40));

        dateLabel = new JLabel("Date :");
        dateLabel.setFont(labelFont);
        dateLabel.setForeground(Color.darkGray);
        dateLabel.setVerticalAlignment(SwingConstants.CENTER);

        dateChooser = new JDateChooser(calendar.getTime());
        dateChooser.setMaxSelectableDate(calendar.getTime());
        dateChooser.setPreferredSize(new Dimension(150, 35));
        dateChooser.setFont(labelFont);

        timeLabel = new JLabel("Time :");
        timeLabel.setForeground(Color.darkGray);
        timeLabel.setFont(labelFont);
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);

        timePicker = new TimePicker();
        timePicker.setPreferredSize(new Dimension(100, 35));
        timePicker.setFont(labelFont);
        timePicker.setForeground(Color.darkGray);

        datePanel.add(dateLabel);
        datePanel.add(dateChooser);
        datePanel.add(timeLabel);
        datePanel.add(timePicker);

        headingPanel.add(heading);
        headingPanel.add(datePanel);

        mainPanel.add(headingPanel);

        contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        contentPanel.setPreferredSize(new Dimension(680, 200));
        contentPanel.setBackground(Color.white);

        formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        formPanel.setBackground(Color.white);
        formPanel.setPreferredSize(new Dimension(530, 150));

        empIdLabel = new JLabel("ID :");
        empIdLabel.setForeground(Color.darkGray);
        empIdLabel.setFont(labelFont);
        empIdLabel.setPreferredSize(new Dimension(100, 20));

        empIdField = new JTextField(30);
        empIdField.setFont(labelFont);
        empIdField.setEnabled(false);

        empNameLabel = new JLabel("Name :");
        empNameLabel.setForeground(Color.darkGray);
        empNameLabel.setFont(labelFont);
        empNameLabel.setPreferredSize(new Dimension(100, 20));

        empNames = new JComboBox<String>();
        empNames.setPreferredSize(new Dimension(390, 30));
        empNames.addItemListener(this);
        empNames.setFont(labelFont);

        hoursLabel = new JLabel("Hours :");
        hoursLabel.setForeground(Color.darkGray);
        hoursLabel.setFont(labelFont);
        hoursLabel.setPreferredSize(new Dimension(100, 20));

        hoursField = new JTextField(30);
        hoursField.setFont(labelFont);

        descLabel = new JLabel("Description :");
        descLabel.setPreferredSize(new Dimension(100, 20));
        descLabel.setFont(labelFont);
        descLabel.setForeground(Color.darkGray);

        descField = new JTextField(30);
        descField.setFont(labelFont);

        formPanel.add(empIdLabel);
        formPanel.add(empIdField);
        formPanel.add(empNameLabel);
        formPanel.add(empNames);
        formPanel.add(hoursLabel);
        formPanel.add(hoursField);
        formPanel.add(descLabel);
        formPanel.add(descField);

        contentPanel.add(formPanel);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setBackground(Color.white);
        buttonPanel.setPreferredSize(new Dimension(500, 40));

        add = new JButton("Add");
        add.setForeground(Color.white);
        add.setBackground(lightBlue);
        add.setFont(buttonFont);
        add.addActionListener(this);

        exit = new JButton("Exit");
        exit.setForeground(Color.white);
        exit.setBackground(red);
        exit.addActionListener(this);
        exit.setFont(buttonFont);

        buttonPanel.add(add);
        buttonPanel.add(exit);

        contentPanel.add(buttonPanel);

        mainPanel.add(contentPanel);

        /*
         * create table model with overriding the methods isCellEditable and
         * fireTableCellUpdated for custom implementation.
         */
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                // set the hours column editable.
                return (column == 1);
            }

            public void fireTableCellUpdated(int row, int column) {
                try {
                    // get data of updated cell.
                    int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    int emp_id = Integer.parseInt(empIdField.getText());
                    double hours = Double.parseDouble(tableModel.getValueAt(row, 1).toString());
                    // query that update the hours.
                    String query = "UPDATE late_arrivals SET no_of_hours = ? WHERE emp_wise_entry_no = ? AND emp_id = ?";
                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                    pst.setDouble(1, hours);
                    pst.setInt(2, id);
                    pst.setInt(3, emp_id);

                    // execute the query and get affected row count.
                    int affectedRows = pst.executeUpdate();
                    // check the count
                    if (affectedRows <= 0) {
                        DialogWindow.showErrorDialog(LateArrivalEntry.this, "Data update failed!");
                    }
                } catch (Exception e) {
                    // execute when exception are found in above code.
                    DialogWindow.showErrorDialog(LateArrivalEntry.this, "Connection error,try again!");
                }
            }
        };

        // set the columns to model.
        tableModel.addColumn("Sr.No.");
        tableModel.addColumn("Hours");
        tableModel.addColumn("Description");
        tableModel.addColumn("Arrival Time");
        tableModel.addColumn("Arrival Date");

        // create table with table model.
        table = new JTable(tableModel);
        table.setFont(labelFont);
        table.setRowHeight(30);
        // add key listener to remove the particular record.
        table.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    // get the selected row
                    int row = table.getSelectedRow();
                    // get key code which is typed.
                    String key = KeyEvent.getKeyText(e.getKeyCode());
                    // get modifier key (Ctrl,Alt,etc.)
                    String modifier = InputEvent.getModifiersExText(e.getModifiersEx());
                    // check the row are selected and press key is "D" and modifier key is "Ctrl".
                    if (row > -1 && key.equals("D") && modifier.equals("Ctrl")) {
                        // get user confirmation before remove the record.
                        boolean res = DialogWindow.showConfirmDialog(LateArrivalEntry.this,
                                "Are you sure to remove this record!");
                        // check result.
                        if (res) {
                            // get entry number of selected row.
                            int entry_no = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                            int id = Integer.parseInt(empIdField.getText());
                            // query to delete the selected record.
                            String query = "DELETE FROM late_arrivals WHERE emp_wise_entry_no = " + entry_no
                                    + " AND emp_id =" + id;
                            // execute a query.
                            res = DBConnection.execute(query);
                            // check the record remove successfully or not.
                            if (res) {
                                // remove the same row from table model.
                                tableModel.removeRow(row);
                                // set new row count to count field.
                                totalEntriesField.setText("" + table.getRowCount());
                                if (table.getRowCount() == 0) {
                                    sr_no = 1;
                                }
                            } else {
                                // show message when record does not remove.
                                DialogWindow.showErrorDialog(LateArrivalEntry.this, "Record remove failed!");
                            }
                        }
                    }
                } catch (Exception exc) {
                    // execute when exception in above code.
                    DialogWindow.showErrorDialog(LateArrivalEntry.this, "Connection error,try again!");
                }
            }
        });

        // create row sorter for sorting the records by user.
        sorter = new TableRowSorter<TableModel>(tableModel);
        // set sorter to table.
        table.setRowSorter(sorter);

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.white);
        header.setForeground(purple);
        header.setFont(labelFont);

        scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setPreferredSize(new Dimension(730, 300));

        tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tablePanel.setBackground(Color.white);
        tablePanel.setPreferredSize(new Dimension(750, 310));

        tablePanel.add(scrollPane);

        mainPanel.add(tablePanel);

        totalEntriesLabel = new JLabel("Total Entries :");
        totalEntriesLabel.setFont(labelFont);
        totalEntriesLabel.setForeground(Color.darkGray);

        totalEntriesField = new JTextField(20);
        totalEntriesField.setFont(labelFont);
        totalEntriesField.setEnabled(false);
        totalEntriesField.setDisabledTextColor(Color.darkGray);
        totalEntriesField.setText("0");

        mainPanel.add(totalEntriesLabel);
        mainPanel.add(totalEntriesField);
        add(mainPanel);

        // get all employees who's working status is true.
        setEmployeeNames();
        // get first selected employee records and set it in table.
        itemStateChanged(null);

        setVisible(true);
    }

    /**
     * This method are get all employees who's working status is true.
     */
    private void setEmployeeNames() {
        try {
            // create hash map to hold name and there id.
            empNameIdHashMap = new HashMap<String, Integer>();

            // query to get the data from DB.
            String query = "SELECT * FROM employees WHERE working_status = 'Yes'";
            // execute query and get result set.
            ResultSet resultSet = DBConnection.executeQuery(query);
            // loop one by one record.
            while (resultSet.next()) {
                // get values of current row.
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                // put the name and id to hash map.
                empNameIdHashMap.put(name, id);
                // add name in the combo box.
                empNames.addItem(name);
                // check the record is first or not.
                if (resultSet.isFirst()) {
                    // set the first entry employee id.
                    empIdField.setText("" + id);
                }
            }
        } catch (Exception e) {
            // execute when data base exception.
            DialogWindow.showErrorDialog(this, "No employee found \nOR\n Connection error,try again!");
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // check the source is exit button or not.
        if (source.equals(exit)) {
            // get user confirmation before exit.
            boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to exit from here..!");
            // check the result is true or not.
            if (res) {
                // go to home menu.
                homeMenuItem.doClick();
            }
        }

        // check the source is add button or not.
        if (source.equals(add)) {
            try {
                // get the id of employee who is selected.
                int id = empNameIdHashMap.get(empNames.getSelectedItem().toString());
                // get other information from text field.
                double hours = Double.parseDouble(hoursField.getText());
                String desc = descField.getText();
                String date = format.format(dateChooser.getDate());
                // convert the time to specific format.
                String time = new SimpleDateFormat("hh:mm a").format(timePicker.getValue());

                // query to insert a new record into DB.
                String query = "INSERT INTO late_arrivals (emp_id,no_of_hours,date,time,description,emp_wise_entry_no)VALUES(?,?,'"
                        + date + "',?,?,?)";
                // create an prepare statement.
                PreparedStatement pst = DBConnection.con.prepareStatement(query);

                pst.setInt(1, id);
                pst.setDouble(2, hours);
                pst.setString(3, time);
                pst.setString(4, desc);
                pst.setInt(5, sr_no);

                // execute a query and get affected rows.
                int affectedRows = pst.executeUpdate();
                // check the affected row are greater then zero means a record are insert
                // successfully.
                if (affectedRows > 0) {
                    // add record in the table model.
                    tableModel.addRow(new Object[] { sr_no++, hours, desc, time, date });
                    // display new row count.
                    totalEntriesField.setText("" + table.getRowCount());
                } else {
                    // failure in query execution.
                    DialogWindow.showErrorDialog(this, "Failed to add!");
                }
            } catch (Exception exc) {
                // exception in above code.
                DialogWindow.showErrorDialog(this, "Connection error,try again! \nOR\n invalid value of hours!");
            }
        }
    }

    public void itemStateChanged(ItemEvent e) {

        try {
            if (empNames.getSelectedItem() == null) {
                DialogWindow.showMessageDialog(this, "No employee found!");
                return;
            }
            // get the selected name.
            String name = empNames.getSelectedItem().toString();
            // get id of employee.
            int id = empNameIdHashMap.get(name);
            // set id in the id field.
            empIdField.setText("" + id);

            // query to get all records of the particular employee from DB.
            String query = "SELECT * FROM late_arrivals WHERE emp_id = " + id;
            ResultSet result = DBConnection.executeQuery(query);
            // remove previous rows.
            while (table.getRowCount() > 0) {
                tableModel.removeRow(0);
            }
            boolean status = false;
            // loop on new records.
            while (result.next()) {
                // get values of record from result set.
                id = result.getInt("id");
                String desc = result.getString("description");
                String time = result.getString("time");
                String date = result.getString("date");
                double hours = result.getDouble("no_of_hours");
                int entry_no = result.getInt("emp_wise_entry_no");

                // add row in the table model.
                tableModel.addRow(new Object[] { entry_no, hours, desc, time, date });
                if (result.isLast()) {
                    sr_no = entry_no + 1;
                    status = true;
                }
            }
            if (!status) {
                sr_no = 1;
            }
            // set a row count by table records.
            totalEntriesField.setText("" + table.getRowCount());
        } catch (Exception exc) {
            // execute when exception in above code.
            DialogWindow.showErrorDialog(this, "No record found \nOR\n Connection error,try again!");
        }
    }

}
/**
 * This component are end here..
 */
