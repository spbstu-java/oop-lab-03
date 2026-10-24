import java.util.List;

public class Translator {

    private final Dictionary dictionary;

    public Translator(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String translate(String text) {

        StringBuilder result = new StringBuilder();

        List<DictionaryEntry> entries =
                dictionary.getEntries();

        int position = 0;

        while (position < text.length()) {

            DictionaryEntry bestEntry = null;

            // Записи уже отсортированы от длинных к коротким.
            for (DictionaryEntry entry : entries) {

                String word = entry.getWord();

                if (position + word.length() > text.length()) {
                    continue;
                }

                // Сравнение без учёта регистра.
                boolean matches = text.regionMatches(
                        true,
                        position,
                        word,
                        0,
                        word.length()
                );

                if (!matches) {
                    continue;
                }

                // Проверяем границы слова или выражения.
                if (!isWordBoundary(
                        text,
                        position,
                        position + word.length())) {
                    continue;
                }

                bestEntry = entry;
                break;
            }

            if (bestEntry != null) {

                result.append(bestEntry.getTranslation());

                position += bestEntry.getWord().length();

            } else {

                // Если совпадения нет, сохраняем исходный символ.
                result.append(text.charAt(position));

                position++;
            }
        }

        return result.toString();
    }

    private boolean isWordBoundary(
            String text,
            int start,
            int end) {

        boolean leftBoundary =
                start == 0
                        || !Character.isLetterOrDigit(
                        text.charAt(start - 1)
                );

        boolean rightBoundary =
                end == text.length()
                        || !Character.isLetterOrDigit(
                        text.charAt(end)
                );

        return leftBoundary && rightBoundary;
    }
}