/**
 * 
 * 
 * This component are contains the form which manipulate the data of employee
 * which are new employee or update the details of existing employee.
 * 
 * 
 */

package components.utilities;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import components.report.partial.*;
import config.*;
import partial.*;
import partial.event.*;

/**
 * The AddEmployee class are create a form for employees.
 */
public class AddEmployee extends components.abstracts.AbstractButton implements FocusListener {

    // instance variables.
    JPanel headingPanel, mainPanel, contentPanel, genderPanel;
    EmployeeView employeeView;
    JLabel nameLabel, addressLabel, mobileLabel, emailLabel, genderLabel, heading, empIdLabel, workingStatusLabel,
            employeeListHeading, searchLabel;
    JTextField nameField, mobileField, emailField, empIdField, currentDate, searchField;
    JComboBox<String> workingStatus;
    JTextArea addressArea;
    ButtonGroup genderGroup;
    JRadioButton male, female, otherGender;
    JTable viewTable;
    JScrollPane scrollPane;
    TableRowSorter<TableModel> sorter;
    DefaultTableModel tableModel;
    JMenuItem homeMenuItem, addEmployeeMenu;
    String gender;
    Integer action = 1;
    int employeeIdForOperation;
    final Integer UPDATE_ACTION = 0, INSERT_ACTION = 1;

    /**
     * the method are construct the form for employee manipulation.
     * 
     * @param homeMenu    a reference of home menu item.
     * @param clickedItem a reference of current clicked menu item.
     * @param dataViewer  a reference of javax.swing.JDialog
     */
    public AddEmployee(JMenuItem homeMenu, JMenuItem clickedItem, JDialog dataViewer) {

        // call the abstract button class constructor with JDialog reference.
        super(0, 1, 1, dataViewer);

        // menus for navigation.
        homeMenuItem = homeMenu;
        addEmployeeMenu = clickedItem;

        // setting an layout to panel.
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.white);

        // main panel that holds the form and other related component.
        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        mainPanel.setPreferredSize(new Dimension(900, 580));
        mainPanel.setBorder(lightGrayBorder);
        mainPanel.setBackground(Color.white);

        // heading panel for heading.
        headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        headingPanel.setPreferredSize(new Dimension(800, 50));
        headingPanel.setBackground(Color.white);

