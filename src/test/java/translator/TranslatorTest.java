package translator;

import dictionary.Dictionary;
import dictionary.DictionaryLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TranslatorTest {

    @TempDir
    Path tempDir;

    private Dictionary loadDictionary(String content) throws Exception {
        Path file = tempDir.resolve("dict.txt");
        Files.writeString(file, content);
        return DictionaryLoader.load(file);
    }

    @Test
    void prefersTheLongestMatchingPhrase() throws Exception {
        Dictionary dictionary = loadDictionary("look | смотреть\nlook forward | ожидать\n");
        Translator translator = new Translator(dictionary);

        String result = translator.translate("dog look to the window, dog look forward");

        assertEquals("dog смотреть to the window, dog ожидать", result);
    }

    @Test
    void leavesUnknownWordsUnchanged() throws Exception {
        Dictionary dictionary = loadDictionary("cat | кот\n");
        Translator translator = new Translator(dictionary);

        assertEquals("a dog runs", translator.translate("a dog runs"));
    }

    @Test
    void ignoresCaseWhenMatching() throws Exception {
        Dictionary dictionary = loadDictionary("look | смотреть\n");
        Translator translator = new Translator(dictionary);

        assertEquals("смотреть", translator.translate("LOOK"));
        assertEquals("смотреть", translator.translate("Look"));
    }

    @Test
    void doesNotJoinWordsSeparatedByPunctuationIntoAPhrase() throws Exception {
        Dictionary dictionary = loadDictionary("look forward | ожидать\n");
        Translator translator = new Translator(dictionary);

        assertEquals("look, forward", translator.translate("look, forward"));
    }
}