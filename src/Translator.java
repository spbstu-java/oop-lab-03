import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Translator {
    private static final Pattern TOKENS = Pattern.compile(
            "(\\p{javaWhitespace}+)|\\w+(?:['’]\\w+)*|.",
            Pattern.UNICODE_CHARACTER_CLASS | Pattern.DOTALL
    );

    private final Node root = new Node();

    void addEntry(String expression, String translation) {
        Node node = root;
        for (Token token : tokenize(expression)) {
            node = node.children.computeIfAbsent(token.key(), key -> new Node());
        }
        node.translation = translation;
    }

    String translate(String text) {
        List<Token> tokens = tokenize(text);
        StringBuilder result = new StringBuilder(text.length());

        int start = 0;
        while (start < tokens.size()) {
            Node node = root;
            String translation = null;
            int next = start + 1;

            // Если нашли короткую фразу, продолжаем поиск по тому же префиксу.
            for (int index = start; index < tokens.size(); index++) {
                node = node.children.get(tokens.get(index).key());
                if (node == null) {
                    break;
                }
                if (node.translation != null) {
                    translation = node.translation;
                    next = index + 1;
                }
            }

            result.append(translation == null ? tokens.get(start).original() : translation);
            start = next;
        }
        return result.toString();
    }

    private static List<Token> tokenize(String text) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKENS.matcher(text);
        while (matcher.find()) {
            String original = matcher.group();
            String key = matcher.group(1) == null ? original : " ";
            tokens.add(new Token(original, key));
        }
        return tokens;
    }

    private record Token(String original, String key) {
    }

    private static final class Node {
        private final Map<String, Node> children = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        private String translation;
    }
}
