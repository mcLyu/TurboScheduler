package com.netcracker.education.beans;


import com.netcracker.education.cache.entities.AuthenticationData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Mill on 25.03.2015.
 */
public class SecurityService {

    public AuthenticationData createHashSaltPassword(AuthenticationData authData) {
        authData.setSalt(SecureRandom.getSeed(4).toString());

        String combinedPassword = authData.getSalt() + authData.getPassword();

        authData.setHashSaltPassword(hashString(combinedPassword));

        return authData;
    }

    public String hashString(String string) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Algorhytm SHA-256 not found " + e.getMessage());
        }

        md.update(string.getBytes());

        byte byteData[] = md.digest();
        StringBuilder hexString = new StringBuilder();
        int passwordComplication = 5;
        for (int i=0;i<passwordComplication;i++) {
            String hex=Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String checkUserExistence(AuthenticationData authData,AuthenticationData dataBaseAuthData) {
         String combinedPassword = dataBaseAuthData.getSalt() + authData.getPassword();
         String session = "";
         String hashPasswordFromClient= hashString(combinedPassword);
         if (hashPasswordFromClient.equals(dataBaseAuthData.getHashSaltPassword()))
             session = hashString(String.valueOf(System.currentTimeMillis()));
         return session;
    }
}
