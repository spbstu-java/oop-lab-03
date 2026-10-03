import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Путь к словарю: ");
            String path = scanner.nextLine().trim();
            if (path.isEmpty()) {
                path = args.length > 0 ? args[0] : "dictionary.txt";
            }

            Translator translator = new Translator();
            translator.load(Path.of(path));
            assert translator.size() > 0 : "словарь пустой";

            System.out.println("Готово. Текст для перевода (exit — выход):");
            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String line = scanner.nextLine();
                if (line.equalsIgnoreCase("exit")) {
                    break;
                }
                System.out.println(translator.translate(line));
            }
        } catch (InvalidFileFormatException e) {
            System.err.println("формат файла: " + e.getMessage());
        } catch (FileReadException e) {
            System.err.println("чтение файла: " + e.getMessage());
        }
    }
}
