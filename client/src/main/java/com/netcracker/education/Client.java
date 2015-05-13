package com.netcracker.education;


import com.netcracker.education.cache.beans.xml.jaxb.JAXBParser;
import com.netcracker.education.cache.entities.*;
import com.netcracker.education.cache.interfaces.*;
import com.netcracker.education.cache.beans.request.XMLRequest;
import com.netcracker.education.cache.beans.xml.jaxb.JAXBConverter;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mill on 19.03.2015.
 */
public class Client implements Observable {
    private static Logger log = Logger.getLogger(Client.class.getName());
    private Socket fromserver;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private transient List<Observer> observers = new LinkedList<>();
    private TaskList updatedTaskList;
    private String session;

    public Client(){
        new WaiterAlertedTask().start();
    }

    private void openConnection(int port) {
        try {
            fromserver = new Socket("127.0.0.1", port);
            in = new ObjectInputStream(fromserver.getInputStream());
            out = new ObjectOutputStream(fromserver.getOutputStream());
        } catch (IOException e) {
            log.error("Unable to connect to server.",e);
        }
    }

    private void closeConnection() {
        try {
            out.close();
            in.close();
            fromserver.close();
        } catch (IOException e) {
            log.error("Unable to close connection.", e);
        }
    }

    public String autorizeUser(AuthenticationData authData) throws IOException, ClassNotFoundException {
        try {
            openConnection(1111);
            Request<AuthenticationData> authenticationRequest = new XMLRequest<>(Command.AUTHORIZATION,"",authData);
            sendRequest(authenticationRequest);
            XMLParser xmlParser = new JAXBParser();
            String response = (String)in.readObject();
            session = String.valueOf(xmlParser.parseResponse(response).getObject());
        }
        finally {
            closeConnection();
        }
        return session;
    }

    public void registerUser(AuthenticationData authData) throws IOException, ClassNotFoundException {
        try {
            openConnection(1111);
            Request<AuthenticationData> authenticationRequest = new XMLRequest<>(Command.REGISTRATION,"",authData);
            sendRequest(authenticationRequest);
        }
        finally {
            closeConnection();
        }
    }

    private void sendRequest(String xmlRequest) throws IOException{
        out.writeObject(xmlRequest);
    }

    private void sendRequest(Request request) throws IOException {
        XMLConverter jaxbConverter = new JAXBConverter();
        String xmlRequest = jaxbConverter.convertToXML(request);
        sendRequest(xmlRequest);
    }

    private void sendCommandToServer(Command command) throws IOException {
        out.writeObject(command);
    }

    public void updateTaskList(TaskList taskList) {
        this.updatedTaskList = taskList;
    }

    public TaskList loadTaskList(String session){
        try {
            openConnection(1111);
            Request request = new XMLRequest(Command.LOAD,session,null);
            sendRequest(request);
            XMLParser xmlParser = new JAXBParser();
            String response = (String)in.readObject();
            updatedTaskList = (TaskList) xmlParser.parseResponse(response).getObject();
            notifyObservers();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Unable to load task list.", e);
        } finally {
            closeConnection();
        }
        if (updatedTaskList != null) return updatedTaskList;
        else return updatedTaskList;
    }

    public void addTask(Task task){
        try {
            openConnection(1111);
            Request<TaskImpl> request = new XMLRequest<>(Command.ADD,session,(TaskImpl)task);
            sendRequest(request);
            updatedTaskList = (TaskList) in.readObject();
            notifyObservers();
        } catch (IOException e) {
            log.error("Unable to add task.", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void deleteTask(int index) {
        try {
            openConnection(1111);
            Request<RemoveTaskNumber> request = new XMLRequest<>(Command.REMOVE,session,new RemoveTaskNumber(index));
            sendRequest(request);
            updatedTaskList = (TaskList) in.readObject();
            notifyObservers();
        } catch (IOException |ClassNotFoundException e) {
            log.error("Unable to delete task.", e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(updatedTaskList);
        }
    }

    public void shutDownServer() {
        try {
            openConnection(1111);
            sendCommandToServer(Command.SHUTDOWN);
        } catch (IOException e) {
            log.error("Unable to shutdown server",e);
        } finally {
            closeConnection();
        }
    }
}
