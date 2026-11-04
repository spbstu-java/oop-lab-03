import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Trie<V> {
  private static class Node<V> {
    final Map<String, Node<V>> children = new HashMap<>();
    V value;
  }

  public record Match<V>(V value, int endIndex) {
  }

  private final Node<V> root = new Node<>();

  public boolean add(List<String> keys, V value) {
    Objects.requireNonNull(value, "value");

    if (keys.isEmpty()) {
      throw new IllegalArgumentException("Key sequence must be nonempty");
    }

    Node<V> node = root;

    for (String key : keys) {
      node = node.children.computeIfAbsent(key, unused -> new Node<>());
    }

    if (node.value != null) {
      return false;
    }

    node.value = value;

    return true;
  }

  public Match<V> longestMatch(List<String> keys, int startIndex) {
    Objects.checkFromToIndex(startIndex, startIndex, keys.size());
    Node<V> node = root;
    Match<V> best = null;

    for (int i = startIndex; i < keys.size(); i++) {
      node = node.children.get(keys.get(i));

      if (node == null) {
        break;
      }

      if (node.value != null) {
        best = new Match<>(node.value, i + 1);
      }
    }

    return best;
  }
}
