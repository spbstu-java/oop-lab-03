import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");

        try {
            TranslationDirection direction = askDirection(scanner);
            Language language = askLanguage(scanner, direction);
            Path dictionaryPath = findDictionary(language.getFileName());

            DictionaryLoader loader = new DictionaryLoader();
            List<DictionaryEntry> entries = loader.load(dictionaryPath);
            TranslationDictionary dictionary = TranslationDictionary.create(entries, direction);
            Translator translator = new Translator(dictionary);

            System.out.println();
            System.out.print("Введите фразу для перевода: ");
            String text = scanner.nextLine();

            System.out.println("Перевод: " + translator.translate(text));
        } catch (FileReadException | InvalidFileFormatException exception) {
            System.err.println("Ошибка: " + exception.getMessage());
        }
    }

    private static TranslationDirection askDirection(Scanner scanner) {
        System.out.println("Вы хотите перевести фразу с иностранного языка на русский");
        System.out.println("или с русского языка на иностранный?");
        System.out.println("1 - с иностранного языка на русский");
        System.out.println("2 - с русского языка на иностранный");

        int choice = readNumber(scanner, 1, 2);
        return choice == 1
                ? TranslationDirection.TO_RUSSIAN
                : TranslationDirection.FROM_RUSSIAN;
    }

    private static Language askLanguage(
            Scanner scanner,
            TranslationDirection direction
    ) {
        System.out.println();
        if (direction == TranslationDirection.TO_RUSSIAN) {
            System.out.println("С какого языка перевести фразу?");
        } else {
            System.out.println("На какой язык перевести фразу?");
        }

        for (Language language : Language.values()) {
            System.out.println(language.getNumber() + " - " + language.getDisplayName());
        }

        return Language.fromNumber(readNumber(scanner, 1, Language.values().length));
    }

    private static int readNumber(Scanner scanner, int min, int max) {
        while (true) {
            System.out.print("Ваш выбор: ");
            String input = scanner.nextLine().trim();

            try {
                int number = Integer.parseInt(input);
                if (number >= min && number <= max) {
                    return number;
                }
            } catch (NumberFormatException ignored) {
                // Сообщение ниже одинаково обрабатывает число вне диапазона и нечисловой ввод.
            }

            System.out.println("Введите число от " + min + " до " + max + ".");
        }
    }

    private static Path findDictionary(String fileName) {
        Path current = Paths.get("").toAbsolutePath();

        for (int level = 0; level < 5 && current != null; level++) {
            Path candidate = current.resolve("dictionaries").resolve(fileName);
            if (Files.exists(candidate)) {
                return candidate;
            }
            current = current.getParent();
        }

        return Paths.get("dictionaries", fileName);
    }
}
