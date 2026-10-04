package translator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Tokenizer {
    // Токен - цельное слово
    // p{L} - любая буква, p{N} - любая цифра
    private static final Pattern TOKEN = Pattern.compile("[\\p{L}\\p{N}]+|\\s+|.");

    // Строка разбивается на токены
    public static List<String> split(String text) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = TOKEN.matcher(text);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        return tokens;
    }

    // Нормализация токена
    public static String normalize(String token) {
        return token.isBlank() ? " " : token.toLowerCase();
    }
}
