/**
 * 
 * This file contains the code of creating a backup of PostgreSQL database .
 * 
 */
package partial.backup;

import java.io.*;
import partial.*;

/**
 * The class Backup create an backup file of database.
 */
public class Backup {
    /**
     * The constructor which create an backup of DB.
     */
    public Backup() {
        // DB configuration details.
        String host = "localhost";
        String port = "5432";
        String database = "KK Enterprises";
        String user = "postgres";
        String password = "grs2004311";
        String backupPath = "C:\\kk_enterprises\\backup\\database.sql";
        // method to create an backup.
        createBackUp(host, port, database, user, password, backupPath);
    }

    /**
     * This method are create a actual backup file of DB.
     * 
     * @param host       local machine IP or just "localhost"
     * @param port       is process address of postgresql server.
     * @param db         is name of database.
     * @param user       is username of database.
     * @param password   is password of database which is used to access given DB.
     * @param backupPath the path where a backup file will store.
     */
    private void createBackUp(String host, String port, String db, String user, String password, String backupPath) {
        try {
            /*
             * the location of pg_dump.exe file,which help to create an backup of postgresql
             * database through cmd or it is a application provided by postgresql to create
             * an backup through command.
             */
            String pgDumpPath = "C:\\Program Files\\PostgreSQL\\17\\bin\\pg_dump";
            // create a new file,where given path.
            File backupFile = new File(backupPath);

            // create a process builder object to execute a command of backup through java.
            ProcessBuilder processBuilder = new ProcessBuilder(pgDumpPath, "-h", host, "-p", port, "-U", user, "-F",
                    "c", "-b", "-v", "-f", backupFile.getAbsolutePath(), db);
            // put a password in process builder environment **it is compulsory and write as
            // it is.
            processBuilder.environment().put("PGPASSWORD", password);

            processBuilder.redirectErrorStream(true);
            // start the process execution.
            Process process = processBuilder.start();
            // get a exit code.
            int exitCode = process.waitFor();
            // check a exit code is 0 means backup file created successfully.
            if (exitCode == 0) {
                DialogWindow.showMessageDialog(null, "Backup get successfully at:\n" + backupPath);
                System.out.println("backup created successfully at :" + backupPath);
            } else {
                // execute when error in backup.
                DialogWindow.showWarningDialog(null, "Backup could not created! at:\n" + backupPath);
                System.err.println("Error during backup.Exit code: " + exitCode);
                ;
            }
        } catch (IOException | InterruptedException e) {
            // execute when an exception occur in above code.
            e.printStackTrace();
            DialogWindow.showErrorDialog(null, "Error :" + e.getMessage());
        }
    }
}
/**
 * This class end here...
 */