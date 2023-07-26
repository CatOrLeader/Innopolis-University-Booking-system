package Database;

import Utilities.Services;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Class for Database connection control
 */
public class DbConnector {
    private static final String DB_URL = Services.getEnv("DB_URL");
    private static final String DB_USERNAME = Services.getEnv("DB_USERNAME");
    private static final String DB_PASSWORD = Services.getEnv("DB_PASSWORD");

    private Connection connection;

    /**
     * Default constructor
     */
    public DbConnector() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connects to the database
     *
     * @return connection value
     */
    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                if (connection != null) {
                    System.out.println("Database connected successfully");
                } else {
                    System.out.println("Database connection failed");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return connection;
    }

    /**
     * Closes database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
