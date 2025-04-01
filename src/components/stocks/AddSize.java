/**
 * 
 * This file hold the class which create the form to add ,modify the details of size
 * with functionality.
 * 
 */

package components.stocks;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import components.abstracts.AbstractButton;
import components.report.partial.*;
import config.*;
import partial.*;
import partial.event.*;

/**
 * The class AddSize contains the form which allows user to modify the size ,add
 * the size,etc.
 */
public class AddSize extends AbstractButton {

    JPanel guageRatePanel, headingPanel, mainPanel;
    JLabel id, size, heading;
    JTextField idField, sizeField;
    JMenuItem sizeMenu, homeMenuItem;
    SizeView sizeView;
    Integer action = 1, sizeIdForOperation = 0;
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    final Integer SAVE_ACTION = 1, UPDATE_ACTION = 0, DELETE_ACTION = -1;

    /**
     * The constructor which create the form for size.
     * 
     * @param homeMenu   a reference of home menu item.
     * @param current    a reference of current menu item.
     * @param dataViewer a reference of javax.swing.JDialog.
     */
    public AddSize(JMenuItem homeMenu, JMenuItem current, JDialog dataViewer) {
        // allow to create delete button and set the reference of JDialog to parent.
        super(1, 1, 1, dataViewer);
        homeMenuItem = homeMenu;
        sizeMenu = current;
        // set the flow layout to the panel.
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        setBackground(Color.white);

        // create the labels and text fields.
        allocateToLabel();
        allocateToTextField();

        // main panel that hold the actual info.
        mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.setPreferredSize(new Dimension(490, 600));
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(border);

        // heading panel to hold the heading of the form.
        headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        headingPanel.setBackground(Color.white);
        headingPanel.setPreferredSize(new Dimension(390, 75));
        // add heading to heading panel.
        headingPanel.add(heading);
        // add heading panel to main panel.
        mainPanel.add(headingPanel);
        // add other labels and fields to the main panel.
        mainPanel.add(id);
        mainPanel.add(idField);
        mainPanel.add(size);
        mainPanel.add(sizeField);
        mainPanel.add(buttonPanel);

        // create the guage rate panel.
        guageRatePanel = new AddGuageRate(homeMenu, current, dataViewer, this);
        // set the new id for new size to the field.
        setId("sizes", idField);

        // add main panel and guage rate panel to the frame.
        add(mainPanel);
        add(guageRatePanel);

        // set the panel visible.
        setVisible(true);
    }

    /**
     * This method are creates the label for the form.
     */
    private void allocateToLabel() {

        heading = new JLabel("Add Size");
        heading.setFont(headingFont);

        id = new JLabel("ID :");
        id.setFont(labelFont);
        id.setPreferredSize(labelSize);

        size = new JLabel("Size :");
        size.setFont(labelFont);
        size.setPreferredSize(labelSize);
    }

    /**
     * This method are create the text fields for the form.
     */
    private void allocateToTextField() {
        idField = new JTextField(28);
        idField.setFont(fieldFont);
        idField.setEnabled(false);
        idField.setDisabledTextColor(Color.gray);
        idField.addKeyListener(this);
        idField.setFocusable(true);

        sizeField = new JTextField(28);
        sizeField.setFont(fieldFont);
        sizeField.setFocusable(true);

        // add key listener to id field ,when user press "Enter" then the focus will
        // switch to next element.
        idField.addKeyListener(new CustomKeyListener(sizeField));
    }

