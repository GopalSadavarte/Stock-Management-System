/**
 * 
 * 
 * 
 * This component will generate an employee payroll with there attendance and there 
 * advance amounts.
 * 
 * 
 * 
 */

package components.utilities;

import javax.swing.*;
import com.toedter.calendar.*;
import components.abstracts.AbstractButton;
import components.report.generate.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import config.*;
import partial.*;
import partial.event.*;

/**
 * The EmployeePayroll are generate the payroll with GUI and also generate a pdf
 * file of particular payroll.
 */
public class EmployeePayroll extends AbstractButton implements ItemListener, PropertyChangeListener {

    // instance variables.
    JMenuItem homeMenuItem, payrollMenu;
    JPanel headingPanel, mainPanel, contentPanel, datePanel, pairHolders[];
    JLabel heading, dateLabel, nameLabel, basicSalaryLabel, advanceAmtLabel, depositAmtLabel, pendingAmtLabel,
            overtimeAmtLabel, payableAmtLabel, payrollStatusLabel,
            presentDaysLabel, absentDaysLabel, halfDaysLabel, overtimeHoursLabel, rateOfPerHourLabel;
    JTextField basicSalaryField, advanceAmtField, presentDaysField, absentDaysField, halfDaysField, overtimeHoursField,
            overtimeAmtField, rateOfPerHourField, pendingAmtField, depositAmtField, payableAmtField;
    JComboBox<String> empNameList;
    JDateChooser dateChooser;
    final Integer SAVE_ACTION = 1, UPDATE_ACTION = 0;
    final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"));
    Integer action = SAVE_ACTION;
    HashMap<String, Integer> empNameIdHashMap;
    int basicSalaryVal = 0, rateOfPerHour = 0, depositAmtVal = 0;
    int previousDepositAmountForDelete = 0;

