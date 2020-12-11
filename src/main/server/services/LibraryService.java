package main.server.services;

import main.server.main.ServerApp;
import main.server.models.documents.GeneralDocument;
import main.server.models.members.Subscriber;

import java.net.Socket;

public class LibraryService extends NetworkService{

    public LibraryService(Socket socket) {
        super(socket);
    }

    protected GeneralDocument findDocument(int id) {
        return ServerApp.documents.stream()
                .filter(doc -> doc.number() == id)
                .findAny()
                .orElse(null);
    }

    protected Subscriber findSubscriber(int id) {
        return ServerApp.subscribers.stream()
                .filter(doc -> doc.getId() == id)
                .findAny()
                .orElse(null);
    }

}
