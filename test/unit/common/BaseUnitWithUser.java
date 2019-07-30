package unit.common;

import models.User;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class BaseUnitWithUser extends BaseUnit {

    public static User user;

    @BeforeClass
    public static void setupUser() {

        user = new User("John", "Test", "john@test.com", 1);
        user.insert();

    }
}
