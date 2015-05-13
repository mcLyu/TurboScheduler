package com.netcracker.education;

import com.netcracker.education.beans.TaskService;
import com.netcracker.education.cache.interfaces.Task;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by Mill on 19.03.2015.
 */
public class ServerManager extends Thread {
    private static Logger log = Logger.getLogger(ServerManager.class.getName());
    static boolean works = true;
    TaskService taskService;

    public ServerManager(){
    }

    public ServerManager(TaskService taskService) {
            this.taskService=taskService;
    }

    public void run(){
        try {
            ServerSocket server = new ServerSocket(1111);
            log.debug("Server thread is started.");
            while (works) {
                new Server(server.accept(), taskService);
            }
        }
        catch(IOException e){
            log.error("Unable to start server thread",e);
        }
    }

    public boolean sendToClientAlertedTask(Task task) {
        boolean taskDone = false;
        log.debug("Send to client alerted task \"" + task.getName() + "\"...");
        try (Socket s = new Socket("127.0.0.1", 2222);
             ObjectInputStream in = new ObjectInputStream(s.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());) {
            out.writeObject(task);
            taskDone = (boolean) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Unable to send to client alerted task",e);
        }
        return taskDone;
    }

    public static void shutDownServer() {
        works = false;
        System.exit(0);
    }
}
