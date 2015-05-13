package com.netcracker.education.beans;

import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.cache.interfaces.Observer;
import com.netcracker.education.cache.interfaces.Task;

/**
 * Created by Mill on 4/12/2015.
 */
public class AlerterService implements Observer {
    private TaskList taskList;
    Thread alerterThread;
    NextGenAlerter alerter;

    public AlerterService(TaskService taskService) {
        taskService.registerObserver(this);
        taskList = taskService.getTaskList();
    }


    private void updateUserTasks(TaskList newTaskList) {
        Task nextTask = newTaskList.getNextTask();
        if (nextTask != null) {
            taskList = newTaskList;
            if (alerter != null)
                alerter.interrupt();

            alerter = new NextGenAlerter(taskList.getNextTask());
            alerterThread = new Thread(alerter);
            alerterThread.start();
        }
        else alerter.interrupt();
    }

    @Override
    public void update(Object obj) {
        updateUserTasks((TaskList) obj);
    }
}
