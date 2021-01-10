package main.server.services;

import main.server.main.ServerApp;
import main.server.models.State;
import main.server.utils.BorrowUtil;
import main.server.models.Document;
import main.server.models.ObserverLibrary;
import main.server.models.documents.GeneralDocument;
import main.server.models.members.Subscriber;
import main.server.utils.AutomatedBorrowSchedule;
import main.server.utils.AutomatedCancellationReservation;
import main.server.utils.AutomatedCancellationSuspension;

import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/** LibraryService : A service for the library
 * List of reservations, suspensions, borrows and notifications to do
 * With this service you can :
 *  / cancel or schedule a reservation, suspension, borrow task
 *  / find a document, subscriber
 *  / check the state of a borrow
 *  / get information of the reservation
 *  / utils for the notification feature of the library
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class LibraryService extends NetworkService {

    private static final Timer endReservations = new Timer();
    private static final Timer endSuspensions = new Timer();
    private static final Timer endBorrows = new Timer();

    private static final HashMap<Integer, TimerTask> reservations = new HashMap<>();
    private static final HashMap<Integer, TimerTask> suspensions = new HashMap<>();
    private static final HashMap<BorrowUtil, TimerTask> borrows = new HashMap<>();

    private static final HashMap<Document, List<ObserverLibrary>> notifyList = new HashMap<>();

    private static final int MAX_BORROW_WEEKS = 3;
    private static final int MONTH_SUSPENDED = 1;
    // 2 hours
    private static final int MAX_RESERVATION_TIME = 72000000;
    private static final int RESERVATION_EXPIRING_DELAY = 30000;
    // 60 seconds for development test
//    private static final int MAX_RESERVATION_TIME = 60000;

    public String getCatalogue() {
        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String YELLOW = "\u001B[33m";

        StringBuilder sb = new StringBuilder();
        sb.append("Catalogue of all documents\n");
        sb.append("Availability :\t" + GREEN + "Available\t" + YELLOW + "Reserved\t" + RED + "Borrowed" + RESET + "\n");
        for (GeneralDocument d : ServerApp.documents) {
            synchronized (d) {
                switch (d.getStatus()) {
                    case AVAILABLE:
                        sb.append(GREEN);
                        break;
                    case RESERVED:
                        sb.append(YELLOW);
                        break;
                    case BORROWED:
                        sb.append(RED);
                        break;
                    default:
                        break;
                }
            }
            sb.append(d.number() + ". '" + d.getTitle() + "'" + RESET + "\n");
        }
        return sb.toString();
    }

    public LibraryService(Socket socket) {
        super(socket);
    }

    public static int getMaxReservationTime() {
        return MAX_RESERVATION_TIME;
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
        Subscriber sb;
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
        GeneralDocument doc;
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

    protected String requestInput(String[] choices, String message) {
        String response;
        do {
            send(message);
            response = (String) read();
            // check if the value read is on the choices array stream
        }while(Arrays.stream(choices).noneMatch(response::equalsIgnoreCase));
        return response;
    }

    public static boolean isBorrowLate(GeneralDocument doc) {
        LocalDateTime borrowDate = doc.getBorrowDate();
        // return true if today date exceed the max borrow time
        return Duration.between(borrowDate, LocalDateTime.now()).toMillis() >
                Duration.between(borrowDate, borrowDate.plusWeeks(MAX_BORROW_WEEKS)).toMillis();
        /*
        for development test
        return Duration.between(borrowDate, LocalDateTime.now()).toMillis() >
                Duration.between(borrowDate, borrowDate.plusSeconds(30)).toMillis();
        */
    }

    public static boolean isReservationExpiring(GeneralDocument doc) {
        if(doc.isReserved()) {
            LocalDateTime reservationEndDate = doc.getReservationDate().plusSeconds(MAX_RESERVATION_TIME/1000);
            LocalDateTime expiringDate = reservationEndDate.minusSeconds(RESERVATION_EXPIRING_DELAY/1000);

            return LocalDateTime.now().isAfter(expiringDate) && LocalDateTime.now().isBefore(reservationEndDate);
        }
        return false;
    }

    public static long getReservationRemindingTimeFor(GeneralDocument doc) {
        if(doc.isReserved()) {
            LocalDateTime reservationEndDate = doc.getReservationDate().plusSeconds(MAX_RESERVATION_TIME/1000);
            return Duration.between(LocalDateTime.now(), reservationEndDate).getSeconds();
        }
        return -1;
    }

    public static void addNotifier(Document doc, ObserverLibrary observer){
        if(observer != null) {
            if(notifyList.get(doc) == null){
                // there isn't a notifications list for this document
                List<ObserverLibrary> observers = new ArrayList<>();
                notifyList.put(doc, observers);
            }
            notifyList.get(doc).add(observer);
        }
    }

    public static void removeNotifier(Document doc){
        if(notifyList.get(doc) != null)
            notifyList.remove(doc);
    }

    public static void notifyAllObservers(Document doc) {
        if(notifyList.get(doc) != null)
            notifyList.get(doc).forEach(obs -> obs.update(doc));
        removeNotifier(doc);
    }


    public static void cancelReservation(Integer id) {
        reservations.get(id).cancel();
        reservations.remove(id);
    }

    public static void cancelSuspension(Integer id) {
        suspensions.get(id).cancel();
        suspensions.remove(id);
    }

    public static void cancelBorrow(Integer id) {
        BorrowUtil brU = borrows.keySet()
                .stream()
                .filter(borrowU -> borrowU.getDocNumber().equals(id))
                .findFirst()
                .get();
        borrows.get(brU).cancel();
        borrows.remove(brU);
    }

    public static void scheduleReservation(Document doc) {
        TimerTask autoReservation = new AutomatedCancellationReservation(doc);
        reservations.put(doc.number(), autoReservation);
        endReservations.schedule(autoReservation, MAX_RESERVATION_TIME);
    }

    public static void scheduleSuspension(Subscriber sub) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusMonths(MONTH_SUSPENDED);
//        30s for development test
//        LocalDateTime end = now.plusSeconds(30);
        long time = Duration.between(now, end).toMillis();

        TimerTask autoSuspension = new AutomatedCancellationSuspension(sub);
        suspensions.put(sub.getId(), autoSuspension);
        endSuspensions.schedule(autoSuspension, time);
    }

    public static void scheduleBorrow(Subscriber sub, Document doc) {
        LocalDateTime now = LocalDateTime.now();
        long time = Duration.between(now, now.plusWeeks(MAX_BORROW_WEEKS)).toMillis();
//         30s for development test
//        long time = Duration.between(now, now.plusSeconds(30)).toMillis();
        TimerTask borrowSchedule = new AutomatedBorrowSchedule(sub, doc);
        borrows.put(new BorrowUtil(doc.number(), sub.getId()), borrowSchedule);
        endBorrows.schedule(borrowSchedule, time);
    }

}
