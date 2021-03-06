package com.netcracker.education.cache.entities;

import com.netcracker.education.cache.exceptions.TaskIndexOutOfBoundException;
import com.netcracker.education.cache.interfaces.Observable;
import com.netcracker.education.cache.interfaces.Observer;
import com.netcracker.education.cache.interfaces.Task;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 1 on 04.11.2014.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskList extends ClientCommandParameter implements Serializable, Observable {

    @XmlElementWrapper
    @XmlAnyElement
    private List<Task> activeTasks;

    @XmlElementWrapper
    @XmlAnyElement
    private List<Task> performedTasks;
    private transient List<Observer> observers;

    public TaskList() {
        activeTasks = new LinkedList<>();
        performedTasks = new LinkedList<>();
        observers = new LinkedList<>();
    }

    public TaskList(List<Task> tasks) {
        activeTasks = new LinkedList<>();
        performedTasks = new LinkedList<>();
        observers = new LinkedList<>();
        for (Task task : tasks) {
            addTask(task);
        }
    }

    public void setPerformedTasks(List<Task> performedTasks) {
        this.performedTasks = performedTasks;
    }

    public void setActiveTasks(List<Task> activeTasks) {
        this.activeTasks = activeTasks;
    }

    public void initObservers() {
        observers = new LinkedList<>();
    }

    public void addTask(Task task) {
        if (task.getAlertTime().getTime() > new Date().getTime())
            addActiveTask(task);
        else
            addPerformedTask(task);
        notifyObservers();
    }

    private void addActiveTask(Task activeTask) {
        for (int i = 0; i < activeTasks.size() - 1; i++) {
            Task previous = activeTasks.get(i);
            Task next = activeTasks.get(i + 1);
            if ((activeTask.getAlertTime().getTime() >= previous.getAlertTime().getTime())
                    && (activeTask.getAlertTime().getTime() <= next.getAlertTime().getTime())) {
                activeTasks.add(i + 1, activeTask);
                notifyObservers();
                return;
            }
        }
        if (activeTasks.size() == 1) {
            if (activeTasks.get(0).getAlertTime().getTime() > activeTask.getAlertTime().getTime()) {
                activeTasks.add(0, activeTask);
                notifyObservers();
                return;
            }
        }
        activeTasks.add(activeTask);
        notifyObservers();
    }

    public void addPerformedTask(Task performedTask) {
        performedTasks.add(performedTask);
        notifyObservers();
    }

    public void removeTask(int taskNumber) {
        int tasksSize = activeTasks.size() + performedTasks.size();
        if (taskNumber < 0 || taskNumber >= tasksSize)
            throw new TaskIndexOutOfBoundException();
        else {
            if (taskNumber < activeTasks.size())
                activeTasks.remove(taskNumber);
            else performedTasks.remove(taskNumber - activeTasks.size());
        }
        notifyObservers();
    }

    public void removeTask(Task task) {
        int index = 0;
        for (Task t : activeTasks) {
            if (t.equals(task)) {
                removeTask(index);
                return;
            }
            index++;
        }
    }

    public void setTask(int taskNumber, Task task) {
        if (taskNumber < 0 || taskNumber >= activeTasks.size())
            throw new TaskIndexOutOfBoundException();
        else activeTasks.set(taskNumber, task);
        notifyObservers();
    }

    public void setTask(Task oldTask, Task newTask) {
        int index = 0;
        for (Task task : activeTasks) {
            if (task.equals(oldTask)) {
                setTask(index, newTask);
                return;
            }
            index++;
        }
    }

    public Task getTask(int taskNumber) {
        int tasksSize = activeTasks.size() + performedTasks.size();
        if (taskNumber < 0 || taskNumber >= tasksSize)
            throw new TaskIndexOutOfBoundException();
        else {
            if (taskNumber < activeTasks.size())
                return activeTasks.get(taskNumber);
            else return performedTasks.get(taskNumber - activeTasks.size());
        }
    }

    public void addAll(List<Task> taskList) {
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            this.addTask(task);
        }
        notifyObservers();
    }

    public void addAll(TaskList taskList) {
        for (int i = 0; i < taskList.getActiveTasks().size(); i++) {
            Task task = taskList.getActiveTasks().get(i);
            this.addTask(task);
        }
        for (int i = 0; i < taskList.getPerformedTasks().size(); i++) {
            Task task = taskList.getPerformedTasks().get(i);
            this.addTask(task);
        }
        notifyObservers();
    }

    public Task getNextTask() {
        if (activeTasks.isEmpty()) return null;
        return activeTasks.get(0);
    }


    public Object[] asList() {
        int tasksSize = activeTasks.size() + performedTasks.size();
        Object[] list = new Object[tasksSize];
        for (int i = 0; i < activeTasks.size(); i++) {
            list[i] = activeTasks.get(i).getName();
        }
        int index = 0;
        for (int i = activeTasks.size(); i < tasksSize; i++) {
            list[i] = performedTasks.get(index++).getName();
        }
        return list;
    }

    public boolean isEmpty() {
        return activeTasks.isEmpty();
    }

    public List<Task> getActiveTasks() {
        return activeTasks;
    }

    public List<Task> getPerformedTasks() {
        return performedTasks;
    }

    public Object[] getActiveTaskNames() {
        Object[] tasks = new Object[activeTasks.size()];
        for (int i = 0; i < activeTasks.size(); i++) {
            tasks[i] = activeTasks.get(i).getName();
        }
        return tasks;
    }

    public Object[] getPerformedTaskNames() {
        Object[] tasks = new Object[performedTasks.size()];
        for (int i = 0; i < performedTasks.size(); i++) {
            tasks[i] = performedTasks.get(i).getName();
        }
        return tasks;
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
            observer.update(this);
        }
    }

    public void removeUserTasks(String owner) {
        for (int i = 0; i < getActiveTasks().size(); i++) {
            Task currentTask = getActiveTasks().get(i);
            if ((currentTask != null) &&currentTask.getOwner().equals(owner))
                removeTask(currentTask);
        }
        for (int i = 0; i < getPerformedTasks().size(); i++) {
            Task currentTask = getPerformedTasks().get(i);
            if ((currentTask != null) && currentTask.getOwner().equals(owner))
                removeTask(currentTask);
        }
        notifyObservers();
    }
}
