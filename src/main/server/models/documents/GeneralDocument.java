package main.server.models.documents;

import main.server.main.ServerApp;
import main.server.models.Document;
import main.server.models.State;
import main.server.models.exception.BorrowException;
import main.server.models.exception.ReservationException;
import main.server.models.exception.SuspensionException;
import main.server.models.members.Subscriber;
import main.server.services.LibraryService;

import java.time.LocalDateTime;

public class GeneralDocument implements Document {
    private final int number;
    private final String title;

    private State status;
    private Subscriber holder;

    private LocalDateTime borrowDate;
    private static final int MAX_RESERVATION_TIME = 72000000;

    public GeneralDocument(String title) {
        this.title = title;
        number = ServerApp.getNewDocNumber();
        this.status = State.AVAILABLE;
        this.holder = null;
        this.borrowDate = null;
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
            if(sb.isSuspended())
                throw new SuspensionException("The subscriber is suspended !");
            if(!isAvailable())
                throw new ReservationException("The document is not available");
            status = State.RESERVED;
            this.holder = sb;
            // scheduling the end of the reservation (2 hours)
            LibraryService.scheduleReservation(this, MAX_RESERVATION_TIME);
        }
    }

    @Override
    public void borrowBy(Subscriber sb) throws BorrowException {
        synchronized (this) {
            if(sb.isSuspended())
                throw new SuspensionException("The subscriber is suspended !");
            if(!isAvailable())
                throw new BorrowException("The document is not available");
            if(this.holder == null)
                holder = sb;
            else {
                status = State.BORROWED;
                borrowDate = LocalDateTime.now();
                LibraryService.cancelReservation(this.number);
            }
        }
    }

    @Override
    public void returnBack() {
        synchronized (this) {
            if(isAvailable())
                return;
            if(isReserved())
                LibraryService.cancelReservation(this.number);
            else if(isBorrowed())
                if(LibraryService.isBorrowLate(this))
                    holder.suspend();

            holder = null;
            status = State.AVAILABLE;

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
                '}';
    }
}
