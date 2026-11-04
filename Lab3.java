import java.nio.file.Path;
import java.util.Scanner;

public class Lab3 {
  private static final Path DICTIONARY_PATH = Path.of("dictionary.txt");

  public static void main() {
    try (Scanner scanner = new Scanner(System.in, "UTF-8")) {
      Translator translator = DictionaryLoader.load(DICTIONARY_PATH);

      System.out.println("Enter text to translate:");

      if (scanner.hasNextLine()) {
        System.out.println(translator.translate(scanner.nextLine()));
      }
    } catch (InvalidFileFormatException | FileReadException e) {
      System.err.println(e.getMessage());
    }
  }
}
