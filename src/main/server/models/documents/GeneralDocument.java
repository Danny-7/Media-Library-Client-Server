package main.server.models.documents;

import main.server.ServerApp;
import main.server.models.Document;
import main.server.models.State;
import main.server.models.exception.BorrowException;
import main.server.models.exception.ReservationException;
import main.server.models.members.Subscriber;
import main.server.models.utils.AutomatedCancellationReservation;

import java.time.LocalDateTime;
import java.util.Timer;

public class GeneralDocument implements Document {
    private int number;
    private String title;

    private State status;
    private Subscriber holder;

    private LocalDateTime borrowDate;
    private static final int MAX_RESERVATION_TIME = 72000000;
    private Timer endReservation;

    public GeneralDocument(String title) {
        this.title = title;
        this.number = ServerApp.getNewDocNumber();
        this.status = State.AVAILABLE;
        this.holder = null;
        this.borrowDate = null;
        this.endReservation = new Timer();
    }

    @Override
    public int number() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public State getStatus() {
        return status;
    }

    public Subscriber getHolder() {
        return holder;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public boolean isReserved() {
        return this.status.equals(State.RESERVED);
    }

    public boolean isBorrowed() {
        return this.status.equals(State.BORROWED);
    }

    public boolean isAvailable() {
        return this.status.equals(State.AVAILABLE);
    }

    @Override
    public void reservationFor(Subscriber sb) throws ReservationException {
        synchronized (this) {
            if(!isAvailable())
                throw new ReservationException("The document is not available");
            status = State.RESERVED;
            this.holder = sb;
            // scheduling the end of the reservation (2 hours)
            endReservation.schedule(new AutomatedCancellationReservation(this), MAX_RESERVATION_TIME);
        }
    }

    @Override
    public void borrowBy(Subscriber sb) throws BorrowException {
        synchronized (this) {
            if(!isAvailable())
                throw new BorrowException("The document is not available");
            if(this.holder == null){
                holder = sb;
            }
            else {
                status = State.BORROWED;
                borrowDate = LocalDateTime.now();
            }
        }
    }

    @Override
    public void returnBack() {
        synchronized (this) {
            if(isAvailable())
                return;
            if(isReserved() || isBorrowed()) {
                holder = null;
                status = State.AVAILABLE;
                endReservation.cancel();
            }
        }
    }

    @Override
    public String toString() {
        return "GeneralDocument{" +
                "number=" + number +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", holder=" + holder +
                ", borrowDate=" + borrowDate +
                ", endReservation=" + endReservation +
                '}';
    }
}
