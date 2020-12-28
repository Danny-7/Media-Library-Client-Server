package main.server.models;

import main.server.models.exceptions.BorrowException;
import main.server.models.exceptions.ReservationException;
import main.server.models.members.Subscriber;

public interface Document {
    int number();
    void reservationFor(Subscriber sb) throws ReservationException;
    void borrowBy(Subscriber sb) throws BorrowException;
    void returnBack();
}
