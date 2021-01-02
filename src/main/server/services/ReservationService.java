package main.server.services;

import main.server.models.documents.GeneralDocument;
import main.server.models.exceptions.ReservationException;
import main.server.models.exceptions.SuspensionException;
import main.server.models.members.Subscriber;

import java.net.Socket;

public class ReservationService extends LibraryService implements Runnable {

    public ReservationService(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        Subscriber sb = null;
        GeneralDocument doc = null;

        sb = requestSubscriber();
        doc = requestDocument();
        boolean success = false;

        do {
            try {

                doc.reservationFor(sb);
                send("The document : " + doc + " has been successfully reserved");
                success = true;

            } catch(ReservationException e1) {

                if(LibraryService.isReservationExpiring(doc)) {
                    while(!doc.isAvailable()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        long seconds = LibraryService.getReservationRemindingTimeFor(doc);
                        send("Please wait! The previous reservation will expire in " + seconds + " seconds ...\n");
                    }
                }
                else {

                    send("error : " + e1.getMessage());
                    send("Do you want to put an alert on this document ? (Y/N)");
                    String response = (String)read();
//                if(response.equals("Y"))
                    doc.register(sb);
                }
            } catch (SuspensionException e2) {
                send("error : " + e2.getMessage());
            }
        } while(!success);
    }
}
