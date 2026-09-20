public enum Language {
    FRENCH(1, "французский", "francais_top_500.txt"),
    DANISH(2, "датский", "dansk_top_500.txt"),
    CHINESE(3, "китайский", "chinese_top_500.txt"),
    JAPANESE(4, "японский", "japanese_top_500.txt"),
    SPANISH(5, "испанский", "espanol_top_500.txt");

    private final int number;
    private final String displayName;
    private final String fileName;

    Language(int number, String displayName, String fileName) {
        this.number = number;
        this.displayName = displayName;
        this.fileName = fileName;
    }

    public int getNumber() {
        return number;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFileName() {
        return fileName;
    }

    public static Language fromNumber(int number) {
        for (Language language : values()) {
            if (language.number == number) {
                return language;
            }
        }
        return null;
    }
}
