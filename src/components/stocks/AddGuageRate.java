/**
 * 
 * The Guage rate file contains the class which creates a form to manage the 
 * data of guage rate .
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
import java.util.*;
import partial.event.*;

/**
 * The AddGuageRate class which allows to manage the data of guage-rate-size
 * using form.
 */
public class AddGuageRate extends AbstractButton {

    JLabel id, guageLabel, sizeLabel, rateLabel, heading;
    JTextField idField, guageField, rateField;
    JComboBox<String> sizes;
    JPanel headingPanel, mainPanel, parent;
    HashMap<String, Integer> sizeIdMap;
    HashMap<Integer, String> idSizeMap;
    GuageRateView guageRateView;
    JMenuItem sizeMenu, homeMenuItem;
    Integer action = 1, guageRateIdForOperation = 0;
    final Toolkit toolkit = Toolkit.getDefaultToolkit();
    final Integer SAVE_ACTION = 1, UPDATE_ACTION = 0, DELETE_ACTION = -1;

    /**
     * The class constructor are create the form for managing guage-rate.
     * 
     * @param homeMenu   a reference of home menu item.
     * @param current    a reference current menu item.
     * @param dataViewer a reference of javax.swing.JDialog.
     */
    public AddGuageRate(JMenuItem homeMenu, JMenuItem current, JDialog dataViewer, JPanel parent) {
        // calling the super class constructor with delete button and JDialog reference.
        super(1, 1, 1, dataViewer);
        this.parent = parent;
        homeMenuItem = homeMenu;
        sizeMenu = current;

        setBackground(Color.white);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setPreferredSize(new Dimension(500, 600));

        // create the labels
        allocateToLabel();
        // create the text fields.
        allocateToText();

        // create the main panel which hold the heading and other fields.
        mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.setPreferredSize(new Dimension(490, 600));
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(border);

        // heading panel to hold the heading.
        headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        headingPanel.setBackground(Color.white);
        headingPanel.setPreferredSize(new Dimension(390, 75));
        headingPanel.add(heading);

        // add the heading panel and other labels and fields in the main panel.
        mainPanel.add(headingPanel);
        mainPanel.add(id);
        mainPanel.add(idField);
        mainPanel.add(sizeLabel);
        mainPanel.add(sizes);
        mainPanel.add(guageLabel);
        mainPanel.add(guageField);
        mainPanel.add(rateLabel);
        mainPanel.add(rateField);
        mainPanel.add(buttonPanel);

        // add main panel to the frame.
        add(mainPanel);
        // set the new id to the id field.
        setId("guage_rate", idField);
        // set the sizes to the JCombo box.
        setSizes();
        setVisible(true);
    }

    /**
     * This method are create all labels for the form.
     */
    private void allocateToLabel() {
        id = new JLabel("ID :");
        id.setPreferredSize(labelSize);
        id.setFont(labelFont);

        guageLabel = new JLabel("Guage :");
        guageLabel.setPreferredSize(labelSize);
        guageLabel.setFont(labelFont);

        sizeLabel = new JLabel("Size :");
        sizeLabel.setPreferredSize(labelSize);
        sizeLabel.setFont(labelFont);

        rateLabel = new JLabel("Rate :");
        rateLabel.setPreferredSize(labelSize);
        rateLabel.setFont(labelFont);

        heading = new JLabel("Add Guage And Rate");
        heading.setFont(headingFont);
        heading.setAlignmentY(CENTER_ALIGNMENT);
    }

    /**
     * This method are create the text fields for form.
     */
    private void allocateToText() {
        idField = new JTextField(28);
        idField.setFont(fieldFont);
        idField.setEnabled(false);
        idField.setDisabledTextColor(Color.gray);
        idField.addKeyListener(this);
        idField.setFocusable(true);

        guageField = new JTextField(28);
        guageField.setFont(fieldFont);
        guageField.setFocusable(true);

        rateField = new JTextField(28);
        rateField.setFont(fieldFont);
        rateField.setFocusable(true);

        sizes = new JComboBox<String>();
        sizes.setPreferredSize(new Dimension(395, 30));
        sizes.setFont(fieldFont);
        sizes.setFocusable(true);

        // add key listeners to the fields ,when use press enter key then the focus will
        // switch.
        idField.addKeyListener(new CustomKeyListener(sizes));
        sizes.addKeyListener(new CustomKeyListener(guageField));
        guageField.addKeyListener(new CustomKeyListener(rateField));
    }

