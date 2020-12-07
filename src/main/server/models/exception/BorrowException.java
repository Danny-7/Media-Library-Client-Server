package main.server.models.exception;

public class BorrowException extends RuntimeException {

    public BorrowException(String message) {
        super(message);
    }
}
