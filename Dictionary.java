import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dictionary {
    // Отбор отдельных слов: кириллица и латиница + слова через дефис/апостроф
    private static final Pattern WORD = Pattern.compile("[\\p{L}\\p{N}]+(?:['’-][\\p{L}\\p{N}]+)*");

    private final Map<String, String> entries = new HashMap<>();
    private int longestEntry = 0;

    public Dictionary() throws InvalidFileFormatException, FileReadException {
        this(Path.of("dictionary.txt"));
    }

    public Dictionary(Path file) throws InvalidFileFormatException, FileReadException {
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                int separator = line.indexOf('|');
                if (separator < 0 || separator != line.lastIndexOf('|')) {
                    throw new InvalidFileFormatException("Строка " + lineNumber + ": ожидается 'слово или выражение | перевод'");
                }

                String source = line.substring(0, separator).trim();
                String translation = line.substring(separator + 1).trim();
                if (source.isEmpty() || translation.isEmpty()) {
                    throw new InvalidFileFormatException("Строка " + lineNumber + ": слово и перевод не должны быть пустыми");
                }

                String key = source.replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
                entries.put(key, translation);
                longestEntry = Math.max(longestEntry, key.split(" ").length);
            }
        } catch (IOException | SecurityException e) {
            throw new FileReadException("Не удалось прочитать словарь: " + file, e);
        }
    }

    public String translate(String text) {
        List<Match> words = new ArrayList<>();
        Matcher matcher = WORD.matcher(text);
        while (matcher.find()) {
            words.add(new Match(matcher.start(), matcher.end()));
        }

        StringBuilder result = new StringBuilder();
        int copiedUntil = 0;
        for (int i = 0; i < words.size();) {
            Match first = words.get(i);
            result.append(text, copiedUntil, first.start);

            StringBuilder phrase = new StringBuilder();
            String replacement = null;
            int matchedWords = 0;
            int limit = Math.min(longestEntry, words.size() - i);
            for (int count = 1; count <= limit; count++) {
                Match current = words.get(i + count - 1);
                if (count > 1) {
                    Match previous = words.get(i + count - 2);
                    String between = text.substring(previous.end, current.start);
                    if (!between.isBlank()) {
                        break;
                    }
                    phrase.append(' ');
                }
                phrase.append(text, current.start, current.end);
                String found = entries.get(phrase.toString().toLowerCase(Locale.ROOT));
                if (found != null) {
                    replacement = found;
                    matchedWords = count;
                }
            }

            if (replacement == null) {
                result.append(text, first.start, first.end);
                copiedUntil = first.end;
                i++;
            } else {
                result.append(replacement);
                copiedUntil = words.get(i + matchedWords - 1).end;
                i += matchedWords;
            }
        }
        return result.append(text, copiedUntil, text.length()).toString();
    }

    private static class Match {
        final int start;
        final int end;

        Match(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
