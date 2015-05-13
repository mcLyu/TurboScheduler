package com.netcracker.education.cache.interfaces;


import java.io.Serializable;
import java.util.Date;

public interface Task extends Serializable{

    enum Status {PERFORMED, NOT_PERFORMED, OUTDATED};
	String getName();
	void setName(String name);
	String getDescription();
	void setDescription(String description);
	Date getAlertTime();
	void setAlertTime(Date alertTime);
	Status getStatus();
	void setStatus(Status status);
    String getOwner();
    void setOwner(String ownerLogin);
	void updateToCurrentTimeZone();
}
