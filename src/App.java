
/**
 * This class is a initial point of the application.
 */

import components.auth.*;
import config.*;

public class App {
    public static void main(String s[]) throws Exception {
        DBConnection.createConnection();
        new Login();
    }
}