/**
 * 
 * This class have contains the individual employee attendance details.
 * 
 */

package components.utilities.support;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import config.*;
import partial.*;
import partial.interfaces.*;

/**
 * This class creates an individual employee attendance panel with there
 * options.
 */
public class EmployeeAttendanceRegister extends JPanel implements FontInterface, ItemListener, KeyListener {

    // instance variables.
    JLabel srNoLabel, nameLabel;
    public JCheckBox absentCheckBox, presentCheckBox, overTimeCheckBox, halfDayCheckBox;
    ButtonGroup checkBoxGroup;
    public JTextField overTimeHoursField;
    int empId, srNo, attendanceMonth;
    String empName;
    double overTimeVal = 0d;
    Dimension srNoSize = new Dimension(100, 20);
    Dimension nameSize = new Dimension(350, 20);
    JPanel parent;

    /**
     * This constructor create an panel with employee attendance options.
     * 
     * @param parent the panel reference where the object are created.
     * @param sr_no  the value for maintaining sequence.
     * @param id     the employee id.
     * @param name   the employee name.
     */
    public EmployeeAttendanceRegister(JPanel parent, int sr_no, int id, String name, String date) {

        // set the instance vars.
        empId = id;
        empName = name;
        srNo = sr_no;
        attendanceMonth = Integer.parseInt(date.split("\\-")[1].trim());
        this.parent = parent;

        // the GUI start here..
        setLayout(new FlowLayout(FlowLayout.LEFT, 7, 7));
        setBackground(Color.white);
        setPreferredSize(new Dimension(new Dimension(1220, 40)));

        srNoLabel = new JLabel(Integer.toString(sr_no));
        srNoLabel.setFont(buttonFont);
        srNoLabel.setPreferredSize(srNoSize);
        srNoLabel.setForeground(Color.darkGray);
        srNoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        nameLabel = new JLabel(empName);
        nameLabel.setFont(buttonFont);
        nameLabel.setPreferredSize(nameSize);
        nameLabel.setForeground(Color.darkGray);

        absentCheckBox = new JCheckBox();
        absentCheckBox.setSelected(false);
        absentCheckBox.setBackground(Color.white);
        absentCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        absentCheckBox.setPreferredSize(new Dimension(100, 20));

        presentCheckBox = new JCheckBox();
        presentCheckBox.setSelected(true);
        presentCheckBox.setBackground(Color.white);
        presentCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        presentCheckBox.setPreferredSize(new Dimension(100, 20));

        halfDayCheckBox = new JCheckBox();
        halfDayCheckBox.setSelected(true);
        halfDayCheckBox.setBackground(Color.white);
        halfDayCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        halfDayCheckBox.setPreferredSize(new Dimension(100, 20));

        checkBoxGroup = new ButtonGroup();

        checkBoxGroup.add(absentCheckBox);
        checkBoxGroup.add(presentCheckBox);
        checkBoxGroup.add(halfDayCheckBox);

        overTimeCheckBox = new JCheckBox();
        overTimeCheckBox.setSelected(false);
        overTimeCheckBox.setBackground(Color.white);
        overTimeCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        overTimeCheckBox.setPreferredSize(new Dimension(100, 20));
        overTimeCheckBox.addItemListener(this);

        JPanel fieldHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fieldHolder.setPreferredSize(new Dimension(150, 30));
        fieldHolder.setBackground(Color.white);

        overTimeHoursField = new JTextField(6);
        overTimeHoursField.setEnabled(false);
        overTimeHoursField.setFont(labelFont);
        overTimeHoursField.addKeyListener(this);
        overTimeHoursField.setDisabledTextColor(Color.gray);
        overTimeHoursField.addMouseListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                setOverTimeValue();
            }
        });

        fieldHolder.add(overTimeHoursField);

        add(srNoLabel);
        add(nameLabel);
        add(presentCheckBox);
        add(absentCheckBox);
        add(halfDayCheckBox);
        add(overTimeCheckBox);
        add(fieldHolder);
        setEmployeeAttendanceEnabled();
        setVisible(true);

        // GUI end here..
    }

    /**
     * This method check the payroll are generated of this employee of the given
     * month.
     */
    private void setEmployeeAttendanceEnabled() {
        try {
            String query = "SELECT * FROM salaries WHERE extract(MONTH FROM payment_month) = " + attendanceMonth
                    + " AND emp_id = " + empId;
            ResultSet res = DBConnection.executeQuery(query);
            if (res.next()) {
                presentCheckBox.setEnabled(false);
                absentCheckBox.setEnabled(false);
                overTimeCheckBox.setEnabled(false);
                halfDayCheckBox.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        // check the source is overtime check box or not.
        if (source.equals(overTimeCheckBox)) {
            // check the checkbox selected or not.
            if (overTimeCheckBox.isSelected()) {
                overTimeHoursField.setEnabled(true);
                overTimeHoursField.requestFocus();
            } else {
                overTimeHoursField.setEnabled(false);
                overTimeHoursField.setText("");
                overTimeVal = 0;
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        Object source = e.getSource();
        String key = KeyEvent.getKeyText(e.getKeyCode());

        // check the typed key is "Enter".
        if (source.equals(overTimeHoursField) && key.equals("Enter")) {
            setOverTimeValue();
        }
    }

    private void setOverTimeValue() {
        try {
            // parsing the value and set it.
            overTimeVal = Double.parseDouble(overTimeHoursField.getText());
            overTimeHoursField.setEnabled(false);
        } catch (Exception exc) {
            // execute when value are not valid int or double.
            DialogWindow.showMessageDialog(parent, "Enter Hours value in the field!");
        }
    }

    /**
     * This method return the employee id.
     * 
     * @return int
     */
    public int getEmpId() {
        return empId;
    }

    /**
     * This method return the name of the employee.
     * 
     * @return java.lang.String
     */
    public String getEmpName() {
        return empName;
    }

    /**
     * This method return the overtime hours value.
     * 
     * @return double
     */
    public double getOvertimeHours() {
        return overTimeVal;
    }

    /**
     * This method return the serial number of calling object.
     * 
     * @return int
     */
    public int getSrNo() {
        return srNo;
    }
}
/**
 * This component end here...
 */