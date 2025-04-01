/**
 * 
 * This class are contains all the option panels which are useful within 
 * an application.
 * 
 */
package partial;

import java.awt.*;
import javax.swing.*;

/** In this class different methods perform different option pane. */
public class DialogWindow {
    /**
     * This method are show the Confirmation JOptionPane.
     * 
     * @param parent  the component reference where the option panel will shows.
     * @param message the message which will be display on the option pane.
     * @return boolean
     */
    public static boolean showConfirmDialog(Component parent, String message) {
        int res = JOptionPane.showConfirmDialog(parent, message, "Confirmation", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
        return res == 0;
    }

    /**
     * This method are show the Error JOptionPane.
     * 
     * @param parent  the component reference where the option panel will shows.
     * @param message the message which will be display on the option pane.
     */
    public static void showErrorDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method are show the Message JOptionPane.
     * 
     * @param parent  the component reference where the option panel will shows.
     * @param message the message which will be display on the option pane.
     */
    public static void showMessageDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method are show the Warning JOptionPane.
     * 
     * @param parent  the component reference where the option panel will shows.
     * @param message the message which will be display on the option pane.
     */
    public static void showWarningDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
/**
 * This component end here...
 */