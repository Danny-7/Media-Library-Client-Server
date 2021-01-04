package main.server.models.exceptions;

/** BorrowException: call on an exception throw when someone desired reserve
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class ReservationException extends RuntimeException {

    public ReservationException(String message) {
        super(message);
    }
}