    /**
     * This constructor are create an GUI to handle the employee payroll.
     * 
     * @param homeMenu the reference of home menu.
     * @param current  the reference of current menu.
     */
    public EmployeePayroll(JMenuItem homeMenu, JMenuItem current) {
        // create button panel with new,delete and exit option only.
        super(1, 0, 0, null);
        // references for navigation.
        homeMenuItem = homeMenu;
        payrollMenu = current;
        // GUI start here..
        setLayout(new FlowLayout());

        mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.setPreferredSize(new Dimension(1250, 600));
        mainPanel.setBorder(border);
        mainPanel.setBackground(Color.white);

        headingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        headingPanel.setPreferredSize(new Dimension(1240, 70));
        headingPanel.setBorder(border);
        headingPanel.setBackground(Color.white);

        payrollStatusLabel = new JLabel();
        payrollStatusLabel.setFont(labelFont);
        payrollStatusLabel.setPreferredSize(labelSize);
        payrollStatusLabel.setVerticalAlignment(SwingConstants.CENTER);

        heading = new JLabel("Employee Payroll");
        heading.setForeground(Color.darkGray);
        heading.setFont(headingFont);
        heading.setVerticalAlignment(SwingConstants.CENTER);

        datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        datePanel.setPreferredSize(new Dimension(500, 50));
        datePanel.setBackground(Color.white);

        dateLabel = new JLabel("Date :");
        dateLabel.setFont(labelFont);
        dateLabel.setPreferredSize(new Dimension(100, 30));
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dateLabel.setForeground(Color.darkGray);
        dateLabel.setVerticalAlignment(SwingConstants.CENTER);

        // set the current date to calendar.
        calendar.setTime(new Date());
        dateChooser = new JDateChooser(calendar.getTime());
        dateChooser.setMaxSelectableDate(calendar.getTime());
        dateChooser.setPreferredSize(new Dimension(250, 30));
        dateChooser.setFont(labelFont);
        dateChooser.addPropertyChangeListener("date", this);

        datePanel.add(dateLabel);
        datePanel.add(dateChooser);

        headingPanel.add(payrollStatusLabel);
        headingPanel.add(heading);
        headingPanel.add(datePanel);

        mainPanel.add(headingPanel);

        contentPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        contentPanel.setPreferredSize(new Dimension(1100, 450));
        contentPanel.setBorder(border);
        contentPanel.setBackground(Color.white);

        pairHolders = new JPanel[12];

        Dimension labelSize = new Dimension(140, 20);

        for (int i = 0; i < 12; i++) {
            pairHolders[i] = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            pairHolders[i].setBackground(Color.white);
            pairHolders[i].setPreferredSize(new Dimension(300, 50));
        }

        nameLabel = new JLabel("Name :");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(Color.darkGray);
        nameLabel.setPreferredSize(labelSize);

        empNameList = new JComboBox<String>();
        empNameList.setPreferredSize(new Dimension(270, 30));
        empNameList.setFont(labelFont);
        addEmployeesIntoList();
        empNameList.addItemListener(this);

        pairHolders[0].add(nameLabel);
        pairHolders[0].add(empNameList);

        basicSalaryLabel = new JLabel("Basic Salary :");
        basicSalaryLabel.setFont(labelFont);
        basicSalaryLabel.setForeground(Color.darkGray);
        basicSalaryLabel.setPreferredSize(labelSize);

        basicSalaryField = new JTextField(20);
        basicSalaryField.setFont(labelFont);
        basicSalaryField.addKeyListener(this);

        pairHolders[1].add(basicSalaryLabel);
        pairHolders[1].add(basicSalaryField);

        presentDaysLabel = new JLabel("Present Days :");
        presentDaysLabel.setFont(labelFont);
        presentDaysLabel.setForeground(Color.darkGray);
        presentDaysLabel.setPreferredSize(labelSize);

        presentDaysField = new JTextField(20);
        presentDaysField.setFont(labelFont);
        presentDaysField.setEnabled(false);
        presentDaysField.setDisabledTextColor(Color.darkGray);

        pairHolders[6].add(presentDaysLabel);
        pairHolders[6].add(presentDaysField);

        absentDaysLabel = new JLabel("Absent Days :");
        absentDaysLabel.setFont(labelFont);
        absentDaysLabel.setForeground(Color.darkGray);
        absentDaysLabel.setPreferredSize(labelSize);

        absentDaysField = new JTextField(20);
        absentDaysField.setFont(labelFont);
        absentDaysField.setEnabled(false);
        absentDaysField.setDisabledTextColor(Color.darkGray);

        pairHolders[9].add(absentDaysLabel);
        pairHolders[9].add(absentDaysField);

        halfDaysLabel = new JLabel("Half Days :");
        halfDaysLabel.setFont(labelFont);
        halfDaysLabel.setForeground(Color.darkGray);
        halfDaysLabel.setPreferredSize(labelSize);

        halfDaysField = new JTextField(20);
        halfDaysField.setFont(labelFont);
        halfDaysField.setEnabled(false);
        halfDaysField.setDisabledTextColor(Color.darkGray);

        pairHolders[4].add(halfDaysLabel);
        pairHolders[4].add(halfDaysField);

        overtimeHoursLabel = new JLabel("Overtime Hours :");
        overtimeHoursLabel.setFont(labelFont);
        overtimeHoursLabel.setForeground(Color.darkGray);
        overtimeHoursLabel.setPreferredSize(labelSize);

        overtimeHoursField = new JTextField(20);
        overtimeHoursField.setFont(labelFont);
        overtimeHoursField.setEnabled(false);
        overtimeHoursField.setDisabledTextColor(Color.darkGray);

        pairHolders[5].add(overtimeHoursLabel);
        pairHolders[5].add(overtimeHoursField);

        rateOfPerHourLabel = new JLabel("Rate Of Hour:");
        rateOfPerHourLabel.setFont(labelFont);
        rateOfPerHourLabel.setForeground(Color.darkGray);
        rateOfPerHourLabel.setPreferredSize(labelSize);

        rateOfPerHourField = new JTextField(20);
        rateOfPerHourField.setFont(labelFont);
        rateOfPerHourField.addKeyListener(this);

        pairHolders[2].add(rateOfPerHourLabel);
        pairHolders[2].add(rateOfPerHourField);

        overtimeAmtLabel = new JLabel("Overtime Amount :");
        overtimeAmtLabel.setFont(labelFont);
        overtimeAmtLabel.setForeground(Color.darkGray);
        overtimeAmtLabel.setPreferredSize(labelSize);

        overtimeAmtField = new JTextField(20);
        overtimeAmtField.setFont(labelFont);
        overtimeAmtField.setEnabled(false);
        overtimeAmtField.setDisabledTextColor(Color.darkGray);

        pairHolders[7].add(overtimeAmtLabel);
        pairHolders[7].add(overtimeAmtField);

        advanceAmtLabel = new JLabel("Advance Amount :");
        advanceAmtLabel.setFont(labelFont);
        advanceAmtLabel.setForeground(Color.darkGray);
        advanceAmtLabel.setPreferredSize(labelSize);

        advanceAmtField = new JTextField(20);
        advanceAmtField.setFont(labelFont);
        advanceAmtField.setEnabled(false);
        advanceAmtField.setDisabledTextColor(Color.darkGray);

        pairHolders[8].add(advanceAmtLabel);
        pairHolders[8].add(advanceAmtField);

        depositAmtLabel = new JLabel("Deposit Amount :");
        depositAmtLabel.setFont(labelFont);
        depositAmtLabel.setForeground(Color.darkGray);
        depositAmtLabel.setPreferredSize(labelSize);

        depositAmtField = new JTextField(20);
        depositAmtField.setFont(labelFont);
        depositAmtField.addKeyListener(this);
        depositAmtField.setDisabledTextColor(Color.darkGray);

        pairHolders[3].add(depositAmtLabel);
        pairHolders[3].add(depositAmtField);

        pendingAmtLabel = new JLabel("Pending Amount :");
        pendingAmtLabel.setFont(labelFont);
        pendingAmtLabel.setForeground(Color.darkGray);
        pendingAmtLabel.setPreferredSize(labelSize);

        pendingAmtField = new JTextField(20);
        pendingAmtField.setFont(labelFont);
        pendingAmtField.setEnabled(false);
        pendingAmtField.setDisabledTextColor(Color.darkGray);

        pairHolders[10].add(pendingAmtLabel);
        pairHolders[10].add(pendingAmtField);

        payableAmtLabel = new JLabel("Payable Amount :");
        payableAmtLabel.setFont(labelFont);
        payableAmtLabel.setForeground(Color.darkGray);
        payableAmtLabel.setPreferredSize(labelSize);

        payableAmtField = new JTextField(20);
        payableAmtField.setFont(labelFont);
        payableAmtField.setEnabled(false);
        payableAmtField.setDisabledTextColor(Color.darkGray);

        pairHolders[11].add(payableAmtLabel);
        pairHolders[11].add(payableAmtField);

        for (int i = 0; i < 12; i++) {
            contentPanel.add(pairHolders[i]);
        }

        basicSalaryField.addKeyListener(new CustomKeyListener(rateOfPerHourField));
        rateOfPerHourField.addKeyListener(new CustomKeyListener(depositAmtField));

        mainPanel.add(contentPanel);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
        mainPanel.add(buttonPanel);

        add(mainPanel);
        setBackground(Color.white);
        setVisible(true);
        // GUI end here..
        itemStateChanged(null);
    }

