package main.server.services;

import main.server.main.ServerApp;
import main.server.models.documents.GeneralDocument;
import main.server.models.members.Subscriber;

import java.net.Socket;

public class LibraryService extends NetworkService {

    public LibraryService(Socket socket) {
        super(socket);
    }

    private GeneralDocument findDocument(int id) {
        return ServerApp.documents.stream()
                .filter(doc -> doc.number() == id)
                .findAny()
                .orElse(null);
    }

    private Subscriber findSubscriber(int id) {
        return ServerApp.subscribers.stream()
                .filter(doc -> doc.getId() == id)
                .findAny()
                .orElse(null);
    }

    protected Subscriber requestSubscriber() {
        send("Enter your subscriber number please ?");
        Subscriber sb = null;
        while (true) {
            try {
                int subNumber = Integer.parseInt(read().toString());
                sb = findSubscriber(subNumber);
                if (sb == null)
                    throw new IllegalAccessException("The subscriber doesn't exist !");
                break;
            } catch (NumberFormatException e) {
                send("Please, enter a number");
            } catch (IllegalAccessException e) {
                send(e.getMessage());
            }
        }
        return sb;
    }

    protected GeneralDocument requestDocument() {
        send("Enter a document number please ?");
        GeneralDocument doc = null;
        while (true) {
            try {
                int docNumber = Integer.parseInt(read().toString());
                doc = findDocument(docNumber);
                if (doc == null)
                    throw new IllegalAccessException("The document doesn't exist !");
                break;
            } catch (NumberFormatException e) {
                send("Please, enter a number");
            } catch (IllegalAccessException e) {
                send(e.getMessage());
            }
        }
        return doc;
    }

}
