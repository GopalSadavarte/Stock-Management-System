/**
 * 
 * This files contains the class SizeView,which can create a java UI table to view the 
 * all sizes in tabular format which are located into the DB.
 * 
 * 
 */

package components.report.partial;

import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import config.*;
import partial.*;
import partial.interfaces.*;

/**
 * The SizeView class are create a JTable with entire size records available in
 * DB.
 */
public class SizeView extends JPanel implements FontInterface {

    public JTable table;
    public JScrollPane scrollPane;
    DefaultTableModel tableModel;
    TableRowSorter<TableModel> sorter;

    /**
     * This method are create the table model and add the columns into the table
     * with scroll pane.
     */
    public void createSizeView() {
        /*
         * creating the object of DefaultTableModel by overriding the method
         * isCellEditable(),which allows
         * to set which cell make editable by user or not.
         */
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add an columns to table model.
        tableModel.addColumn("ID");
        tableModel.addColumn("Size");

        // create JTable object with table model.
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(labelFont);
        // create sorter for sorting the records of table model.
        sorter = new TableRowSorter<TableModel>(tableModel);
        // set sorter to table.
        table.setRowSorter(sorter);

        // Set the comparator to sorter for column no.0,which treat the column no.0
        // records as an integer.
        sorter.setComparator(0, new Comparator<Integer>() {
            public int compare(Integer first, Integer second) {
                return first.compareTo(second);
            }
        });

        // Get the reference of table header to modify it.
        JTableHeader header = table.getTableHeader();
        // Here,modify the table header.
        header.setFont(labelFont);
        header.setBackground(Color.white);
        header.setForeground(purple);

        // create the scroll pane for table for adding the scroll to the table when
        // needed.
        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.white);
        // set records to JTable.
        addRecordsIntoTable();
    }

    /**
     * This method are get the id of record and return the ResultSet of the given id
     * from DB.
     * 
     * @param id the size id of database table
     * @return java.sql.ResultSet
     */
    public ResultSet getSelectedData(int id) {
        ResultSet res = null;
        try {
            // get record from DB.
            String query = "SELECT * FROM sizes WHERE id=" + id;
            res = DBConnection.executeQuery(query);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return res;
    }

    /**
     * This method Get entire record related to sizes from DB and set it into the
     * JTable.
     */
    private void addRecordsIntoTable() {
        try {
            // Get records from DB by ascending order.
            String query = "SELECT * FROM sizes ORDER BY id ASC";
            ResultSet resultSet = DBConnection.executeQuery(query);
            // set records one by one to table model.
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String size = resultSet.getString("size");

                tableModel.addRow(new Object[] { id, size });
            }
        } catch (Exception e) {
            DialogWindow.showMessageDialog(this, "Connection error,try again!");
        }
    }
}
/**
 * This component end here...
 */