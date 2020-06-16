package utiltests;

import org.junit.Assert;
import org.junit.Test;
import util.HashSaltAuthentication;

public class HashSaltAuthenticationTests {

    @Test
    public void checkSaltedHash() {
        //create a new hash to imitate a stored user password.
        String hash = HashSaltAuthentication.getSaltedHash("123");
        boolean correct = HashSaltAuthentication.check("123", hash);

        Assert.assertTrue(correct);
    }
}
