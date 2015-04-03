package com.netcracker.education.storage.data;

import java.io.*;
import java.util.Properties;

import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.storage.exceptions.ConfigStorageException;
import com.netcracker.education.storage.exceptions.StorageException;
import com.netcracker.education.storage.interfaces.Storable;
import com.netcracker.education.cache.interfaces.Storage;

/**
 * @author Osipov Ivan
 */
public class FSTaskStore implements Storage {
    private final String STORAGE_ADDRESS_PROPERTY = "STORAGE_ADDRESS";
    private final String CONFIG_PATH_DEFAULT = "configurator.ini";
    private File storage;

    public FSTaskStore() {
    }

    /**
     * Loading default config from "resources/config/configurator.ini"
     *
     * @throws ConfigStorageException Config file is not found
     */
    public void setDefaultConfig() throws ConfigStorageException {
        setConfig(CONFIG_PATH_DEFAULT);
    }

    public void setConfig(String configPath) throws ConfigStorageException {
        try (InputStream propertiesStream = new FileInputStream(new File(configPath))) {
            Properties properties = new Properties();
            properties.load(propertiesStream);
            String storageAddress = properties.getProperty(STORAGE_ADDRESS_PROPERTY);
            storage = new File(System.getProperty("user.dir") + "\\" + storageAddress);
            if (!storage.exists())
                storage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FSTaskStore createFSTaskStore() throws ConfigStorageException {
        FSTaskStore newTaskStore = new FSTaskStore();
        newTaskStore.setDefaultConfig();
        return newTaskStore;
    }

    public static FSTaskStore createFSTaskStore(String configPath) throws ConfigStorageException {
        FSTaskStore newTaskStore = new FSTaskStore();
        newTaskStore.setConfig(configPath);
        return newTaskStore;
    }

    public Storable getData() throws StorageException {
        try (FileInputStream fileInStream = new FileInputStream(storage);
             ObjectInputStream serialInputStream = new ObjectInputStream(fileInStream);) {
            return (Storable) serialInputStream.readObject();
        } catch (FileNotFoundException | EOFException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException();
        }
    }

    public void saveData(TaskList object) throws StorageException {
        try (FileOutputStream fileOutStream = new FileOutputStream(storage);
             ObjectOutputStream serialOutputStream = new ObjectOutputStream(fileOutStream);) {
            serialOutputStream.writeObject(object);
        } catch (IOException e) {
            throw new StorageException();
        }

    }

}
