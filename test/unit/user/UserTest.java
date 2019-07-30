package unit.user;

import models.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import unit.common.BaseUnit;

import java.util.Date;

import static org.junit.Assert.*;

public class UserTest extends BaseUnit {

    static User user;

    @BeforeClass
    public static void setupUser() {
        user = new User("John", "Smith", "john@test.com", 1);
        user.insert();
    }

    @Test
    public void testUserCreate() {
        assertNotEquals(null, user.getId());
    }

    @Test
    public void testSetAuthToken() {
        String token = user.setToken();
        assertEquals(token, user.getToken());
    }

    @Test
    public void testIsAdmin() {

        user.setAccountType(1);

        user.update();

        assertEquals(true, user.isAdmin());
    }

    @Test
    public void testPasswordHash() {
        String password = "123";
        user.setPassword("123");

        assertNotEquals(user.getPassword(), password);
    }

}
