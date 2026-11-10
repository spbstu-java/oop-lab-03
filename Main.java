import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Path file = Path.of(args.length == 0 ? "dictionary.txt" : args[0]);
        try {
            Dictionary dictionary = new Dictionary(file);
            Scanner input = new Scanner(System.in);
            System.out.println("Введите текст для перевода:");
            while (input.hasNextLine()) {
                String line = input.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                System.out.println(dictionary.translate(line));
            }
        } catch (InvalidFileFormatException | FileReadException e) {
            System.err.println(e.getMessage());
        }
    }
}
