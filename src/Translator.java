import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Translator {
    private final Map<String, String> dictionary = new HashMap<>();

    public void load(Path path) throws FileReadException, InvalidFileFormatException {
        dictionary.clear();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int number = 0;
            while ((line = reader.readLine()) != null) {
                number++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (number == 1 && line.charAt(0) == '\uFEFF') {
                    line = line.substring(1).trim();
                }

                String[] parts = line.split("\\|", -1);
                if (parts.length != 2) {
                    throw new InvalidFileFormatException(
                            "строка " + number + ": нужен формат «слово | перевод»");
                }

                String left = parts[0].trim();
                String right = parts[1].trim();
                if (left.isEmpty() || right.isEmpty()) {
                    throw new InvalidFileFormatException(
                            "строка " + number + ": пустая левая или правая часть");
                }

                dictionary.put(left.toLowerCase(Locale.ROOT), right);
            }
        } catch (IOException e) {
            throw new FileReadException("не удалось прочитать файл: " + path, e);
        }
    }

    public String translate(String text) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        String lower = text.toLowerCase(Locale.ROOT);

        while (i < text.length()) {
            String key = longestKeyAt(lower, i);
            if (key != null) {
                result.append(dictionary.get(key));
                i += key.length();
            } else {
                result.append(text.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    private String longestKeyAt(String lowerText, int index) {
        String best = null;
        for (String key : dictionary.keySet()) {
            if (index + key.length() > lowerText.length()) {
                continue;
            }
            if (!lowerText.startsWith(key, index)) {
                continue;
            }
            if (!isBoundaryOk(lowerText, index, key.length())) {
                continue;
            }
            if (best == null || key.length() > best.length()) {
                best = key;
            }
        }
        return best;
    }

    private boolean isBoundaryOk(String text, int start, int length) {
        int end = start + length;
        if (start > 0 && letterOrDigit(text.charAt(start - 1)) && letterOrDigit(text.charAt(start))) {
            return false;
        }
        if (end < text.length() && letterOrDigit(text.charAt(end - 1)) && letterOrDigit(text.charAt(end))) {
            return false;
        }
        return true;
    }

    private boolean letterOrDigit(char c) {
        return Character.isLetterOrDigit(c);
    }

    public int size() {
        return dictionary.size();
    }
}
