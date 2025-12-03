package dataaccess;

import model.Userdata;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class SQLUserTest {

    private SQLUser sqlUser;

    @BeforeEach
    public void setup() throws Exception {
        sqlUser = new SQLUser();
        sqlUser.clear();
    }

    @Test
    public void getUserPositive() throws DataAccessException {
        sqlUser.addUser("userA", "passA", "userA@email.com");
        Userdata user = sqlUser.getUser("userA");

        assertNotNull(user);
        assertEquals("userA", user.username());
        assertEquals("userA@email.com", user.email());
        assertNotEquals("passA", user.password());
    }

    @Test
    public void getUserNegative() throws DataAccessException {
        Userdata user = sqlUser.getUser("fakeUser");
        assertNull(user);
    }

    @Test
    public void addUserPositive() throws DataAccessException {
        sqlUser.addUser("userB", "passB", "userB@email.com");

        Userdata user = sqlUser.getUser("userB");
        assertNotNull(user);
        assertEquals("userB", user.username());
        assertEquals("userB@email.com", user.email());
        assertNotEquals("passB", user.password()); // hashed
    }

    @Test
    public void addUserNegative() throws DataAccessException {
        sqlUser.addUser("sameUsername", "pass1", "person1@email.com");
        assertThrows(DataAccessException.class, () -> {
            sqlUser.addUser("sameUsername", "pass2", "person2@mail.com");
        });
    }

    @Test
    public void matchingPassPositive() throws DataAccessException {
        sqlUser.addUser("userC", "passC", "userC@email.com");
        Userdata user = sqlUser.getUser("userC");
        boolean matches = sqlUser.matchingPass("passC", user.password());
        assertTrue(matches);
    }

    @Test
    public void matchingPassNegative() throws DataAccessException {
        sqlUser.addUser("userD", "passD", "userD@email.com");
        Userdata user = sqlUser.getUser("userD");
        boolean matches = sqlUser.matchingPass("fakePass", user.password());
        assertFalse(matches);
    }

    @Test
    public void clearTest() throws DataAccessException {
        sqlUser.addUser("user1", "pass1", "user1@email.com");
        sqlUser.addUser("user2", "pass2", "user2@email.com");
        sqlUser.clear();

        assertNull(sqlUser.getUser("user1"));
        assertNull(sqlUser.getUser("user2"));
    }
}