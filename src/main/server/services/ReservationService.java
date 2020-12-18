package main.server.services;

import main.server.models.documents.GeneralDocument;
import main.server.models.exception.ReservationException;
import main.server.models.exception.SuspensionException;
import main.server.models.members.Subscriber;

import java.net.Socket;

public class ReservationService extends LibraryService implements Runnable{

    public ReservationService(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try {
            Subscriber sb = requestSubscriber();
            GeneralDocument doc = requestDocument();

            doc.reservationFor(sb);

            send("The document : " + doc + " has been successfully reserved");

        } catch(ReservationException | SuspensionException e1) {
            send("error : " + e1.getMessage());
        }
    }
}
