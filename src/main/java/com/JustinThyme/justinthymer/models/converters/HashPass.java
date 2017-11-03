package com.JustinThyme.justinthymer.models.converters;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.security.SecureRandom;


//note below derived from https://dzone.com/articles/storing-passwords-java-web

public class HashPass {



    //Map<String, String> SALTEDHASH = new HashMap<String, String>();
    //public static final String SALT = "this-is-some-salty-stuff";

    public static String saltShaker() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt.toString();
    }

    //byte[] salt = saltyPhrase.getBytes();



    public static String saltHash(String password) {
        String saltedPassword = saltShaker() + password;
        String hashedPassword = generateHash(saltedPassword);
        return hashedPassword;
    }


    public static String generateHash(String password) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-512");
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