    /**
     * This method are get the data of employees from DB and set the id-name in
     * key-value pair in the Hash Map.
     */
    private void addEmployeesIntoList() {
        // create a instance of HashMap.
        empNameIdHashMap = new HashMap<String, Integer>();
        try {
            // get and set the emp id and there name.
            String query = "SELECT * FROM employees";
            ResultSet result = DBConnection.executeQuery(query);
            // loop one by one and set it.
            while (result.next()) {
                empNameIdHashMap.put(result.getString("name"), result.getInt("id"));
                empNameList.addItem(result.getString("name"));
            }
        } catch (Exception e) {
            // execute when exception are occur.
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }
    }

    /**
     * This method are insert a new record into employee advance table(Prepayments
     * table).
     * 
     * @param emp_id      the value of employee id .
     * @param positiveVal the true value when the inserted amount set as positive
     *                    value means the advance have give us otherwise false.
     * @return int the number of affected rows.
     * @throws Exception when error in query execution.
     */
    private int insertIntoPrepayments(int emp_id, boolean positiveVal) throws Exception {
        int affectedRows = 0;

        // query that select the last entry number.
        String query = "SELECT emp_wise_entry_id FROM prepayments WHERE emp_id = " + emp_id
                + " ORDER BY emp_wise_entry_id DESC LIMIT 1";
        ResultSet result = DBConnection.executeQuery(query);
        // set a new date to calender which selected by user.
        calendar.setTime(dateChooser.getDate());
        // get last date of selected date of month.
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        // get selected date.
        String date = format.format(dateChooser.getDate());
        // convert it to string array.
        String d[] = date.split("\\-");
        // set last date of month when employee deposit a amount of that particular
        // payroll month.
        date = d[0] + "-" + d[1] + "-" + lastDay;
        // query that insert a new record.
        query = "INSERT INTO prepayments(emp_id,amount,date,emp_wise_entry_id,description)VALUES(?,?,'"
                + date + "',?,?)";
        int newEntryId = 1;
        // check the last entry found or not.
        if (result.next()) {
            // set the new entry number.
            newEntryId = result.getInt("emp_wise_entry_id") + 1;
        }
        // crating the prepare statement.
        PreparedStatement pst = DBConnection.con.prepareStatement(query);
        pst.setInt(1, emp_id);
        // check the positiveVal is true or false.
        if (positiveVal) {
            // set the previous deposit value as it is.
            pst.setInt(2, previousDepositAmountForDelete);
            pst.setString(4, "The deposit amount have restore, because the payroll are deleted.");
        } else {
            // set the amount in minus means the value is submitted.
            pst.setInt(2, Integer.parseInt("-" + depositAmtVal));
            pst.setString(4, "The advance amount are submitted by employee.");
        }
        // set the entry number.
        pst.setInt(3, newEntryId);

        // execute the query and get affected rows.
        affectedRows = pst.executeUpdate();
        // again set new current date to calender.
        calendar.setTime(new Date());
        // return the affected rows.
        return affectedRows;
    }

