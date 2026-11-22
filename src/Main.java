import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String fileName = args.length > 0 ? args[0] : "dictionary.txt";
        Dictionary dictionary = new Dictionary();

        try {
            dictionary.loadFromFile(fileName);
            System.out.println("Словарь успешно загружен.");

            Scanner scanner = new Scanner(System.in, "UTF-8");
            System.out.println("Введите текст для перевода:");
            String input = scanner.nextLine();

            System.out.println("Перевод:");
            System.out.println(dictionary.translate(input));

        } catch (InvalidFileFormatException e) {
            System.out.println("Ошибка формата словаря: " + e.getMessage());
        } catch (FileReadException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}
