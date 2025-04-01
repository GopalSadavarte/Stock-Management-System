/**
 * 
 * This is Navbar component which are use in entire application to show the 
 * default navigation bar on each frame with related menus and menu items 
 * with there action according to each menu.
 * 
 */

package components.layout;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import config.*;
import partial.*;
import partial.backup.*;

/**
 * The Navbar class hold the default navbar for the application.
 */
public abstract class Navbar extends JFrame implements ActionListener {

    // instance variables.
    JMenuBar menuBar;
    JMenu menus[];
    protected static final JDialog dataViewer = new JDialog();
    protected JMenuItem menuItems[], exitMenu, homeMenu;
    JPanel navbar;
    Toolkit toolkit;
    Dimension size;
    // main menus
    String menuStrings[] = { "Stock", "Report", "Utilities", "Settings" };
    // sub menus
    String menuItemStrings[][] = {
            { "stock entry", "add size" },
            { "stock report", "employees report", "employee attendance report", "employee payroll report",
                    "inventory report", "payment report", "receipt report", "late arrival report" },
            { "add employee", "employee attendance", "employee prepayment", "payroll", "receipt entry",
                    "late arrival log" },
            { "change password" },
    };

    /**
     * Construct a new navbar component.
     */
    public Navbar() {

        // get the reference of screen.
        toolkit = Toolkit.getDefaultToolkit();
        // get the size of screen.
        size = toolkit.getScreenSize();

        // set size cut-to-cut same of screen ,where application are run.
        setSize(size);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        // panel to hold the menubar
        navbar = new JPanel();
        navbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        navbar.setBackground(Color.white);

        // menubar to hold the menus
        menuBar = new JMenuBar();
        menuBar.setBackground(Color.white);

        homeMenu = new JMenuItem("Home");
        homeMenu.setFont(new Font("Arial", 20, 15));
        homeMenu.setBackground(Color.white);
        homeMenu.setHorizontalAlignment(SwingConstants.CENTER);
        homeMenu.addActionListener(this);
        menuBar.add(homeMenu);

        // menus are main menu with length of given string menu array
        menus = new JMenu[menuStrings.length];

        // loop to allocate the memory to each menu of the menubar
        for (int i = 0; i < menuStrings.length; i++) {
            // it allocate an memory to each menu and set name of the menu from menuString
            // array with i index.
            menus[i] = new JMenu(menuStrings[i]);
            menus[i].setFont(new Font("Arial", 20, 15));
            menus[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // This is a list of menu items of each main menu of menu bar.
            menuItems = new JMenuItem[menuItemStrings[i].length];

            // This loop will allocate an memory to each menu item according to the length
            // of menu item strings.
            for (int j = 0; j < menuItemStrings[i].length; j++) {
                menuItems[j] = new JMenuItem(menuItemStrings[i][j]);
                menuItems[j].setFont(new Font("Arial", 10, 13));
                menuItems[j].setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                menuItems[j].addActionListener(this);
                menus[i].add(menuItems[j]);
            }
            menuBar.add(menus[i]);
        }
        // backup and exitMenu menu

        exitMenu = new JMenuItem("Backup and Exit");
        exitMenu.setFont(new Font("Arial", 20, 15));
        exitMenu.setBackground(Color.white);
        exitMenu.setForeground(Color.red);
        exitMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        exitMenu.addActionListener(this);
        menuBar.add(exitMenu);

        navbar.add(menuBar);
        add(navbar, BorderLayout.NORTH);

        // Window listener handler for user closes the window
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent w) {
                // confirmation before closing the frame/dispose the frame.
                boolean res = DialogWindow.showConfirmDialog(Navbar.this, "Are you sure to exit?");
                if (res) {
                    try {
                        new Backup();
                        DBConnection.close();
                        dataViewer.dispose();
                        dispose();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
/**
 * This component end here...
 */