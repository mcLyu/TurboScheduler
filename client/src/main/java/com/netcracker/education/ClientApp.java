package com.netcracker.education;

import com.netcracker.education.gui.AuthorizationForm;

import java.io.IOException;
/**
 * Created by Mill on 20.03.2015.
 */
public class ClientApp {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        AuthorizationForm authorizationForm = new AuthorizationForm(new Client());

    }
}
