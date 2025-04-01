/**
 * 
 * This file contains a class EmployeeView which have the table with information
 * of employee details which is use in other class like employee report .
 * 
 */

package components.report.partial;

import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import config.*;
import partial.interfaces.*;

/**
 * The EmployeeView class have details of employees in tabular format.
 */
public class EmployeeView extends JPanel implements FontInterface {

    public JTable table;
    public JScrollPane scrollPane;
    protected DefaultTableModel tableModel;
    protected TableRowSorter<TableModel> sorter;

    /**
     * The method create an table layout using the table,tableModel and scrollPane.
     */
    public void createEmployeeView(boolean haveDates) {

        /*
         * This create an object of DefaultTableModel with override the method which
         * allow to set table cell editable by user or not.
         * false means no one field are editable.
         */
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Adds an column headings to the table or number of columns with there name.
        tableModel.addColumn("Sr.No.");
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Mobile No.");
        tableModel.addColumn("Gender");
        tableModel.addColumn("Address");
        tableModel.addColumn("Working Status");
        if (haveDates) {
            tableModel.addColumn("Joining");
            tableModel.addColumn("Leaving");
        }

        // creates an table with table model.
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(labelFont);
        // method to add data into the table model.
        addRecordsIntoTable(haveDates);

        // This add an table row sorter to the table model.
        sorter = new TableRowSorter<TableModel>(tableModel);
        table.setRowSorter(sorter);
        table.setFont(labelFont);

        // this sets the comparator object to column no.0, which checks int values when
        // sorting by id.
        sorter.setComparator(0, new Comparator<Integer>() {
            public int compare(Integer first, Integer second) {
                return first.compareTo(second);
            }
        });

        // The getTableHeader() returns the table header reference of the table which
        // allows to modify the table header.
        JTableHeader header = table.getTableHeader();
        header.setFont(labelFont);
        header.setBackground(Color.white);
        header.setForeground(purple);

        // The table are added into the scroll panel which allows to scroll the data
        // when table are expanded out of height.
        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.white);
    }

    /**
     * This method are get id as a argument and return the ResultSet object which
     * contains the sql result from DB.
     * 
     * @param id employee id.
     * @return java.sql.ResultSet
     */
    public ResultSet getSelectedData(int id) {
        ResultSet res = null;
        try {
            String query = "SELECT * FROM employees WHERE id=" + id;
            res = DBConnection.executeQuery(query);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return res;
    }

    /**
     * This method get the all of the data related to employee from DB and set it
     * into the TableModel.
     */
    private void addRecordsIntoTable(boolean haveDates) {
        try {
            String query = "SELECT * FROM employees ORDER BY id ASC";
            ResultSet resultSet = DBConnection.executeQuery(query);
            int sr_no = 1;
            // Loop to each record
            while (resultSet.next()) {
                // set record one by one.
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                long mobile = resultSet.getLong("mobile_no");
                String addr = resultSet.getString("address");
                String email = resultSet.getString("email");
                String gender = resultSet.getString("gender");
                String status = resultSet.getBoolean("working_status") ? "Yes" : "No";
                if (haveDates) {
                    String joining = "-";
                    String leaving = "-";
                    joining = format.format(resultSet.getDate("created_at"));
                    if (status.equals("No")) {
                        leaving = format.format(resultSet.getDate("updated_at"));
                    }
                    tableModel.addRow(
                            new Object[] { sr_no++, id, name, email, mobile, gender, addr, status, joining, leaving });
                } else {
                    // add data into TableModel.
                    tableModel.addRow(new Object[] { sr_no++, id, name, email, mobile, gender, addr, status });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
/**
 * This component end here...
 */