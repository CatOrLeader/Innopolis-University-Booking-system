package Database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Class for Database connection control
 */
public class DbConnection {

    /**
     * Connects to the database
     * @param dbName database name
     * @param username database owner name
     * @param password database password
     * @return connection value
     */
    public Connection connect(String dbName, String username, String password) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, username, password);

            if (connection != null){
                System.out.println("Db connected successfully");
            } else {
                System.out.println("Db connection failed");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }
}
