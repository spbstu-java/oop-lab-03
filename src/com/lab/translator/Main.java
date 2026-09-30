package com.lab.translator;

import com.lab.translator.controller.TranslatorController;
import com.lab.translator.exception.FileReadException;
import com.lab.translator.exception.InvalidFileFormatException;
import com.lab.translator.model.Dictionary;
import com.lab.translator.repository.DictionaryReader;
import com.lab.translator.repository.FileDictionaryReader;
import com.lab.translator.service.Translator;
import com.lab.translator.view.ConsoleView;

public class Main
{
    public static final String DEFAULT_DICTIONARY_FILE = "dictionary.txt";

    public static void main(String[] args)
    {
        String fileName = args.length > 0 ? args[0] : DEFAULT_DICTIONARY_FILE;
        ConsoleView view = new ConsoleView();
        DictionaryReader reader = new FileDictionaryReader();

        try
        {
            Dictionary dictionary = reader.read(fileName);
            new TranslatorController(new Translator(dictionary), view).run();
        }
        catch (FileReadException | InvalidFileFormatException e)
        {
            view.showError(e.getMessage());
        }
    }
}
