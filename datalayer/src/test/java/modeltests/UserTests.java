package modeltests;

import enums.Role;
import objects.user.User;
import org.junit.Assert;
import org.junit.Test;

public class UserTests {

    @Test
    public void testCreateUser() {
        User user = new User("username", "password", Role.USER);
        Assert.assertEquals(Role.USER, user.getRole());
        Assert.assertNull(user.getEmailAddress());
    }
}
