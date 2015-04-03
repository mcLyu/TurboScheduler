package com.netcracker.education.cache.interfaces;


import com.netcracker.education.cache.entities.Contact;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface Task extends Serializable {

    public enum Status {PERFORMED, NOT_PERFORMED, OUTDATED};
	void addContact(Contact contact);
	int getId();
	void setId(int id);
	String getName();
	void setName(String name);
	String getDescription();
	void setDescription(String description);
	Date getAlertTime();
	void setAlertTime(Date alertTime);
	List<Contact> getContacts();
	void setContacts(List<Contact> contacts);
	Status getStatus();
	void setStatus(Status status);
    String getOwner();
    void setUserLogin(String ownerLogin);
}
