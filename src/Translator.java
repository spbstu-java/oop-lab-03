import java.util.Locale;

public class Translator {
    private final TranslationDictionary dictionary;

    public Translator(TranslationDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String translate(String text) {
        String normalizedText = text.toLowerCase(Locale.ROOT);
        StringBuilder result = new StringBuilder();
        int position = 0;

        while (position < text.length()) {
            String matchedKey = findLongestMatch(normalizedText, position);

            if (matchedKey == null) {
                result.append(text.charAt(position));
                position++;
            } else {
                result.append(dictionary.getTranslation(matchedKey));
                position += matchedKey.length();
            }
        }

        return result.toString();
    }

    private String findLongestMatch(String text, int position) {
        for (String key : dictionary.getKeysByLength()) {
            if (position + key.length() > text.length()) {
                continue;
            }

            if (!text.startsWith(key, position)) {
                continue;
            }

            if (hasValidBoundaries(text, position, key)) {
                return key;
            }
        }

        return null;
    }

    private boolean hasValidBoundaries(String text, int position, String key) {
        int end = position + key.length();
        char first = key.charAt(0);
        char last = key.charAt(key.length() - 1);

        if (requiresBoundary(first)
                && position > 0
                && isSameAlphabetWordCharacter(text.charAt(position - 1), first)) {
            return false;
        }

        return !requiresBoundary(last)
                || end >= text.length()
                || !isSameAlphabetWordCharacter(text.charAt(end), last);
    }

    private boolean requiresBoundary(char character) {
        Character.UnicodeScript script = Character.UnicodeScript.of(character);
        return Character.isLetterOrDigit(character)
                && (script == Character.UnicodeScript.LATIN
                || script == Character.UnicodeScript.CYRILLIC);
    }

    private boolean isSameAlphabetWordCharacter(char neighbor, char keyCharacter) {
        if (!Character.isLetterOrDigit(neighbor)) {
            return false;
        }

        Character.UnicodeScript keyScript = Character.UnicodeScript.of(keyCharacter);
        Character.UnicodeScript neighborScript = Character.UnicodeScript.of(neighbor);
        return keyScript == neighborScript;
    }
}
