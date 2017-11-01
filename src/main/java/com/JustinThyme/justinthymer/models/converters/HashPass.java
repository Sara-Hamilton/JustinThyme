package com.JustinThyme.justinthymer.models.converters;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

//public class HashPass {
//    public HashPass(char[] passwordChars, byte[] salt, int i, int i1) {
//    }
//
//    //note taken with slight modifications from OWASP Hashing Java page
//    //https://www.owasp.org/index.php/Hashing_Java
//
//    public static byte[] hashPass(char[] password, byte[] salt, int iterations, int keyLength) {
//
//        try {
//            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
//            // try PBKDF2WithHmacSHA1 if above throws NoSuchAlgo
//            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
//            SecretKey key = skf.generateSecret(spec);
//            byte[] res = key.getEncoded();
//            return res;
//
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}

//note below derived from https://dzone.com/articles/storing-passwords-java-web
//package com.sandbox;

import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.util.HashMap;
        import java.util.Map;

public class HashPass {

    //Map<String, String> SALTEDHASH = new HashMap<String, String>();
    public static final String SALT = "this-is-some-salty-stuff";


    public void saltHash(String password) {
        String saltedPassword = SALT + password;
        String hashedPassword = generateHash(saltedPassword);
    }


    public static String generateHash(String password) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(password.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }

        return hash.toString();
    }

}
