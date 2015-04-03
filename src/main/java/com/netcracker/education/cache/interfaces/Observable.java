package com.netcracker.education.cache.interfaces;

/**
 * Created by Lyu on 12.11.2014.
 */
public interface Observable {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}
