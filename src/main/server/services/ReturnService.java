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

            send("Enter your subscriber number please ?");

            int subNumber = (int) read();

            Subscriber sb = findSubscriber(subNumber);
            if(sb == null)
                throw new IllegalAccessException("The subscriber doesn't exist !");

            send("Enter a document number please ?");

            int docNumber = (int) read();
            GeneralDocument doc = findDocument(docNumber);
            if(doc == null)
                throw new IllegalAccessException("The document doesn't exist !");

            doc.returnBack();

            send("The document : " + doc + " has been successfully returned");

        } catch(IllegalAccessException | BorrowException e1 ) {
            send("error : " + e1.getMessage());
        }
    }



}
