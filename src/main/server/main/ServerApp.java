package main.server.main;

import main.server.BorrowServer;
import main.server.ReservationServer;
import main.server.ReturnServer;
import main.server.models.documents.DVD;
import main.server.models.documents.GeneralDocument;
import main.server.models.members.Subscriber;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** ServerApp : A server app which contains all objects of the library
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class ServerApp {
    public static List<GeneralDocument> documents;
    public static List<Subscriber> subscribers;

    private static int lastDocumentNumber = 0;
    private static int lastMemberNumber = 0;

    public static int getNewDocNumber() {
        return ++lastDocumentNumber;
    }
    public static int getNewMemberNumber() {
        return ++lastMemberNumber;
    }

    // we launch 3 threads : reservation, borrow and return
    public static void main(String[] args) {

        documents = new ArrayList<>();
        subscribers = new ArrayList<>();

        documents.add(new DVD("Interstellar", 15));
        documents.add(new DVD("Inception", 16));
        documents.add(new DVD("Fast and Furious", 10));
        documents.add(new DVD("Joker", 12));

        subscribers.add(new Subscriber("Jules Doumèche", LocalDate.of(2001,5,6)));
        subscribers.add(new Subscriber("Gwénolé Martin", LocalDate.of(2001,5,6)));
        subscribers.add(new Subscriber("Daniel Aguiar", LocalDate.of(2001,7,24)));
        subscribers.add(new Subscriber("Hugo Aguiar", LocalDate.of(2011,4,29)));

        ReservationServer reservationServer = new ReservationServer();
        BorrowServer borrowServer = new BorrowServer();
        ReturnServer returnServer = new ReturnServer();

        new Thread(reservationServer).start();
        new Thread(borrowServer).start();
        new Thread(returnServer).start();
    }
}
