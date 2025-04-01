/**
 * 
 * 
 * This file contains the class which have a form to change the password for login the 
 * system.
 * 
 * 
 */

package components.settings;

import javax.swing.*;
import config.*;
import partial.*;
import partial.event.*;
import partial.interfaces.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * The ChangePassword class have form to change the password of the system which
 * are use to login the system.
 */
public final class ChangePassword extends JPanel implements ActionListener, FontInterface, FocusListener {

    JPanel mainPanel, headingPanel, contentPanel, buttonPanel;
    JButton save, exit;
    JLabel headingLabel, oldPassLabel, newPassLabel, confirmPassLabel;
    JTextField oldPassField;
    JPasswordField newPassField, confirmPassField;
    JMenuItem homeMenuItem, changePasswordMenu;

    /**
     * This method are construct the form for updating the password.
     * 
     * @param clickedItem a reference of current menu item.
     * @param homeMenu    a reference of home menu item.
     */
    public ChangePassword(JMenuItem clickedItem, JMenuItem homeMenu) {

        // set the menus.
        changePasswordMenu = clickedItem;
        homeMenuItem = homeMenu;
        // set the flow layout to panel.
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));
        setBackground(Color.white);

        // set the flow layout to panel.
        mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
        mainPanel.setBackground(Color.white);
        mainPanel.setPreferredSize(new Dimension(400, 410));
        mainPanel.setBorder(lightGrayBorder);

        // create heading panel which hold the heading of the form.
        headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headingPanel.setBackground(Color.white);
        headingPanel.setPreferredSize(new Dimension(390, 60));

        // create heading label.
        headingLabel = new JLabel("Change Password");
        headingLabel.setForeground(Color.darkGray);
        headingLabel.setFont(headingFont);

        // add label to heading panel.
        headingPanel.add(headingLabel);

        // add heading panel to the main panel.
        mainPanel.add(headingPanel);

        // create the content panel which holds the labels and fields for changing the
        // password.
        contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        contentPanel.setBackground(Color.white);
        contentPanel.setPreferredSize(new Dimension(390, 300));

        // size for label.
        Dimension labelSize = new Dimension(315, 20);

        // create label and field.
        oldPassLabel = new JLabel("Enter Old Password :");
        oldPassLabel.setFont(labelFont);
        oldPassLabel.setPreferredSize(labelSize);
        oldPassLabel.setForeground(Color.darkGray);

        oldPassField = new JTextField(24);
        oldPassField.setFont(labelFont);
        oldPassField.addFocusListener(this);

        // add label and field to content panel.
        contentPanel.add(oldPassLabel);
        contentPanel.add(oldPassField);

        // create label and field.
        newPassLabel = new JLabel("Enter New Password :");
        newPassLabel.setFont(labelFont);
        newPassLabel.setForeground(Color.darkGray);
        newPassLabel.setPreferredSize(labelSize);

        newPassField = new JPasswordField(24);
        newPassField.setFont(labelFont);
        newPassField.addFocusListener(this);
        newPassField.setEchoChar('*');

        // add label and field to content panel.
        contentPanel.add(newPassLabel);
        contentPanel.add(newPassField);

        // create label and field.
        confirmPassLabel = new JLabel("Repeat Password :");
        confirmPassLabel.setFont(labelFont);
        confirmPassLabel.setPreferredSize(labelSize);
        confirmPassLabel.setForeground(Color.darkGray);

        confirmPassField = new JPasswordField(24);
        confirmPassField.setFont(labelFont);
        confirmPassField.addFocusListener(this);
        confirmPassField.setEchoChar('*');

        // add label and field to content panel.
        contentPanel.add(confirmPassLabel);
        contentPanel.add(confirmPassField);

        // button panel which hold the buttons.
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setPreferredSize(new Dimension(390, 40));
        buttonPanel.setBackground(Color.white);

        // save button to update the info.
        save = new JButton("Save");
        save.setBackground(lightBlue);
        save.setForeground(Color.white);
        save.setPreferredSize(new Dimension(100, 30));
        save.setFont(buttonFont);
        save.addActionListener(this);

        // exit button to leave from change password panel.
        exit = new JButton("Exit");
        exit.setBackground(orange);
        exit.setForeground(Color.white);
        exit.setPreferredSize(new Dimension(100, 30));
        exit.setFont(buttonFont);
        exit.addActionListener(this);

        // add buttons to button panel.
        buttonPanel.add(save);
        buttonPanel.add(exit);

        // add button panel to content panel.
        contentPanel.add(buttonPanel);

        // add content panel to main panel.
        mainPanel.add(contentPanel);

        // add main panel to frame.
        add(mainPanel);

        // add key listener to fields,which allows user press enter key then the focus
        // switch to other component.
        oldPassField.addKeyListener(new CustomKeyListener(newPassField));
        newPassField.addKeyListener(new CustomKeyListener(confirmPassField));
        confirmPassField.addKeyListener(new CustomKeyListener(save));

        // set the panel visible.
        setVisible(true);
    }

    public void focusGained(FocusEvent e) {
        Object source = e.getSource();
        JTextField textField = ((JTextField) source);
        // change background color when focus gained.
        textField.setBackground(borderColor);
    }

    public void focusLost(FocusEvent e) {
        Object source = e.getSource();
        JTextField textField = ((JTextField) source);
        // change background color when focus lost.
        textField.setBackground(Color.white);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // check the source is save or not.
        if (source.equals(save)) {
            // Get the values from fields.
            String oldPassVal = oldPassField.getText().trim();
            String newPassVal = new String(newPassField.getPassword()).trim();
            String conPassVal = new String(confirmPassField.getPassword()).trim();

            // validate the new password.
            boolean passResult = Validation.checkPassword(newPassVal);
            try {
                // check old pass field value is empty or null or not.
                if (!oldPassVal.isBlank()) {
                    // query that checks the password is already exist or not.
                    String query = "SELECT * FROM users WHERE password = ?";
                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                    pst.setString(1, oldPassVal);
                    ResultSet resultSet = pst.executeQuery();
                    // password is exist then user which are requested to update the password is
                    // Authorized or not.
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        // check the new password is valid or not.
                        if (passResult) {
                            // check the both password are not equal.
                            if (!newPassVal.equals(oldPassVal)) {
                                // checks the confirm password and new password is equal or not.
                                if (conPassVal.equals(newPassVal)) {
                                    // both password are equal then this will updated by new password.
                                    query = "UPDATE users SET password = ? WHERE id = ?";
                                    pst = DBConnection.con.prepareStatement(query);
                                    pst.setString(1, newPassVal);
                                    pst.setInt(2, id);

                                    boolean res = (pst.executeUpdate() > 0);
                                    // check password updated successfully or not.
                                    if (res) {
                                        // show the message.
                                        DialogWindow.showMessageDialog(this, "Password Updated Successfully!");
                                        oldPassField.setText("");
                                        newPassField.setText("");
                                        confirmPassField.setText("");
                                        // set focus to old password field.
                                        oldPassField.requestFocus();
                                    } else {
                                        DialogWindow.showMessageDialog(this, "Password cannot updated,try later!");
                                    }
                                } else {
                                    DialogWindow.showErrorDialog(this,
                                            "The Repeat password doesn't match with new password!");
                                }
                            } else {
                                DialogWindow.showMessageDialog(this, "The New password is similar to Old password!");
                            }
                        } else {
                            DialogWindow.showErrorDialog(this,
                                    "Invalid New Password! \n Password must contains exact 8 characters with digits or lowercase alphabets or uppercase alphabets!");
                        }
                    } else {
                        DialogWindow.showErrorDialog(this, "The old password is wrong, enter correct password!");
                    }
                } else {
                    DialogWindow.showErrorDialog(this, "Please Enter old password!");
                }
            } catch (Exception exc) {
                DialogWindow.showErrorDialog(this, "Connection error,try again!");
            }
        }

        // checks the source is exit button or not.
        if (source.equals(exit)) {
            boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to exit from here..!");
            // true then exit from change password window.
            if (res) {
                homeMenuItem.doClick();
            }
        }
    }
}
/**
 * 
 * This component are ends here...
 */