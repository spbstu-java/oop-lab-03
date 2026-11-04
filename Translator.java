import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {
  // Punctuation is a separate token, so a phrase cannot skip a comma or a period.
  private static final Pattern TOKEN = Pattern.compile(
      "\\w+|\\S", Pattern.UNICODE_CHARACTER_CLASS);

  private static class Token {
    final String key;
    final int start;
    final int end;

    Token(String text, int start, int end) {
      this.key = text.toLowerCase(Locale.ROOT);
      this.start = start;
      this.end = end;
    }
  }

  private final Trie<String> trie = new Trie<>();

  // returns false if duplicate
  public boolean add(String source, String translation) {
    List<Token> tokens = tokenize(source);

    if (tokens.isEmpty() || translation.isBlank()) {
      throw new IllegalArgumentException("Empty translation");
    }

    return trie.add(tokens.stream().map(token -> token.key).toList(), translation);
  }

  public String translate(String text) {
    List<Token> tokens = tokenize(text);
    List<String> keys = tokens.stream().map(token -> token.key).toList();
    StringBuilder result = new StringBuilder();
    int copiedUntil = 0;
    int i = 0;

    while (i < tokens.size()) {
      Trie.Match<String> match = trie.longestMatch(keys, i);

      if (match == null) {
        i++;
      } else {
        result.append(text, copiedUntil, tokens.get(i).start);
        result.append(match.value());
        copiedUntil = tokens.get(match.endIndex() - 1).end;
        i = match.endIndex();
      }
    }

    return result.append(text, copiedUntil, text.length()).toString();
  }

  private static List<Token> tokenize(String text) {
    List<Token> tokens = new ArrayList<>();
    Matcher matcher = TOKEN.matcher(text);

    while (matcher.find()) {
      tokens.add(new Token(matcher.group(), matcher.start(), matcher.end()));
    }

    return tokens;
  }
}
