package translator;

public class InvalidFileFormatException extends Exception{
    public InvalidFileFormatException(int lineNumber, String line) {
        super("Строка " + lineNumber + " не в формате \"слово или выражение | перевод\": " + line);
    }
}
