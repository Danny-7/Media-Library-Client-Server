package main.server.services;

import main.server.models.documents.GeneralDocument;
import main.server.models.exceptions.ReservationException;
import main.server.models.exceptions.SuspensionException;
import main.server.models.members.Subscriber;

import java.net.Socket;

/** ReservationService : A service for reserve a document
 * if the reservation expiring in less than 30 s
 * the subscriber wait for the end and the document will reserve for him
 * Also if isn't available he can choose to set an alert on this document
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class ReservationService extends LibraryService implements Runnable {

    public ReservationService(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        Subscriber sb;
        GeneralDocument doc;

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
                        send("Please wait! The previous reservation will expire in "
                                + LibraryService.getReservationRemindingTimeFor(doc) + " seconds ...\n");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    send("error : " + e1.getMessage());
                    send("Do you want to put an alert on this document ? (Y/N)!");
                    String response = (String) read();
                    if (response.equalsIgnoreCase("y")) {
                        doc.register(sb);
                        send("An alert was enabled! Bye :)");
                    } else
                        send("Bye :)");
                    success = true;
                }
            } catch (SuspensionException e2) {
                send("error : " + e2.getMessage());
            }
        } while(!success);
    }
}
