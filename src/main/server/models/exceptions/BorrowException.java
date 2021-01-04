package main.server.models.exceptions;

/** BorrowException: call on an exception throw when someone desired borrow
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class BorrowException extends RuntimeException {

    public BorrowException(String message) {
        super(message);
    }
}