    public void propertyChange(PropertyChangeEvent e) {
        // set the data according to selected date .
        itemStateChanged(null);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // check the source is new button or not.
        if (source.equals(newButton)) {
            payrollMenu.doClick();
        }
        // check the source is delete button or not.
        if (source.equals(delete)) {
            // Get the confirmation before delete the payroll.
            boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to remove this information!");
            // check the confirmation is granted or not.
            if (res) {
                // get the id of selected employee from HashMap by there name.
                int emp_id = empNameIdHashMap.get(empNameList.getSelectedItem().toString());
                // get and split the selected date into string[]
                String date[] = format.format(dateChooser.getDate()).split("\\-");
                // get the month value from date array.
                int month = Integer.parseInt(date[1]);
                // get the year from date array.
                int year = Integer.parseInt(date[0]);
                try {
                    // delete the record from vouchers table where emp_id and month and year are
                    // matched.
                    String query = "DELETE FROM vouchers WHERE emp_id = ? AND extract(MONTH FROM date) = ? AND extract(YEAR FROM date) = ? AND status = 'Paid'";
                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                    pst.setInt(1, emp_id);
                    pst.setInt(2, month);
                    pst.setInt(3, year);

                    // delete from vouchers table.
                    int affectedRows2 = pst.executeUpdate();
                    // Delete the data from salaries table.here extract() is a method of postgresql
                    // database that will get the month,year,day from the particular date.
                    query = "DELETE FROM salaries WHERE emp_id = ? AND extract(MONTH FROM payment_month) = ? AND extract(YEAR FROM payment_month) = ?";
                    pst = DBConnection.con.prepareStatement(query);
                    pst.setInt(1, emp_id);
                    pst.setInt(2, month);
                    pst.setInt(3, year);

                    // delete the data where emp_id and particular month and year are matched.
                    int affectedRows1 = pst.executeUpdate();

                    int affectedRows3 = 0;
                    // check the previous deposit amount is greater than 0 or not.
                    if (previousDepositAmountForDelete > 0) {
                        // restore the deposit amount as a advance amount into prepayments table.
                        affectedRows3 = insertIntoPrepayments(emp_id, true);
                        // check the record insert success or not.
                        if (affectedRows3 == 0) {
                            DialogWindow.showMessageDialog(this,
                                    "The deposit cannot restore ,please add prepayment entry for this deposit!");
                        }
                    }

                    // check the both salaries and vouchers entries are deleted successfully or not.
                    if (affectedRows1 > 0 && affectedRows2 > 0) {
                        DialogWindow.showMessageDialog(this, "The Payroll removed successfully!");
                        payrollMenu.doClick();
                    } else {
                        // execute when any one of this entry are not executed successfully.
                        DialogWindow.showMessageDialog(this, "Failed!");
                    }

                } catch (Exception exc) {
                    // execute when the exception are occur in above code.
                    DialogWindow.showErrorDialog(this, "Connection error,try again!");
                }

            }
        }

        // check the source is exit button or not.
        if (source.equals(exit)) {
            // get the confirmation before exit from window.
            boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to exit from here...!");
            if (res) {
                homeMenuItem.doClick();
            }
        }
        // check the source is save button or not.
        if (source.equals(save)) {
            if (empNameIdHashMap.size() <= 0) {
                DialogWindow.showMessageDialog(this, "Employee not found!");
                return;
            }
            // get the pending amount.
            int pendingAmt = Integer.parseInt(pendingAmtField.getText());
            // check the pending amount not in minus.
            if (pendingAmt >= 0) {
                // check the action is SAVE_ACTION.
                if (action.equals(SAVE_ACTION)) {
                    double payableAmt = Double.parseDouble(payableAmtField.getText());
                    // check the payable amount not should be zero.
                    if (payableAmt > 0) {
                        // get the values from text fields.
                        int advanceAmt = Integer.parseInt(pendingAmtField.getText());
                        String paymentDate = format.format(dateChooser.getDate());
                        double overtimeHours = Double.parseDouble(overtimeHoursField.getText());
                        String empName = empNameList.getSelectedItem().toString();
                        // get the id of employee from there name.
                        int emp_id = empNameIdHashMap.get(empName);

                        try {
                            // query to insert a new entry into salaries table.
                            String query = "INSERT INTO salaries (emp_id,basic_salary,advance_amt,created_at,updated_at,payment_month,deposit_amount,overtime_hours,rate_of_per_hour)VALUES(?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'"
                                    + paymentDate + "',?,?,?)";
                            PreparedStatement pst = DBConnection.con.prepareStatement(query);
                            pst.setInt(1, emp_id);
                            pst.setInt(2, basicSalaryVal);
                            pst.setInt(3, advanceAmt);
                            pst.setInt(4, depositAmtVal);
                            pst.setDouble(5, overtimeHours);
                            pst.setInt(6, rateOfPerHour);

                            // execute the query and get affected rows.
                            int affectedRows1 = pst.executeUpdate();
                            int salaries_id = 0;
                            if (affectedRows1 > 0) {
                                // this query to get entry id which initially save.
                                query = "SELECT id FROM salaries ORDER BY id DESC LIMIT 1";
                                ResultSet resultSet = DBConnection.executeQuery(query);
                                if (resultSet.next())
                                    /*
                                     * get last entry id for set a reference of foreign key to vouchers table when
                                     * insert a record in it (see line 541,where this id are use).
                                     */
                                    salaries_id = resultSet.getInt("id");
                            }

                            int affectedRows2 = 0;
                            // check the deposit amount not should be zero.
                            if (depositAmtVal > 0) {
                                /*
                                 * insert a new entry in prepayments table with negative value,because this
                                 * amount will be reduce from there actual advance amount(the employee have
                                 * to return this amount to admin.).
                                 */
                                affectedRows2 = insertIntoPrepayments(emp_id, false);
                                // check the record inserted successfully or not.
                                if (affectedRows2 == 0) {
                                    DialogWindow.showMessageDialog(this,
                                            "The deposit cannot restore ,please add prepayment entry for this deposit!");
                                }
                            }

                            // this query insert a new record into vouchers table.
                            query = "INSERT INTO vouchers (status,payment_state,description,amount,user_id,emp_id,date,created_at,updated_at,salaries_id)VALUES(?,?,?,?,101,?,'"
                                    + paymentDate + "',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?)";
                            pst = DBConnection.con.prepareStatement(query);
                            pst.setString(1, "Paid");
                            pst.setString(2, "Success");
                            pst.setString(3, "Paid salary");
                            pst.setDouble(4, payableAmt);
                            pst.setInt(5, emp_id);
                            pst.setInt(6, salaries_id);

                            // execute query and get the affected rows.
                            int affectedRows3 = pst.executeUpdate();

                            // check the both records are inserted successfully.
                            if (affectedRows1 > 0 && affectedRows3 > 0) {

                                // directory path where pdf file will store/save.
                                String path = "C:\\kk_enterprises\\payrolls";
                                // call method to create a pdf.
                                String generatedPath = generatePDF(empName, path, false);

                                // check a path not null that means a file are created successfully.
                                if (generatedPath != null) {
                                    DialogWindow.showMessageDialog(this,
                                            "Payroll Generated Successfully at: \n" + generatedPath);
                                } else {
                                    DialogWindow.showWarningDialog(this,
                                            "Payroll Generated but PDF not create at: \n" + path);
                                }
                                payrollMenu.doClick();
                            } else {
                                // else display failed message.
                                DialogWindow.showMessageDialog(this, "Failed!");
                            }
                        } catch (Exception exc) {
                            // execute when exception in above code.
                            DialogWindow.showErrorDialog(this, "Connection error,try again!");
                        }
                    } else {
                        // execute when the payable amount is zero.
                        DialogWindow.showErrorDialog(this, "The payable amount not should be zero!");
                    }
                }

                // check the action is UPDATE_ACTION or not.
                if (action.equals(UPDATE_ACTION)) {
                    // get the confirmation before update.
                    boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to update this information!");
                    if (res) {
                        double payableAmt = Double.parseDouble(payableAmtField.getText());
                        // check the payable amount not should be zero.
                        if (payableAmt > 0) {
                            // get the data from text fields.
                            int advanceAmt = Integer.parseInt(pendingAmtField.getText());
                            // get the date and convert it to array.
                            String[] paymentDate = format.format(dateChooser.getDate()).split("\\-");
                            // get the month and year.
                            int month = Integer.parseInt(paymentDate[1]);
                            int year = Integer.parseInt(paymentDate[0]);
                            double overtimeHours = Double.parseDouble(overtimeHoursField.getText());
                            // get the selected name.
                            String empName = empNameList.getSelectedItem().toString();
                            // get the emp id from selected name.
                            int emp_id = empNameIdHashMap.get(empName);

                            try {
                                // query that update the salaries record of current employee.
                                String query = "UPDATE salaries SET basic_salary=?,advance_amt=?,updated_at=CURRENT_TIMESTAMP,deposit_amount=?,overtime_hours=?,rate_of_per_hour=? WHERE emp_id = ? AND extract(MONTH FROM payment_month) = ? AND extract(YEAR FROM payment_month) = ?";
                                PreparedStatement pst = DBConnection.con.prepareStatement(query);
                                pst.setInt(1, basicSalaryVal);
                                pst.setInt(2, advanceAmt);
                                pst.setInt(3, depositAmtVal);
                                pst.setDouble(4, overtimeHours);
                                pst.setInt(5, rateOfPerHour);
                                pst.setInt(6, emp_id);
                                pst.setInt(7, month);
                                pst.setInt(8, year);

                                // execute the query and get affected rows.
                                int affectedRows1 = pst.executeUpdate();

                                // query to update the vouchers record.
                                query = "UPDATE vouchers SET amount=?,updated_at=CURRENT_TIMESTAMP WHERE emp_id=? AND extract(MONTH FROM date) = ? AND extract(YEAR FROM date) = ?";
                                pst = DBConnection.con.prepareStatement(query);

                                pst.setDouble(1, payableAmt);
                                pst.setInt(2, emp_id);
                                pst.setInt(3, month);
                                pst.setInt(4, year);

                                // execute and get affected rows.
                                int affectedRows3 = pst.executeUpdate();

                                // check the records are inserted successfully or not.
                                if (affectedRows1 >= 0 && affectedRows3 >= 0) {
                                    // A directory path where pdf file will save/store.
                                    String path = "C:\\kk_enterprises\\payrolls";
                                    // call method to create a pdf.
                                    String generatedPath = generatePDF(empName, path, true);

                                    // check path not null means a pdf are successfully created at specified path.
                                    if (generatedPath != null) {
                                        DialogWindow.showMessageDialog(this,
                                                "Payroll Generated Successfully at:\n" + generatedPath);
                                    } else {
                                        DialogWindow.showMessageDialog(this,
                                                "Payroll Generated but PDF not create at:\n" + path);
                                    }
                                    payrollMenu.doClick();
                                } else {
                                    // execute when any of one are not inserted.
                                    DialogWindow.showMessageDialog(this, "Failed!");
                                }
                            } catch (Exception exc) {
                                // execute when exception in above code.
                                DialogWindow.showErrorDialog(this, "Connection error,try again!");
                            }
                        } else {
                            // execute when the payable amount is zero.
                            DialogWindow.showErrorDialog(this, "The payable amount not should be zero!");
                        }
                    }
                }
            } else {
                // execute when pending amount is negative value.
                DialogWindow.showErrorDialog(this, "The pending amount not should be negative !");
            }
        }
    }

