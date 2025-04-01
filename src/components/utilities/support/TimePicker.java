/**
 * 
 * 
 * 
 * This file contains a class of pick a time from GUI by user.
 * 
 */
package components.utilities.support;

import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * A TimePicker is create a small picker for pick a time by user.
 */
public class TimePicker extends JSpinner {
    // instance variables/identifiers.
    SpinnerDateModel model;
    JSpinner.DateEditor editor;

    /**
     * A constructor to create a time picker component.
     */
    public TimePicker() {
        setBackground(Color.white);
        // create a new spinner date model to set a current time to picker.
        model = new SpinnerDateModel(new Date(), null, null, Calendar.AM_PM);
        // set a created model.
        setModel(model);
        // create a date editor with format of time.
        editor = new JSpinner.DateEditor(this, "hh:mm a");
        // set a editor to edit a time with given format.
        setEditor(editor);
        // set this component visibility.
        setVisible(true);
    }

    /**
     * This method are set a new date value.
     * 
     * @param date a reference of java.util.Date.
     */
    public void setTime(Date date) {
        model.setValue(date);
    }
}
/**
 * This component end here...
 */