/**
 * 
 * This is base component which allow user to navigate from one component 
 * to another component when user clicking on the navigation bar menu items.
 *  
 */

package components;

import java.awt.*;
import javax.swing.*;
import components.layout.*;
import components.report.*;
import components.settings.*;
import components.stocks.*;
import components.utilities.*;
import config.*;
import partial.*;
import partial.backup.*;
import java.awt.event.*;

/**
 * The home class inherit the default navbar to added as it is navbar.
 */
public class DefaultFrame extends Navbar {

    // instance variables.
    private JPanel displayComponent, currentComponent;
    private ImageIcon icon = new ImageIcon(DefaultFrame.class.getResource("logo.png"));

    /**
     * A constructor to add default display panel(home panel) in the frame.
     */
    public DefaultFrame() {
        displayComponent = new Home();
        add(displayComponent, BorderLayout.CENTER);
        currentComponent = displayComponent;
        setTitle("KK Enterprises");
        setIconImage(icon.getImage());
        setVisible(true);
    }

    /**
     * This method are remove previous panel and add new panel at that position.
     * 
     * @param component a JPanel object which will display.
     */
    protected void reRender(JPanel component) {
        remove(currentComponent);
        add(component, BorderLayout.CENTER);
        currentComponent = component;
        // repaint a frame after removing and adding components.
        revalidate();
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        // get the source of clicked menu item by user.
        JMenuItem clickedItem = ((JMenuItem) e.getSource());
        // get the text hold by this menu item.
        String item = clickedItem.getText();

        // compare this text with others and execute block where it will match.
        switch (item) {
            // in all case different JPanel objects will create
            case "Home":
                displayComponent = new Home();
                break;
            case "stock entry":
                displayComponent = new StockEntry();
                break;
            case "stock report":
                displayComponent = new InventoryReport(homeMenu, clickedItem);
                break;
            case "add employee":
                displayComponent = new AddEmployee(homeMenu, clickedItem, dataViewer);
                break;
            case "change password":
                displayComponent = new ChangePassword(clickedItem, homeMenu);
                break;
            case "add size":
                displayComponent = new AddSize(homeMenu, clickedItem, dataViewer);
                break;
            case "employees report":
                displayComponent = new EmployeeReport(homeMenu, clickedItem);
                break;
            case "employee attendance":
                displayComponent = new EmployeeAttendance(homeMenu, clickedItem);
                break;
            case "employee attendance report":
                displayComponent = new EmployeeAttendanceReport(homeMenu);
                break;
            case "employee payroll report":
                displayComponent = new EmployeePayrollReport(homeMenu, clickedItem);
                break;
            case "inventory report":
                displayComponent = new StockReport(homeMenu);
                break;
            case "payment report":
                displayComponent = new PaymentReport(homeMenu, clickedItem);
                break;
            case "payroll":
                displayComponent = new EmployeePayroll(homeMenu, clickedItem);
                break;
            case "receipt report":
                displayComponent = new ReceiptReport(homeMenu, clickedItem);
                break;
            case "receipt entry":
                displayComponent = new ReceiptEntry(homeMenu);
                break;
            case "employee prepayment":
                displayComponent = new Prepayment(homeMenu);
                break;
            case "late arrival log":
                displayComponent = new LateArrivalEntry(homeMenu);
                break;
            case "late arrival report":
                displayComponent = new LateArrivalReport(homeMenu, clickedItem);
                break;
            // in this case i get an confirmation from user before exit from application.
            case "Backup and Exit":
                try {
                    boolean res = DialogWindow.showConfirmDialog(this, "Are you sure to exit?");
                    if (res) {
                        // create Backup object to get database backup.
                        new Backup();
                        DBConnection.close();
                        dataViewer.dispose();
                        dispose();
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                break;
            default:
                displayComponent = currentComponent;
                break;
        }
        // add a new panel which object are created.
        reRender(displayComponent);
    }
}
/**
 * This component end here...
 */