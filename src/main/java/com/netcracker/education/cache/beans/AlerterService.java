package com.netcracker.education.cache.beans;

import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.cache.interfaces.Observer;
import com.netcracker.education.cache.interfaces.Task;

import java.util.Date;

/**
 * Created by Lyu on 12.11.2014.
 */
public class AlerterService extends Thread implements Observer {
    NextGenAlerter alerter;
    Thread alerterThread;
    private TaskService taskService;

    public AlerterService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void update(Object obj) {
        TaskList taskList = (TaskList) obj;
        if (taskList.isEmpty()) return;

        if (alerterThread != null)
            alerterThread.stop();

        alerter = new NextGenAlerter(taskList.getNextTask(), taskService);
        alerterThread = new Thread(alerter);
        alerterThread.start();
    }
}
