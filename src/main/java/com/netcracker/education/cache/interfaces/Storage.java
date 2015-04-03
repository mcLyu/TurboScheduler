package com.netcracker.education.cache.interfaces;

import com.netcracker.education.cache.entities.TaskList;
import com.netcracker.education.storage.exceptions.ConfigStorageException;
import com.netcracker.education.storage.exceptions.StorageException;
import com.netcracker.education.storage.interfaces.Storable;

import java.io.Serializable;

public interface Storage {
	Storable getData()  throws StorageException;
	void saveData(TaskList data)  throws StorageException;
	void setDefaultConfig() throws ConfigStorageException;
	void setConfig(String configPath) throws ConfigStorageException;
}