    /**
     * This method are set the sizes to JCombo box from DB.
     */
    private void setSizes() {
        try {
            String query = "SELECT * FROM sizes";
            ResultSet res = DBConnection.executeQuery(query);
            // create the has map collection framework objects that hold the size and there
            // ID's and wise versa.
            sizeIdMap = new HashMap<String, Integer>();
            idSizeMap = new HashMap<Integer, String>();
            // set sizes one by one to the combo box.
            while (res.next()) {
                sizeIdMap.put(res.getString("size"), res.getInt("id"));
                idSizeMap.put(res.getInt("id"), res.getString("size"));
                sizes.addItem(res.getString("size"));
            }
        } catch (Exception e) {
            DialogWindow.showErrorDialog(parent, "Connection error,try again!");
        }
    }

    public void actionPerformed(ActionEvent e) {
        // check the source is save button or not.
        if (e.getSource().equals(save)) {
            // check the action is save_action or not.
            if (action.equals(SAVE_ACTION)) {
                try {
                    // get the id of selected size from hash map.
                    int sizeId = sizeIdMap.get(sizes.getSelectedItem().toString());
                    double rateVal = Double.parseDouble(rateField.getText().trim());
                    double guageVal = Double.parseDouble(guageField.getText().trim());

                    // check the guage rate are exist or not.
                    String query = "SELECT * FROM guage_rate WHERE size_id = ? AND guage = ?";
                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                    pst.setInt(1, sizeId);
                    pst.setDouble(2, guageVal);
                    ResultSet result = pst.executeQuery();

                    if (!result.next()) {
                        // insert a new record into DB.
                        query = "INSERT INTO guage_rate (rate,guage,size_id,user_id)VALUES(?,?,?,101)";
                        pst = DBConnection.con.prepareStatement(query);
                        pst.setDouble(1, rateVal);
                        pst.setDouble(2, guageVal);
                        pst.setInt(3, sizeId);

                        boolean res = (pst.executeUpdate() > 0);
                        // execute when record are inserted successfully.
                        if (res) {
                            DialogWindow.showMessageDialog(parent, "Guage-rate added successful!");
                            sizeMenu.doClick();
                        } else {
                            DialogWindow.showMessageDialog(parent, "Guage-rate cannot added!");
                        }
                    } else {
                        DialogWindow.showErrorDialog(parent, "Guage-rate already exist!");
                    }

                } catch (Exception exc) {
                    DialogWindow.showErrorDialog(parent,
                            "Connection error,try again! or invalid values for guage and rate!");
                }
            }

            if (action.equals(UPDATE_ACTION)) {
                boolean res = DialogWindow.showConfirmDialog(parent, "Do you want to update this information!");
                // execute when confirmation are approved.
                if (res) {
                    try {
                        // get the id of selected size from hash map.
                        int sizeId = sizeIdMap.get(sizes.getSelectedItem().toString());
                        double rateVal = Double.parseDouble(rateField.getText().trim());
                        Double guageVal = Double.parseDouble(guageField.getText().trim());

                        String query = "SELECT * FROM guage_rate WHERE size_id = ? AND guage = ? AND id != ?";
                        PreparedStatement pst = DBConnection.con.prepareStatement(query);
                        pst.setInt(1, sizeId);
                        pst.setDouble(2, guageVal);
                        pst.setInt(3, guageRateIdForOperation);
                        ResultSet result = pst.executeQuery();

                        if (!result.next()) {
                            // update the record into DB.
                            query = "UPDATE guage_rate SET rate = ?, guage = ?,size_id = ? WHERE id = ?";
                            pst = DBConnection.con.prepareStatement(query);
                            pst.setDouble(1, rateVal);
                            pst.setDouble(2, guageVal);
                            pst.setInt(3, sizeId);
                            pst.setInt(4, guageRateIdForOperation);
                            res = (pst.executeUpdate() > 0);
                            // true when record update success.
                            if (res) {
                                DialogWindow.showMessageDialog(parent, "Guage-Rate updated successfully!");
                                sizeMenu.doClick();
                            } else {
                                DialogWindow.showMessageDialog(parent, "Guage-Rate cannot updated!");
                            }
                        } else {
                            DialogWindow.showErrorDialog(parent, "Guage-Rate already exist!");
                        }
                    } catch (Exception exc) {
                        DialogWindow.showErrorDialog(parent, "Connection error,try again or Invalid value of ID!");
                    }
                } else {
                    // execute when confirmation are rejected.
                    sizeMenu.doClick();
                }
            }

            // check the action is delete action or not.
            if (action.equals(DELETE_ACTION)) {
                boolean res = DialogWindow.showConfirmDialog(parent, "Do you want to delete this information!");
                // execute when confirmation are approved.
                if (res) {
                    try {
                        // delete record from DB.
                        String query = "DELETE FROM guage_rate WHERE id = ?";
                        PreparedStatement pst = DBConnection.con.prepareStatement(query);
                        pst.setInt(1, guageRateIdForOperation);
                        res = (pst.executeUpdate() > 0);
                        // true when record delete success.
                        if (res) {
                            DialogWindow.showMessageDialog(parent, "Guage-Rate deleted successfully!");
                            sizeMenu.doClick();
                        } else {
                            DialogWindow.showMessageDialog(parent, "Guage-Rate cannot deleted!");
                        }
                    } catch (Exception exc) {
                        DialogWindow.showErrorDialog(parent,
                                "Connection error or invalid value of ID!");
                    }
                } else {
                    // execute when confirmation are rejected.
                    sizeMenu.doClick();
                }
            }
        }
        // checks the source is delete button or edit button.
        if (e.getSource().equals(delete) || e.getSource().equals(edit)) {
            idField.setEnabled(true);
            idField.requestFocus();
            // set action to delete action when clicked button is delete otherwise update
            // action.
            if (e.getSource().equals(delete)) {
                action = DELETE_ACTION;
            } else {
                action = UPDATE_ACTION;
            }
        }

        // check the source is cancel button or not.
        if (e.getSource().equals(cancel)) {
            // when click on cancel then edit and delete buttons will enabled.
            edit.setEnabled(true);
            delete.setEnabled(true);
        }

        // check the source is exit button or not.
        if (e.getSource().equals(exit)) {
            // get user confirmation before exit the window.
            boolean res = DialogWindow.showConfirmDialog(parent, "Do you want to exit from here!");
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
            // get the row no of selected row.
            int row = guageRateView.table.getSelectedRow();
            if (row != -1) {
                try {
                    // get the value of id column of the selected row.
                    String val = guageRateView.table.getValueAt(row, 0).toString().trim();
                    int id = Integer.parseInt(val);
                    // get the record of selected id.
                    ResultSet resultSet = guageRateView.getSelectedData(id);
                    // set the record to the form.
                    setGuageRateIntoForm(resultSet);
                    // set invisible JDialog.
                    dataViewer.setVisible(false);
                } catch (Exception exc) {
                    DialogWindow.showErrorDialog(parent, "Connection error,try again!");
                }
            } else {
                DialogWindow.showErrorDialog(parent, "Please select a record for edit!");
            }
        }

    }

