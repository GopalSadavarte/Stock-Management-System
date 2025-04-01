/**
 * 
 * 
 * This component create an employee payroll report in tabular format with payroll data.
 * 
 */
package components.report.partial;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import config.*;
import partial.*;
import partial.interfaces.*;

/**
 * The EmployeePayrollView contains the table with data of employee payroll.
 */
public class EmployeePayrollView extends JPanel implements FontInterface {
    // instance variables.
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JScrollPane scrollPane;
    protected TableRowSorter<TableModel> sorter;

    /**
     * A constructor which create an table with employee payroll data.
     */
    public EmployeePayrollView() {

        // create an instance of table model with no one column are editable.
        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // set the columns to model.
        tableModel.addColumn("Sr.No.");
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Basic Salary");
        tableModel.addColumn("Advance");
        tableModel.addColumn("Deposit");
        tableModel.addColumn("Pending");
        tableModel.addColumn("Overtime Hours");
        tableModel.addColumn("rate");
        tableModel.addColumn("Paid Amount");
        tableModel.addColumn("Month");

        // create a object of JTable with table model.
        table = new JTable(tableModel);
        table.setFont(labelFont);
        table.setRowHeight(30);

        // get the header of table for modifying it.
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.white);
        header.setFont(labelFont);
        header.setForeground(purple);

        // create a sorter instance.
        sorter = new TableRowSorter<TableModel>(tableModel);
        // add sorter to table.
        table.setRowSorter(sorter);

        // create scroll pane for table.
        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // set background color white .
        scrollPane.getViewport().setBackground(Color.white);

        // add data into the table.
        addDataIntoTable();
    }

    /**
     * This method add the data in to table from DB.
     */
    private void addDataIntoTable() {
        try {
            // query for getting data from DB.
            String query = "SELECT * FROM employees,salaries,vouchers WHERE employees.id = salaries.emp_id AND salaries.id=vouchers.salaries_id";
            // execute query and get result set.
            ResultSet resultSet = DBConnection.executeQuery(query);
            int sr_no = 1;
            // loop one by one record.
            while (resultSet.next()) {
                // get the data from result set.
                int emp_id = resultSet.getInt("emp_id");
                String name = resultSet.getString("name");
                int salary = resultSet.getInt("basic_salary");
                int advAmt = resultSet.getInt("advance_amt");
                int deposit = resultSet.getInt("deposit_amount");
                double hours = resultSet.getDouble("overtime_hours");
                int rate = resultSet.getInt("rate_of_per_hour");
                int pending = advAmt - deposit;
                double payable_amount = resultSet.getDouble("amount");
                // get payment date and split it into array.
                String date[] = resultSet.getString("payment_month").split("\\-");
                // set only month and year of payment.
                String month = date[1] + "-" + date[0];

                // add row into the table model.
                tableModel.addRow(
                        new Object[] { sr_no++, emp_id, name, salary, advAmt, deposit, pending, hours, rate,
                                payable_amount, month });
            }
        } catch (Exception e) {
            // execute when exception are occur in above code.
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }
    }
}
/**
 * This component end here...
 */