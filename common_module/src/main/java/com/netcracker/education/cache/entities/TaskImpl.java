package com.netcracker.education.cache.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.netcracker.education.cache.interfaces.Task;

public class TaskImpl implements Task {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String description;
    private Date alertTime;
    private List<Contact> contacts;
    private Status status;
    private String ownerLogin;

    public TaskImpl(String name, String description, Date alertTime,
                    List<Contact> contacts, Status status) {
        id++;
        this.name = name;
        this.description = description;
        this.alertTime = alertTime;
        this.contacts = contacts;
        this.status = status;
    }

    public TaskImpl(String name, String description, Date alertTime,
                    List<Contact> contacts) {
        this(name, description, alertTime, contacts, Status.NOT_PERFORMED);
    }

    public TaskImpl(String name, String description, Date alertTime) {
        this(name, description, alertTime, new ArrayList<Contact>());
    }


    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(Date alertTime) {
        this.alertTime = alertTime;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getOwner() {
        return ownerLogin;
    }

    @Override
    public void setUserLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

}
