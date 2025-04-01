/**
 * 
 * 
 * 
 * This component have an data of which employees are came late on the work and how many ares it have been late are display in tabular format.
 * 
 * 
 */

package components.report.partial;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import config.*;
import partial.interfaces.*;

/**
 * The class LateArrivalEmployeeView are contains the report data in tabular format.
 */
public class LateArrivalEmployeeView extends JPanel implements FontInterface {
    protected DefaultTableModel tableModel;
    JTable table;
    protected TableRowSorter<TableModel> sorter;
    protected JScrollPane scrollPane;

    /**
     * A constructor are create an table with late arrival employee data from DB.
     */
    public LateArrivalEmployeeView() {
        // create an table model with no one cell are editable.
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // add columns in table model.
        tableModel.addColumn("Sr.No.");
        tableModel.addColumn("Name");
        tableModel.addColumn("Hours");
        tableModel.addColumn("Description");
        tableModel.addColumn("Time");
        tableModel.addColumn("Date");

        // create table with table model.
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(labelFont);

        // get the table header to modify it.
        JTableHeader header = table.getTableHeader();
        header.setFont(labelFont);
        header.setForeground(purple);
        header.setBackground(Color.white);

        // create a sorter instance for table row sorting.
        sorter = new TableRowSorter<TableModel>(tableModel);
        // set sorter to table.
        table.setRowSorter(sorter);

        // create scroll pane for table.
        scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.white);

        // add data into the table.
        addDataIntoTable();
    }

    /**
     * This method are get data from DB and set it into table model.
     */
    private void addDataIntoTable() {
        try {
            // query for getting data from DB.
            String query = "SELECT * FROM employees,late_arrivals WHERE employees.id = late_arrivals.emp_id";
            // execute query and get result set.
            ResultSet resultSet = DBConnection.executeQuery(query);
            int sr_no = 1;
            // loop one by one record and set it into the table by rows.
            while (resultSet.next()) {
                // extract data from result set.
                String name = resultSet.getString("name");
                String desc = resultSet.getString("description");
                double hours = resultSet.getDouble("no_of_hours");
                String time = resultSet.getString("time");
                String date = resultSet.getString("date");

                // add data into the table model.
                tableModel.addRow(new Object[] { sr_no++, name.trim(), hours, desc, time, date });
            }
        } catch (Exception e) {
            // execute when an exception occur in above code.
            e.printStackTrace();
        }

    }
}
/**
 * This component end here...
 */