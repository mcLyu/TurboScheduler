package com.netcracker.education.cache.entities;

import com.netcracker.education.cache.interfaces.Task;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@XmlType( propOrder = { "name", "description", "alertTime", "status" , "owner"} )
@XmlRootElement( name = "Task" )
public class TaskImpl extends ServerCommandParameter implements Task {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private Date alertTime;
    private Status status;
    private String owner;
    public TaskImpl() {}

    public TaskImpl(String name, String description, Date alertTime) {
        this.name = name;
        this.description = description;
        setAlertTime(alertTime);
    }

    public TaskImpl(String name, String description, Date alertTime,String owner) {
        this(name, description, alertTime);
        this.owner = owner;
    }

    //Getters and Setters

    public String getName() {
        return name;
    }
    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    @XmlElement(name = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAlertTime() {
        return alertTime;
    }

    @XmlElement(name = "alertTime")
    public void setAlertTime(Date alertTime)  {
        this.alertTime = alertTime;
    }

    public Status getStatus() {
        return status;
    }
    @XmlElement(name = "status")
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }
    @XmlElement(name = "owner")
    public void setOwner(String ownerLogin) {
        this.owner = ownerLogin;
    }


    public void updateToCurrentTimeZone(){
        if (alertTime != null){
            DateFormat myFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
            myFormat.setTimeZone(TimeZone.getDefault());
            String parsedFormat = myFormat.format(alertTime);
            try {
                this.alertTime = myFormat.parse(parsedFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
