package com.netcracker.education.cache.exceptions;

/**
 * Created by 1 on 04.11.2014.
 */
public class TaskIndexOutOfBoundException extends IndexOutOfBoundsException {
    public TaskIndexOutOfBoundException() {
        super("Задачи с таким номером не существует!");
    }
}
