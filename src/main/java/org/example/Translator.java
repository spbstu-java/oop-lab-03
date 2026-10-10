package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Translator {
    private static final Pattern WORD = Pattern.compile("[\\p{L}\\p{N}]+(?:['-][\\p{L}\\p{N}]+)*");

    private final Map<String, String> dictionary = new HashMap<>();
    private int maxWords = 1;

    public Translator(Path path) throws FileReadException, InvalidFileFormatException {
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new FileReadException("Не удалось прочитать файл: " + path, e);
        }
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) continue;
            String[] parts = line.split("\\|", -1);
            if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                throw new InvalidFileFormatException("Неверный формат строки " + (i + 1) + ": " + line);
            }
            String key = parts[0].strip().toLowerCase().replaceAll("\\s+", " ");
            dictionary.put(key, parts[1].strip());
            maxWords = Math.max(maxWords, key.split(" ").length);
        }
    }

    public String translate(String text) {
        List<MatchResult> words = WORD.matcher(text).results().toList();
        StringBuilder result = new StringBuilder();
        int pos = 0;
        for (int i = 0; i < words.size(); ) {
            int n = Math.min(maxWords, words.size() - i);
            while (n > 1 && !dictionary.containsKey(phrase(text, words, i, n))) n--;
            MatchResult first = words.get(i);
            result.append(text, pos, first.start())
                    .append(dictionary.getOrDefault(phrase(text, words, i, n), first.group()));
            pos = words.get(i + n - 1).end();
            i += n;
        }
        return result.append(text.substring(pos)).toString();
    }

    private String phrase(String text, List<MatchResult> words, int from, int count) {
        StringJoiner joiner = new StringJoiner(" ");
        for (int j = from; j < from + count; j++) {
            if (j > from && !text.substring(words.get(j - 1).end(), words.get(j).start()).isBlank()) return null;
            joiner.add(words.get(j).group().toLowerCase());
        }
        return joiner.toString();
    }
}
