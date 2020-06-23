package contexttests;

import enums.Role;
import objects.user.User;
import org.junit.Assert;
import org.junit.Test;
import usercontext.IUserContext;
import usercontext.UserContext;
import util.DbConnections;

public class UserContextTests {

    private IUserContext context = new UserContext(DbConnections.inMemoryDB());

    @Test
    public void testGetUser() {
        User u = context.getByName("tjerk");

        Assert.assertEquals(9, u.getId());
        Assert.assertEquals(Role.ADMIN, u.getRole());
        Assert.assertNotNull(u.getEmailAddress());
    }

    @Test
    public void testGetEmail() {
        String email = context.getEmail(9);

        Assert.assertNotNull(email);
        Assert.assertTrue(email.contains("@"));
    }
}
