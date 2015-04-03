package com.netcracker.education;

import com.netcracker.education.beans.TaskService;
import com.netcracker.education.cache.entities.AuthenticationData;
import com.netcracker.education.cache.entities.Command;
import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.cache.interfaces.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Mill on 19.03.2015.
 */
public class Server extends Thread {
    Socket fromclient;
    int num;
    TaskList taskList;
    com.netcracker.education.beans.TaskService taskService;
    static Map<String, String> sessions = new HashMap();

    public static void main(String args[]) {
        try {
            int i = 0; // счётчик подключений
            ServerSocket server = new ServerSocket(1111);
            System.out.println("Welcome to server side");
            while (true) {
                new Server(i, server.accept());
                i++;
            }
        } catch (Exception e) {
            System.out.println("init error: " + e);
        }
    }

    public Server(int num, Socket fromclient) {
        taskService = new TaskService();
        this.num = num;
        this.fromclient = fromclient;
        System.out.println("Client connected");
        setPriority(NORM_PRIORITY);
        start();
    }

    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(fromclient.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(fromclient.getInputStream());
            String command;
            command = in.readObject().toString();
            if (Command.valueOf(command) != null) {
                System.out.println("Command : " + command);
                executeCommand(Command.valueOf(command), in, out);
            }

            out.close();
            in.close();
        } catch (Exception e) {
            System.out.println("init error: " + e);
        } // вывод исключений
    }

    private void executeCommand(Command command, ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        // TODO:make interface
        switch (command) {
            case AUTHORIZATION: {
                AuthenticationData authData = (AuthenticationData) in.readObject();
                String session = taskService.authorizeUser(authData);
                sessions.put(session, authData.getLogin());
                out.writeObject(session);
            }
            break;
            case REGISTRATION: {
                AuthenticationData authData = (AuthenticationData) in.readObject();
                taskService.registerUser(authData);
            }
            break;
            case LOAD: {
                String session = (String) in.readObject();
                String login = sessions.get(session);
                TaskList taskList;
                if (login != null) {
                    taskList = taskService.getTaskList(login);
                    out.writeObject(taskList);
                }
            }
            break;
            case ADD: {
                String session = (String) in.readObject();
                String login = sessions.get(session);
                Task task = (Task) in.readObject();
                task.setUserLogin(login);
                if (login != null) {
                    taskService.addTask(task);
                    taskList = taskService.getTaskList(login);
                    out.writeObject(taskList);
                }
            }
            break;
            case REMOVE:{
                String session = (String) in.readObject();
                String login = sessions.get(session);
                int taskNumber = (Integer) in.readObject();
                taskService.getTaskList(login);
                taskService.removeTask(taskNumber);
                taskList = taskService.getTaskList(login);
                out.writeObject(taskList);
            }
            break;
        }
    }
}
