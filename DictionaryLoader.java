import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class DictionaryLoader {
  public static Translator load(Path path) throws InvalidFileFormatException, FileReadException {
    Translator translator = new Translator();

    try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      String line;
      int lineNumber = 0;

      while ((line = reader.readLine()) != null) {
        lineNumber++;
        int separator = line.indexOf('|');

        if (separator < 0 || separator != line.lastIndexOf('|')) {
          throw invalidLine(path, lineNumber, "wrong format");
        }

        String source = line.substring(0, separator).strip();
        String translation = line.substring(separator + 1).strip();

        if (source.isEmpty() || translation.isEmpty()) {
          throw invalidLine(path, lineNumber, "not enough data");
        }

        if (!translator.add(source, translation)) {
          throw invalidLine(path, lineNumber, "duplicated key: " + source);
        }
      }
    } catch (IOException | SecurityException e) {
      throw new FileReadException("Cant read dictionary: " + path, e);
    }

    return translator;
  }

  private static InvalidFileFormatException invalidLine(Path path, int line, String reason) {
    return new InvalidFileFormatException(path + ": line " + line + ": " + reason);
  }
}
