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

/** GeneralDocument : It represent a general document in the library.
 * We can make a reservation, borrow and return a document.
 * He's defined by his number, name. He has a status and another attributes.
 * He can be degraded by a subscriber
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
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
        // we notify all people who set an alert on this document
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
                    // get the endTime of the reservation
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
                // here the subscriber didn't reserve the document
                throw new BorrowException("This document is not available");
            if(this.holder == null)
                holder = sb;
            else
                LibraryService.cancelReservation(this.number);
            setStatus(State.BORROWED);
            borrowDate = LocalDateTime.now();
            // max borrow 3 weeks
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
                    /* we suspend the subscriber because he exceeded the time of borrow
                      or the document is degraded
                     */
                    holder.suspend();
                if(this.degraded)
                    this.setDegraded(false);
                else
                    // if isn't late, we cancel the timer task(borrow)
                    LibraryService.cancelBorrow(number());
            }
            holder = null;
            setStatus(State.AVAILABLE);
        }
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public void register(ObserverLibrary observer) {
        // registration of a subscriber to set an alert
        LibraryService.addNotifier(this, observer);
    }

    @Override
    public void notifyObservers() {
        // notifying all subscribers who set an alert
        LibraryService.notifyAllObservers(this);
    }

}
