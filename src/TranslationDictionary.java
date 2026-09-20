import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TranslationDictionary {
    private final Map<String, String> translations;
    private final List<String> keysByLength;

    private TranslationDictionary(Map<String, String> translations) {
        this.translations = translations;
        this.keysByLength = new ArrayList<>(translations.keySet());
        this.keysByLength.sort(
                Comparator.comparingInt(String::length).reversed()
        );
    }

    public static TranslationDictionary create(
            List<DictionaryEntry> entries,
            TranslationDirection direction
    ) {
        Map<String, String> translations = new LinkedHashMap<>();

        for (DictionaryEntry entry : entries) {
            if (direction == TranslationDirection.TO_RUSSIAN) {
                putIfAbsent(translations, entry.getSource(), entry.getTranslation());
            } else {
                addReverseTranslations(translations, entry);
            }
        }

        return new TranslationDictionary(translations);
    }

    private static void addReverseTranslations(
            Map<String, String> translations,
            DictionaryEntry entry
    ) {
        String foreignWord = entry.getSource();
        String russianTranslation = entry.getTranslation();

        putIfAbsent(translations, russianTranslation, foreignWord);

        String[] variants = russianTranslation.split(";");
        for (String variant : variants) {
            String trimmed = variant.trim();
            putIfAbsent(translations, trimmed, foreignWord);

            String withoutComment = trimmed
                    .replaceAll("\\s*\\([^)]*\\)", "")
                    .trim();
            putIfAbsent(translations, withoutComment, foreignWord);
        }
    }

    private static void putIfAbsent(
            Map<String, String> translations,
            String source,
            String translation
    ) {
        String normalized = normalize(source);
        if (!normalized.isEmpty()) {
            translations.putIfAbsent(normalized, translation);
        }
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }

    public String getTranslation(String normalizedKey) {
        return translations.get(normalizedKey);
    }

    public List<String> getKeysByLength() {
        return keysByLength;
    }
}
