package main.server.models;

/** Subject : Interface of a subject (general document)
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public interface Subject {
    void register(ObserverLibrary observer);
    void notifyObservers();
}
