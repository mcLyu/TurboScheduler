package com.netcracker.education;

import com.netcracker.education.cache.beans.AlerterService;
import com.netcracker.education.cache.beans.TaskService;
import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.cache.interfaces.Storage;
import com.netcracker.education.storage.exceptions.ConfigStorageException;
import com.netcracker.education.storage.exceptions.StorageException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Scheduler
 */
//
public class App {
    public static void main(String[] args) throws ConfigStorageException, StorageException {

    }

    public static void withTL(TaskList taskList) {
        setCorrectTime();
        TaskService service = new TaskService();
        AlerterService alerterService = new AlerterService(service);
        taskList.registerObserver(alerterService);
       /* MainForm mainForm = new MainForm();
        mainForm.setTaskService(service);
        service.setView(mainForm);
        mainForm.updateTaskList();*/
        alerterService.start();
    }

    private static void setCorrectTime() {
        String timezone = "";
        try {
            Process p = Runtime.getRuntime().exec("wmic.exe TimeZone get /value");
            BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line=reader.readLine();
            while(line!=null)
            {
                line = reader.readLine();
                if (line.contains("Caption")) {
                    timezone = line;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (timezone.contains("RTZ"))
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Samara"));
    }


    private static TaskList loadTaskList(Storage storage) throws ConfigStorageException, StorageException {
        TaskList taskList = new TaskList();
        TaskList storageTaskList = (TaskList) storage.getData();
        if (storageTaskList==null) return new TaskList();
        storageTaskList.initObservers();
        taskList.addAll(storageTaskList.getActiveTasks());
        taskList.addAll(storageTaskList.getPerformedTasks());
        return taskList;
    }
}
