package dictionary;

import java.util.Locale;

/** Normalizes phrases for case-insensitive, whitespace-tolerant dictionary lookups. */
public final class PhraseNormalizer {

    private PhraseNormalizer() {
    }

    public static String normalize(String phrase) {
        return phrase.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }
}