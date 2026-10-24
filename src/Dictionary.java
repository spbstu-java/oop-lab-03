import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Dictionary {

    private final List<DictionaryEntry> entries;

    public Dictionary(String fileName)
            throws FileReadException, InvalidFileFormatException {

        entries = new ArrayList<>();

        readFile(fileName);

        // Сначала идут самые длинные выражения.
        entries.sort(
                Comparator.comparingInt(
                        (DictionaryEntry entry) ->
                                entry.getWord().length()
                ).reversed()
        );
    }

    private void readFile(String fileName)
            throws FileReadException, InvalidFileFormatException {

        try (BufferedReader reader = Files.newBufferedReader(
                Path.of(fileName),
                StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Пустые строки пропускаются.
                if (line.trim().isEmpty()) {
                    continue;
                }

                int separator = line.indexOf('|');

                // Проверяем, что символ | присутствует ровно один раз.
                if (separator == -1
                        || separator != line.lastIndexOf('|')) {

                    throw new InvalidFileFormatException(
                            "Неверный формат строки "
                                    + lineNumber
                                    + ": "
                                    + line
                    );
                }

                String word = line.substring(0, separator).trim();

                String translation =
                        line.substring(separator + 1).trim();

                // Левая и правая части не должны быть пустыми.
                if (word.isEmpty() || translation.isEmpty()) {
                    throw new InvalidFileFormatException(
                            "Неверный формат строки "
                                    + lineNumber
                                    + ": "
                                    + line
                    );
                }

                entries.add(
                        new DictionaryEntry(word, translation)
                );
            }
            if (entries.isEmpty()) {
                throw new FileReadException("Словарь пуст", null);
            }
        } catch (IOException | SecurityException
                 | InvalidPathException e) {

            throw new FileReadException(
                    "Не удалось прочитать файл: " + fileName, e
            );
        }
    }

    public List<DictionaryEntry> getEntries() {
        return List.copyOf(entries);
    }
}