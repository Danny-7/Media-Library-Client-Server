package main.server.services;

import main.server.main.ServerApp;
import main.server.models.Document;
import main.server.models.documents.GeneralDocument;
import main.server.models.members.Subscriber;
import main.server.models.utils.AutomatedCancellationReservation;
import main.server.models.utils.AutomatedCancellationSuspension;

import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class LibraryService extends NetworkService {

    private static final Timer endReservations = new Timer();
    private static final Timer endSuspensions = new Timer();

    private static final HashMap<Integer, TimerTask> reservations = new HashMap<>();
    private static final HashMap<Integer, TimerTask> suspensions = new HashMap<>();

    private static final int MAX_BORROW_WEEKS = 3;
    private static final int MONTH_SUSPENDED = 1;


    public LibraryService(Socket socket) {
        super(socket);
    }

    private GeneralDocument findDocument(int id) {
        return ServerApp.documents.stream()
                .filter(doc -> doc.number() == id)
                .findAny()
                .orElse(null);
    }

    private Subscriber findSubscriber(int id) {
        return ServerApp.subscribers.stream()
                .filter(doc -> doc.getId() == id)
                .findAny()
                .orElse(null);
    }

    protected Subscriber requestSubscriber() {
        send("Enter your subscriber number please ?");
        Subscriber sb = null;
        while (true) {
            try {
                int subNumber = Integer.parseInt(read().toString());
                sb = findSubscriber(subNumber);
                if (sb == null)
                    throw new IllegalAccessException("The subscriber doesn't exist !");
                break;
            } catch (NumberFormatException e) {
                send("Please, enter a number");
            } catch (IllegalAccessException e) {
                send(e.getMessage());
            }
        }
        return sb;
    }

    protected GeneralDocument requestDocument() {
        send("Enter a document number please ?");
        GeneralDocument doc = null;
        while (true) {
            try {
                int docNumber = Integer.parseInt(read().toString());
                doc = findDocument(docNumber);
                if (doc == null)
                    throw new IllegalAccessException("The document doesn't exist !");
                break;
            } catch (NumberFormatException e) {
                send("Please, enter a number");
            } catch (IllegalAccessException e) {
                send(e.getMessage());
            }
        }
        return doc;
    }

    public static boolean isBorrowLate(GeneralDocument doc) {
        LocalDateTime borrowDate = doc.getBorrowDate();

        return Duration.between(borrowDate, LocalDateTime.now()).toMillis() >
                Duration.between(borrowDate, borrowDate.plusWeeks(MAX_BORROW_WEEKS)).toMillis();
//        return Duration.between(borrowDate, LocalDateTime.now()).toMillis() >
//                Duration.between(borrowDate, borrowDate.plusMinutes(2)).toMillis();
    }


    public static void cancelReservation(Integer id) {
        reservations.get(id).cancel();
    }

    public static void cancelSuspension(Integer id) {
        suspensions.get(id).cancel();
    }

    public static void scheduleReservation(Document doc, long time) {
        TimerTask autoReservation = new AutomatedCancellationReservation(doc);
        reservations.put(doc.number(), autoReservation);
        endReservations.schedule(autoReservation, time);
    }

    public static void scheduleSuspension(Subscriber sub) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusMonths(MONTH_SUSPENDED);
        // 2 minutes
//        LocalDateTime end = now.plusMinutes(2);
        long time = Duration.between(now, end).toMillis();

        TimerTask autoSuspension = new AutomatedCancellationSuspension(sub);
        suspensions.put(sub.getId(), autoSuspension);
        endSuspensions.schedule(autoSuspension, time);
    }

}
