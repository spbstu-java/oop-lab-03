package exception;

/** Thrown when a dictionary file cannot be read: missing, inaccessible, or an I/O failure. */
public final class FileReadException extends Exception {

    public FileReadException(String message, Throwable cause) {
        super(message, cause);
    }
}