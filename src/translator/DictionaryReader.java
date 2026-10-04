package translator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.util.List;

public class DictionaryReader {
    public static Dictionary read(String fileName) throws FileReadException, InvalidFileFormatException {
        List<String> lines = readLines(fileName);
        Dictionary dictionary = new Dictionary();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            // Пустые строки не считаем между записями
            if (line.isBlank()) {
                continue;
            }

            String[] parts = line.split("\\|", -1);

            if(parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                throw new InvalidFileFormatException(i + 1, line);
            }
            
            dictionary.add(parts[0].trim(), parts[1].trim());
        }

        return dictionary;
    }

    private static List<String> readLines(String fileName) throws FileReadException {
        try {
            return Files.readAllLines(Path.of(fileName), StandardCharsets.UTF_8);
        } catch (NoSuchFileException e) {
            throw new FileReadException("файл не найден: " + fileName, e);
        } catch (AccessDeniedException e) {
            throw new FileReadException("нет доступа к файлу: " + fileName, e);
        } catch (IOException | InvalidPathException e) {
            throw new FileReadException("не удалось прочитать файл: " + fileName, e);
        }
    }
}
