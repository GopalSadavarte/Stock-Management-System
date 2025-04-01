/***
 * 
 * This file contains the code of stock entry component of the project ,which allow 
 * user to handle the entries with there CRUD using the javax.swing and java.awt components
 * and the java events.
 * 
 */

package components.stocks;

import javax.swing.*;
import com.toedter.calendar.*;
import config.*;
import partial.*;
import partial.event.*;
import partial.interfaces.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;

/**
 * This Class are the component which is UI for inserting,updating and deleting
 * the stock entries into the database by user.
 */
public class StockEntry extends JPanel
        implements ActionListener, FocusListener, KeyListener, TableModelListener, PropertyChangeListener,
        ItemListener, FontInterface {

    // panels that are holds the other components.
    JPanel form, contentPanel, bottomPanel, headingPanel, basePanel, mainPanel;
    // text fields for inputs.
    JTextField quantity, weight, counterNo, entryNo, totalEntries, totalAmount, totalQuantity,
            totalWeight, previousEntryAmount;
    // labels for input fields.
    JLabel sizeLabel, quantityLabel, weightLabel, guageLabel, heading, isSmallLabel, entryDateLabel, rateLabel,
            counterNoLabel, entryNoLabel, totalEntryLabel, totalAmtLabel, totalQtyLabel, totalWeightLabel,
            prevEntryAmtLabel;
    // Date picker for selecting date by user.
    JDateChooser dateChooser;
    // scroll pane holds the table.
    JScrollPane addedDataScrollPane;
    // the JTable are represent the data in row and column format.
    JTable table;
    // The table model,which allows to construct table easily.
    DefaultTableModel tableModel;
    // combo boxes for selecting values.
    JComboBox<Boolean> isSmall;
    JComboBox<Integer> guage;
    JComboBox<String> size;
    Integer sr_no = 0;
    Double tAmount = 0d;
    // hash map holds the key-value for guage and there corresponding rate.
    HashMap<String, Integer> sizeAndIdHashMap;
    // sorter for sorting the table ascending or descending order.
    TableRowSorter<TableModel> sorter;
    Dimension labelSize = new Dimension(100, 20);
    Dimension comboBoxSize = new Dimension(130, 25);

    public StockEntry() {
        // the GUI are start here.
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(new Color(247, 246, 246)));

        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        mainPanel.setPreferredSize(new Dimension(1250, 600));
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(border);

        headingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        heading = new JLabel("Stock Entry");
        heading.setFont(headingFont);
        headingPanel.setPreferredSize(new Dimension(1150, 40));
        headingPanel.setBackground(Color.white);
        heading.setForeground(Color.DARK_GRAY);
        headingPanel.add(heading);

        mainPanel.add(headingPanel);

        // this panel holds an top text fields
        basePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        basePanel.setBackground(Color.white);

        counterNoLabel = new JLabel("Counter No. :");
        counterNoLabel.setFont(labelFont);
        counterNoLabel.setPreferredSize(labelSize);
        counterNo = new JTextField("1");
        counterNo.setColumns(10);
        counterNo.setFont(labelFont);
        counterNo.setEnabled(false);

        entryNoLabel = new JLabel("Entry No. :");
        entryNoLabel.setFont(labelFont);
        entryNoLabel.setPreferredSize(labelSize);
        entryNo = new JTextField("1");
        entryNo.setColumns(10);
        entryNo.setFont(labelFont);
        entryNo.setEnabled(false);
        entryNo.setFocusable(true);
        entryNo.addFocusListener(this);
        entryNo.addKeyListener(this);
        // this method get and set the entry number from DB
        getAndSetEntryNo();

        entryDateLabel = new JLabel("Date :");
        entryDateLabel.setFont(labelFont);
        entryDateLabel.setPreferredSize(labelSize);
        // JDateChooser are create a date picker
        dateChooser = new JDateChooser(calendar.getTime());
        dateChooser.setFont(labelFont);
        dateChooser.setPreferredSize(comboBoxSize);
        dateChooser.setMaxSelectableDate(calendar.getTime());
        dateChooser.addPropertyChangeListener("date", this);

        basePanel.add(counterNoLabel);
        basePanel.add(counterNo);
        basePanel.add(entryNoLabel);
        basePanel.add(entryNo);
        basePanel.add(entryDateLabel);
        basePanel.add(dateChooser);

        // base panel add in main panel
        mainPanel.add(basePanel);

        // The form are start here.
        form = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        form.setBackground(Color.white);
        form.setSize(new Dimension(600, 50));

        sizeLabel = new JLabel("Size :");
        sizeLabel.setFont(labelFont);
        sizeLabel.setPreferredSize(labelSize);
        size = new JComboBox<String>();
        size.setFocusable(true);
        size.setPreferredSize(comboBoxSize);
        size.setFont(labelFont);
        // get and set values for guage from DB
        getAndSetSizes();
        size.addItemListener(this);

        quantityLabel = new JLabel("Bags :");
        quantityLabel.setFont(labelFont);
        quantityLabel.setPreferredSize(labelSize);
        quantity = new JTextField();
        quantity.setColumns(10);
        quantity.setFont(labelFont);
        quantity.setFocusable(true);
        quantity.addFocusListener(this);

        weightLabel = new JLabel("Weight :");
        weightLabel.setFont(labelFont);
        weightLabel.setPreferredSize(labelSize);
        weight = new JTextField();
        weight.setColumns(10);
        weight.setFont(labelFont);
        weight.setFocusable(true);
        weight.addFocusListener(this);
        weight.addActionListener(this);

        guageLabel = new JLabel("Guage :");
        guageLabel.setFont(labelFont);
        guageLabel.setPreferredSize(labelSize);
        guage = new JComboBox<Integer>();
        guage.setPreferredSize(comboBoxSize);
        guage.setFont(labelFont);
        guage.setFocusable(true);

        isSmallLabel = new JLabel("Is small :");
        isSmallLabel.setFont(labelFont);
        isSmallLabel.setPreferredSize(labelSize);
        isSmall = new JComboBox<Boolean>();
        isSmall.addItem(true);
        isSmall.addItem(false);
        isSmall.setPreferredSize(comboBoxSize);
        isSmall.setEnabled(false);

        form.add(sizeLabel);
        form.add(size);
        form.add(quantityLabel);
        form.add(quantity);
        form.add(isSmallLabel);
        form.add(isSmall);
        form.add(guageLabel);
        form.add(guage);
        form.add(weightLabel);
        form.add(weight);

        // form are added.
        mainPanel.add(form);

        // The custom key listener are added which move focus to next element
        size.addKeyListener(new CustomKeyListener(quantity));
        quantity.addKeyListener(new CustomKeyListener(guage));
        guage.addKeyListener(new CustomKeyListener(weight));
        weight.addKeyListener(new CustomKeyListener(size));

        // The main panel which contains the table with entry records.
        contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(1230, 380));
        contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        contentPanel.setBackground(Color.white);
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.gray));

        // create an object with overriding the methods of class
        tableModel = new DefaultTableModel() {
            // this method are override with custom functionality,it allows columns are
            // editable or not
            public boolean isCellEditable(int row, int column) {

                // set 0,1,4,5,6,7 columns aren't editable and other are editable.
                return (column == 2 || column == 3);
            }

            // this method are fire when the table cell values are updated,this also
            // override.
            public void fireTableCellUpdated(int row, int column) {
                updateIntoDB(row);
                try {
                    // validating the valid guage and quantity are get or not in the entry.
                    Double.parseDouble(tableModel.getValueAt(row, 2).toString().trim());
                    Integer.parseInt(tableModel.getValueAt(row, 4).toString().trim());
                } catch (Exception exc) {
                    DialogWindow.showErrorDialog(StockEntry.this, "Invalid values for guage and bags!");
                }
                try {
                    // checks the column no 3 updated or not.
                    if (column == 3) {
                        double weight = Double.parseDouble(tableModel.getValueAt(row, column).toString().trim());
                        String guage = tableModel.getValueAt(row, 4).toString().trim();
                        String size = tableModel.getValueAt(row, 1).toString().trim();
                        double rate = 0;

                        int size_id = sizeAndIdHashMap.get(size);

                        String query = "SELECT * FROM guage_rate WHERE guage = ? AND size_id = ? LIMIT 1";
                        PreparedStatement pst = DBConnection.con.prepareStatement(query);
                        pst.setInt(1, Integer.parseInt(guage));
                        pst.setInt(2, size_id);

                        ResultSet result = pst.executeQuery();

                        if (result.next()) {
                            rate = result.getDouble("rate");
                        }

                        double ans = weight * rate;
                        // Sets an total value after multiplying the values with 2 precision value.
                        tableModel.setValueAt(String.format("%.2f", ans), row, 7);
                    }
                } catch (Exception exc) {
                    DialogWindow.showErrorDialog(StockEntry.this, "Invalid values for rate or weight!");
                }
                // Again calculate entire total.
                callCalculate();
            }

            // Fire when table row are updated.
            public void fireTableRowsUpdated(int row, int column) {
                // Again calculate entire total.
                callCalculate();
            }
        };
        tableModel.addColumn("Sr.No.");
        tableModel.addColumn("Size");
        tableModel.addColumn("Bags (NOS)");
        tableModel.addColumn("Weight (KG)");
        tableModel.addColumn("Guage (GSM)");
        tableModel.addColumn("Date");
        tableModel.addColumn("Is Small");
        tableModel.addColumn("Total");

        // Add table model listener when table are changed.
        tableModel.addTableModelListener(this);
        // create a sorter instance which allows sort the table.
        sorter = new TableRowSorter<TableModel>(tableModel);

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(labelFont);
        table.setSize(new Dimension(1210, 200));
        table.setBorder(BorderFactory.createLineBorder(Color.black));
        table.addKeyListener(this);
        table.setRowSorter(sorter);

        // this method are return an table header to modify the header style.
        JTableHeader h = table.getTableHeader();
        h.setFont(tableColumnFont);

        // Add table on scroll pane with scrolling allow if needed.
        addedDataScrollPane = new JScrollPane(table);
        addedDataScrollPane.getViewport().setBackground(Color.white);
        addedDataScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        addedDataScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addedDataScrollPane.setPreferredSize(new Dimension(1220, 365));

        contentPanel.add(addedDataScrollPane);
        // The main panel are added.
        mainPanel.add(contentPanel);

        // The bottom panel which holds the additional information of entry.
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        bottomPanel.setBackground(Color.white);
        bottomPanel.setBorder(BorderFactory.createLineBorder(new Color(216, 216, 216)));

        prevEntryAmtLabel = new JLabel("Last Entry Amount :");
        prevEntryAmtLabel.setFont(labelFont);
        prevEntryAmtLabel.setBorder(emptyBorderLeft);
        previousEntryAmount = new JTextField("0");
        previousEntryAmount.setFont(labelFont);
        previousEntryAmount.setColumns(9);
        previousEntryAmount.setEnabled(false);
        previousEntryAmount.setDisabledTextColor(Color.gray);

        totalEntryLabel = new JLabel("Total Entries :");
        totalEntryLabel.setFont(labelFont);
        totalEntryLabel.setBorder(emptyBorderLeft);
        totalEntries = new JTextField();
        totalEntries.setFont(labelFont);
        totalEntries.setColumns(7);
        totalEntries.setEnabled(false);
        totalEntries.setDisabledTextColor(Color.gray);
        totalEntries.setText("0");

        totalWeightLabel = new JLabel("Total Weight :");
        totalWeightLabel.setFont(labelFont);
        totalWeightLabel.setBorder(emptyBorderLeft);
        totalWeight = new JTextField();
        totalWeight.setFont(labelFont);
        totalWeight.setColumns(7);
        totalWeight.setEnabled(false);
        totalWeight.setDisabledTextColor(Color.gray);
        totalWeight.setText("0");

        totalQtyLabel = new JLabel("Total Quantity :");
        totalQtyLabel.setFont(labelFont);
        totalQtyLabel.setBorder(emptyBorderLeft);
        totalQuantity = new JTextField();
        totalQuantity.setFont(labelFont);
        totalQuantity.setColumns(7);
        totalQuantity.setEnabled(false);
        totalQuantity.setDisabledTextColor(Color.gray);
        totalQuantity.setText("0");

        totalAmtLabel = new JLabel("Total Amount :");
        totalAmtLabel.setFont(labelFont);
        totalAmtLabel.setBorder(emptyBorderLeft);
        totalAmount = new JTextField();
        totalAmount.setFont(buttonFont);
        totalAmount.setColumns(9);
        totalAmount.setEnabled(false);
        totalAmount.setDisabledTextColor(Color.black);
        totalAmount.setText("0");

        bottomPanel.add(prevEntryAmtLabel);
        bottomPanel.add(previousEntryAmount);
        bottomPanel.add(totalEntryLabel);
        bottomPanel.add(totalEntries);
        bottomPanel.add(totalWeightLabel);
        bottomPanel.add(totalWeight);
        bottomPanel.add(totalQtyLabel);
        bottomPanel.add(totalQuantity);
        bottomPanel.add(totalAmtLabel);
        bottomPanel.add(totalAmount);
        bottomPanel.setVisible(true);

        mainPanel.add(bottomPanel);
        add(mainPanel);
        getAndSetAllRecords();
        itemStateChanged(null);
    }

    /**
     * This function are perform action when clicking on the buttons
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // if the action are occur on weight input then it add the record in the table
        if (source.equals(weight)) {
            try {
                // when all fields are not empty then add record in the table
                if (!isFieldsBlank()) {
                    String sizeVal = size.getSelectedItem().toString();
                    String quantityVal = quantity.getText().trim();
                    double weightVal = Double.parseDouble(weight.getText().trim());
                    String guageVal = guage.getSelectedItem().toString().trim();
                    String ob = isSmall.getSelectedItem().toString();

                    String query = "SELECT * FROM guage_rate WHERE guage = ? AND size_id = ? LIMIT 1";
                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                    pst.setInt(1, Integer.parseInt(guageVal));
                    pst.setInt(2, sizeAndIdHashMap.get(sizeVal));

                    ResultSet result = pst.executeQuery();
                    double rate = 0;
                    if (result.next()) {
                        rate = result.getDouble("rate");
                    }

                    double total = rate * weightVal;
                    tAmount = tAmount + total;
                    totalAmount.setText(String.format("%.2f", tAmount));
                    sr_no = sr_no + 1;
                    String selectedDate = format.format(dateChooser.getDate());
                    boolean res = true;
                    if (isRecordExist(sizeVal, selectedDate, guageVal)) {
                        res = DialogWindow.showConfirmDialog(this, "Record Already Exist! Are you sure to continue.");
                    }
                    if (res && insertIntoDB(rate)) {
                        getAndSetEntryNo();
                        // this is add a new row into table
                        tableModel.addRow(
                                new Object[] { sr_no, sizeVal, quantityVal, weightVal, guageVal,
                                        selectedDate, ob,
                                        String.format("%.2f", total) });
                        quantity.setText("");
                        weight.setText("");
                        previousEntryAmount.setText(String.format("%.2f", total));
                        callCalculate();
                    }
                } else {
                    DialogWindow.showErrorDialog(this, "All fields are required!");
                }
            } catch (Exception exc) {
                DialogWindow.showErrorDialog(this,
                        "Invalid values for weight or bags \n OR \n size and guage does not selected!");
            }
        }
    }

    /**
     * check the size with same date and guage are already exist or not.
     * 
     * @param size  a string value a size.
     * @param date  a string value of selected date.
     * @param guage a string value of guage.
     * @return boolean
     */
    private boolean isRecordExist(String size, String date, String guage) {
        try {
            // query for checking the record.
            String query = "SELECT * FROM \"stocks\" WHERE size=? AND entry_month='" + date + "' AND guage=?";
            PreparedStatement st = DBConnection.con.prepareStatement(query);
            st.setString(1, size);
            st.setInt(2, Integer.parseInt(guage));

            ResultSet res = st.executeQuery();
            // if record found then return true means record already exist.
            return res.next();
        } catch (Exception e) {
            DialogWindow.showMessageDialog(this, "Connection error,try again!");
        }
        return false;
    }

    /**
     * This function are execute when the focus are gained on the particular UI
     * component, which have the listener are applied.
     */
    public void focusGained(FocusEvent e) {
        // checks the resource of event instance.
        if (e.getSource() instanceof JTextField) {
            JTextField f = ((JTextField) e.getSource());
            f.setBackground(new Color(238, 238, 238));
        }
    }

    /**
     * This function are execute when the focus are lost of the particular UI
     * component ,which have the listener are applied.
     */
    public void focusLost(FocusEvent e) {
        // checks the instance of the event resource
        if (e.getSource() instanceof JTextField) {
            JTextField f = ((JTextField) e.getSource());
            f.setBackground(Color.white);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    /**
     * This method will execute when the key released event are occur.
     */
    public void keyReleased(KeyEvent e) {
        Object source = e.getSource();
        String key = KeyEvent.getKeyText(e.getKeyCode());
        // Checks the source component is JTable or not.
        if (source.equals(table)) {
            /*
             * The InputEvent are class for all mouse and keyboard inputs.
             * The getModifiersExText() are return the name of modifier key like
             * Ctrl,Alt,Shift,etc.in text format.
             */
            String modifier = InputEvent.getModifiersExText(e.getModifiersEx());
            // checks the Ctrl+D shortcut on table for removing the particular row from
            // table.
            if (key.equals("D") && modifier.equals("Ctrl")) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    // removeRow() method removes the particular row from table.
                    boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to remove this record!");
                    if (res) {
                        if (deleteFromDB(row)) {
                            tableModel.removeRow(row);
                        }
                    }
                    size.requestFocus();
                }
            }
        }
    }

    /**
     * This method are execute when the table are changed.
     */
    public void tableChanged(TableModelEvent e) {
        // calculate all total if the particular row are removed.
        if (e.getType() == TableModelEvent.DELETE) {
            callCalculate();
        }
        // calculate all total if the particular row are inserted.
        if (e.getType() == TableModelEvent.INSERT) {
            callCalculate();
        }
    }

    /**
     * This method are calculates the total of row and columns and display total
     * amount of all entries which
     * are mentioned into the total amount text field.
     */
    private void callCalculate() {
        // gets the row count of table model.
        int totalRows = tableModel.getRowCount();
        tAmount = 0d;
        // this loop calculate the total amount.
        for (int i = 0; i < totalRows; i++) {
            String val = tableModel.getValueAt(i, 7).toString();
            tAmount = tAmount + Double.parseDouble(val);
        }
        // display the total amount into totalAmount text field with 2 precision values.
        totalAmount.setText(((Object) Math.round(tAmount)).toString());
        double tWeight = 0d;
        double totalQty = 0;
        // this loop calculate the total weight and total guage of entry and display it
        // respectively.
        for (int i = 0; i < totalRows; i++) {
            String val1 = tableModel.getValueAt(i, 2).toString();
            double qtyVal = Double.parseDouble(val1);
            String val2 = tableModel.getValueAt(i, 3).toString();
            double weightVal = Double.parseDouble(val2);

            tWeight = tWeight + weightVal;
            totalQty = totalQty + qtyVal;
        }
        // The total wight,quantity and rows are display into the text fields.
        totalQuantity.setText(String.format("%.2f", totalQty));
        totalWeight.setText(String.format("%.2f", tWeight));
        totalEntries.setText(Integer.toString(totalRows));
    }

    /**
     * This method are get the last entry number of the current date and set into
     * entry no text field.
     */
    private void getAndSetEntryNo() {
        try {
            // The query will get the entry no from DB by date.
            String query = "SELECT * FROM \"stocks\" WHERE \"entry_month\"=CURRENT_DATE ORDER BY \"day_wise_entry_no\" DESC LIMIT 1";
            ResultSet res = DBConnection.executeQuery(query);
            // if any entry are exist into DB of current date then sets the new entry number
            // to entry no text field,otherwise the entry no is 1.
            if (res.next()) {
                entryNo.setText(Integer.toString(res.getInt("day_wise_entry_no") + 1));
            } else {
                // entry no 1 ,if no record found.
                entryNo.setText("1");
            }
        } catch (Exception exc) {
            DialogWindow.showMessageDialog(this, "Connection error,try again!");
        }
    }

    /**
     * This method are return boolean value if all fields are fill of the form of
     * entry.
     * 
     * @return boolean
     */
    private boolean isFieldsBlank() {
        // check all fields.
        return (quantity.getText().isBlank() || weight.getText().isBlank());
    }

    /**
     * This method are executed when the item change event are occur.
     */
    public void itemStateChanged(ItemEvent e) {
        if (size.getSelectedItem() == null) {
            DialogWindow.showMessageDialog(this, "No records of size found!");
            return;
        }
        guage.removeAllItems();
        // convert string to char[]
        char ch = size.getSelectedItem().toString().toCharArray()[0];
        int limit = Integer.parseInt(Character.toString(ch));
        /*
         * check a limit if expression result are true then set selected item true
         * otherwise false.
         */
        isSmall.setSelectedItem(limit > 1 && limit <= 4.5);
        try {
            String s = size.getSelectedItem().toString();
            int id = sizeAndIdHashMap.get(s);
            // get the rate and guages related to this size.
            String query = "SELECT * FROM \"guage_rate\" WHERE \"size_id\"=" + id;
            ResultSet res = DBConnection.executeQuery(query);
            // add guages one by one into combo box.
            while (res.next()) {
                guage.addItem(res.getInt("guage"));
            }
        } catch (Exception exc) {
            DialogWindow.showMessageDialog(this, "Connection error,try again!");
        }
    }

    /**
     * This method are gets the records of guage-rate from DB and set into HashMap.
     */
    private void getAndSetSizes() {
        try {
            String query = "SELECT * FROM \"sizes\"";

            ResultSet res = DBConnection.executeQuery(query);
            // HashMap,which are store the data in key-value pair format.
            sizeAndIdHashMap = new HashMap<String, Integer>();
            // sizeIdAndRateGuageHashMap = new HashMap<Integer, String>();
            while (res.next()) {
                // here the size is a key for hashMap and there id is value for this key.
                String sizeVal = res.getString("size");
                // add an item to the guage combo box.
                size.addItem(sizeVal);
                // put the key value into hashMap.
                sizeAndIdHashMap.put(sizeVal, res.getInt("id"));
            }
        } catch (Exception exc) {
            DialogWindow.showMessageDialog(this, "Connection error,try again!");
        }
    }

    /**
     * This method are execute when the property of particular field are changed for
     * exp,there value will be change,
     * then this method will execute.
     */
    public void propertyChange(PropertyChangeEvent e) {
        // checks the source of event
        if (e.getSource().equals(dateChooser)) {
            // gets the new value of component.
            java.util.Date date = ((java.util.Date) e.getNewValue());
            String formattedDate = format.format(date);
            // Gets an last entry no of the selected date from DB.
            String query = "SELECT \"day_wise_entry_no\" FROM \"stocks\" WHERE \"entry_month\"='" + formattedDate
                    + "' ORDER BY \"day_wise_entry_no\" DESC LIMIT 1";
            try {
                ResultSet res = DBConnection.executeQuery(query);
                // if the records are found.
                if (res.next()) {
                    // record found then the new entry no will set to the entry no text field.
                    entryNo.setText(Integer.toString(res.getInt("day_wise_entry_no") + 1));
                } else {
                    // if record not found then the entry no will set to 1.
                    entryNo.setText("1");
                }
            } catch (Exception exc) {
                DialogWindow.showMessageDialog(this, "Connection error,try again!");
            }
        }
    }

    /**
     * This method are insert an new record into the Database.
     * 
     * @return boolean
     */
    private boolean insertIntoDB(double rate) {
        try {
            // save records into DB.

            String insert = "INSERT INTO\"stocks\"(\"day_wise_entry_no\",\"size\",\"size_type\",\"rate\",\"bag\",\"weight\",\"guage\",\"is_small\",\"entry_month\",\"created_at\",\"updated_at\",\"user_id\")VALUES(?,?,?,?,?,?,?,?,'"
                    + format.format(dateChooser.getDate()) + "',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?)";
            PreparedStatement preparedStatement = DBConnection.con.prepareStatement(insert);

            preparedStatement.setInt(1, Integer.parseInt(entryNo.getText().trim()));
            preparedStatement.setString(2, size.getSelectedItem().toString());
            preparedStatement.setString(3,
                    isSmall.getSelectedItem().toString().equals("true") ? "small" : "big");
            preparedStatement.setDouble(4, rate);
            preparedStatement.setDouble(5, Double.parseDouble(quantity.getText().trim()));
            preparedStatement.setDouble(6, Double.parseDouble(weight.getText().trim()));
            preparedStatement.setInt(7, Integer.parseInt(guage.getSelectedItem().toString()));
            preparedStatement.setBoolean(8,
                    Boolean.parseBoolean(isSmall.getSelectedItem().toString()));
            preparedStatement.setInt(9, 101);

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (Exception exc) {
            DialogWindow.showMessageDialog(this,
                    "Connection error,try again \n OR \n Invalid values for rate,quantity and weight!");
        }
        return false;
    }

    /**
     * This method are delete the record from Database.
     * 
     * @param row a row number of table model.
     * @return boolean
     */
    private boolean deleteFromDB(int row) {
        boolean res = false;
        try {
            // get the value of id for delete from table model and concat it to query.
            String query = "DELETE FROM \"stocks\" WHERE \"id\"=" + tableModel.getValueAt(row, 0).toString();
            res = DBConnection.execute(query);
        } catch (Exception exc) {
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }
        return res;
    }

    /**
     * This method are update a record into the database.
     * 
     * @param row a row number of table model.
     * @return boolean
     */
    private boolean updateIntoDB(int row) {
        // get the value of id in which row are updated.
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        try {
            String query = "UPDATE stocks SET bag=?,weight=? WHERE id=?";
            PreparedStatement st = DBConnection.con.prepareStatement(query);
            st.setDouble(1, Double.parseDouble(tableModel.getValueAt(row, 2).toString()));
            st.setDouble(2, Double.parseDouble(tableModel.getValueAt(row, 3).toString()));
            st.setInt(3, id);

            // update into Database.
            int res = st.executeUpdate();
            return res > 0;
        } catch (Exception exc) {
            DialogWindow.showMessageDialog(this,
                    "Connection error \nOR\n Invalid values for bag,rate,guage and weight for update!");
        }
        return false;
    }

    /**
     * This method are get all records from DB and set it into the table model.
     */
    private void getAndSetAllRecords() {
        try {
            String query = "SELECT * FROM \"stocks\"";
            ResultSet res = DBConnection.executeQuery(query);
            while (res.next()) {
                sr_no = res.getInt("id");
                double rate = res.getDouble("rate");
                double weight = res.getDouble("weight");
                tableModel.addRow(new Object[] {
                        sr_no,
                        res.getString("size"),
                        res.getDouble("bag"),
                        weight,
                        res.getInt("guage"),
                        res.getString("entry_month"),
                        res.getBoolean("is_small"),
                        String.format("%.2f", (rate * weight))
                });
                if (res.isLast()) {
                    previousEntryAmount.setText(String.format("%.2f", rate * weight));
                }
            }
        } catch (Exception exc) {
            DialogWindow.showMessageDialog(this, "Connection error,try again!");
        }
    }
}
/**
 * This component are ends here...
 */