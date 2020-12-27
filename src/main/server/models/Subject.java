package main.server.models;

public interface Subject {
    void register(ObserverLibrary observer);
    void notifyObservers();
}
