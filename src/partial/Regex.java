/**
 * This class are hold all the regular expressions
 * which are use in field validation within a application
 */

package partial;

public class Regex {
    public static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{8}$";

    public static final String EMAIL_PATTERN="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
}
