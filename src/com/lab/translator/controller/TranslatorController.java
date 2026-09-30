package com.lab.translator.controller;

import com.lab.translator.service.Translator;
import com.lab.translator.view.ConsoleView;

import java.util.Optional;

public class TranslatorController
{
    public static final String EXIT_COMMAND = "exit";

    private final Translator translator;
    private final ConsoleView view;

    public TranslatorController(Translator translator, ConsoleView view)
    {
        this.translator = translator;
        this.view = view;
    }

    public void run()
    {
        view.showMessage("Dictionary loaded: " + translator.getDictionary().size() + " entries.");
        view.showMessage("Enter text to translate (type '" + EXIT_COMMAND + "' to quit).");

        Optional<String> input = view.readText();
        while (input.isPresent() && !input.get().trim().equalsIgnoreCase(EXIT_COMMAND))
        {
            view.showMessage(translator.translate(input.get()));
            input = view.readText();
        }
        view.showMessage("Bye!");
    }
}
