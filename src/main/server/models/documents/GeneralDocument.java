package main.server.models.documents;

import main.server.main.ServerApp;
import main.server.models.Document;
import main.server.models.ObserverLibrary;
import main.server.models.State;
import main.server.models.Subject;
import main.server.models.exceptions.BorrowException;
import main.server.models.exceptions.ReservationException;
import main.server.models.exceptions.SuspensionException;
import main.server.models.members.Subscriber;
import main.server.services.LibraryService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class GeneralDocument implements Document, Subject {
    private final int number;
    private final String title;

    private State status;
    private Subscriber holder;

    private LocalDateTime borrowDate;
    private  LocalDateTime reservationDate;

    private boolean degraded;

    public GeneralDocument(String title) {
        this.title = title;
        number = ServerApp.getNewDocNumber();
        this.status = State.AVAILABLE;
        this.holder = null;
        this.borrowDate = null;
        this.degraded = false;
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

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public boolean isDegraded() {
        return degraded;
    }

    public void setDegraded(boolean degraded) {
        this.degraded = degraded;
    }

    public void setStatus(State status){
        this.status = status;
        if(status == State.AVAILABLE)
            notifyObservers();
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
            if(!isAvailable()) {
                String message = "";
                if(isReserved()) {
                    LocalDateTime endOfReservationTime = getReservationDate().
                            plusSeconds(LibraryService.getMaxReservationTime() / 1000);
                    message += "This document is reserved until " +
                            endOfReservationTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                }
                else
                    message += "This document is borrowed";
                throw new ReservationException(message);
            }
            setStatus(State.RESERVED);
            reservationDate = LocalDateTime.now();
            this.holder = sb;
            // scheduling the end of the reservation (2 hours)
            LibraryService.scheduleReservation(this);
        }
    }

    @Override
    public void borrowBy(Subscriber sb) throws BorrowException {
        synchronized (this) {
            if(sb.isSuspended())
                throw new SuspensionException("The subscriber is suspended !");
            if(isBorrowed() || (isReserved() && !holder.equals(sb)))
                throw new BorrowException("This document is not available");
            if(this.holder == null)
                holder = sb;
            else
                LibraryService.cancelReservation(this.number);
            setStatus(State.BORROWED);
            borrowDate = LocalDateTime.now();
            // MAX BORROW 3 WEEKS
            LibraryService.scheduleBorrow(sb, this);
        }
    }

    @Override
    public void returnBack() {
        synchronized (this) {
            if(isAvailable())
                return;
            if(isReserved())
                LibraryService.cancelReservation(this.number);
            else if(!holder.isSuspended() && isBorrowed()) {
                if(this.degraded || LibraryService.isBorrowLate(this))
                    holder.suspend();
                if(this.degraded)
                    this.setDegraded(false);
                else
                    // IF isn't late, we cancel the timer task
                    LibraryService.cancelBorrow(number());
            }
            holder = null;
            setStatus(State.AVAILABLE);
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

    @Override
    public void register(ObserverLibrary observer) {
        LibraryService.addNotifier(this, observer);
    }

    @Override
    public void notifyObservers() {
        LibraryService.notifyAllObservers(this);
    }

}
