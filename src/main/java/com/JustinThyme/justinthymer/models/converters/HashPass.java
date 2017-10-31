package com.JustinThyme.justinthymer.models.converters;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class HashPass {
    public HashPass(char[] passwordChars, byte[] salt, int i, int i1) {
    }

    //note taken with slight modifications from OWASP Hashing Java page
    //https://www.owasp.org/index.php/Hashing_Java

    public static byte[] hashPass(char[] password, byte[] salt, int iterations, int keyLength) {

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            // try PBKDF2WithHmacSHA1 if above throws NoSuchAlgo
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            return res;

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
