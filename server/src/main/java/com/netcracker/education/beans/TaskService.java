package com.netcracker.education.beans;

import com.netcracker.education.cache.entities.AuthenticationData;
import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.cache.interfaces.Observable;
import com.netcracker.education.cache.interfaces.Observer;
import com.netcracker.education.cache.interfaces.Task;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Lyu on 12.11.2014.
 */
public class TaskService implements Observable {
    private TaskList taskList;
    List<Observer> observers = new LinkedList<>();
    public TaskService() {
        taskList = new TaskList();
    }

    public TaskService(String login){
        taskList = DataBaseService.loadUserTasks(login);
        notifyObservers();
    }

    public TaskService(TaskList taskList) {
        this.taskList = taskList;
    }

    public void registerUser(AuthenticationData authData){
        DataBaseService.registerUser(authData);
    }

    public void addTask(Task task) {
        DataBaseService.addTask(task);
        taskList.addTask(task);
        notifyObservers();
    }

    public void removeTask(int taskNumber) {
        DataBaseService.removeTask(taskList.getTask(taskNumber));
        taskList.removeTask(taskNumber);
        notifyObservers();
    }

    public String authorizeUser(AuthenticationData authData) {
        String session = DataBaseService.authorizeUser(authData);
        return session;
    }

    public void deleteTask(Task task) {
        taskList.removeTask(task);
    }

    public void setTask(int taskNumber, Task task) {
        taskList.setTask(taskNumber, task);
    }

    public void setTask(Task oldTask, Task newTask) {
        taskList.setTask(oldTask, newTask);
    }

    public Task getTask(int taskNumber) {
        return taskList.getTask(taskNumber);
    }

    public TaskList getTaskList(String login) {
        taskList = DataBaseService.loadUserTasks(login);
        return taskList;
    }

    public TaskList getTaskList(){
        return taskList;
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
        for (Observer observer : observers)
        {
            observer.update(taskList);
        }
    }
}
