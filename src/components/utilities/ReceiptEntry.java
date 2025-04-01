/**
 * 
 * 
 * 
 * This Receipt entry component to allow user to add receipt entry which amount have been received by higher authority for business work.
 * 
 * 
 */
package components.utilities;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.toedter.calendar.*;
import config.*;
import partial.*;
import partial.interfaces.*;

/**
 * The class ReceiptEntry are create an component of receipt entry to add entry
 * information.
 */
public class ReceiptEntry extends JPanel implements FontInterface, ActionListener, TableModelListener, KeyListener {

    // instance variables.
    JPanel headingPanel, mainPanel, datePanel, formPanel, buttonPanel, tablePanel;
    JLabel heading, dateLabel, entryNoLabel, descriptionLabel, amountLabel, totalAmountLabel, totalEntriesLabel;
    JTextField entryNoField, descField, amountField, totalAmtField, totalEntriesField;
    JDateChooser dateChooser;
    JButton add, exit;
    JTable table;
    DefaultTableModel tableModel;
    TableRowSorter<TableModel> sorter;
    JScrollPane scrollPane;
    JMenuItem homeMenuItem;
    int sr_no = 1;

    /**
     * A constructor to create a receipt entry component.
     * 
     * @param homeMenu for navigating to home page.
     */
    public ReceiptEntry(JMenuItem homeMenu) {
        // assign reference of home menu.
        homeMenuItem = homeMenu;
        // GUI start here...
        setBackground(Color.white);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.setPreferredSize(new Dimension(800, 630));
        mainPanel.setBorder(border);
        mainPanel.setBackground(Color.white);

        headingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 5));
        headingPanel.setPreferredSize(new Dimension(790, 60));
        headingPanel.setBorder(border);
        headingPanel.setBackground(Color.white);

        heading = new JLabel("Receipt Entry");
        heading.setFont(headingFont);
        heading.setForeground(Color.darkGray);

        headingPanel.add(heading);

        datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        datePanel.setPreferredSize(new Dimension(250, 50));
        datePanel.setBackground(Color.white);

        dateLabel = new JLabel("Date :");
        dateLabel.setFont(labelFont);
        dateLabel.setForeground(Color.darkGray);
        dateLabel.setVerticalAlignment(SwingConstants.CENTER);

        dateChooser = new JDateChooser(calendar.getTime());
        dateChooser.setMaxSelectableDate(calendar.getTime());
        dateChooser.setPreferredSize(new Dimension(180, 40));
        dateChooser.setFont(labelFont);

        datePanel.add(dateLabel);
        datePanel.add(dateChooser);

        headingPanel.add(datePanel);

        mainPanel.add(headingPanel);

        formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        formPanel.setPreferredSize(new Dimension(605, 200));
        formPanel.setBackground(Color.white);

        entryNoLabel = new JLabel("Entry No. :");
        entryNoLabel.setPreferredSize(new Dimension(100, 20));
        entryNoLabel.setForeground(Color.darkGray);
        entryNoLabel.setFont(labelFont);

        entryNoField = new JTextField(30);
        entryNoField.setFont(labelFont);
        entryNoField.setEnabled(false);
        entryNoField.setText("1");

        amountLabel = new JLabel("Amount :");
        amountLabel.setForeground(Color.darkGray);
        amountLabel.setPreferredSize(new Dimension(100, 20));
        amountLabel.setFont(labelFont);

        amountField = new JTextField(30);
        amountField.setFont(labelFont);

        descriptionLabel = new JLabel("Description :");
        descriptionLabel.setForeground(Color.darkGray);
        descriptionLabel.setPreferredSize(new Dimension(100, 20));
        descriptionLabel.setFont(labelFont);

        descField = new JTextField(30);
        descField.setFont(labelFont);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPanel.setBackground(Color.white);
        buttonPanel.setPreferredSize(new Dimension(460, 40));

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

        formPanel.add(entryNoLabel);
        formPanel.add(entryNoField);
        formPanel.add(amountLabel);
        formPanel.add(amountField);
        formPanel.add(descriptionLabel);
        formPanel.add(descField);
        formPanel.add(buttonPanel);

        mainPanel.add(formPanel);

        tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tablePanel.setPreferredSize(new Dimension(760, 300));
        tablePanel.setBorder(border);
        tablePanel.setBackground(Color.white);

        /*
         * create table model with overriding the methods isCellEditable() and
         * fireTableCellUpdated() for custom implementation.
         */
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                // check column is 1 if yes then set it editable and other are not editable.
                return (column == 1);
            }

            public void fireTableCellUpdated(int row, int column) {
                try {
                    // get the updated cell value.
                    int amount = Integer.parseInt(tableModel.getValueAt(row, column).toString());
                    // get the updated cell record id.
                    int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    // query for update record.
                    String query = "UPDATE vouchers SET amount = ? WHERE receipt_voucher_no = ? AND status = 'Received'";
                    // create prepare statement.
                    PreparedStatement pst = DBConnection.con.prepareStatement(query);
                    pst.setInt(1, amount);
                    pst.setInt(2, id);

                    // execute query and get affected rows.
                    int affectedRows = pst.executeUpdate();
                    // check the record is update or not.
                    if (affectedRows > 0) {
                        // again calculate a total amount.
                        calculate();
                    } else {
                        // execute when updating record failed.
                        DialogWindow.showErrorDialog(ReceiptEntry.this, "Failed!");
                    }

                } catch (Exception e) {
                    // execute when exception in above code.
                    DialogWindow.showErrorDialog(ReceiptEntry.this, "Connection error OR Error while updating!");
                }
            }
        };
        // add listener to table model.
        tableModel.addTableModelListener(this);

        tableModel.addColumn("Sr.No.");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Description");
        tableModel.addColumn("Date");

        // create JTable with table model.
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(labelFont);
        table.addKeyListener(this);

        // create a row sorter for sorting an records.
        sorter = new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(sorter);

        // get table header for modifying it.
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.white);
        header.setForeground(purple);
        header.setFont(labelFont);

        // create scroll pane for scroll functionality on table.
        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setPreferredSize(new Dimension(750, 290));

        tablePanel.add(scrollPane);

        totalEntriesLabel = new JLabel("Total Entries :");
        totalEntriesLabel.setFont(labelFont);

        totalEntriesField = new JTextField(15);
        totalEntriesField.setFont(labelFont);
        totalEntriesField.setEnabled(false);
        totalEntriesField.setDisabledTextColor(Color.darkGray);
        totalEntriesField.setText("0");

        totalAmountLabel = new JLabel("Total Amount :");
        totalAmountLabel.setFont(labelFont);

        totalAmtField = new JTextField(15);
        totalAmtField.setFont(labelFont);
        totalAmtField.setEnabled(false);
        totalAmtField.setDisabledTextColor(Color.darkGray);
        totalAmtField.setText("0");

        mainPanel.add(tablePanel);
        mainPanel.add(totalEntriesLabel);
        mainPanel.add(totalEntriesField);
        mainPanel.add(totalAmountLabel);
        mainPanel.add(totalAmtField);

        add(mainPanel);

        setVisible(true);
        getDataFromDB();
    }

    /**
     * this method are get all receipt voucher data from database.
     */
    private void getDataFromDB() {
        try {
            // query to get records.
            String query = "SELECT * FROM vouchers WHERE status = 'Received' ORDER BY receipt_voucher_no ASC";
            ResultSet resultSet = DBConnection.executeQuery(query);
            /*
             * status for serial numbers if record are found then set new serial numbers
             * otherwise set serial number to 1.
             */
            boolean status = false;
            // loop one by one record and set it into the table.
            while (resultSet.next()) {
                int id = resultSet.getInt("receipt_voucher_no");
                int amount = resultSet.getInt("amount");
                String desc = resultSet.getString("description");
                String date = resultSet.getString("date");
                // add new row.
                tableModel
                        .addRow(new Object[] {
                                id, amount, desc, date
                        });
                // check the record is last record or not.
                if (resultSet.isLast()) {
                    // assign new serial number.
                    sr_no = id + 1;
                    // set new entry number.
                    entryNoField.setText(Integer.toString(sr_no));
                    // get last row number.
                    int row = resultSet.getRow();
                    // set the row number(total number of rows).
                    totalEntriesField.setText("" + row);
                    // set status to true means have at least one record into DB.
                    status = true;
                }
            }
            // set sr_no to 1 when no record in DB.
            if (!status) {
                sr_no = 1;
            }

        } catch (Exception e) {
            // execute when exception in above code.
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // check the source is add button or not.
        if (source.equals(add)) {
            try {
                // get the values.
                int amount = Integer.parseInt(amountField.getText());
                String desc = descField.getText().trim();
                desc = desc.length() <= 0 ? "-" : desc;
                String date = format.format(dateChooser.getDate());

                // query to insert a new receipt entry.
                String query = "INSERT INTO vouchers (status,payment_state,description,amount,user_id,date,created_at,updated_at,receipt_voucher_no)VALUES('Received','Success',?,?,101,'"
                        + date + "',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?)";
                PreparedStatement pst = DBConnection.con.prepareStatement(query);
                pst.setString(1, desc);
                pst.setInt(2, amount);
                pst.setInt(3, sr_no);

                // execute query and get affected rows.
                int affectedRows = pst.executeUpdate();
                // check the affected rows are greater then zero means the row are added
                // successfully.
                if (affectedRows > 0) {
                    // add row in to the table model.
                    tableModel.addRow(new Object[] { sr_no, amount, desc, date });
                    // set new entry number.
                    entryNoField.setText(Integer.toString(++sr_no));
                    // set total entries.
                    totalEntriesField.setText(Integer.toString(Integer.parseInt(totalEntriesField.getText()) + 1));
                } else {
                    // execute when record does not added.
                    DialogWindow.showErrorDialog(this, "Failed!");
                }

            } catch (Exception exc) {
                // execute when an exception are occur in above code.
                DialogWindow.showErrorDialog(this, "Connection error OR Invalid amount value!");
            }
        }
        // check the source is exit button or not.
        if (source.equals(exit)) {
            // get user confirmation before exit.
            boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to exit from here..!");
            if (res) {
                homeMenuItem.doClick();
            }
        }
    }

    // this method are calculate the total amount of sum of each row amount.
    private void calculate() {
        // get the number of rows.
        int rows = tableModel.getRowCount();
        int totalAmount = 0;
        // calculate the total amount.
        for (int i = 0; i < rows; i++) {
            int val = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
            totalAmount += val;
        }
        // set a total amount.
        totalAmtField.setText(Integer.toString(totalAmount));
    }

    public void tableChanged(TableModelEvent e) {
        if (totalAmtField != null) {
            calculate();
        }
    }

    public void keyTyped(KeyEvent e) {
        //
    }

    public void keyReleased(KeyEvent e) {
        // get the typed key in string .
        String key = KeyEvent.getKeyText(e.getKeyCode());
        // get the modifier key means Alt,Ctrl,etc.
        String modifier = InputEvent.getModifiersExText(e.getModifiersEx());
        // check the key is "D" and modifier is "Ctrl"(Ctrl+D shortcut).
        if (key.equals("D") && modifier.equals("Ctrl")) {
            try {
                // get the selected row.
                int row = table.getSelectedRow();
                // get is of selected row.
                int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                // get user confirmation before deletion.
                boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to remove this record!");
                if (res) {
                    // query to delete the record.
                    String query = "DELETE FROM vouchers WHERE receipt_voucher_no =" + id + " AND status = 'Received'";
                    res = DBConnection.execute(query);
                    // check the query are executed successfully.
                    if (res) {
                        // remove record from table model.
                        tableModel.removeRow(row);
                        // check the table model is empty or not,if it is empty then set serial number
                        // to 1 and row count to zero.
                        if (tableModel.getRowCount() == 0) {
                            sr_no = 1;
                            entryNoField.setText("" + sr_no);
                        }
                        totalEntriesField.setText(Integer.toString(tableModel.getRowCount()));
                    } else {
                        // execute when record did not removed.
                        DialogWindow.showErrorDialog(this, "Failed to remove!");
                    }
                }
            } catch (Exception exc) {
                // execute when an exception in above code.
                DialogWindow.showErrorDialog(this, "Connection error,try again!");
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        //
    }
}
/**
 * This component are end here...
 */