package com.netcracker.education.gui;

import com.netcracker.education.Client;
import com.netcracker.education.cache.entities.AuthenticationData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Mill on 19.03.2015.
 */
public class AuthorizationForm extends JFrame {
    Client client;

    public AuthorizationForm(Client client) {
        this.client = client;
        initComponents();
    }

    private void initComponents() {
        setTitle("Authorization");
        setPreferredSize(new Dimension(250, 150));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Box mainBox = Box.createVerticalBox();
        Box box1 = Box.createHorizontalBox();
        box1.add(new Label("Login"));
        final JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(800, 20));
        box1.add(textField);
        mainBox.add(box1);

        Box passwordBox = Box.createHorizontalBox();
        passwordBox.add(new Label("Password: "));
        final JPasswordField passwordTextField = new JPasswordField();
        passwordTextField.setPreferredSize(new Dimension(100, 20));
        passwordBox.add(passwordTextField);
        mainBox.add(passwordBox);

        Box box2 = Box.createHorizontalBox();

        JButton okButton = new JButton("Sign in");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String session = null;
                try {
                    AuthenticationData authData = new AuthenticationData(textField.getText(), passwordTextField.getText());
                    session = client.autorizeUser(authData);
                    MainForm mainForm = new MainForm(client);
                    mainForm.updateTaskList(client.loadTaskList(session));
                    setVisible(false);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        box2.add(okButton);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AuthenticationData authData = new AuthenticationData(textField.getText(),passwordTextField.getText());
                    client.registerUser(authData);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        box2.add(registerButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        box2.add(cancelButton);
        mainBox.add(box2);
        add(mainBox);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


}
