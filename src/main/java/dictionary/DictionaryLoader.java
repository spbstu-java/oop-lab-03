package dictionary;

import exception.FileReadException;
import exception.InvalidFileFormatException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Parses a dictionary file where each non-blank line has the form "phrase | translation". */
public final class DictionaryLoader {

    private static final String SEPARATOR = "|";

    private DictionaryLoader() {
    }

    public static Dictionary load(Path path) throws FileReadException, InvalidFileFormatException {
        List<String> lines = readLines(path);

        Map<String, String> entries = new LinkedHashMap<>();
        for (int lineNumber = 1; lineNumber <= lines.size(); lineNumber++) {
            String rawLine = lines.get(lineNumber - 1);
            if (!rawLine.isBlank()) {
                parseLine(rawLine, lineNumber, entries);
            }
        }

        if (entries.isEmpty()) {
            throw new InvalidFileFormatException("Dictionary file has no valid entries: " + path);
        }

        return new Dictionary(entries);
    }

    private static List<String> readLines(Path path) throws FileReadException {
        try {
            return Files.readAllLines(path);
        } catch (NoSuchFileException e) {
            throw new FileReadException("Dictionary file not found: " + path, e);
        } catch (AccessDeniedException e) {
            throw new FileReadException("No access to dictionary file: " + path, e);
        } catch (IOException e) {
            throw new FileReadException("Failed to read dictionary file: " + path, e);
        }
    }

    private static void parseLine(String rawLine, int lineNumber, Map<String, String> entries)
            throws InvalidFileFormatException {
        int separatorIndex = rawLine.indexOf(SEPARATOR);
        if (separatorIndex < 0) {
            throw new InvalidFileFormatException(
                    "Line " + lineNumber + " is missing the '|' separator: \"" + rawLine + "\"");
        }

        String phrase = PhraseNormalizer.normalize(rawLine.substring(0, separatorIndex));
        String translation = rawLine.substring(separatorIndex + 1).trim();

        if (phrase.isEmpty() || translation.isEmpty()) {
            throw new InvalidFileFormatException(
                    "Line " + lineNumber + " has an empty phrase or translation: \"" + rawLine + "\"");
        }

        entries.put(phrase, translation);
    }
}