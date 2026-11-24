import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

final class DictionaryLoader {
    private DictionaryLoader() {
    }

    static Translator load(Path file) throws FileReadException, InvalidFileFormatException {
        Translator translator = new Translator();
        boolean hasEntries = false;
        int lineNumber = 0;

        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 && line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                if (line.isBlank()) {
                    continue;
                }

                String[] fields = line.split("\\|", -1);
                if (fields.length != 2 || fields[0].isBlank() || fields[1].isBlank()) {
                    throw new InvalidFileFormatException(
                            "Строка " + lineNumber + " в словаре " + file
                                    + " должна иметь формат «слово или выражение | перевод»."
                    );
                }

                translator.addEntry(fields[0].strip(), fields[1].strip());
                hasEntries = true;
            }
        } catch (IOException | SecurityException e) {
            throw new FileReadException("Не удалось прочитать словарь " + file + ": " + e.getMessage(), e);
        }

        if (!hasEntries) {
            throw new InvalidFileFormatException("Словарь " + file + " пуст.");
        }

        return translator;
    }
}
