package com.lab.translator.view;

import java.util.Optional;
import java.util.Scanner;

public class ConsoleView
{
    private final Scanner scanner = new Scanner(System.in);

    public void showMessage(String message)
    {
        System.out.println(message);
    }

    public void showError(String message)
    {
        System.err.println("Error: " + message);
    }

    public Optional<String> readText()
    {
        System.out.print("> ");
        if (!scanner.hasNextLine())
        {
            return Optional.empty();
        }
        return Optional.of(scanner.nextLine());
    }
}