    public void keyReleased(KeyEvent e) {
        Object source = e.getSource();
        int keyCode = e.getKeyCode();
        String key = KeyEvent.getKeyText(keyCode);
        // check the source is id field and the pressed key is "Enter"
        if (source.equals(idField) && key.equals("Enter")) {
            try {
                // get the record of selected id from DB.
                String query = "SELECT * FROM guage_rate WHERE id = ?";
                PreparedStatement pst = DBConnection.con.prepareStatement(query);
                pst.setInt(1, Integer.parseInt(idField.getText().trim()));
                ResultSet res = pst.executeQuery();
                // set the record to the form.
                setGuageRateIntoForm(res);
            } catch (Exception exc) {
                DialogWindow.showErrorDialog(parent, "Connection error,try again!");
            }
        }

        // check the source is id field or not.
        if (source.equals(idField)) {
            // get the modifier text like "Ctrl","Alt",etc.
            String modifier = InputEvent.getModifiersExText(e.getModifiersEx());
            // check the modifier is "Alt" and key is 'V' or not.
            if (modifier.equals("Alt") && key.equals("V")) {
                // create the object of view.
                guageRateView = new GuageRateView();
                // create the JDialog with view.
                createViewer(null, null, guageRateView);
            }
        }
    }

    /**
     * This method are set the data to the form hold by ResultSet.
     * 
     * @param res a object of java.sql.ResultSet.
     * @throws Exception when result set throws SQLException.
     */
    private void setGuageRateIntoForm(ResultSet res) throws Exception {
        // check the record are found or not.
        if (res.next()) {
            // set the id.
            idField.setText(res.getObject("id").toString());
            guageRateIdForOperation = res.getInt("id");
            // get the size id.
            int id = res.getInt("size_id");
            // get the size using id from hash map.
            String size = idSizeMap.get(id);
            // set the size selected.
            sizes.setSelectedItem(size);
            // set the guage and rate .
            guageField.setText(Integer.toString(res.getInt("guage")));
            rateField.setText(Double.toString(res.getDouble("rate")));
        } else {
            // execute when no record found.
            DialogWindow.showMessageDialog(parent, "No record found!");
            sizeMenu.doClick();
        }
    }
}
/**
 * This component end here...
 */