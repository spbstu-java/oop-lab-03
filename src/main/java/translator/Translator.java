package translator;

import dictionary.Dictionary;
import dictionary.PhraseNormalizer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Translates text word by word, preferring the longest matching phrase.
 * Case is ignored during lookup; words absent from the dictionary and all
 * separators (whitespace, punctuation) are copied through unchanged.
 */
public final class Translator {

    private final Dictionary dictionary;

    public Translator(Dictionary dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary, "Dictionary cannot be null");
    }

    public String translate(String text) {
        Objects.requireNonNull(text, "Text cannot be null");

        List<Token> tokens = Tokenizer.tokenize(text);
        StringBuilder result = new StringBuilder();

        int index = 0;
        while (index < tokens.size()) {
            Token token = tokens.get(index);

            if (!token.isWord()) {
                result.append(token.value());
                index++;
                continue;
            }

            Optional<PhraseMatch> match = findLongestMatch(tokens, index);
            if (match.isPresent()) {
                result.append(match.get().translation());
                index = match.get().nextIndex();
            } else {
                result.append(token.value());
                index++;
            }
        }

        return result.toString();
    }

    private Optional<PhraseMatch> findLongestMatch(List<Token> tokens, int start) {
        for (int wordCount = dictionary.maxPhraseWordCount(); wordCount >= 1; wordCount--) {
            Optional<String> phrase = buildPhrase(tokens, start, wordCount);
            if (phrase.isEmpty()) {
                continue;
            }

            Optional<String> translation = dictionary.lookup(PhraseNormalizer.normalize(phrase.get()));
            if (translation.isPresent()) {
                return Optional.of(new PhraseMatch(translation.get(), start + 2 * wordCount - 1));
            }
        }
        return Optional.empty();
    }

    /**
     * Joins {@code wordCount} consecutive word tokens starting at {@code start}
     * with single spaces, requiring every separator between them to be pure
     * whitespace. Returns empty if the tokens don't line up that way.
     */
    private Optional<String> buildPhrase(List<Token> tokens, int start, int wordCount) {
        StringBuilder phrase = new StringBuilder();

        for (int w = 0; w < wordCount; w++) {
            int wordIndex = start + 2 * w;
            if (wordIndex >= tokens.size() || !tokens.get(wordIndex).isWord()) {
                return Optional.empty();
            }

            if (w > 0) {
                Token separator = tokens.get(wordIndex - 1);
                if (!separator.value().isBlank()) {
                    return Optional.empty();
                }
                phrase.append(' ');
            }

            phrase.append(tokens.get(wordIndex).value());
        }

        return Optional.of(phrase.toString());
    }

    private record PhraseMatch(String translation, int nextIndex) {
    }
}