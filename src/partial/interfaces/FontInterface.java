/**
 * 
 * This file contains an interface that hold the fonts,colors and borders and other
 * things,which are use in the application development.
 * 
 * 
 */
package partial.interfaces;

import javax.swing.*;
import java.awt.*;
import java.text.*;
import java.util.*;
import javax.swing.border.*;

/**
 * The FontInterface hold the fonts,colors and borders and many other things.
 */
public interface FontInterface {
    // default label size.
    final Dimension labelSize = new Dimension(390, 20);
    // colors for the application.
    final Color purple = new Color(147, 3, 204);
    final Color green = new Color(61, 131, 3);
    final Color lightBlue = new Color(29, 145, 247);
    final Color orange = new Color(250, 81, 3);
    final Color red = new Color(238, 5, 5);
    final Color blue = new Color(3, 60, 247);
    final Color lightGray = new Color(194, 194, 194);
    final Color borderColor = new Color(247, 246, 246);
    final Color navyBlue = new Color(16, 1, 83);

    // fonts for the application.
    final Font buttonFont = new Font("cambria", 10, 17);
    final Font labelFont = new Font("Arial", 5, 17);
    final Font headingFont = new Font("cambria", 20, 30);
    final Font subHeadingFont = new Font("cambria", 20, 20);
    final Font fieldFont = new Font("Arial", 10, 18);
    final Font tableColumnFont = new Font("Arial", 5, 15);

    // different borders with different colors for app.
    final Border border = BorderFactory.createLineBorder(borderColor);
    final Border emptyBorderLeft = BorderFactory.createEmptyBorder(0, 15, 0, 0);
    final Border grayBorder = BorderFactory.createLineBorder(Color.gray);
    final Border lightGrayBorder = BorderFactory.createLineBorder(lightGray);

    // calendar for the app.
    final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"));
    // format for date
    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * This method convert a two dimensional string array to a single string.
     * 
     * @param arr the two dimensional string array.
     * @return java.lang.String
     */
    default String convertArrayToString(String[][] arr) {
        String s = "";
        // loop on each sub array.
        for (String[] strings : arr) {
            // convert a string array to string.
            String str = Arrays.toString(strings);
            // concatenate a string to main string.
            s = s.concat(str.substring(1, str.length() - 1));
            // concatenate a semicolon which separate a each record in single string.
            s = s.concat(";");
        }
        // get substring from generated string excepting a last semicolon.
        s = s.substring(0, s.length() - 1);
        // return a string conversion.
        return s;
    }

    /**
     * This method convert a string to the two dimensional string array.
     * 
     * @implNote this method only support the array have many rows and 3 columns.
     * @param arr this is string have two separators (comma) and (semicolon).
     * @return java.lang.String[][]
     */
    default String[][] convertStringToArray(String arr) {
        String arr1[][];
        // first split a string with semicolon.
        String ar[] = arr.split(";");
        // create a multidimensional array with multiple rows and three columns.
        arr1 = new String[ar.length][3];
        // loop on each row/each value of string array.
        for (int i = 0; i < ar.length; i++) {
            // again split a string by comma.
            String[] a = ar[i].split(",");
            // loop on each value of string array which get after split a string with comma.
            for (int j = 0; j < a.length; j++) {
                // set each value to multidimensional array.
                arr1[i][j] = a[j];
            }
        }
        // return a generated multidimensional array.
        return arr1;
    }
}
/**
 * This interface end here...
 */