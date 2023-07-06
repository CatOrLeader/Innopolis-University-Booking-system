package Database.Services;

import Database.DbConnector;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class UserDataServiceTest {

    @Test
    void addUserDataTest() {
    }

    @Test
    void updateUserDataTest() {
    }

    @Test
    void getUserDataTest() {
    }

    @Test
    void userExistsTest() {
        DbConnector connection = new DbConnector();
        UserDataService userDataService = new UserDataService(connection.getConnection());
        assertEquals(userDataService.userExists(90000000), false);
    }

    @Test
    void isAuthorizedTest() {
        DbConnector connection = new DbConnector();
        UserDataService userDataService = new UserDataService(connection.getConnection());
        assertEquals(userDataService.isAuthorized(580245280), true);
//        580245280
//        UserDataService userDataService = new UserDataService("org.postgresql.jdbc.PgConnection@201a4587")
    }
}