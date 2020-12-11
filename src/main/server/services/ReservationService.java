package main.server.services;

import main.server.models.documents.GeneralDocument;
import main.server.models.exception.BorrowException;
import main.server.models.members.Subscriber;

import java.net.Socket;

public class ReservationService extends LibraryService implements Runnable{

    public ReservationService(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try {
            send("Enter your subscriber number please ?");

            String read;
            do {
                read = read().toString();
                System.out.println("read : " + read);
            } while(!isInteger(read));

            int subNumber = Integer.parseInt(read);
//            int subNumber = (int) read();

            Subscriber sb = findSubscriber(subNumber);
            if(sb == null)
                throw new IllegalAccessException("The subscriber doesn't exist !");

            send("Enter a document number please ?");

            do {
                read = read().toString();
            } while(!isInteger(read));
            send("read: " + read);

            int docNumber = Integer.parseInt(read);
//            int docNumber = (int) read();

            GeneralDocument doc = findDocument(docNumber);
            if(doc == null)
                throw new IllegalAccessException("The document doesn't exist !");

            doc.reservationFor(sb);

            send("The document : " + doc + " has been successfully reserved");

        } catch(IllegalAccessException | BorrowException e1 ) {
            send("error : " + e1.getMessage());
        }
    }

    private static boolean isInteger(String str)
    {
        try
        {
            int i = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
