import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DictionaryLoader {
    public List<DictionaryEntry> load(Path path)
            throws FileReadException, InvalidFileFormatException {
        final List<String> lines;

        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException | SecurityException exception) {
            throw new FileReadException(
                    "Не удалось прочитать файл словаря: " + path,
                    exception
            );
        }

        List<DictionaryEntry> entries = new ArrayList<>();

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index);
            if (index == 0 && line.startsWith("\uFEFF")) {
                line = line.substring(1);
            }

            int separator = line.indexOf('|');
            boolean invalidSeparator = separator < 0 || separator != line.lastIndexOf('|');

            if (invalidSeparator) {
                throw invalidFormat(path, index + 1);
            }

            String source = line.substring(0, separator).trim();
            String translation = line.substring(separator + 1).trim();

            if (source.isEmpty() || translation.isEmpty()) {
                throw invalidFormat(path, index + 1);
            }

            entries.add(new DictionaryEntry(source, translation));
        }

        return entries;
    }

    private InvalidFileFormatException invalidFormat(Path path, int lineNumber) {
        return new InvalidFileFormatException(
                "Некорректный формат словаря " + path
                        + " в строке " + lineNumber
                        + ". Ожидается: слово или выражение | перевод"
        );
    }
}
