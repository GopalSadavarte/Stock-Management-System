/**
 * 
 * 
 * This file contains the basic functionality like create the buttons which are
 * use in many other components and this are use in other components.
 * 
 * 
 * 
 */
package components.abstracts;

import java.awt.event.*;
import java.sql.*;
import java.awt.*;
import javax.swing.*;
import components.report.partial.*;
import config.*;
import partial.*;
import partial.interfaces.*;

/**
 * The class AbstractButton which are create the buttons and other basic
 * functionality like creating the
 * JDialog by request.
 */
public abstract class AbstractButton extends JPanel
        implements ActionListener, FontInterface, KeyListener {
    // instance variables.
    protected JButton save, edit, delete, cancel, newButton, exit, select;
    protected JPanel buttonPanel, dataViewHeadingPanel, viewerButtonPanel;
    protected JDialog dataViewer;
    protected JLabel listHeading;

    /**
     * 
     * The constructor which creates the buttons for the application.
     * 
     * @param haveDeleteOption if 1 then the delete button are create and if 0 then
     *                         delete are not create.
     * @param haveEditOption   if 1 then the edit button are create and if 0 then
     *                         edit are not create.
     * @param haveCancelOption if 1 then the cancel button will create and if 0 then
     *                         cancel are not create.
     * @param dataViewer       JDialog reference for view data.
     */
    public AbstractButton(int haveDeleteOption, int haveEditOption, int haveCancelOption, JDialog dataViewer) {
        // set JDialog reference.
        this.dataViewer = dataViewer;
        // create the buttons.
        createButtons(haveDeleteOption, haveEditOption, haveCancelOption);
        // set action listeners to the buttons.
        newButton.addActionListener(this);
        save.addActionListener(this);
        if (haveEditOption == 1) {
            edit.addActionListener(this);
        }
        // set action listener to delete button if user requested.
        if (haveDeleteOption == 1) {
            delete.addActionListener(this);
        }

        // set action listener to cancel button if user requested.
        if (haveCancelOption == 1) {
            cancel.addActionListener(this);
        }
        exit.addActionListener(this);
    }

    /**
     * This method are create the buttons for the application.
     * 
     * @param haveDeleteOption if 1 then Delete button will added otherwise set to
     *                         0.
     * @param haveEditOption   if 1 then Edit button will added otherwise set to
     *                         0.
     * @param haveCancelOption if 1 then cancel button will added otherwise set to
     *                         0.
     */
    private void createButtons(int haveDeleteOption, int haveEditOption, int haveCancelOption) {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        buttonPanel.setBackground(Color.white);

        save = new JButton("Save");
        save.setBackground(purple);
        save.setForeground(Color.white);
        save.setSize(new Dimension(20, 15));
        save.setFont(buttonFont);
        save.setFocusable(true);

        if (haveEditOption == 1) {
            edit = new JButton("Edit");
            edit.setForeground(Color.white);
            edit.setBackground(green);
            edit.setSize(new Dimension(30, 15));
            edit.setFont(buttonFont);
            edit.setEnabled(false);
            edit.setFocusable(true);
        }

        if (haveCancelOption == 1) {
            cancel = new JButton("Cancel");
            cancel.setForeground(Color.white);
            cancel.setBackground(lightBlue);
            cancel.setSize(new Dimension(30, 15));
            cancel.setFont(buttonFont);
            cancel.setFocusable(true);
        }

        newButton = new JButton("New");
        newButton.setSize(new Dimension(20, 20));
        newButton.setBackground(blue);
        newButton.setForeground(Color.white);
        newButton.setFont(buttonFont);
        newButton.setFocusable(true);

        if (haveDeleteOption == 1) {
            delete = new JButton("Delete");
            delete.setSize(new Dimension(20, 20));
            delete.setBackground(orange);
            delete.setForeground(Color.white);
            delete.setFont(buttonFont);
            delete.setEnabled(false);
            delete.setFocusable(true);
        }

        exit = new JButton("Exit");
        exit.setForeground(Color.white);
        exit.setBackground(red);
        exit.setSize(new Dimension(20, 20));
        exit.setFont(buttonFont);
        exit.setFocusable(true);

        buttonPanel.add(newButton);
        buttonPanel.add(save);
        if (haveEditOption == 1) {
            buttonPanel.add(edit);
        }
        if (haveDeleteOption == 1) {
            buttonPanel.add(delete);
        }
        if (haveCancelOption == 1) {
            buttonPanel.add(cancel);
        }
        buttonPanel.add(exit);
    }

    /**
     * This method are set the new entry no to the text field.
     * 
     * @param table the name of database table.
     * @param text  the object of javax.swing.JTextField.
     */
    protected void setId(String table, JTextField text) {
        try {
            String query = "SELECT * FROM " + table + " ORDER BY id DESC LIMIT 1";
            ResultSet res = DBConnection.executeQuery(query);
            // true if record is exist and set new id.
            if (res.next()) {
                text.setText(Integer.toString(res.getInt("id") + 1));
            } else {
                text.setText("1");
            }
        } catch (Exception e) {
            DialogWindow.showErrorDialog(this, "Connection error,try again!");
        }
    }

    /**
     * This method are set the data in the JDialog according to View.
     * 
     * @param view  the reference of EmployeeView object if have to create otherwise
     *              pass null.
     * @param view1 the reference of SizeView object if have to create otherwise
     *              pass null.
     * @param view2 the reference of GuageRateView object if have to create
     *              otherwise pass null.
     */
    protected void createViewer(EmployeeView view, SizeView view1, GuageRateView view2) {
        // removes previous components.
        dataViewer.getContentPane().removeAll();
        int scrollWidth = 0;
        // modify the views data according to condition.
        if (view != null) {
            view.setPreferredSize(new Dimension(1090, 330));
            view.createEmployeeView(false);
            view.setVisible(true);
            view.scrollPane.setPreferredSize(new Dimension(1080, 300));
            view.add(view.scrollPane);
            scrollWidth = 1080;
        } else if (view1 != null) {
            view1.setPreferredSize(new Dimension(300, 330));
            view1.createSizeView();
            view1.setVisible(true);
            view1.scrollPane.setPreferredSize(new Dimension(290, 300));
            view1.add(view1.scrollPane);
            scrollWidth = 290;
        } else {
            view2.setPreferredSize(new Dimension(400, 330));
            view2.createGuageRateView();
            view2.setVisible(true);
            view2.scrollPane.setPreferredSize(new Dimension(390, 300));
            view2.add(view2.scrollPane);
            scrollWidth = 390;
        }

        // set the component with fixed size.
        dataViewer.setResizable(false);
        dataViewer.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        int width = 0;
        if (view != null) {
            width = 1100;
        } else if (view1 != null) {
            width = 310;
        } else {
            width = 410;
        }
        // vary the size as per view.
        dataViewer.setSize(new Dimension(width, 525));
        dataViewer.setLayout(new FlowLayout(FlowLayout.LEFT));
        dataViewer.setBackground(Color.white);

        int x = ((int) Math.round(
                (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - dataViewer.getWidth()) / 2));
        // set the JDialog to center of the window.
        dataViewer.setLocation(new Point(x, 100));

        // create the heading panel for JDialog.
        dataViewHeadingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dataViewHeadingPanel.setPreferredSize(new Dimension(scrollWidth, 30));
        dataViewHeadingPanel.setBorder(border);
        dataViewHeadingPanel.setBackground(Color.white);

        // set heading according to the view.
        String headLabel = "Employee List";
        if (view1 != null) {
            headLabel = "Size List";
        } else if (view2 != null) {
            headLabel = "Guage-Rate List";
        }

        // create heading label
        listHeading = new JLabel(headLabel);
        listHeading.setFont(headingFont);

        // add labels and field to the heading panel.
        dataViewHeadingPanel.add(listHeading);

        // add heading to the JDialog.
        dataViewer.add(listHeading);
        if (view != null) {
            dataViewer.add(view);
        } else if (view1 != null) {
            dataViewer.add(view1);
        } else {
            dataViewer.add(view2);
        }

        // create button panel for select button.
        viewerButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        viewerButtonPanel.setPreferredSize(new Dimension(scrollWidth, 50));
        viewerButtonPanel.setBorder(border);

        // create select button for selection.
        select = new JButton("Select");
        select.setPreferredSize(new Dimension(100, 30));
        select.setBackground(orange);
        select.setForeground(Color.white);
        select.setFont(buttonFont);
        select.addActionListener(this);

        // add select button to button panel.
        viewerButtonPanel.add(select);

        // add button panel to JDialog.
        dataViewer.add(viewerButtonPanel);
        // Repaint the JDialog.
        dataViewer.getContentPane().revalidate();
        dataViewer.getContentPane().repaint();
        // make JDialog visible.
        dataViewer.setVisible(true);
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
/**
 * This component end here...
 */
