import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    private static final String PATH_TO_DICTIONARY = "dictionary.txt";

    public static void main(String[] args)
    {
        Path dictionaryPath = Path.of(PATH_TO_DICTIONARY);

        Dictionary dictionary;
        try {
            dictionary = Dictionary.load(dictionaryPath);
        } catch (FileReadException e) {
            System.err.println("Ошибка чтения словаря: " + e.getMessage());
            return;
        } catch (InvalidFileFormatException e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Словарь загружен - записей: " + dictionary.size());
        System.out.println("Введите текст для перевода (пустая строка - выход):");

        Translator translator = new Translator(dictionary);
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            System.out.print("> ");
            if (!scanner.hasNextLine())
            {
                break;
            }

            String line = scanner.nextLine();
            if (line.isBlank())
            {
                break;
            }

            System.out.println(translator.translate(line));
        }
    }
}
