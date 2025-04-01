/**
 * 
 * 
 * This class contains the UI for employee attendance and save attendance.
 * 
 * 
 */
package components.utilities;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import com.toedter.calendar.*;
import config.*;
import partial.*;
import components.abstracts.AbstractButton;
import components.utilities.support.*;

/**
 * This class have the functionality to add the attendance of employees.
 */
public class EmployeeAttendance extends AbstractButton implements PropertyChangeListener {

    // the panels that hold the other components.
    JPanel mainPanel, headingPanel, datePanel, contentPanel, attendanceColumnHeadingPanel;
    // the labels the identical text of components.
    JLabel heading, dateLabel, srNoLabel, nameLabel, absentLabel, presentLabel, halfDayLabel, overtimeLabel,
            overtimeHoursLabel, attendanceStatusLabel;
    // date picker for attendance.
    JDateChooser dateChooser;
    // scroll panel hold the attendance component.
    JScrollPane scrollPane;
    // this array of objects have the individual employee attendance report.
    EmployeeAttendanceRegister register[];
    // menus for navigation.
    JMenuItem homeMenuItem, currentMenu;
    // structure to hold employee ids and there name.
    HashMap<Integer, String> empIdNameMap;
    // action denotes the new entry or update in existing entry.
    Integer action = 1;
    // constants for size of label component.
    final Dimension srNoSize = new Dimension(100, 20);
    final Dimension nameSize = new Dimension(350, 20);
    // actions.
    final Integer UPDATE_ACTION = 0, SAVE_ACTION = 1;

    /**
     * The constructor that create an UI for Employee Attendance.
     * 
     * @param homeMenu is the menu reference of home page menu button.
     * @param current  is the menu reference of current display page menu button.
     */
    public EmployeeAttendance(JMenuItem homeMenu, JMenuItem current) {
        // call super class constructor.
        super(0, 1, 1, null);
        // set menus for navigation.
        homeMenuItem = homeMenu;
        currentMenu = current;
        // The GUI program start here..
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.white);

        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        mainPanel.setPreferredSize(new Dimension(1250, 610));
        mainPanel.setBorder(border);
        mainPanel.setBackground(Color.white);

        headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 5));
        headingPanel.setPreferredSize(new Dimension(1230, 60));
        headingPanel.setBackground(Color.white);
        headingPanel.setBorder(border);

        attendanceStatusLabel = new JLabel();
        attendanceStatusLabel.setFont(labelFont);
        attendanceStatusLabel.setPreferredSize(labelSize);

        headingPanel.add(attendanceStatusLabel);

        heading = new JLabel("Employee Attendance");
        heading.setFont(headingFont);
        heading.setForeground(Color.darkGray);

        headingPanel.add(heading);

        datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        datePanel.setPreferredSize(new Dimension(370, 50));
        datePanel.setBackground(Color.white);

        dateLabel = new JLabel("Date :");
        dateLabel.setFont(labelFont);
        dateLabel.setForeground(Color.darkGray);
        dateLabel.setPreferredSize(new Dimension(100, 30));
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);

        datePanel.add(dateLabel);

        dateChooser = new JDateChooser(calendar.getTime());
        dateChooser.setMaxSelectableDate(calendar.getTime());
        dateChooser.setFont(labelFont);
        dateChooser.setEnabled(false);
        dateChooser.setForeground(Color.darkGray);
        dateChooser.setPreferredSize(new Dimension(250, 30));
        dateChooser.addPropertyChangeListener("date", this);

        datePanel.add(dateChooser);

        headingPanel.add(datePanel);

        mainPanel.add(headingPanel);

        contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        contentPanel.setBackground(Color.white);
        contentPanel.setPreferredSize(new Dimension(1225, 800));
        contentPanel.setBorder(border);

        attendanceColumnHeadingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 7));
        attendanceColumnHeadingPanel.setBorder(border);
        attendanceColumnHeadingPanel.setPreferredSize(new Dimension(1230, 40));
        attendanceColumnHeadingPanel.setBackground(Color.white);

        srNoLabel = new JLabel("Sr.No.");
        srNoLabel.setFont(buttonFont);
        srNoLabel.setPreferredSize(srNoSize);
        srNoLabel.setForeground(Color.darkGray);
        srNoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        attendanceColumnHeadingPanel.add(srNoLabel);

        nameLabel = new JLabel("Name");
        nameLabel.setFont(buttonFont);
        nameLabel.setPreferredSize(nameSize);
        nameLabel.setForeground(Color.darkGray);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        attendanceColumnHeadingPanel.add(nameLabel);

        presentLabel = new JLabel("Present");
        presentLabel.setFont(buttonFont);
        presentLabel.setPreferredSize(new Dimension(100, 20));
        presentLabel.setForeground(Color.darkGray);
        presentLabel.setHorizontalAlignment(SwingConstants.CENTER);

        attendanceColumnHeadingPanel.add(presentLabel);

        absentLabel = new JLabel("Absent");
        absentLabel.setFont(buttonFont);
        absentLabel.setPreferredSize(new Dimension(100, 20));
        absentLabel.setForeground(Color.darkGray);
        absentLabel.setHorizontalAlignment(SwingConstants.CENTER);

        attendanceColumnHeadingPanel.add(absentLabel);

        halfDayLabel = new JLabel("Half-Day");
        halfDayLabel.setFont(buttonFont);
        halfDayLabel.setPreferredSize(new Dimension(100, 20));
        halfDayLabel.setForeground(Color.darkGray);
        halfDayLabel.setHorizontalAlignment(SwingConstants.CENTER);

        attendanceColumnHeadingPanel.add(halfDayLabel);

        overtimeLabel = new JLabel("Over-Time");
        overtimeLabel.setFont(buttonFont);
        overtimeLabel.setPreferredSize(new Dimension(100, 20));
        overtimeLabel.setForeground(Color.darkGray);
        overtimeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        attendanceColumnHeadingPanel.add(overtimeLabel);

        overtimeHoursLabel = new JLabel("Over-Time-Hours");
        overtimeHoursLabel.setFont(buttonFont);
        overtimeHoursLabel.setPreferredSize(new Dimension(150, 20));
        overtimeHoursLabel.setForeground(Color.darkGray);
        overtimeHoursLabel.setHorizontalAlignment(SwingConstants.CENTER);

        attendanceColumnHeadingPanel.add(overtimeHoursLabel);
        // contentPanel.add(attendanceColumnHeadingPanel);
        // get and set the data of employees in key-value.
        getEmpDataFromDBForEmpIdNameMap();
        String date = format.format(dateChooser.getDate());
        // set the attendance data by current date.
        setDataByDate(date);

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(1230, 435));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setBorder(border);

        mainPanel.add(attendanceColumnHeadingPanel);
        mainPanel.add(scrollPane);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 425, 0, 0));
        mainPanel.add(buttonPanel);

        add(mainPanel);

        setVisible(true);

    }

    /**
     * This method are set the attendance data from ResultSet.
     * 
     * @param result is object of java.sql.ResultSet.
     * @return boolean
     */
    private boolean addDataIntoTableFromDB(ResultSet result, String date) {
        boolean res = false;
        try {
            // move the cursor at the last row.
            result.last();
            // get the last row no(number of rows).
            int rows = result.getRow();
            contentPanel.setPreferredSize(new Dimension(1225, rows * 50));
            // create panels which hold the individual employee attendance.
            register = new EmployeeAttendanceRegister[rows];
            // set cursor to before first row.
            result.beforeFirst();
            // set attendance one by one.
            for (int i = 0; result.next(); i++) {
                // get data from result set current row.
                int id = result.getInt("id");
                String name = result.getString("name");
                // create object of individual employee attendance.
                register[i] = new EmployeeAttendanceRegister(this, ++i, id, name, date);
                // add individual panel to container.
                contentPanel.add(register[--i]);
                // set result as true that means the records are found otherwise set false
                // record not found.
                res = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return result
        return res;
    }

    /**
     * This method are get data of employees from DB and set it into hash map.
     */
    private void getEmpDataFromDBForEmpIdNameMap() {
        try {
            // get data from DB.
            String query = "SELECT * FROM employees";
            PreparedStatement pst = DBConnection.con.prepareStatement(query,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet result = pst.executeQuery();
            // move cursor to last row.
            result.last();
            // get no of rows.
            int rows = result.getRow();
            // create hash map with specified size.
            empIdNameMap = new HashMap<Integer, String>(rows);
            // move cursor to before of first row.
            result.beforeFirst();
            // set one by one id and name of employees in hash map.
            while (result.next()) {
                empIdNameMap.put(result.getInt("id"), result.getString("name"));
            }

        } catch (Exception e) {
            DialogWindow.showMessageDialog(this, "No record found!");
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // check the source is save button.
        if (source.equals(save)) {
            String date = format.format(dateChooser.getDate());
            if (register.length <= 0) {
                DialogWindow.showMessageDialog(this, "No record found!");
                return;
            }
            if (action.equals(SAVE_ACTION)) {
                boolean confirm = DialogWindow.showConfirmDialog(this,
                        "Are you sure to add this attendance details,because you will not remove it in future.\nCheck the following points before save :\n 1.Have any new employee are join ?,then first register it in system.\n2.Make sure all working employees are included in attendance register up to '"
                                + date + "'.");
                if (!confirm) {
                    return;
                }
            }
            // create new string multidimensional array with specified size.
            String attendanceArray[][] = new String[register.length + 1][3];

            // set the identifier heading row in the array.
            attendanceArray[0][0] = "id";
            attendanceArray[0][1] = "status";
            attendanceArray[0][2] = "overtime";

            // create string array according to the attendance information.
            for (int j = 0, i = 1; j < register.length; j++, i++) {

                // set employee id to array.
                attendanceArray[i][0] = Integer.toString(register[j].getEmpId());

                // set status of attendance one of below three in the array.
                if (register[j].absentCheckBox.isSelected()) {
                    attendanceArray[i][1] = "Absent";
                }

                if (register[j].presentCheckBox.isSelected()) {
                    attendanceArray[i][1] = "Present";
                }

                if (register[j].halfDayCheckBox.isSelected()) {
                    attendanceArray[i][1] = "HalfDay";
                }

                // check the overtime check box is selected or not.
                if (register[j].overTimeCheckBox.isSelected()) {
                    // get the value of overtime hours.
                    String val = register[j].overTimeHoursField.getText();
                    // check the hours value is does not empty/null.
                    if (!val.isBlank()) {
                        try {
                            // set the overtime hours in the array with validating it is double or int value
                            // only.
                            attendanceArray[i][2] = Double.toString(Double.parseDouble(val));
                        } catch (Exception exc) {
                            // execute when the specified hours value is not a valid int or double.
                            DialogWindow.showMessageDialog(this,
                                    "only numeric value (5,5.30) are allowed overtime hours field no. "
                                            + register[j].getSrNo());
                            return;
                        }
                    } else {
                        // execute when the hours value is empty or null.
                        DialogWindow.showMessageDialog(this,
                                "Enter Hours value in the overtime hours field no. " + register[j].getSrNo());
                        return;
                    }
                } else {
                    // execute when the overtime are not selected ,set default as "No" overtime.
                    attendanceArray[i][2] = "No";
                }
            }

            // convert this string multidimensional array to single string.
            String attendance = convertArrayToString(attendanceArray);
            String query = "";
            try {
                // check the action is save means insert new entry else update existing record.
                if (action.equals(SAVE_ACTION)) {
                    query = "INSERT INTO attendance(status,created_at,updated_at,attendance_date)VALUES(?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'"
                            + date + "')";
                } else {
                    query = "UPDATE attendance SET status = ?,updated_at = CURRENT_TIMESTAMP WHERE attendance_date = '"
                            + date + "'";
                }
                // create prepare statement for above query.
                PreparedStatement pst = DBConnection.con.prepareStatement(query);
                // set first parameter as attendance string.
                pst.setString(1, attendance);

                // execute the above query.
                int res = pst.executeUpdate();
                // check the affected rows means the data are successfully updated or inserted.
                if (res > 0) {
                    DialogWindow.showMessageDialog(this, "Saved!");
                    attendanceStatusLabel.setText("Attendance Applied!");
                    attendanceStatusLabel.setForeground(green);
                    action = UPDATE_ACTION;
                } else {
                    DialogWindow.showErrorDialog(this, "Failed!");
                }
            } catch (Exception exc) {
                // execute when exception in above code.
                DialogWindow.showErrorDialog(this, "Connection error,try again!");
            }
        }

        // check the source is cancel button.
        if (source.equals(cancel)) {
            edit.setEnabled(true);
        }

        // check the source is edit button.
        if (source.equals(edit)) {
            dateChooser.setEnabled(true);
            action = UPDATE_ACTION;
            dateChooser.requestFocus();
        }

        // check the source is exit button.
        if (source.equals(exit)) {
            boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to exit from here...!");
            if (res) {
                homeMenuItem.doClick();
            }
        }

        // check the source is new button.
        if (source.equals(newButton)) {
            currentMenu.doClick();
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        // get selected date.
        String date = format.format(dateChooser.getDate());
        // set the attendance data of the selected date.
        setDataByDate(date);
    }

    /**
     * This method accept an date as a parameter and set the attendance data
     * accordingly.
     * 
     * @param date is parameter date value in string format.
     */
    private void setDataByDate(String date) {
        // check the date value is not empty or null.
        if (!date.isBlank()) {
            try {
                // get the attendance data of given date from DB.
                String query = "SELECT * FROM attendance WHERE attendance_date ='" + date + "'";
                ResultSet resultSet = DBConnection.executeQuery(query);
                // check the register are not null.
                if (register != null) {
                    // remove existing employee attendance panels.
                    for (EmployeeAttendanceRegister empAttendance : register) {
                        contentPanel.remove(empAttendance);
                    }
                }
                boolean status = false;
                // if the attendance are found then display one by one.
                if (resultSet.next()) {
                    register = null;
                    // get the attendance status string.
                    String attendance = resultSet.getString("status");
                    // convert string to two dimensional string array.
                    String[][] attendanceArray = convertStringToArray(attendance);
                    // allocate memory according to length of array.
                    contentPanel.setPreferredSize(new Dimension(1225, (attendanceArray.length - 1) * 50));
                    register = new EmployeeAttendanceRegister[attendanceArray.length - 1];

                    // add one by one attendance data into panel and add it into content panel.
                    for (int i = 1, j = 0; i < attendanceArray.length; i++, j++) {
                        // get the employee id.
                        int id = Integer.parseInt(attendanceArray[i][0].trim());
                        // get the name of employee from id.
                        String name = empIdNameMap.get(id);
                        // create an employee attendance panel for individual employee.
                        register[j] = new EmployeeAttendanceRegister(this, i, id, name, date);

                        // switch the attendance status of current employee.
                        switch (attendanceArray[i][1].trim()) {
                            case "Absent":
                                register[j].absentCheckBox.setSelected(true);
                                break;
                            case "Present":
                                register[j].presentCheckBox.setSelected(true);
                                break;
                            default:
                                register[j].halfDayCheckBox.setSelected(true);
                                break;
                        }

                        // check the overtime hours value is not equal to "No"
                        if (!attendanceArray[i][2].trim().equals("No")) {
                            /*
                             * checks the overtime checkbox and set the value of overtime hours in the
                             * overtime hours field.
                             */
                            register[j].overTimeCheckBox.setSelected(true);
                            register[j].overTimeHoursField.setText(attendanceArray[i][2]);
                            register[j].overTimeHoursField.setEnabled(false);
                        }

                        // add the employee attendance panel to content panel.
                        contentPanel.add(register[j]);
                    }

                    // set action to update.
                    action = UPDATE_ACTION;
                    status = true;
                    // this else part will execute when the don't have attendance data of give date.
                } else {
                    // get data of employees which are registered at the given date or before that
                    // date.
                    String query1 = "SELECT * FROM employees WHERE working_status = ? AND DATE(created_at) <= '" + date
                            + "'";
                    // create a prepare statement with type scrollable which allow move cursor to
                    // any row of result set.
                    PreparedStatement pst = DBConnection.con.prepareStatement(query1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    pst.setBoolean(1, true);
                    // execute the query.
                    resultSet = pst.executeQuery();
                    // set the data into the GUI.
                    if (!addDataIntoTableFromDB(resultSet, date)) {
                        // execute when data not found in the result set.
                        DialogWindow.showMessageDialog(this, "No Record found!");
                    }
                    action = SAVE_ACTION;
                }
                if (status) {
                    attendanceStatusLabel.setText("Attendance Applied!");
                    attendanceStatusLabel.setForeground(green);
                } else {
                    attendanceStatusLabel.setText("Attendance Pending!");
                    attendanceStatusLabel.setForeground(red);
                }
                // revalidate and repaint the component.
                contentPanel.revalidate();
                contentPanel.repaint();

                revalidate();
                repaint();

            } catch (Exception exc) {
                // execute when any exception are occur in the above code.
                exc.printStackTrace();
                DialogWindow.showErrorDialog(this, "Connection error,try again!");
            }
        } else {
            // execute when invalid date selected by user.
            DialogWindow.showErrorDialog(this, "Please,select valid date!");
        }
    }
}
/**
 * This component end here...
 */