    /**
     * This method are get the data to generate a pdf file.
     * 
     * @param empName  a name of employee who's payroll will create.
     * @param path     a directory path where payroll pdf will save.
     * @param isUpdate boolean true value if existing pdf in update or just have a
     *                 another copy of same payroll otherwise false if it is a new
     *                 payroll entry.
     * @return java.lang.String - a path where pdf are generate.
     */
    private String generatePDF(String empName, String path, boolean isUpdate) {
        // get values of each text field.
        String presentDays = presentDaysField.getText();
        String absentDays = absentDaysField.getText();
        String halfDays = halfDaysField.getText();
        String overtimeRate = Integer.toString(rateOfPerHour);
        String hours = overtimeHoursField.getText();
        String overtimeAmount = overtimeAmtField.getText();
        String depositAmount = Integer.toString(depositAmtVal);
        String advanceAmount = advanceAmtField.getText();
        String payableAmount = payableAmtField.getText();
        String pending = pendingAmtField.getText();
        String basicSalary = Integer.toString(basicSalaryVal);
        // set month of payroll with format exp.,February 2025.
        String month = new SimpleDateFormat("MMMM yyyy").format(dateChooser.getDate());

        // create a instance of PDFPayrollMaker to generate a pdf file with given data.
        PDFPayrollMaker payrollMaker = new PDFPayrollMaker(empName, basicSalary, presentDays, halfDays,
                absentDays, overtimeRate, hours, overtimeAmount, advanceAmount, depositAmount, pending,
                payableAmount, month, path, isUpdate);

        // call method of PDFPayrollMaker to generate a actual pdf file with given data.
        String generatedPath = payrollMaker.generatePDF();
        // return a path where pdf are generate.
        return generatedPath;
    }

