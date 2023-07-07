package Database.Services;

import Database.DbConnector;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserDataServiceTest {
    @Test
    public void userExistsTest() {
        DbConnector connection = new DbConnector();
        UserDataService userDataService = new UserDataService(connection.getConnection());
        assertEquals(userDataService.userExists(90000000), false);
    }

    @Test
    public void isAuthorizedTest() {
        DbConnector connection = new DbConnector();
        UserDataService userDataService = new UserDataService(connection.getConnection());
        assertEquals(userDataService.isAuthorized(580245280), false);
    }
}