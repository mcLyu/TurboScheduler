package com.netcracker.education;


import com.netcracker.education.cache.entities.AuthenticationData;
import com.netcracker.education.cache.entities.Command;
import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.cache.interfaces.Observable;
import com.netcracker.education.cache.interfaces.Observer;
import com.netcracker.education.cache.interfaces.Task;

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

    private Socket fromserver;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private transient List<Observer> observers = new LinkedList<Observer>();
    private TaskList updatedTaskList;
    private String session;

    private void openConnection() {
        try {
            fromserver = new Socket("127.0.0.1", 1111);
            in = new ObjectInputStream(fromserver.getInputStream());
            out = new ObjectOutputStream(fromserver.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            out.close();
            in.close();
            fromserver.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String autorizeUser(AuthenticationData authData) throws IOException, ClassNotFoundException {
        try {
            openConnection();
            sendCommandToServer(Command.AUTHORIZATION);
            out.writeObject(authData);
            session = (String) in.readObject();
        }
        finally {
            closeConnection();
        }
        return session;
    }

    public void registerUser(AuthenticationData authData) throws IOException, ClassNotFoundException {
        try {
            openConnection();
            sendCommandToServer(Command.REGISTRATION);
            sendObjectToServer(authData);
        }
        finally {
            closeConnection();
        }
    }

    private void sendObjectToServer(Object object) throws IOException {
        out.writeObject(object);
    }

    private void sendCommandToServer(Command command) throws IOException {
        out.writeObject(command);
    }

    public void updateTaskList(TaskList taskList) {
        this.updatedTaskList = taskList;
    }

    public TaskList loadTaskList(String session){
        TaskList taskList = null;
        try {
            openConnection();
            sendCommandToServer(Command.LOAD);
            sendObjectToServer(session);
            updatedTaskList = (TaskList) in.readObject();
            notifyObservers();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        if (taskList != null) return taskList;
        else return null;
    }

    public void addTask(Task task) {
        try {
            openConnection();
            sendCommandToServer(Command.ADD);
            sendObjectToServer(session);
            sendObjectToServer(task);
            updatedTaskList = (TaskList) in.readObject();
            notifyObservers();
        } catch (IOException |ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void deleteTask(int index) {
        try {
            openConnection();
            sendCommandToServer(Command.REMOVE);
            sendObjectToServer(session);
            sendObjectToServer(index);
            updatedTaskList = (TaskList) in.readObject();
            notifyObservers();
        } catch (IOException |ClassNotFoundException e) {
            e.printStackTrace();
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

}