    public void actionPerformed(ActionEvent e) {
        // check the source is save button or not.
        if (e.getSource().equals(save)) {
            // check the action is save_action or not.
            if (action.equals(SAVE_ACTION)) {
                // validate the size.
                boolean res = Validation.checkSize(sizeField.getText().trim());
                // true if validation are success.
                if (res) {
                    // check the size is already exist or not.
                    if (!isSizeExist(sizeField.getText().trim())) {
                        try {
                            // insert new size into the DB.
                            String query = "INSERT INTO sizes (size,user_id)VALUES(?,101)";
                            PreparedStatement pst = DBConnection.con.prepareStatement(query);
                            pst.setString(1, sizeField.getText().trim());
                            res = (pst.executeUpdate() > 0);
                            // execute when record inserted successfully.
                            if (res) {
                                DialogWindow.showMessageDialog(this, "Size added successful!");
                                sizeMenu.doClick();
                            } else {
                                // size does not added.
                                DialogWindow.showMessageDialog(this, "Size cannot added!");
                            }
                        } catch (Exception exc) {
                            DialogWindow.showErrorDialog(this, "Connection error,try again!");
                        }
                    } else {
                        // execute when size already exist.
                        DialogWindow.showErrorDialog(this, "Size already exist");
                    }
                } else {
                    // execute when validation failed.
                    DialogWindow.showErrorDialog(this,
                            "Please Enter valid size in field like 2*4 or 2.5*4!");
                }
            }

            // check the action is update_action or not.
            if (action.equals(UPDATE_ACTION)) {
                // confirmation for updating.
                boolean res = DialogWindow.showConfirmDialog(this, "Do you want to update this information!");
                if (res) {
                    // validate the size.
                    res = Validation.checkSize(sizeField.getText().trim());
                    if (res) {
                        // check the size already exist or not.
                        if (!isSizeExist(sizeField.getText().trim())) {
                            try {
                                // update the record into DB.
                                String query = "UPDATE sizes SET size = ? WHERE id = ?";
                                PreparedStatement pst = DBConnection.con.prepareStatement(query);
                                pst.setString(1, sizeField.getText().trim());
                                pst.setInt(2, sizeIdForOperation);
                                res = (pst.executeUpdate() > 0);
                                // execute when size updated successfully.
                                if (res) {
                                    DialogWindow.showMessageDialog(this, "Size updated successful!");
                                    sizeMenu.doClick();
                                } else {
                                    // execute when size does not update.
                                    DialogWindow.showMessageDialog(this, "Size cannot updated!");
                                }
                            } catch (Exception exc) {
                                DialogWindow.showErrorDialog(this, "Connection error,try again!");
                            }
                        } else {
                            // execute when size already exist into DB.
                            DialogWindow.showErrorDialog(this, "Size already exist");
                        }
                    } else {
                        // execute when validation failed.
                        DialogWindow.showErrorDialog(this,
                                "Please Enter valid size in field like 2*4 or 2.5*4!");
                    }
                } else {
                    // execute when confirmation rejected.
                    sizeMenu.doClick();
                }
            }

            // check the action is delete_action or not.
            if (action.equals(DELETE_ACTION)) {
                // user confirmation before deleting.
                boolean res = DialogWindow.showConfirmDialog(this, "Do you want to delete this information!");
                if (res) {
                    try {
                        // delete the record from DB.
                        String query = "DELETE FROM sizes WHERE id = ?";
                        PreparedStatement pst = DBConnection.con.prepareStatement(query);
                        pst.setInt(1, sizeIdForOperation);
                        res = (pst.executeUpdate() > 0);
                        // execute when record deleted successfully.
                        if (res) {
                            DialogWindow.showMessageDialog(this, "Size deleted successful!");
                            sizeMenu.doClick();
                        } else {
                            // execute when record cannot deleted.
                            DialogWindow.showMessageDialog(this, "Size cannot deleted!");
                        }
                    } catch (Exception exc) {
                        DialogWindow.showErrorDialog(this,
                                "Connection error!\n OR \nsize are assign to other \nOR\n invalid value for ID!");
                    }
                } else {
                    // confirmation rejected.
                    sizeMenu.doClick();
                }
            }
        }

        // check the source is delete button or edit button .
        if (e.getSource().equals(delete) || e.getSource().equals(edit)) {
            idField.setEnabled(true);
            idField.requestFocus();
            // set the action as per button clicked.
            if (e.getSource().equals(delete)) {
                action = DELETE_ACTION;
            } else {
                action = UPDATE_ACTION;
            }
        }

        // check the source is cancel button or not.
        if (e.getSource().equals(cancel)) {
            edit.setEnabled(true);
            delete.setEnabled(true);
        }

        // check the source is exit button or not.
        if (e.getSource().equals(exit)) {
            // confirmation before exit.
            boolean res = DialogWindow.showConfirmDialog(this, "Do you want to exit from here!");
            if (res) {
                homeMenuItem.doClick();
            }
        }

        // check the source is new button or not.
        if (e.getSource().equals(newButton)) {
            sizeMenu.doClick();
        }

        // check the source is select button or not.
        if (e.getSource().equals(select)) {
            // get the row which are selected.
            int row = sizeView.table.getSelectedRow();
            if (row != -1) {
                // get the value of id column from selected row.
                String val = sizeView.table.getValueAt(row, 0).toString().trim();
                int id = sizeIdForOperation = Integer.parseInt(val);
                // get the data of selected id from DB.
                ResultSet resultSet = sizeView.getSelectedData(id);
                try {
                    // true if record are found.
                    if (resultSet.next()) {
                        // set the record to the form.
                        idField.setText(resultSet.getObject("id").toString());
                        sizeField.setText(resultSet.getString("size"));
                    } else {
                        // execute when record not found.
                        DialogWindow.showMessageDialog(this, "No record found!");
                    }
                } catch (Exception exc) {
                    DialogWindow.showErrorDialog(this, "Connection error,try again!");
                }
                // set JDialog to invisible.
                dataViewer.setVisible(false);
            } else {
                DialogWindow.showErrorDialog(this, "Please select a record for edit!");
            }
        }
    }

