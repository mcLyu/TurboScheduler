package com.netcracker.education;

import com.netcracker.education.beans.TaskService;
import com.netcracker.education.cache.interfaces.Request;
import com.netcracker.education.cache.beans.xml.jaxb.JAXBParser;
import com.netcracker.education.cache.entities.Command;
import com.netcracker.education.cache.interfaces.XMLParser;
import com.netcracker.education.entities.commands.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mill on 10.04.2015.
 */
public class Server extends Thread{
    private static Logger log = Logger.getLogger(Server.class.getName());
    Socket fromclient;
    TaskService taskService;
    static Map<String, String> sessions = new HashMap<>();
    static Map<Command, Class<? extends com.netcracker.education.interfaces.Command>> commandsMapping = new EnumMap<>(Command.class);

    public Server(Socket fromclient, TaskService taskService) {
        initCommands();
        this.taskService = taskService;
        this.fromclient = fromclient;
        log.debug("Client connected.");
        start();
    }

    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(fromclient.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(fromclient.getInputStream());) {

            String xmlRequest = in.readObject().toString();
            log.debug("Request from client:\n" + xmlRequest);
            XMLParser xmlParser = new JAXBParser();
            Request request = xmlParser.parseRequest(xmlRequest);
            Command currentCommand;
            if ((currentCommand = request.getCommand()) != null) {
                Class<? extends com.netcracker.education.interfaces.Command> commandClass = commandsMapping.get(currentCommand);
                com.netcracker.education.interfaces.Command command = commandClass.newInstance();
                sessions = command.execute(in, out, taskService, sessions,request);
            }

        } catch (ClassNotFoundException|IOException e) {
            log.error("Error reading command",e);
        } catch (InstantiationException|IllegalAccessException e) {
            log.error("This command can not be processed", e);
        }
    }


    private static void initCommands() {
        commandsMapping.put(Command.AUTHORIZATION, AuthCommand.class);
        commandsMapping.put(Command.REGISTRATION, RegistrationCommand.class);
        commandsMapping.put(Command.ADD, AddCommand.class);
        commandsMapping.put(Command.LOAD, LoadCommand.class);
        commandsMapping.put(Command.REMOVE, RemoveCommand.class);
        commandsMapping.put(Command.SHUTDOWN, ShutDownCommand.class);
    }
}
