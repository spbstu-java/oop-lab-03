package translator;

import java.util.List;

public class Translator {
    private final Dictionary dictionary;

    public Translator(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String translate(String text) {
        List<String> tokens = Tokenizer.split(text);
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < tokens.size()) {
            Dictionary.Match match = dictionary.findLongest(tokens, i);

            if (match == null) {
                result.append(tokens.get(i));
                i++;
            } else {
                result.append(match.translation());
                i += match.length();
            }
        }

        return result.toString();
    }
}
