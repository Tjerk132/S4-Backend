package nl.fhict.s4.restserver.security.jwt.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

class AESAlgorithm {

    AESAlgorithm() {

        final String key = "Bar12345Bar12345"; // 128 bit key
        // Create key and cipher
        try {
            aesKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher = Cipher.getInstance("AES");
        }
        catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            logger.error(e.toString());
        }
    }
    private static final Logger logger = LoggerFactory.getLogger(AESAlgorithm.class.getName());

    private Key aesKey;
    private Cipher cipher;

    String decrypt(String text) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        // convert the string to byte array
        byte[] bb = new byte[text.length()];
        for (int i=0; i<text.length(); i++) {
            bb[i] = (byte) text.charAt(i);
        }
        // decrypt the text
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(cipher.doFinal(bb));
    }

    String encrypt(String text) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b: encrypted) {
            sb.append((char)b);
        }
        return sb.toString();
    }
}
