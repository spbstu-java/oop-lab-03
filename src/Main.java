import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println("Укажи путь к одному файлу словаря.");
            return;
        }

        String dictionary = args.length == 0 ? "dictionary.txt" : args[0];
        try {
            Translator translator = DictionaryLoader.load(Path.of(dictionary));
            translateInput(translator);
        } catch (FileReadException | InvalidFileFormatException e) {
            System.err.println(e.getMessage());
        } catch (InvalidPathException e) {
            System.err.println("Некорректный путь к словарю: " + dictionary);
        } catch (IOException e) {
            System.err.println("Не удалось прочитать текст из консоли: " + e.getMessage());
        }
    }

    private static void translateInput(Translator translator) throws IOException {
        Console console = System.console();
        Reader input = console == null
                ? new InputStreamReader(System.in, StandardCharsets.UTF_8)
                : console.reader();
        BufferedReader reader = new BufferedReader(input);

        System.out.println("Введи текст и нажми Enter. Пустая строка завершает программу.");
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            System.out.println(translator.translate(line));
        }
    }
}