    /**
     * This method are check the given size is already exist in the DB or not.
     * 
     * @param size a string value of size.
     * @return boolean
     */
    private boolean isSizeExist(String size) {
        try {
            // get the record of given size from DB.
            String query = "SELECT * FROM sizes WHERE size ='" + size + "'";
            ResultSet result = DBConnection.executeQuery(query);
            // return true when record are found otherwise return false.
            return result.next();
        } catch (Exception e) {
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }
        return false;
    }

    public void keyReleased(KeyEvent e) {
        Object source = e.getSource();
        int keyCode = e.getKeyCode();
        String key = KeyEvent.getKeyText(keyCode);
        // check the source is id field and press key is "Enter".
        if (source.equals(idField) && key.equals("Enter")) {
            try {
                // Get the record of size as per entered id from DB.
                String query = "SELECT * FROM sizes WHERE id = ?";
                PreparedStatement pst = DBConnection.con.prepareStatement(query);
                pst.setInt(1, Integer.parseInt(idField.getText()));
                ResultSet res = pst.executeQuery();
                // set record when found.
                if (res.next()) {
                    sizeIdForOperation = Integer.parseInt(idField.getText().trim());
                    sizeField.setText(res.getString("size"));
                } else {
                    // execute when no record found.
                    DialogWindow.showMessageDialog(this, "No record found!");
                    sizeMenu.doClick();
                }
            } catch (Exception exc) {
                DialogWindow.showErrorDialog(this, "Connection error,try again \nOR\n Invalid size id,it must have a number!");
            }
        }

        // check the source is id field or not.
        if (source.equals(idField)) {
            String modifier = InputEvent.getModifiersExText(e.getModifiersEx());
            // check the modifier is "Alt" and key is 'V'.
            if (modifier.equals("Alt") && keyCode == KeyEvent.VK_V) {
                // create the SizeView.
                sizeView = new SizeView();
                // create the JDialog with the SizeView.
                createViewer(null, sizeView, null);
            }
        }
    }
}
/**
 * This component end here...
 */