/**
 * 
 * This Validation class holds the method that are use to validate 
 * all of the field of entire application.
 * 
 */
package partial;

import java.util.regex.*;
import java.sql.*;
import config.*;
import partial.interfaces.*;

/** This class holds the Validation related methods with different cases. */
public class Validation implements Regex {

    /**
     * this method are use to validate an password.
     * 
     * @param password actual password string which are entered by user in the
     *                 field.
     * @return boolean
     */
    public static boolean checkPassword(String password) {

        if (password == null) {
            return false;
        }
        if (password.isBlank()) {
            return false;
        }

        boolean patternMatch = Pattern.matches(PASSWORD_PATTERN, password);

        return patternMatch;
    }

    /**
     * this method are use to validate username.
     * 
     * @param username actual username string which are entered by user in the
     *                 field.
     * @return boolean
     */
    public static boolean checkUsername(String username) {
        if (username == null || username.length() <= 0) {
            return false;
        }

        boolean res = username.isBlank();
        return !res;
    }

    /**
     * This method validate an name.
     * 
     * @param name actual name string which are entered by user in the field.
     * @return boolean
     */
    public static boolean checkName(String name) {
        if (name.length() <= 0 || name == null) {
            return false;
        }

        boolean res = Pattern.compile(NAME_PATTERN).matcher(name).matches();
        return res;
    }

    /**
     * this method use to validate email.
     * 
     * @param email actual email string which are entered by user in the field.
     * @return boolean
     */
    public static boolean checkEmail(String email) {
        if (email.length() <= 0 || email == null) {
            return false;
        }

        boolean res = Pattern.matches(EMAIL_PATTERN, email);
        return res;
    }

    /**
     * This method check the mobile no. is valid or not.
     * 
     * @param mobile_no actual mobile number which are entered by user in the
     *                  field.
     * @return boolean
     */
    public static Boolean checkMobile(String mobile_no) {
        if (mobile_no.length() != 10 || mobile_no == null) {
            return false;
        }

        boolean res = Pattern.matches(MOBILE_NO_PATTERN, mobile_no);
        return res;
    }

    /**
     * This method check the size are valid or not.
     * 
     * @param size actual size which are entered by user in the field.
     * @return boolean
     */
    public static boolean checkSize(String size) {
        if (size.isBlank()) {
            return false;
        }

        if (size.contains("*")) {
            String str[] = size.split("\\*");

            if ((str[0].matches(DOUBLE_VAL_PATTERN) || str[0].matches(INTEGER_VAL_PATTERN))
                    && (str[1].matches(DOUBLE_VAL_PATTERN) || str[1].matches(INTEGER_VAL_PATTERN))) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * This method check the name or email are already exist in DB or not.
     * 
     * @param name  The current name which entered in from by user.
     * @param email The current email which entered in form by user.
     * @param id    The employee id which is store in db if record is for update
     *              then id are require otherwise 0 are require.
     * @return boolean - true when name or email are already exist otherwise false.
     */
    public static boolean isEmployeeRecordAlreadyExist(String name, String email, int id) {
        try {
            String query = "";
            if (id <= 0) {
                query = "SELECT * FROM employees WHERE name = ? OR email = ? LIMIT 1";
            } else {
                query = "SELECT * FROM employees WHERE (name = ? OR email = ?) AND id != ? LIMIT 1";
            }

            PreparedStatement pst = DBConnection.con.prepareStatement(query);
            pst.setString(1, name.trim().toLowerCase());
            pst.setString(2, email.trim());

            if (id != 0) {
                pst.setInt(3, id);
            }

            ResultSet resultSet = pst.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
/**
 * This component end here...
 */