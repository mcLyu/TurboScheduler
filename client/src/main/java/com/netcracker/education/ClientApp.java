package com.netcracker.education;

import com.netcracker.education.gui.AuthorizationForm;

import java.io.IOException;
import java.util.logging.LogManager;

/**
 * Created by Mill on 20.03.2015.
 */
public class ClientApp {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        LogManager.getLogManager().readConfiguration(
                ClientApp.class.getResourceAsStream("/log4j.properties"));

        AuthorizationForm authorizationForm = new AuthorizationForm(new Client());
    }
}
