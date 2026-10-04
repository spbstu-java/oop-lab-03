package translator;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Dictionary {
    private static class Node {
        private final Map<String, Node> next = new HashMap<>();
        private String translation;
    }

    // Найденное выражение
    public record Match(int length, String translation) {

    }

    private final Node root = new Node();

    public void add(String phrase, String translation) {
        Node node = root;

        for (String token : Tokenizer.split(phrase)) {
            node = node.next.computeIfAbsent(Tokenizer.normalize(token), key -> new Node());
        }

        node.translation = translation;
    }

    // Ищем самое длинное выражение, иначе возвращаем null
    public Match findLongest(List<String> tokens, int from) {
        Match longest = null;
        Node node = root;

        for (int i = from; i < tokens.size(); i++) {
            node = node.next.get(Tokenizer.normalize(tokens.get(i)));

            if (node == null) {
                break;
            }

            if (node.translation != null) {
                longest = new Match( i - from + 1, node.translation);
            }
        }

        return longest;
    }
}
