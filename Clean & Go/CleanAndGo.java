import java.util.*;
import java.sql.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CleanAndGo {

    public static void main(String args[]) {
        MenuLogic obj = new MenuLogic();
        Connection conn = null;
        try {
            // Step 1: Load the JDBC driver(You have to have the connector Jar file in your
            // project Class path)

            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database(Change the URL)

            String url = "jdbc:mysql://localhost:3306/CleaningServices?serverTimezone=UTC&useSSL=TRUE";
            String user, pass;
            user = readEntry("userid : ");
            pass = readEntry("password: ");
            conn = DriverManager.getConnection(url, user, pass);

            boolean done = false;
            do {
                obj.mainMenu(conn);
                done = true;
            } // switch
            while (!done);

        } catch (ClassNotFoundException e) {
            System.out.println("Could not load the driver");
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
    }

    static String readEntry(String prompt) {
        try {
            StringBuffer buffer = new StringBuffer();
            System.out.print(prompt);
            System.out.flush();
            int c = System.in.read();
            while (c != '\n' && c != -1) {
                buffer.append((char) c);
                c = System.in.read();
            }
            return buffer.toString().trim();
        } catch (IOException e) {
            return "";
        }
    }

}