    public void keyReleased(KeyEvent e) {
        String key = KeyEvent.getKeyText(e.getKeyCode());
        // check the typed key is "Enter" or not.
        if (key.equals("Enter")) {
            // calculate the all salary.
            calculateSalary();
        }
    }

    /**
     * This method are calculate the entire salary of employee.
     */
    private void calculateSalary() {
        try {
            // get the values from text fields.
            int basicSalary = Integer.parseInt(basicSalaryField.getText());
            int perHourRate = Integer.parseInt(rateOfPerHourField.getText());
            int deposit = Integer.parseInt(depositAmtField.getText());
            // get the number of days in current month.
            int noOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            /*
             * check the basic salary should be greater than zero and also deposit amount
             * and per hour rate is zero or greater.
             */
            if (basicSalary > 0 && deposit >= 0 && perHourRate >= 0) {
                int advAmt = Integer.parseInt(advanceAmtField.getText());
                if (deposit <= advAmt) {
                    // get the values from text fields.
                    int halfDays = Integer.parseInt(halfDaysField.getText());
                    int presentDays = Integer.parseInt(presentDaysField.getText());
                    double overtimeHours = Double.parseDouble(overtimeHoursField.getText());

                    // calculate the salary.
                    int perDayAmt = basicSalary / noOfDays;
                    int presentAmt = perDayAmt * presentDays;
                    int halfDayAmt = (perDayAmt / 2) * halfDays;
                    double overtimeAmt = overtimeHours * perHourRate;
                    double total = presentAmt + halfDayAmt + overtimeAmt;
                    // check the pending amount should be greater than zero then it will set
                    // otherwise set to zero.
                    int pendingAmt = (advAmt - deposit) > 0 ? advAmt - deposit : 0;
                    double payable = total - deposit;

                    // set the values after calculation.
                    overtimeAmtField.setText(Double.toString(overtimeAmt));
                    pendingAmtField.setText(Integer.toString(pendingAmt));
                    payableAmtField.setText(Double.toString(payable));

                    // set the basic salary,rate and deposit globally which are use when user click
                    // on save button to insert it.
                    basicSalaryVal = basicSalary;
                    rateOfPerHour = perHourRate;
                    depositAmtVal = deposit;
                } else {
                    // execute when deposit are greater then advance amount.
                    DialogWindow.showErrorDialog(this,
                            "Deposit amount not should be greater then advance amount!");
                }
            } else {
                // execute when basic salary,rate or deposit are invalid.
                DialogWindow.showErrorDialog(this,
                        "The basic salary is must be greater then Zero \n And Deposit amount and per hour rate allow 0 or greater!");
            }
        } catch (Exception e) {
            // execute when an error are occur when parse the values.
            DialogWindow.showErrorDialog(this,
                    "The basic salary,rate of per hour and deposit amount have valid number!\n AND\n 0 are applicable when don't have deposit amount or rate of per hour!");
        }
    }

