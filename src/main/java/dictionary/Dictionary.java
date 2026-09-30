package dictionary;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Immutable phrase-to-translation map, keyed by normalized (lowercase,
 * single-spaced) phrases. Instances are only created by {@link DictionaryLoader}
 * so every entry is guaranteed to have passed format validation.
 */
public final class Dictionary {

    private final Map<String, String> translations;
    private final int maxPhraseWordCount;

    Dictionary(Map<String, String> translations) {
        this.translations = Collections.unmodifiableMap(translations);
        this.maxPhraseWordCount = translations.keySet().stream()
                .mapToInt(phrase -> phrase.split(" ").length)
                .max()
                .orElse(1);
    }

    /** @param normalizedPhrase must already be normalized via {@link PhraseNormalizer#normalize} */
    public Optional<String> lookup(String normalizedPhrase) {
        return Optional.ofNullable(translations.get(normalizedPhrase));
    }

    public int maxPhraseWordCount() {
        return maxPhraseWordCount;
    }
}