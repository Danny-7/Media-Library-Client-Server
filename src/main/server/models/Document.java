package main.server.models;

import main.server.models.exception.BorrowException;
import main.server.models.exception.ReservationException;
import main.server.models.members.Subscriber;

public interface Document {
    int number();
    void reservationFor(Subscriber sb) throws ReservationException;
    void borrowBy(Subscriber sb) throws BorrowException;
    void returnBack();
}
