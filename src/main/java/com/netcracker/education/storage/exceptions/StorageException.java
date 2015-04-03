package com.netcracker.education.storage.exceptions;

public class StorageException extends Exception {
	public StorageException(){
		super("Save / load is not successful");
	}
}
