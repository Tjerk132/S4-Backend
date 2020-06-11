package util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Logger;

public final class HashSaltAuthentication
{
    private HashSaltAuthentication() {
        //Nothing
    }

    private static final Logger logger = Logger.getLogger(HashSaltAuthentication.class.getName());

    // The higher the number of iterations the more expensive
    // computing the hash is for us but also for the attacker.
    private static final int iterations = 20*1000;
    private static final int desiredKeyLen = 256;

    /** Computes a salted PBKDF2 hash of given plaintext password
     suitable for storing in a database.
     Empty passwords are not supported. */
    public static String getSaltedHash(String password)  {

        final int saltLength = 32;
        byte[] salt = new byte[saltLength];
        try {

            salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength);
            // store the salt with the password
        } catch (NoSuchAlgorithmException e) {
            logger.warning(e.toString());
        }
        return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
    }

    /** Checks whether given plaintext password corresponds
     to a stored salted hash of the password. */
    public static boolean check(String password, String stored) {

        String[] saltAndHash = stored.split("\\$");
        if (saltAndHash.length != 2) {
            throw new IllegalStateException(
                    "The stored password must have the form 'salt$hash'");
        }

        String hashOfInput = hash(password, Base64.decodeBase64(saltAndHash[0]));
        return hashOfInput.equals(saltAndHash[1]);
    }

    // using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
    // cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
    private static String hash(String password, byte[] salt) {

        SecretKey key = null;
        try {
            if (password == null || password.length() == 0)
                throw new IllegalArgumentException("Empty passwords are not supported.");

            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            key = f.generateSecret(new PBEKeySpec(
                    password.toCharArray(), salt, iterations, desiredKeyLen));
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.warning(e.toString());
        }
        assert key != null : "SecretKey is null";

        return Base64.encodeBase64String(key.getEncoded());
    }

}

