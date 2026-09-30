package com.lab.translator.repository;

import com.lab.translator.exception.FileReadException;
import com.lab.translator.exception.InvalidFileFormatException;
import com.lab.translator.model.Dictionary;

public interface DictionaryReader
{
    Dictionary read(String source) throws FileReadException, InvalidFileFormatException;
}
