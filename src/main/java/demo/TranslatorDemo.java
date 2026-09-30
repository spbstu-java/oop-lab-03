package demo;

import dictionary.Dictionary;
import dictionary.DictionaryLoader;
import exception.FileReadException;
import exception.InvalidFileFormatException;
import translator.Translator;

import java.nio.file.Path;
import java.util.Scanner;

/** Console entry point: loads a dictionary file, then translates user input line by line. */
public final class TranslatorDemo {

    private static final String DEFAULT_DICTIONARY_PATH = "dictionary.txt";

    public static void main(String[] args) {
        Path dictionaryPath = Path.of(args.length > 0 ? args[0] : DEFAULT_DICTIONARY_PATH);

        Dictionary dictionary;
        try {
            dictionary = DictionaryLoader.load(dictionaryPath);
        } catch (FileReadException | InvalidFileFormatException e) {
            System.out.println("Failed to load dictionary: " + e.getMessage());
            return;
        }

        Translator translator = new Translator(dictionary);
        System.out.println("Dictionary loaded from " + dictionaryPath);
        System.out.println("Enter text to translate ('exit' to quit):");

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equalsIgnoreCase("exit")) {
                    break;
                }
                System.out.println(translator.translate(line));
            }
        }
    }
}