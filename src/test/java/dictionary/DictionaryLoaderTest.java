package dictionary;

import exception.FileReadException;
import exception.InvalidFileFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DictionaryLoaderTest {

    @TempDir
    Path tempDir;

    @Test
    void loadsValidEntriesIgnoringBlankLines() throws IOException, FileReadException, InvalidFileFormatException {
        Path file = tempDir.resolve("dict.txt");
        Files.writeString(file, "look | смотреть\n\nlook forward | ожидать\n");

        Dictionary dictionary = DictionaryLoader.load(file);

        assertEquals("смотреть", dictionary.lookup("look").orElseThrow());
        assertEquals("ожидать", dictionary.lookup("look forward").orElseThrow());
        assertEquals(2, dictionary.maxPhraseWordCount());
    }

    @Test
    void isCaseInsensitiveAndWhitespaceTolerantOnKeys() throws IOException, FileReadException, InvalidFileFormatException {
        Path file = tempDir.resolve("dict.txt");
        Files.writeString(file, "  Look   Forward  | ожидать\n");

        Dictionary dictionary = DictionaryLoader.load(file);

        assertEquals("ожидать", dictionary.lookup("look forward").orElseThrow());
    }

    @Test
    void throwsInvalidFileFormatExceptionWhenSeparatorIsMissing() throws IOException {
        Path file = tempDir.resolve("dict.txt");
        Files.writeString(file, "look смотреть\n");

        assertThrows(InvalidFileFormatException.class, () -> DictionaryLoader.load(file));
    }

    @Test
    void throwsInvalidFileFormatExceptionWhenPhraseOrTranslationIsEmpty() throws IOException {
        Path file = tempDir.resolve("dict.txt");
        Files.writeString(file, " | смотреть\n");

        assertThrows(InvalidFileFormatException.class, () -> DictionaryLoader.load(file));
    }

    @Test
    void throwsFileReadExceptionWhenFileDoesNotExist() {
        Path missing = tempDir.resolve("missing.txt");

        assertThrows(FileReadException.class, () -> DictionaryLoader.load(missing));
    }
}