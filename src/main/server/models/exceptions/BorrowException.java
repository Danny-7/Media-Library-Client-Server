package main.server.models.exceptions;

public class BorrowException extends RuntimeException {

    public BorrowException(String message) {
        super(message);
    }
}
