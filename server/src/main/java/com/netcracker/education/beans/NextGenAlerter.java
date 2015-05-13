package com.netcracker.education.beans;

import com.netcracker.education.ServerManager;
import com.netcracker.education.cache.interfaces.Task;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Mill on 06.12.2014.
 */
public class NextGenAlerter implements Runnable {
    private Task task;
    boolean interrupted;
    final static int WAIT_MILLISECONDS_INTERVAL = 5000;

    public NextGenAlerter(Task task) {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Samara"));
        this.task = task;
        interrupted = false;
    }

    public void interrupt() {
        interrupted = true;
    }

    /**
     * Waits for the tasks alert time, and signalized about them.
     *
     * @throws InterruptedException
     */
    private void monitorTask() {
        try {
            while (!soonAlert() && !interrupted) {
                Thread.sleep(WAIT_MILLISECONDS_INTERVAL);
                System.out.println("Отслеживается: " + task.getName());
            }
            if (!interrupted)
                new ServerManager().sendToClientAlertedTask(task);
        } catch (InterruptedException e) {
        }
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
        monitorTask();
    }
}
