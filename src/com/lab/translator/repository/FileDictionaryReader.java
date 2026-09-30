package com.lab.translator.repository;

import com.lab.translator.exception.FileReadException;
import com.lab.translator.exception.InvalidFileFormatException;
import com.lab.translator.model.Dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDictionaryReader implements DictionaryReader
{
    @Override
    public Dictionary read(String fileName) throws FileReadException, InvalidFileFormatException
    {
        Path path = Path.of(fileName);
        if (!Files.exists(path))
        {
            throw new FileReadException("File not found: " + fileName);
        }

        Dictionary dictionary = new Dictionary();
        try (BufferedReader reader = Files.newBufferedReader(path))
        {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null)
            {
                lineNumber++;
                if (!line.isBlank())
                {
                    addLine(dictionary, line, lineNumber);
                }
            }
        }
        catch (IOException e)
        {
            throw new FileReadException("Cannot read file: " + fileName);
        }
        return dictionary;
    }

    private void addLine(Dictionary dictionary, String line, int lineNumber)
            throws InvalidFileFormatException
    {
        String[] parts = line.split("\\|");
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank())
        {
            throw new InvalidFileFormatException("Invalid format at line " + lineNumber
                    + ": \"" + line + "\". Expected: word or phrase | translation");
        }
        dictionary.add(parts[0], parts[1]);
    }
}
