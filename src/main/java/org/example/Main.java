package org.example;

import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of(args.length > 0 ? args[0] : "dictionary.txt");
        try {
            Translator translator = new Translator(path);
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите текст: ");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank()) break;
                System.out.println(translator.translate(line));
                System.out.print("Введите текст: ");
            }
        } catch (FileReadException | InvalidFileFormatException e) {
            System.err.println(e.getMessage());
        }
    }
}
