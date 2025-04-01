/**
 * 
 * 
 * This files contains the data of guage-rate in tabular format ,that are 
 * useful to generate the report or other operation related to guage and rate.
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
 * This class have the data of Guage-Rate table in tabular format using
 * JTable,TableModel and
 * JScrollPane.
 */
public class GuageRateView extends JPanel implements FontInterface {

    public JTable table;
    public JScrollPane scrollPane;
    DefaultTableModel tableModel;
    TableRowSorter<TableModel> sorter;

    /**
     * This method are create the table with guage-rate data from DB.
     */
    public void createGuageRateView() {
        /*
         * creating a object of DefaultTableModel by overriding the method
         * isCellEditable(),which
         * allow to set which cell are editable by user or not.
         */
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Adds an columns to the table model.
        tableModel.addColumn("ID");
        tableModel.addColumn("Size");
        tableModel.addColumn("Guage");
        tableModel.addColumn("Rate");

        // create JTable with table model.
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(labelFont);
        // assign table sorter to sort the table
        sorter = new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(sorter);

        // the comparator for column no.0 for sorting.
        sorter.setComparator(0, new Comparator<Integer>() {
            public int compare(Integer first, Integer second) {
                return first.compareTo(second);
            }
        });

        // the comparator for column no.2 for sorting.
        sorter.setComparator(2, new Comparator<Integer>() {
            public int compare(Integer first, Integer second) {
                return first.compareTo(second);
            }
        });

        // the comparator for column no.3 for sorting.
        sorter.setComparator(3, new Comparator<Double>() {
            public int compare(Double first, Double second) {
                return first.compareTo(second);
            }
        });

        // This method return the table header section to modify it.
        JTableHeader header = table.getTableHeader();
        // here modify the table header.
        header.setFont(labelFont);
        header.setBackground(Color.white);
        header.setForeground(purple);

        // Here,table are pass to scroll pane for scrolling the table when needed.
        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.white);
        // method to add data into JTable.
        addRecordsIntoTable();
    }

    /**
     * This method are get the id of particular record and return the whole record
     * as ResultSet.
     * 
     * @param id
     * @return java.sql.ResultSet
     */
    public ResultSet getSelectedData(int id) {
        ResultSet res = null;
        try {
            // Get data from DB and return it.
            String query = "SELECT * FROM guage_rate WHERE id = ?";
            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setInt(1, id);
            res = pst.executeQuery();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return res;
    }

    /**
     * This method are add record into JTable from DB.
     */
    private void addRecordsIntoTable() {
        try {
            // Get record from DB.
            String query = "SELECT *,\"guage_rate\".\"id\" AS \"guage_rate_id\",\"sizes\".\"id\" AS \"sizes_id\" FROM \"guage_rate\",\"sizes\" WHERE \"sizes\".\"id\" = \"guage_rate\".\"size_id\" ORDER BY guage_rate.id ASC";
            ResultSet resultSet = DBConnection.executeQuery(query);
            // Add record one by one into JTable.
            while (resultSet.next()) {
                int id = resultSet.getInt("guage_rate_id");
                String size = resultSet.getString("size");
                int guage = resultSet.getInt("guage");
                Double rate = resultSet.getDouble("rate");

                tableModel.addRow(new Object[] { id, size, guage, rate });
            }
        } catch (Exception e) {
            DialogWindow.showMessageDialog(this, "Connection error,try again!");
        }
    }
}
/**
 * This component end here...
 */