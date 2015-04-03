package com.netcracker.education.beans;

import com.netcracker.education.cache.interfaces.Task;

import java.util.Date;

/**
 * Created by Mill on 06.12.2014.
 */
public class NextGenAlerter implements Runnable {
    Task task;
    TaskService taskService;
    final static int WAIT_MILLISECONDS_INTERVAL = 5000;

    public NextGenAlerter(Task task, TaskService taskService) {
        this.task = task;
        this.taskService = taskService;
    }

    /**
     * Waits for the tasks alert time, and signalized about them.
     * @throws InterruptedException
     */
    private void monitorTask() throws InterruptedException {
        while (!soonAlert()) {
            Thread.sleep(WAIT_MILLISECONDS_INTERVAL);
        }
        taskService.alert(task);
    }

    /**
     * @return time to alert less than WAIT_MILLISECONDS_INTERVAL?
     */
    private boolean soonAlert() {
        Date currentTime = new Date();
        return (task.getAlertTime().getTime() - currentTime.getTime() < WAIT_MILLISECONDS_INTERVAL);
    }

    @Override
    public void run() {
        try {
            monitorTask();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
