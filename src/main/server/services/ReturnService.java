package main.server.services;

import main.server.models.documents.GeneralDocument;
import main.server.models.exception.BorrowException;
import main.server.models.members.Subscriber;

import java.net.Socket;

public class ReturnService extends LibraryService implements Runnable{

    public ReturnService(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try {
            Subscriber sb = requestSubscriber();
            GeneralDocument doc = requestDocument();

            doc.returnBack();

            send("The document : " + doc + " has been successfully returned");

        } catch(BorrowException e1 ) {
            send("error : " + e1.getMessage());
        }
    }
}
