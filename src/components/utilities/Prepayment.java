/**
 * 
 * 
 * 
 * 
 * This component are used to add details of advance payments for a particular employee with
 * the given amount and date.
 */

package components.utilities;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.toedter.calendar.*;
import components.report.generate.*;
import config.*;
import partial.*;
import partial.event.*;
import partial.interfaces.*;

/**
 * The class Prepayment are use add or remove the advance payment of employee
 * also generate a pdf file.
 */
public class Prepayment extends JPanel implements ActionListener, FontInterface, ItemListener, TableModelListener {

    // instance variables.
    JPanel mainPanel, headingPanel, contentPanel, buttonPanel, tablePanel, basePanel, datePanel, formPanel;
    JDateChooser dateChooser;
    JLabel headingLabel, empIdLabel, empNameLabel, advanceAmtLabel, dateLabel, totalAdvAmtLabel, totalPendingAmtLabel,
            paymentDescriptionLabel, tableHeading1, tableHeading2, totalReceiptAmtLabel, totalPaymentAmtLabel;
    JTextField advanceAmtField, totalAdvAmtField, totalReceiptAmtField, totalPaymentAmtField, empIdField,
            paymentDescriptionField;
    JComboBox<String> empNames;
    JMenuItem homeMenuItem;
    JButton add, exit, print;
    JTable table, table1;
    DefaultTableModel tableModel, tableModel1;
    JScrollPane scrollPane, scrollPane1;
    TableRowSorter<TableModel> sorter;
    HashMap<String, Integer> empNameIdHashMap;
    int srNo = 1;

    /**
     * The constructor to create advance payment component.
     * 
     * @param homeMenu is menu to navigate to home component.
     */
    public Prepayment(JMenuItem homeMenu) {
        homeMenuItem = homeMenu;

        // GUI start here..
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.white);

        mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        mainPanel.setBackground(Color.white);
        mainPanel.setPreferredSize(new Dimension(1250, 620));
        mainPanel.setBorder(border);

        headingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 80, 5));
        headingPanel.setPreferredSize(new Dimension(1240, 50));
        headingPanel.setBackground(Color.white);
        headingPanel.setBorder(border);

        headingLabel = new JLabel("Employee Prepayment");
        headingLabel.setFont(headingFont);
        headingLabel.setForeground(Color.darkGray);

        headingPanel.add(headingLabel);

        datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        datePanel.setBackground(Color.white);
        datePanel.setPreferredSize(new Dimension(300, 40));

        dateLabel = new JLabel("Date :");
        dateLabel.setFont(labelFont);
        dateLabel.setForeground(Color.darkGray);

        dateChooser = new JDateChooser(calendar.getTime());
        dateChooser.setPreferredSize(new Dimension(225, 35));
        dateChooser.setFont(labelFont);
        dateChooser.setMaxSelectableDate(calendar.getTime());

        datePanel.add(dateLabel);
        datePanel.add(dateChooser);

        headingPanel.add(datePanel);
        mainPanel.add(headingPanel);

        contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 20));
        contentPanel.setBackground(Color.white);
        contentPanel.setPreferredSize(new Dimension(1240, 140));

        formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        formPanel.setBackground(Color.white);
        formPanel.setPreferredSize(new Dimension(1230, 210));

        empIdLabel = new JLabel("ID :");
        empIdLabel.setFont(labelFont);
        empIdLabel.setPreferredSize(new Dimension(100, 20));

        empIdField = new JTextField(30);
        empIdField.setFont(labelFont);
        empIdField.setEnabled(false);
        empIdField.setDisabledTextColor(Color.darkGray);

        empNameLabel = new JLabel("Name :");
        empNameLabel.setFont(labelFont);
        empNameLabel.setPreferredSize(new Dimension(100, 20));

        empNames = new JComboBox<String>();
        empNames.setFont(labelFont);
        empNames.setPreferredSize(new Dimension(470, 25));
        // add employees names in the combo box.
        addEmployeesIntoList();
        empNames.addItemListener(this);

        advanceAmtLabel = new JLabel("Amount :");
        advanceAmtLabel.setFont(labelFont);
        advanceAmtLabel.setPreferredSize(new Dimension(100, 20));

        advanceAmtField = new JTextField(30);
        advanceAmtField.setFont(labelFont);

        paymentDescriptionLabel = new JLabel("Description :");
        paymentDescriptionLabel.setFont(labelFont);
        paymentDescriptionLabel.setPreferredSize(new Dimension(100, 20));

        paymentDescriptionField = new JTextField(36);
        paymentDescriptionField.setFont(labelFont);

        formPanel.add(empIdLabel);
        formPanel.add(empIdField);
        formPanel.add(empNameLabel);
        formPanel.add(empNames);
        formPanel.add(advanceAmtLabel);
        formPanel.add(advanceAmtField);
        formPanel.add(paymentDescriptionLabel);
        formPanel.add(paymentDescriptionField);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        buttonPanel.setBackground(Color.white);
        buttonPanel.setPreferredSize(new Dimension(1100, 40));

        add = new JButton("Add");
        add.setBackground(purple);
        add.setForeground(Color.white);
        add.addActionListener(this);
        add.setFont(buttonFont);

        exit = new JButton("Exit");
        exit.setBackground(red);
        exit.setForeground(Color.white);
        exit.addActionListener(this);
        exit.setFont(buttonFont);

        print = new JButton("Print");
        print.setForeground(Color.white);
        print.setBackground(lightBlue);
        print.setFont(buttonFont);
        print.addActionListener(this);

        buttonPanel.add(add);
        buttonPanel.add(print);
        buttonPanel.add(exit);

        formPanel.add(buttonPanel);
        contentPanel.add(formPanel);

        mainPanel.add(contentPanel);

        advanceAmtField.addKeyListener(new CustomKeyListener(add));

        // create table model with no one cell are editable.
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("Sr.No.");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Description");
        tableModel.addColumn("Date");

        tableModel.addTableModelListener(this);

        table = new JTable(tableModel);
        table.setFont(labelFont);
        table.setRowHeight(30);

        // add row sorter to table.
        sorter = new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(sorter);

        // modify the header of table here.
        JTableHeader header = table.getTableHeader();
        header.setFont(labelFont);
        header.setForeground(purple);
        header.setBackground(Color.white);

        scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setPreferredSize(new Dimension(610, 320));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // create table model with no one cell are editable.
        tableModel1 = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel1.addColumn("Sr.No.");
        tableModel1.addColumn("Amount");
        tableModel1.addColumn("Description");
        tableModel1.addColumn("Date");

        tableModel1.addTableModelListener(this);

        table1 = new JTable(tableModel1);
        table1.setFont(labelFont);
        table1.setRowHeight(30);

        // add row sorter to table.
        sorter = new TableRowSorter<TableModel>(tableModel1);
        table1.setRowSorter(sorter);

        // modify the header of table here.
        header = table1.getTableHeader();
        header.setFont(labelFont);
        header.setForeground(purple);
        header.setBackground(Color.white);

        scrollPane1 = new JScrollPane(table1);
        scrollPane1.getViewport().setBackground(Color.white);
        scrollPane1.setPreferredSize(new Dimension(610, 320));
        scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tablePanel.setBackground(Color.white);
        tablePanel.setPreferredSize(new Dimension(1240, 370));

        tableHeading1 = new JLabel("Receipt");
        tableHeading1.setFont(subHeadingFont);
        tableHeading2 = new JLabel("Payment");
        tableHeading2.setFont(subHeadingFont);

        tableHeading1.setPreferredSize(new Dimension(600, 20));
        tableHeading2.setPreferredSize(new Dimension(600, 20));

        tablePanel.add(tableHeading1);
        tablePanel.add(tableHeading2);
        tablePanel.add(scrollPane);
        tablePanel.add(scrollPane1);

        mainPanel.add(tablePanel);

        totalAdvAmtLabel = new JLabel("Total Amount :");
        // totalAdvAmtLabel.setPreferredSize(new Dimension(200, 20));
        totalAdvAmtLabel.setFont(labelFont);

        totalAdvAmtField = new JTextField(13);
        totalAdvAmtField.setFont(labelFont);
        totalAdvAmtField.setText("0");
        totalAdvAmtField.setEnabled(false);
        totalAdvAmtField.setDisabledTextColor(Color.darkGray);

        totalReceiptAmtLabel = new JLabel("Total Receipt Amount :");
        totalReceiptAmtLabel.setFont(labelFont);

        totalPaymentAmtLabel = new JLabel("Total Payment Amount :");
        totalPaymentAmtLabel.setFont(labelFont);

        totalReceiptAmtField = new JTextField(13);
        totalReceiptAmtField.setFont(labelFont);
        totalReceiptAmtField.setText("0");
        totalReceiptAmtField.setEnabled(false);
        totalReceiptAmtField.setDisabledTextColor(Color.darkGray);

        totalPaymentAmtField = new JTextField(13);
        totalPaymentAmtField.setFont(labelFont);
        totalPaymentAmtField.setText("0");
        totalPaymentAmtField.setEnabled(false);
        totalPaymentAmtField.setDisabledTextColor(Color.darkGray);

        mainPanel.add(totalReceiptAmtLabel);
        mainPanel.add(totalReceiptAmtField);
        mainPanel.add(totalAdvAmtLabel);
        mainPanel.add(totalAdvAmtField);
        mainPanel.add(totalPaymentAmtLabel);
        mainPanel.add(totalPaymentAmtField);

        add(mainPanel);
        itemStateChanged(null);
        setVisible(true);
        // GUI end here...

    }

    /**
     * This method are add employee list to combo box.
     */
    private void addEmployeesIntoList() {
        // create hash map with employee name and there id.
        empNameIdHashMap = new HashMap<String, Integer>();
        try {
            String query = "SELECT id,name FROM employees WHERE working_status = TRUE";
            ResultSet result = DBConnection.executeQuery(query);
            while (result.next()) {
                // put name and id into hash map.
                empNameIdHashMap.put(result.getString("name"), result.getInt("id"));
                empNames.addItem(result.getString("name"));
                // set the first employee id to id field.
                if (result.isFirst()) {
                    empIdField.setText(result.getString("id"));
                }
            }
        } catch (Exception e) {
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // check the source is add button or not.
        if (source.equals(add)) {
            try {
                boolean res = DialogWindow.showConfirmDialog(this,
                        "Are you sure to add this information,this will not remove or update!");
                if (res) {
                    // get the id of selected employee.
                    int id = Integer.parseInt(empIdField.getText());
                    // get the amount which is entered.
                    int amount = Integer.parseInt(advanceAmtField.getText());
                    // get the selected date.
                    String date = format.format(dateChooser.getDate());
                    String desc = paymentDescriptionField.getText();

                    // insert a new entry in the DB of this employee.
                    String query = "INSERT INTO prepayments(emp_id,amount,date,emp_wise_entry_id,description)VALUES(?,?,'"
                            + date
                            + "',?,?)";
                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                    pst.setInt(1, id);
                    pst.setInt(2, amount);
                    pst.setInt(3, srNo);
                    pst.setString(4, desc);

                    // execute query.
                    int affectedRows = pst.executeUpdate();
                    // check the row are inserted successfully or not.
                    if (affectedRows > 0) {
                        if (amount < 0) {
                            // add a new row into the table model.
                            tableModel.addRow(new Object[] { srNo++, amount, desc.length() > 0 ? desc : "-", date });
                        } else {
                            // add a new row into the table model.
                            tableModel1.addRow(new Object[] { srNo++, amount, desc.length() > 0 ? desc : "-", date });
                        }
                    }
                }

            } catch (Exception exc) {
                DialogWindow.showErrorDialog(this,
                        "Invalid value of amount \nOR\n Employee not found \nOR\n Connection error,try again!");
            }
        }

        // check the source is exit button or not.
        if (source.equals(exit)) {
            // get the confirmation from user before exit.
            boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to exit from here..!");
            if (res) {
                // exit when true.
                homeMenuItem.doClick();
            }
        }

        // check the source is print button or not.
        if (source.equals(print)) {
            // A directory path where pdf will store/save.
            String path = "C:\\kk_enterprises\\prepayment_reports";

            // a list to hold the headers/fields of pdf table.
            java.util.List<String> headers = Arrays.asList("Sr.No.", "Amount", "Description", "Date");

            // declare a list to hold a single record.
            java.util.List<String> list = null;

            // create a array list to hold the multiple list of records.
            java.util.List<java.util.List<String>> data = new ArrayList<java.util.List<String>>();

            // get number of rows of each table model.
            int rows1 = tableModel1.getRowCount();
            int rows = tableModel.getRowCount();

            int cnt1 = table1.getRowSorter().getViewRowCount();
            int cnt = table.getRowSorter().getViewRowCount();
            // for serial number.
            int k = 1;
            // loop on each row of table model 1.
            for (int j = 0; j < cnt1; j++) {
                int i = table1.getRowSorter().convertRowIndexToModel(j);
                // get the values of each cell of each row.
                String sr_no = Integer.toString(k++);
                // check description no empty if yes then "-" will assign.
                String description = tableModel1.getValueAt(i, 2).toString().length() > 0
                        ? tableModel1.getValueAt(i, 2).toString()
                        : "-";
                String amount = tableModel1.getValueAt(i, 1).toString();
                String date = tableModel1.getValueAt(i, 3).toString();

                // create a new array list to hold a record.
                list = new ArrayList<String>();
                list.add(sr_no);
                list.add(amount);
                list.add(description);
                list.add(date);

                // add array list to main list.
                data.add(list);
            }
            // loop on each row of table model 2.
            for (int j = 0; j < cnt; j++) {
                int i = table.getRowSorter().convertRowIndexToModel(j);
                // get the values of each cell of each row.
                String sr_no = Integer.toString(k++);
                // check description no empty if yes then "-" will assign.
                String description = tableModel.getValueAt(i, 2).toString().length() > 0
                        ? tableModel.getValueAt(i, 2).toString()
                        : "-";
                String amount = tableModel.getValueAt(i, 1).toString();
                String date = tableModel.getValueAt(i, 3).toString();

                // create a new array list to hold a record.
                list = new ArrayList<String>();
                list.add(sr_no);
                list.add(amount);
                list.add(description);
                list.add(date);

                // add array list to main list.
                data.add(list);
            }

            // check any one table model have at least single row to generate pdf.
            if (rows > 0 || rows1 > 0) {
                // create a instance of PDFMaker for generating a pdf file with given data.
                PDFMaker pdfMaker = new PDFMaker("Prepayment Report", empNames.getSelectedItem().toString(), headers,
                        data,
                        path, true, null, null, true, "Amount");
                // call method to generate a actual pdf file with given data and get the path
                // where pdf are generated.
                String generatedPath = pdfMaker.generatePDF();
                // check path are not null means pdf are created successfully.
                if (generatedPath != null) {
                    DialogWindow.showMessageDialog(this,
                            "Prepayment Report Generated Successfully at: \n" + generatedPath);
                } else {
                    DialogWindow.showWarningDialog(this,
                            "Prepayment Report cannot generate at: \n" + path);
                }
            } else {
                // execute when no records found.
                DialogWindow.showErrorDialog(this, "No records for print!");
            }
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (empNames.getSelectedIndex() == -1)
            return;
        // get the name of selected employee.
        String name = empNames.getSelectedItem().toString();
        // get the id of selected employee.
        int id = empNameIdHashMap.get(name);
        // set the id of employee to id text field.
        empIdField.setText(Integer.toString(id));
        // remove previous rows from table model.
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        while (tableModel1.getRowCount() > 0) {
            tableModel1.removeRow(0);
        }
        try {
            // status for data are found or not.
            boolean status = false;
            String query = "SELECT * FROM prepayments WHERE emp_id =" + id;
            ResultSet result = DBConnection.executeQuery(query);
            // loop when at least one row are found.
            while (result.next()) {
                // get data from result set.
                id = result.getInt("emp_wise_entry_id");
                int amt = result.getInt("amount");
                String date = result.getString("date");
                String desc = result.getString("description");

                if (amt < 0) {
                    // add record into table model.
                    tableModel.addRow(new Object[] { id, amt, desc, date });
                } else {
                    // add record into table model.
                    tableModel1.addRow(new Object[] { id, amt, desc, date });
                }
                // check the entry is last or not.
                if (result.isLast()) {
                    // set the new sr.no.
                    srNo = id + 1;
                    status = true;
                }
            }

            // false when don't have record of particular employee then sr.no will set to 1.
            if (!status) {
                srNo = 1;
            }
            query = "SELECT * FROM salaries WHERE emp_id = ? AND extract(MONTH FROM payment_month) = ? AND extract(YEAR FROM payment_month) = ?";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            // set a current date to calendar
            calendar.setTime(new Date());
            // back to exact 1 month.
            calendar.add(Calendar.MONTH, -1);
            // get the modified date.
            String date[] = format.format(calendar.getTime()).split("\\-");
            // get month and year.
            int month = Integer.parseInt(date[1]);
            int year = Integer.parseInt(date[0]);
            // set month and year to query.
            pst.setInt(1, id);
            pst.setInt(2, month);
            pst.setInt(3, year);
            // execute a query.
            result = pst.executeQuery();

            /*
             * check a employee have previous month payroll entry if yes then a minimum
             * selectable date will set to next month of it.
             */
            // set date to first day of the current month.
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            /*
             * check have a payroll of previous month ,if yes then set a minimum selectable
             * date to first day of current month.
             */
            if (result.next()) {
                dateChooser.setMinSelectableDate(calendar.getTime());
            } else {
                /*
                 * execute when employee don't have previous payroll entry then a minimum
                 * selectable date set to in which date he/she is join.
                 */
                query = "SELECT DATE(updated_at) FROM employees WHERE id =" + id;
                result = DBConnection.executeQuery(query);
                if (result.next()) {
                    dateChooser.setMinSelectableDate(new Date(result.getDate("date").getTime()));
                } else {
                    dateChooser.setMinSelectableDate(calendar.getTime());
                }
            }
            calendar.setTime(new Date());
        } catch (Exception exc) {
            // execute when an exception in query.
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }
    }

    /**
     * This method are calculate the total amount of all entries.
     */
    private void calculate(Object source) {
        if (source.equals(tableModel1)) {
            // get row count of table model.
            int rows = tableModel1.getRowCount();

            int totalAmount = 0;
            // add amount one by one.
            for (int i = 0; i < rows; i++) {
                // get value of amount from entry.
                int val = Integer.parseInt(tableModel1.getValueAt(i, 1).toString());
                // add it into total amount.
                totalAmount = totalAmount + val;
            }
            totalPaymentAmtField.setText(Integer.toString(totalAmount));
        }

        if (source.equals(tableModel)) {
            // get row count of table model.
            int rows = tableModel.getRowCount();

            int totalAmount = 0;
            // add amount one by one.
            for (int i = 0; i < rows; i++) {
                // get value of amount from entry.
                int val = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                // add it into total amount.
                totalAmount = totalAmount + val;
            }
            if (totalAmount != 0) {
                /*
                 * remove a negative /minus sign from amount.the amount is negative but i just
                 * show it in positive format.
                 */
                totalReceiptAmtField.setText(Integer.toString(totalAmount).split("\\-")[1]);
            } else {
                totalReceiptAmtField.setText("0");
            }
        }

        int prevPaymentAmt = Integer.parseInt(totalPaymentAmtField.getText());
        int prevReceiptAmt = Integer.parseInt(totalReceiptAmtField.getText());

        // set the total amount of all entries in the totalAdvAmtField.
        totalAdvAmtField.setText(Integer.toString(prevPaymentAmt - prevReceiptAmt));

    }

    public void tableChanged(TableModelEvent e) {
        // recalculate total amount when any row are inserted or deleted in table.
        calculate(e.getSource());
    }
}
/**
 * This component end here...
 */