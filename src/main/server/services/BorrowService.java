package main.server.services;

import main.server.models.documents.GeneralDocument;
import main.server.models.exceptions.BorrowException;
import main.server.models.exceptions.SuspensionException;
import main.server.models.members.Subscriber;

import java.net.Socket;

public class BorrowService extends LibraryService implements Runnable {

    public BorrowService(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try {
            Subscriber sb = requestSubscriber();
            GeneralDocument doc = requestDocument();

            doc.borrowBy(sb);

            send("The document : " + doc + " has been successfully borrowed");

        } catch(BorrowException | SuspensionException e1 ) {
            send("error : " + e1.getMessage());
        }
    }
}
