import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String fileName;

        if (args.length > 0) {
            fileName = args[0];
        } else {
            fileName = "dictionary.txt";
        }
        try {
            Dictionary dictionary = new Dictionary(fileName);
            Translator translator = new Translator(dictionary);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Добро пожаловать в переводчик!");

            boolean running = true;

            while (running) {
                System.out.println("\nВведите текст для перевода:");
                System.out.println(
                        "(или введите 'выход', чтобы закончить работу)"
                );

                if (!scanner.hasNextLine()) {
                    break;
                }

                String text = scanner.nextLine().trim();

                if (text.equalsIgnoreCase("выход")) {
                    System.out.println("Работа переводчика завершена.");
                    break;
                }

                if (text.isEmpty()) {
                    System.out.println(
                            "Вы ничего не ввели. Попробуйте ещё раз."
                    );
                    continue;
                }

                String result = translator.translate(text);

                System.out.println("Перевод:");
                System.out.println(result);

                while (true) {
                    System.out.println(
                            "\nХотите перевести ещё текст? (да/нет)"
                    );

                    if (!scanner.hasNextLine()) {
                        running = false;
                        break;
                    }

                    String answer = scanner.nextLine().trim();

                    if (answer.equalsIgnoreCase("нет")
                            || answer.equalsIgnoreCase("н")) {
                        running = false;
                        System.out.println(
                                "Работа переводчика завершена."
                        );
                        break;
                    }

                    if (answer.equalsIgnoreCase("да")
                            || answer.equalsIgnoreCase("д")) {
                        break;
                    }

                    if (answer.equalsIgnoreCase("выход")) {
                        running = false;
                        System.out.println(
                                "Работа переводчика завершена."
                        );
                        break;
                    }

                    System.out.println(
                            "Введите «да» или «нет»."
                    );
                }
            }

        } catch (InvalidFileFormatException e) {
            System.out.println(
                    "Ошибка формата словаря: " + e.getMessage()
            );

        } catch (FileReadException e) {
            System.out.println(
                    "Ошибка чтения файла: " + e.getMessage()
            );
        }
    }
}