package exception;

/** Thrown when a dictionary file violates the expected "phrase | translation" format. */
public final class InvalidFileFormatException extends Exception {

    public InvalidFileFormatException(String message) {
        super(message);
    }
}