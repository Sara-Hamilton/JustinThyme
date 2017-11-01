package com.JustinThyme.justinthymer.models.converters;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;



//note below derived from https://dzone.com/articles/storing-passwords-java-web

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
