package dataaccess;

import model.Authdata;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class SQLAuthTest {

    private SQLAuth sqlAuth;

    @BeforeEach
    public void setup() throws DataAccessException {
        sqlAuth = new SQLAuth();
        sqlAuth.clear();
    }

    @Test
    public void getAuthPositive() throws DataAccessException {
        sqlAuth.makeAuth("myAuthToken", "userA");
        Authdata token = sqlAuth.getAuth("myAuthToken");

        assertNotNull(token);
        assertEquals("myAuthToken", token.authToken());
        assertEquals("userA", token.username());
    }

    @Test
    public void getAuthNegative() throws DataAccessException {
        Authdata token = sqlAuth.getAuth("missingToken");
        assertNull(token);
    }

    @Test
    public void makeAuthPositive() throws DataAccessException {
        sqlAuth.makeAuth("newToken", "userB");
        Authdata token = sqlAuth.getAuth("newToken");
        assertNotNull(token);
        assertEquals("userB", token.username());
    }

    @Test
    public void makeAuthNegative() throws DataAccessException {
        sqlAuth.makeAuth("sameToken", "userC");
        assertThrows(DataAccessException.class, () -> {
            sqlAuth.makeAuth("sameToken", "differentUser");
        });
    }

    @Test
    public void deleteAuthPositive() throws DataAccessException {
        sqlAuth.makeAuth("deleteToken", "userD");
        sqlAuth.deleteAuth("deleteToken");
        assertNull(sqlAuth.getAuth("deleteToken"));
    }

    @Test
    public void deleteAuthNegative() {
        assertDoesNotThrow(() -> sqlAuth.deleteAuth("fakeToken"));
    }

    @Test
    public void clearTest() throws DataAccessException {
        sqlAuth.makeAuth("auth1", "user1");
        sqlAuth.makeAuth("auth2", "user2");
        sqlAuth.clear();
        assertNull(sqlAuth.getAuth("auth1"));
        assertNull(sqlAuth.getAuth("auth2"));
    }
}