/**
 * 
 * This class is handle the keyboard event ,when user type "Enter" where the event listener
 * are added .user enter on the particular field where event listener are added and 
 * that time the another component will pass in which are focused.
 * 
 */

package partial.event;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

/**
 * This is custom key listener which make focus on next element which is pass
 * through constructor when user press "Enter" key.
 */
public class CustomKeyListener extends KeyAdapter {
    private Component component;

    /**
     * This construct a new custom key listener for handling next component focus.
     * 
     * @param component a reference of component in which will focused.
     */
    public CustomKeyListener(Component component) {
        this.component = component;
    }

    // event are triggered where it has been applied.
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        String key = KeyEvent.getKeyText(keyCode);
        // "Enter" key are listen.
        if (key.equals("Enter")) {
            // if the focus component is JButton then do click operation rather than focus
            // operation.
            if (this.component instanceof JButton) {
                ((JButton) this.component).doClick();
            }
            // focus on the given component.
            this.component.requestFocus();
        }
    }
}
/**
 * This component end here...
 */