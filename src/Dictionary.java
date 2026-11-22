
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Dictionary {

    private Map<String, String> translations = Map.of();
    private List<String> expressions = List.of();

    public void loadFromFile(String fileName)
            throws InvalidFileFormatException, FileReadException {

        List<String> lines;

        try {
            lines = Files.readAllLines(
                    Path.of(fileName), StandardCharsets.UTF_8
            );
        } catch (IOException | SecurityException e) {
            throw new FileReadException("Ошибка чтения файла: " + fileName, e);
        }

        Map<String, String> loaded = new LinkedHashMap<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\|", -1);

            if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                throw new InvalidFileFormatException(
                        "Некорректный формат в строке " + (i + 1)
                );
            }

            String word = parts[0].trim().toLowerCase(Locale.ROOT);
            String translation = parts[1].trim();

            loaded.put(word, translation);
        }

        List<String> sorted = new ArrayList<>(loaded.keySet());
        sorted.sort(Comparator.comparingInt(String::length).reversed());

        translations = loaded;
        expressions = sorted;
    }

    public String translate(String text) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length();) {
            String word = findExpression(text, i);

            if (word == null) {
                result.append(text.charAt(i++));
                continue;
            }

            result.append(translations.get(word));
            i += word.length();
        }

        return result.toString();
    }

    private String findExpression(String text, int start) {
        if (start > 0 && isWordCharacter(text.charAt(start - 1))) {
            return null;
        }

        for (String word : expressions) {
            int end = start + word.length();

            if (end > text.length()) {
                continue;
            }

            if (!text.regionMatches(true, start, word, 0, word.length())) {
                continue;
            }

            if (end == text.length() || !isWordCharacter(text.charAt(end))) {
                return word;
            }
        }

        return null;
    }

    private boolean isWordCharacter(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }
}