    /**
     * This method are calculate the total present,absent and half days and overtime
     * hours of employee.
     * 
     * @param month the month value of salary
     * @param year  the year value of salary
     * @param id    the id of employee
     * @return java.lang.Object[]
     */
    private Object[] calculateAttendance(int month, int year, int id) {
        // initial values set to 0.
        int presentCount = 0, absentCount = 0, halfDayCount = 0;
        double overtimeHours = 0;
        try {
            // query to get the attendance data of particular month of year.
            String query = "SELECT * FROM attendance WHERE extract(MONTH FROM attendance_date) = ? AND extract(YEAR FROM attendance_date) = ?";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setInt(1, month);
            pst.setInt(2, year);

            // execute the query.
            ResultSet resultSet = pst.executeQuery();

            // loop over result set.
            while (resultSet.next()) {
                // convert the status which is in string to multi dimensional array.
                String status[][] = convertStringToArray(resultSet.getString("status"));
                // loop over the status.
                for (int i = 1; i < status.length; i++) {
                    // select the employee id.
                    int emp_id = Integer.parseInt(status[i][0]);
                    // check the emp id is equal or not.
                    if (emp_id == id) {
                        // switch the status (present,absent or half day) and increment count
                        // accordingly.
                        switch (status[i][1].trim()) {
                            case "Present":
                                presentCount++;
                                break;
                            case "Absent":
                                absentCount++;
                                break;
                            case "HalfDay":
                                halfDayCount++;
                                break;
                        }

                        // check it have overtime hours or not if yes then calculate total hours.
                        if (!status[i][2].trim().equals("No")) {
                            // calculate total hours .
                            overtimeHours += Double.parseDouble(status[i][2].trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            // execute when exception occur in above code.
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }

        // return the entire attendance of month in array format.
        return new Object[] { presentCount, absentCount, halfDayCount, overtimeHours };
    }

    public void itemStateChanged(ItemEvent e) {
        try {
            if(empNameList.getSelectedItem() == null){
                DialogWindow.showMessageDialog(this, "No employee records found!");
                return;
            }
            boolean payrollStatus = false;
            // get the selected employee name.
            String name = empNameList.getSelectedItem().toString();
            // get the id of selected employee by there name.
            int id = empNameIdHashMap.get(name);
            // get and convert the selected date into array.
            String date[] = format.format(dateChooser.getDate()).split("\\-");
            // extract month and year.
            int month = Integer.parseInt(date[1]);
            int year = Integer.parseInt(date[0]);

            // set the selected date to the calendar.
            calendar.setTime(dateChooser.getDate());
            // get the number of days in month of selected date.
            int noOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            // query to get the record of salary of particular employee,month and year if
            // exist.
            String query = "SELECT * FROM salaries WHERE emp_id = ? AND extract(MONTH FROM payment_month) = ? AND extract(YEAR FROM payment_month) = ?";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setInt(1, id);
            pst.setInt(2, month);
            pst.setInt(3, year);

            // execute query and get result set.
            ResultSet resultSet = pst.executeQuery();
            // check the record are found or not.
            if (resultSet.next()) {
                /*
                 * Execute when the payroll entry of current month are already exist.
                 */

                // get the values of record.
                int basicSalary = basicSalaryVal = resultSet.getInt("basic_salary");
                int advanceAmt = resultSet.getInt("advance_amt");
                int depositAmt = depositAmtVal = resultSet.getInt("deposit_amount");
                double hours = resultSet.getDouble("overtime_hours");
                int rate = rateOfPerHour = resultSet.getInt("rate_of_per_hour");

                /*
                 * set the deposit amount in globally which are use to delete the record(payroll
                 * entry).
                 */
                previousDepositAmountForDelete = depositAmt;

                // set the values in the text fields.
                basicSalaryField.setText(Integer.toString(basicSalary));
                advanceAmtField.setText(Integer.toString(advanceAmt));
                depositAmtField.setText(Integer.toString(depositAmt));
                overtimeHoursField.setText(Double.toString(hours));
                rateOfPerHourField.setText(Integer.toString(rate));
                /*
                 * check the advance amount is greater then deposit amount then it minus and set
                 * otherwise set to 0.
                 */
                int pendingAmt = advanceAmt > depositAmt ? advanceAmt - depositAmt : 0;
                pendingAmtField.setText(Integer.toString(pendingAmt));
                overtimeAmtField.setText(Double.toString(hours * rate));

                // calculate the attendance of entire month or number of days it have attend.
                Object ob[] = calculateAttendance(month, year, id);

                // set the total attendance in the text fields.
                absentDaysField.setText(ob[1].toString());
                presentDaysField.setText(ob[0].toString());
                halfDaysField.setText(ob[2].toString());

                // calculate the salary.
                int perDayAmt = basicSalary / noOfDays;
                int presentAmt = perDayAmt * Integer.parseInt(ob[0].toString());
                int halfDayAmt = (perDayAmt / 2) * Integer.parseInt(ob[2].toString());
                double overtimeAmt = hours * rate;
                double total = presentAmt + halfDayAmt + overtimeAmt;
                double payable = total - depositAmt;

                // set the salary in payable amount field.
                payableAmtField.setText(Double.toString(payable));

                // set action to update and enable to delete the payroll.
                action = UPDATE_ACTION;
                delete.setEnabled(true);
                payrollStatus = true;
            } else {

                /*
                 * this block execute when the record of payroll does not exist means this is
                 * new entry.
                 */

                // calculate the attendance of entire month.
                Object ob[] = calculateAttendance(month, year, id);

                // set the attendance in the there fields.
                presentDaysField.setText(ob[0].toString());
                absentDaysField.setText(ob[1].toString());
                halfDaysField.setText(ob[2].toString());
                overtimeHoursField.setText(ob[3].toString());

                // this query will check the salaries entry are exist or not.
                query = "SELECT * FROM salaries WHERE emp_id = ? AND extract(MONTH FROM payment_month) = ? AND extract(YEAR FROM payment_month) = ?";
                pst = DBConnection.con.prepareStatement(query);
                pst.setInt(1, id);
                // set exact previous month date of selected date.
                calendar.add(Calendar.MONTH, -1);
                // get the previous month date.
                String prevMonthDate = format.format(calendar.getTime());
                // convert date string to array.
                String prevMonthDateArr[] = prevMonthDate.split("\\-");
                // extract month and year.
                int prevMonth = Integer.parseInt(prevMonthDateArr[1]);
                int preYear = Integer.parseInt(prevMonthDateArr[0]);
                // set previous month and year to sql query.
                pst.setInt(2, prevMonth);
                pst.setInt(3, preYear);
                calendar.setTime(new Date());

                resultSet = pst.executeQuery();
                /*
                 * if record are found then it set default previous basic salary and rate and
                 * advance amount.
                 */
                if (resultSet.next()) {

                    /*
                     * This execute when the employee have previous month payroll entry.
                     */

                    // get the precious payroll values.
                    int salary = basicSalaryVal = resultSet.getInt("basic_salary");
                    int rate = rateOfPerHour = resultSet.getInt("rate_of_per_hour");
                    int depositAmt = depositAmtVal = 0;
                    // set the advance amount according to the previous entry.
                    int advAmt = resultSet.getInt("advance_amt");
                    // get this advance amount the current month.
                    query = "SELECT * FROM prepayments WHERE emp_id = ? AND extract(MONTH FROM date) = ? AND extract(YEAR FROM date) = ?";
                    pst = DBConnection.con.prepareStatement(query);
                    pst.setInt(1, id);
                    pst.setInt(2, month);
                    pst.setInt(3, year);

                    // this loop are calculate total advance amount of this month.
                    for (resultSet = pst.executeQuery(); resultSet
                            .next(); advAmt += resultSet.getInt("amount"))
                        ;
                    // set the values in text fields.
                    basicSalaryField.setText(Integer.toString(salary));
                    rateOfPerHourField.setText(Integer.toString(rate));
                    depositAmtField.setText(Integer.toString(depositAmt));
                    advanceAmtField.setText(Integer.toString(advAmt));

                    // calculate the salary.
                    int perDayAmt = salary / noOfDays;
                    int presentAmt = perDayAmt * Integer.parseInt(ob[0].toString());
                    int halfDayAmt = (perDayAmt / 2) * Integer.parseInt(ob[2].toString());
                    double overtimeAmt = Double.parseDouble(ob[3].toString()) * rate;
                    double total = presentAmt + halfDayAmt + overtimeAmt;
                    double payable = total - depositAmt;

                    // set the calculated amounts.
                    overtimeAmtField.setText(Double.toString(overtimeAmt));
                    /*
                     * check the advance is greater then deposit then it will subtract and set
                     * otherwise set to zero(this calculate because the value do not go to minus).
                     */
                    int pendingAmt = advAmt > depositAmt ? advAmt - depositAmt : 0;
                    pendingAmtField.setText(Integer.toString(pendingAmt));
                    payableAmtField.setText(Double.toString(payable));

                } else {
                    /*
                     * This execute when the employee don't have previous month payroll entry and
                     * and don't have existing current month entry.
                     */

                    // set default values to 0.
                    int salary = basicSalaryVal = 0;
                    int rate = rateOfPerHour = 0;
                    int depositAmt = depositAmtVal = 0;
                    int advAmt = 0;

                    // this query will get the all advances of employee of current month or selected
                    // month.
                    query = "SELECT * FROM prepayments WHERE emp_id = ? AND extract(MONTH FROM date) = ? AND extract(YEAR FROM date) = ?";
                    pst = DBConnection.con.prepareStatement(query);
                    pst.setInt(1, id);
                    pst.setInt(2, month);
                    pst.setInt(3, year);

                    // execute and get the result set and calculate total advance amount one by one.
                    for (resultSet = pst.executeQuery(); resultSet.next(); advAmt += resultSet.getInt("amount"))
                        ;

                    // set the values in the text fields.
                    basicSalaryField.setText(Integer.toString(salary));
                    rateOfPerHourField.setText(Integer.toString(rate));
                    depositAmtField.setText(Integer.toString(depositAmt));
                    advanceAmtField.setText(Integer.toString(advAmt));

                    // calculate the payable amount(salary).
                    int perDayAmt = salary / noOfDays;
                    int presentAmt = perDayAmt * Integer.parseInt(ob[0].toString());
                    int halfDayAmt = (perDayAmt / 2) * Integer.parseInt(ob[2].toString());
                    double overtimeAmt = Double.parseDouble(ob[3].toString()) * rate;
                    double total = presentAmt + halfDayAmt + overtimeAmt;
                    double payable = total - depositAmt;

                    // set the overtime amount.
                    overtimeAmtField.setText(Double.toString(overtimeAmt));
                    // set the pending amount by subtraction of advance amount and deposit amount.
                    pendingAmtField.setText(Integer.toString(advAmt - depositAmt));
                    // set the payable amount.
                    payableAmtField.setText(Double.toString(payable));

                }
                // set action to SAVE_ACTION means it will execute as a new entry.
                action = SAVE_ACTION;
                // set delete button to disabled because the entry are not exist for deletion.
                delete.setEnabled(false);
            }
            /*
             * this check the advance amount is zero or the action is update or not,if yes
             * the deposit amount field will set to disabled because in this scenario the
             * deposit value does not needed.
             */
            if (Integer.parseInt(advanceAmtField.getText()) <= 0 || action.equals(UPDATE_ACTION)) {
                // set disabled.
                depositAmtField.setEnabled(false);
            } else {
                // otherwise set to enabled.
                depositAmtField.setEnabled(true);
            }
            // check the payroll is already generated or not.
            if (payrollStatus) {
                // set the label to understand to user this payroll are already generated.
                payrollStatusLabel.setText("Payroll Already Generated!");
                payrollStatusLabel.setForeground(green);
            } else {
                // set the label to understand the payroll are pending.
                payrollStatusLabel.setText("Payroll are pending!");
                payrollStatusLabel.setForeground(red);
            }
            // make focus to the basic salary field.
            basicSalaryField.requestFocus();
        } catch (Exception exc) {
            /*
             * execute when an exception are occur in above code basically database
             * exceptions.
             */
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }
    }
}
/**
 * This component are end here...
 */