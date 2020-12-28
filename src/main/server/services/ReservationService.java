package main.server.services;

import main.server.models.documents.GeneralDocument;
import main.server.models.exceptions.ReservationException;
import main.server.models.exceptions.SuspensionException;
import main.server.models.members.Subscriber;

import java.net.Socket;

public class ReservationService extends LibraryService implements Runnable{

    public ReservationService(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        Subscriber sb = null;
        GeneralDocument doc = null;
        try {
            sb = requestSubscriber();
            doc = requestDocument();

            doc.reservationFor(sb);

            send("The document : " + doc + " has been successfully reserved");

        } catch(ReservationException | SuspensionException e1) {
            send("error : " + e1.getMessage());
            if(e1 instanceof ReservationException ) {
                send("Do you want to put an alert on this document ? (Y/N)");
                assert doc != null;
                String response = (String)read();
//                if(response.equals("Y"))
                doc.register(sb);
            }
        }
    }
}
