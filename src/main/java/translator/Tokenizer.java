package translator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Splits text into an alternating sequence of word and separator tokens, preserving every character. */
final class Tokenizer {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("[\\p{L}]+|[^\\p{L}]+");

    private Tokenizer() {
    }

    static List<Token> tokenize(String text) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(text);
        while (matcher.find()) {
            String value = matcher.group();
            tokens.add(new Token(value, Character.isLetter(value.charAt(0))));
        }
        return tokens;
    }
}