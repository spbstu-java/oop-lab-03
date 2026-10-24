public class DictionaryEntry {

    private final String word;
    private final String translation;

    public DictionaryEntry(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }
}