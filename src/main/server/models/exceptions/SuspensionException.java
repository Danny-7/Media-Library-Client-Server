package main.server.models.exceptions;

/** BorrowException: call on an exception throw when someone is suspended
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
public class SuspensionException extends RuntimeException {

    public SuspensionException(String message) {
        super(message);
    }
}
