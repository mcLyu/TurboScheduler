package com.netcracker.education.beans;

import com.netcracker.education.cache.entities.AuthenticationData;
import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.cache.interfaces.Observer;
import com.netcracker.education.cache.interfaces.Task;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Lyu on 12.11.2014.
 */
public class TaskService implements Observer {
    private Queue<Task> alertQueue = new PriorityQueue<>();
    private TaskList taskList;

    public TaskService() {
        taskList = new TaskList();
    }

    public TaskService(String login){
        taskList = DataBaseService.loadUserTasks(login);
    }

    public TaskService(TaskList taskList) {
        this.taskList = taskList;
        taskList.registerObserver(this);
    }

    public void registerUser(AuthenticationData authData){
        DataBaseService.registerUser(authData);
    }

    public void addTask(Task task) {
        DataBaseService.addTask(task);
        taskList.addTask(task);
    }

    public void removeTask(int taskNumber) {
        DataBaseService.removeTask(taskList.getTask(taskNumber));
        taskList.removeTask(taskNumber);
    }

    public String authorizeUser(AuthenticationData authData) {
        String session = DataBaseService.authorizeUser(authData);
        return session;
    }

    public void deleteTask(Task task) {
        taskList.deleteTask(task);
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


    /**
     * Shows task to the user, user response, extends task or transfers it to performed.
     *
     * @param alertTask, it needs to show the user
     */
    public void alert(Task alertTask) {
        /*if (alertQueue.isEmpty()) {
            AlerterForm alerterForm = new AlerterForm();
            boolean done = alerterForm.waitResult(alertTask);
            if (done) {
                removeTask(0);
                Task performedTask = new TaskImpl(alertTask.getName(), alertTask.getDescription(), new Date(alertTask.getAlertTime().getTime() - 1000));
                taskList.addPerformedTask(performedTask);
            } else
                setTask(alertTask, new TaskImpl(alertTask.getName(), alertTask.getDescription(), new Date(alertTask.getAlertTime().getTime() + 30 * 60 * 1000)));
        } else alertQueue.add(alertTask);*/
    }

    @Override
    public void update(Object obj) {
        this.taskList = (TaskList) obj;
        //view.updateTaskList();
    }
}
