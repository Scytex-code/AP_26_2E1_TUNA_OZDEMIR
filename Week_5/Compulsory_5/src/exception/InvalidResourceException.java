package exception;

public class InvalidResourceException extends Exception {
    public InvalidResourceException(String message) {
        super(message);
    }

    public InvalidResourceException(Exception ex) {
        super(ex);
    }
}