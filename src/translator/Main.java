package translator;

import java.io.Console;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Main {
    private static final String DEFAULT_DICTIONARY = "dictionary.txt";

    public static void main(String[] args) {
        String fileName = args.length > 0 ? args[0] : DEFAULT_DICTIONARY;
        Dictionary dictionary;

        try {
            dictionary = DictionaryReader.read(fileName);
        } catch (FileReadException e) {
            System.out.println("Ошибка чтения словаря: " + e.getMessage());
            return;
        } catch (InvalidFileFormatException e) {
            System.out.println("Ошибка формата словаря: " + e.getMessage());
            return;
        }

        Translator translator = new Translator(dictionary);

        Console console = System.console();
        Charset charset = console != null ? console.charset() : Charset.defaultCharset();
        Scanner scanner = new Scanner(System.in, charset);


        System.out.println("Введите текст для перевода: ");
        while (scanner.hasNextLine()) {
            String text = scanner.nextLine();

            if (text.isBlank()) {
                break;
            }

            System.out.println(translator.translate(text));
        }

        scanner.close();
    }
}
