package com.netcracker.education.cache.entities;

import java.io.Serializable;

/**
 * Created by Mill on 26.03.2015.
 */
public class AuthenticationData implements Serializable{
    private String login;
    private String password;
    private String salt;
    private String hashSaltPassword;

    public AuthenticationData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public AuthenticationData(String login, String hashSaltPassword ,String salt) {
        this.login = login;
        this.salt = salt;
        this.hashSaltPassword = hashSaltPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHashSaltPassword() {
        return hashSaltPassword;
    }

    public void setHashSaltPassword(String hashSaltPassword) {
        this.hashSaltPassword = hashSaltPassword;
    }

}

