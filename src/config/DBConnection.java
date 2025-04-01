/**
 * 
 * The Default class to create an connection with PostgreSQL database using JDBC.
 * It contains the connection object and methods which are useful to fire an queries to
 * the data and returns results accordingly.
 * 
 */

package config;

import java.sql.*;

/**
 * Connection for the PostgreSQL database.
 */
public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/KK Enterprises";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "grs2004311";
    public static Connection con;

    /**
     * This method create an connection with DB
     * 
     * @throws Exception throws when it occur.
     */
    public static void createConnection() throws Exception {
        con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * This method accept an SQL query and execute it on the DB and return
     * boolean result.
     * 
     * @param query a sql query which will fire on database.
     * @return boolean
     * @throws Exception throws when it occur.
     */
    public static boolean execute(String query) throws Exception {
        Statement st = con.createStatement();
        int res = st.executeUpdate(query);
        return (res > 0);
    }

    /**
     * This method accept an SQL query and execute it on the DB and returns the
     * Result Set (Data).
     * 
     * @param query a sql query which will fire on database.
     * @return java.sql.ResultSet
     * @throws Exception throws when it occur.
     */
    public static ResultSet executeQuery(String query) throws Exception {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(query);
        return res;
    }

    // This method use to close the connection of DB.
    public static void close() throws Exception {
        con.close();
    }
}
/**
 * This component end here...
 */