        // heading label for the form.
        heading = new JLabel("Employee Registration");
        heading.setFont(headingFont);
        heading.setForeground(Color.DARK_GRAY);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 170, 0, 10));

        // date text field.
        currentDate = new JTextField(12);
        currentDate.setFont(fieldFont);
        currentDate.setEnabled(false);
        currentDate.setDisabledTextColor(Color.darkGray);

        java.util.Date date = calendar.getTime();
        String dateArr[] = format.format(date).split("\\-");
        String formattedDate = dateArr[2] + "-" + dateArr[1] + "-" + dateArr[0];

        // set the current date to text field.
        currentDate.setText(formattedDate);

        // add heading and date text field to heading panel.
        headingPanel.add(heading);
        headingPanel.add(currentDate);

        // content panel that hold the labels and related text fields.
        contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 20));
        contentPanel.setBackground(Color.white);
        contentPanel.setPreferredSize(new Dimension(800, 400));

        // create labels and text fields.
        allocateToLabels();
        allocateToTextFields();

        // add the labels and text fields to the content panel.
        contentPanel.add(empIdLabel);
        contentPanel.add(empIdField);
        contentPanel.add(nameLabel);
        contentPanel.add(nameField);
        contentPanel.add(emailLabel);
        contentPanel.add(emailField);
        contentPanel.add(mobileLabel);
        contentPanel.add(mobileField);
        contentPanel.add(genderLabel);
        contentPanel.add(genderPanel);
        contentPanel.add(workingStatusLabel);
        contentPanel.add(workingStatus);
        contentPanel.add(addressLabel);
        contentPanel.add(addressArea);

        // set left margin to the buttons.
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 0));
        // add heading panel,content panel and button panel to main panel.
        mainPanel.add(headingPanel);
        mainPanel.add(contentPanel);
        mainPanel.add(buttonPanel);

        // set the new entry no to the id field.
        setId("employees", empIdField);

        // add main panel to the frame with constraints.
        add(mainPanel);
        setVisible(true);
    }

    /**
     * This method is create the labels for the form.
     */
    private void allocateToLabels() {
        Dimension size = new Dimension(150, 20);

        nameLabel = new JLabel("Name :");
        nameLabel.setFont(labelFont);
        nameLabel.setPreferredSize(size);

        addressLabel = new JLabel("Address :");
        addressLabel.setFont(labelFont);
        addressLabel.setPreferredSize(size);

        emailLabel = new JLabel("Email :");
        emailLabel.setFont(labelFont);
        emailLabel.setPreferredSize(size);

        mobileLabel = new JLabel("Mobile No. :");
        mobileLabel.setFont(labelFont);
        mobileLabel.setPreferredSize(size);

        empIdLabel = new JLabel("Employee ID :");
        empIdLabel.setFont(labelFont);
        empIdLabel.setPreferredSize(size);

        genderLabel = new JLabel("Gender :");
        genderLabel.setFont(labelFont);
        genderLabel.setPreferredSize(size);

        workingStatusLabel = new JLabel("Working Status :");
        workingStatusLabel.setFont(labelFont);
        workingStatusLabel.setPreferredSize(size);
    }

    /**
     * This method create text fields for form.
     */
    private void allocateToTextFields() {
        empIdField = new JTextField(40);
        empIdField.setFont(labelFont);
        empIdField.setFocusable(true);
        empIdField.addFocusListener(this);
        empIdField.setEnabled(false);
        empIdField.setDisabledTextColor(Color.DARK_GRAY);
        empIdField.addKeyListener(this);

        nameField = new JTextField(40);
        nameField.setFont(labelFont);
        nameField.setFocusable(true);
        nameField.addFocusListener(this);

        addressArea = new JTextArea(4, 40);
        addressArea.setFont(labelFont);
        addressArea.setBorder(grayBorder);
        addressArea.setFocusable(true);
        addressArea.addFocusListener(this);
        // component listener for prevent the resizing of text area.
        addressArea.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                addressArea.setPreferredSize(new Dimension(200, 50));
            }
        });
        emailField = new JTextField(40);
        emailField.setFont(labelFont);
        emailField.setFocusable(true);
        emailField.addFocusListener(this);

        mobileField = new JTextField(40);
        mobileField.setFont(labelFont);
        mobileField.setFocusable(true);
        mobileField.addFocusListener(this);

        genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setPreferredSize(new Dimension(500, 30));
        genderPanel.setBackground(Color.white);

        // button group for single selection.
        genderGroup = new ButtonGroup();

        male = new JRadioButton("Male");
        male.addActionListener(this);
        male.doClick();
        male.setFont(labelFont);
        male.setBackground(Color.white);

        female = new JRadioButton("Female");
        female.addActionListener(this);
        female.setFont(labelFont);
        female.setBackground(Color.white);

        otherGender = new JRadioButton("Other");
        otherGender.addActionListener(this);
        otherGender.setFont(labelFont);
        otherGender.setBackground(Color.white);

        // add gender radio buttons in button group.
        genderGroup.add(male);
        genderGroup.add(female);
        genderGroup.add(otherGender);

        // add gender radio buttons in gender panel.
        genderPanel.add(male);
        genderPanel.add(female);
        genderPanel.add(otherGender);

        workingStatus = new JComboBox<String>();
        workingStatus.setPreferredSize(new Dimension(525, 30));
        workingStatus.setFont(labelFont);
        workingStatus.addItem("Yes");
        workingStatus.addItem("No");

        // add custom key listener for focusing next element when user press "Enter"
        // key.
        empIdField.addKeyListener(new CustomKeyListener(nameField));
        nameField.addKeyListener(new CustomKeyListener(emailField));
        emailField.addKeyListener(new CustomKeyListener(mobileField));
        mobileField.addKeyListener(new CustomKeyListener(workingStatus));
        workingStatus.addKeyListener(new CustomKeyListener(addressArea));
    }

    public void focusGained(FocusEvent e) {
        Object source = e.getSource();
        // check the source is a object of JTextArea or not.
        if (source instanceof JTextArea) {
            // convert (type cast) the source object to JTextArea object.
            JTextArea area = ((JTextArea) source);
            area.setBackground(new Color(238, 238, 238));
        } else {
            // convert (type cast) the source object to JTextField object.
            JTextField field = ((JTextField) source);
            field.setBackground(new Color(238, 238, 238));
        }
    }

    public void focusLost(FocusEvent e) {
        Object source = e.getSource();
        // check the source is a object of JTextArea or not.
        if (source instanceof JTextArea) {
            // convert (type cast) the source object to JTextArea object.
            JTextArea area = ((JTextArea) source);
            area.setBackground(Color.white);
        } else {
            // convert (type cast) the source object to JTextField object.
            JTextField field = ((JTextField) source);
            field.setBackground(Color.white);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // check the source is save button or not.
        if (source.equals(save)) {
            // get all values from input fields.
            String name = nameField.getText().trim().toLowerCase();
            String email = emailField.getText().trim();
            String mobile = mobileField.getText().trim();
            String addr = addressArea.getText().trim();
            String status = workingStatus.getSelectedItem().toString();
            boolean statusVal = false;

            // set the status boolean value according to selection.
            if (status.equals("Yes")) {
                statusVal = true;
            }
            // validate the name,email and mobile number.
            boolean nameResult = Validation.checkName(name);
            boolean emailResult = Validation.checkEmail(email);
            boolean mobileResult = Validation.checkMobile(mobile);

            // check name validation failed or not.
            if (!nameResult) {
                DialogWindow.showErrorDialog(this, "The Name contains only Alphabets!");
                return;
            }
            // check email validation failed or not.
            if (!emailResult) {
                DialogWindow.showErrorDialog(this, "The Email must have @ and . characters.");
                return;
            }
            // check mobile number validation failed or not.
            if (!mobileResult) {
                DialogWindow.showErrorDialog(this, "The mobile number must have 10 digits!");
                return;
            }
            // check address is valid or not.
            if (addr.length() <= 0 || addr == null) {
                DialogWindow.showErrorDialog(this, "The Address not should be empty!");
                return;
            }

            String sql = "";
            /*
             * check the action is insert or if user click on edit button but it does not
             * select any
             * record for editing and it directly fill the form for upload new record
             * without clicking on new
             * button then it will also work properly because we check
             * this condition which checks the employee id are set or not.,that allows to
             * declare this operation is insert or update.
             */
            if (action.equals(INSERT_ACTION) || (action.equals(UPDATE_ACTION) && employeeIdForOperation == 0)) {
                if (Validation.isEmployeeRecordAlreadyExist(name, email, 0)) {
                    DialogWindow.showErrorDialog(this, "The Name OR Email already exist!");
                    return;
                }
                sql = "INSERT INTO employees (name,email,address,mobile_no,gender,user_id,working_status,created_at,updated_at) VALUES (?,?,?,?,?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
            }
            /*
             * if user click on edit button and also it select the data for updating then it
             * will be execute,because after selecting data the employeeIdForOperation are
             * set
             * that provide way to confirm this is a update operation.
             */
            if (action.equals(UPDATE_ACTION) && employeeIdForOperation != 0) {
                if (Validation.isEmployeeRecordAlreadyExist(name, email, employeeIdForOperation)) {
                    DialogWindow.showErrorDialog(this, "The Name OR Email already exist!");
                    return;
                }
                sql = "UPDATE employees SET name = ?,email = ?,address = ?,mobile_no = ?,gender = ?,user_id = ?,working_status = ?,updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            }
            // execute the query against DB.
            try {
                PreparedStatement statement = DBConnection.con.prepareStatement(sql);
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, addr);
                statement.setString(4, mobile);
                statement.setString(5, gender);
                statement.setInt(6, 101);
                statement.setBoolean(7, statusVal);

                /*
                 * check the operation actually update or not,if yes then set the 8 and 9
                 * parameters for query.
                 */
                if (action.equals(UPDATE_ACTION) && employeeIdForOperation != 0) {
                    statement.setInt(8, employeeIdForOperation);
                }

                // execute the query and get the affected rows.
                int sqlResult = statement.executeUpdate();

                /*
                 * checks the affected rows is one or more then say data updated or inserted
                 * successfully.
                 */
                if (sqlResult > 0) {
                    DialogWindow.showMessageDialog(this, "Saved!");
                    addEmployeeMenu.doClick();
                } else {
                    // execute when insertion or updating failed.
                    DialogWindow.showErrorDialog(this, "Failed!");
                }
            } catch (Exception exc) {
                DialogWindow.showMessageDialog(this, "Connection error,try again!");
            }
        }

        // check the source is "male" radio button,then set gender "male".
        if (source.equals(male)) {
            gender = male.getText();
        }

        // check the source is "female" radio button,then set gender "female".
        if (source.equals(female)) {
            gender = female.getText();
        }

        // check the source is "other" radio button ,then set gender "other".
        if (source.equals(otherGender)) {
            gender = otherGender.getText();
        }

        // check the source is exit button or not.
        if (source.equals(exit)) {
            // user confirmation before exit from this window.
            boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to exit from here..");
            if (res) {
                homeMenuItem.doClick();
            }
        }

        // check the source is cancel button or not.
        if (source.equals(cancel)) {
            // make the edit button enable to click it.
            edit.setEnabled(true);
        }

        // check the clicked button is edit or not.
        if (source.equals(edit)) {
            // set action to update action.
            action = UPDATE_ACTION;
            empIdField.setEnabled(true);
            empIdField.requestFocus();
            empIdField.setText("");
        }

        // check the source is new button.
        if (source.equals(newButton)) {
            // re-create an object of current panel.
            addEmployeeMenu.doClick();
        }

        // check the source is select button or not.
        if (source.equals(select)) {
            // get the selected row from JTable which included in JDialog viewer.
            int row = employeeView.table.getSelectedRow();
            // check the row is selected or not by user.
            if (row != -1) {
                // get the value of id column of selected row.
                String val = employeeView.table.getValueAt(row, 1).toString();
                /*
                 * set the selected id to employeeIdForOperation ,which are use to check the
                 * operation is actually update or not when user click on save button.
                 */
                int id = employeeIdForOperation = Integer.parseInt(val);
                // get the entire record of selected id.
                ResultSet resultSet = employeeView.getSelectedData(id);
                // set this record to form for editing .
                setEmployeeDataToForm(resultSet);
                // set JDialog to invisible.
                dataViewer.setVisible(false);
            } else {
                DialogWindow.showErrorDialog(this, "Please select a record for edit!");
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        Object source = e.getSource();
        // get the typed key-text from key-code.
        String key = KeyEvent.getKeyText(e.getKeyCode());
        // get the modifier text like "Ctrl","Alt",etc.
        String modifier = InputEvent.getModifiersExText(e.getModifiersEx());

        // check the source is employee id field and pressed key is "Enter".
        if (source.equals(empIdField) && key.equals("Enter")) {
            try {
                // assign id to employeeIdForOperation variable.
                int id = employeeIdForOperation = Integer.parseInt(empIdField.getText());
                String sql = "SELECT * FROM employees WHERE id = ?";
                PreparedStatement pst = DBConnection.con.prepareStatement(sql);
                pst.setInt(1, id);
                ResultSet result = pst.executeQuery();
                // set the resulted data to form.
                setEmployeeDataToForm(result);
            } catch (Exception exc) {
                // if user enter invalid id.
                DialogWindow.showErrorDialog(this, "Employee ID must have a number!");
            }

        }

        // check the modifier key is "Alter" and key is "V" (This is for Alt+V
        // shortcut).
        if (modifier.equals("Alt") && key.equals("V")) {
            // create employee viewer.
            employeeView = new EmployeeView();
            // make JDialog with employee viewer.
            createViewer(employeeView, null, null);
        }

    }

    /**
     * This method simply receive an ResultSet object and set this data to the form.
     * 
     * @param result java.sql.ResultSet
     */
    private void setEmployeeDataToForm(ResultSet result) {
        try {
            // check the record are found or not in ResultSet.
            if (result.next()) {
                // set data one by one.
                empIdField.setText(Integer.toString(result.getInt("id")));
                nameField.setText(result.getString("name"));
                emailField.setText(result.getString("email"));
                mobileField.setText(result.getString("mobile_no"));
                addressArea.setText(result.getString("address"));

                // check employee status and set working status yes or no accordingly.
                if (result.getBoolean("working_status")) {
                    workingStatus.setSelectedItem("Yes");
                } else {
                    workingStatus.setSelectedItem("No");
                }

                // match the gender and switch accordingly.
                switch (result.getString("gender")) {
                    // if male then set to "Male" gender.
                    case "Male":
                        male.doClick();
                        break;
                    // if female then set to "Female" gender.
                    case "Female":
                        female.doClick();
                        break;
                    // if above does not match then set default "other" gender.
                    default:
                        otherGender.doClick();
                        break;
                }
            } else {
                // execute when no record found.
                DialogWindow.showMessageDialog(this, "No Record Found!");
                addEmployeeMenu.doClick();
            }
        } catch (Exception e) {
            // execute when any exception occur in above block of code.
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }
    }
}
/**
 * This component are end here...
 */