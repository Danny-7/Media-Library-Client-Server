package main.server.models;

import main.server.models.exceptions.BorrowException;
import main.server.models.exceptions.ReservationException;
import main.server.models.members.Subscriber;
/** Document : Interface of a document
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public interface Document {
    int number();
    void reservationFor(Subscriber sb) throws ReservationException;
    void borrowBy(Subscriber sb) throws BorrowException;
    void returnBack();
}
