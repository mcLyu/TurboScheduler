package com.netcracker.education.cache.entities;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Mill on 26.03.2015.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType( propOrder = { "login", "password" } )
@XmlRootElement( name = "authenticationData" )
public class AuthenticationData extends ServerCommandParameter implements Serializable{
    @XmlElement(name = "login")
    private String login;
    @XmlElement(name = "password")
    private String password;
    private String salt;
    private String hashSaltPassword;
    public AuthenticationData(){}
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

