package main.server.services;

import main.server.models.documents.GeneralDocument;
import main.server.models.exceptions.ReservationException;
import main.server.models.exceptions.SuspensionException;
import main.server.models.members.Subscriber;

import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Stream;

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

        boolean success = false;
        do {
            doc = requestDocument();
            try {
                doc.reservationFor(sb);
                send("The document : " + doc + " has been successfully reserved");
                success = true;
            } catch(ReservationException e1) {
                if(LibraryService.isReservationExpiring(doc) && !sb.isSuspended()) {
                    while(!doc.isAvailable()) {
                        if(!doc.isBorrowed()) {
                            send("Please wait! The previous reservation will expire in "
                                    + LibraryService.getReservationRemindingTimeFor(doc) + " seconds ...\n");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            send("Sorry this document was just borrowed");
                            break;
                        }
                    }
                }
                else {
                    send("error : " + e1.getMessage());
                    String response =
                            requestInput(new String[]{"Y", "N"},"Do you want to put an alert on this document ? (Y/N)!");

                    if (response.equalsIgnoreCase("y")) {
                        doc.register(sb);
                        send("An alert was enabled!");
                    }
                    success = true;
                }
            } catch (SuspensionException e2) {
                send("error : " + e2.getMessage());
                success = true;
            }
            if(success) {
                String response =
                        requestInput(new String[]{"Y", "N"},"Do you want to reserve another document ? (Y/N)!");
                success = !response.equalsIgnoreCase("y");
            }
        } while(!success);
        send("Bye ;)");
    }
}
