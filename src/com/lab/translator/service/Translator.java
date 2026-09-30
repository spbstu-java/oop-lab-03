package com.lab.translator.service;

import com.lab.translator.model.Dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Translator
{
    private static final Pattern WORD_PATTERN = Pattern.compile("[\\w'-]+");

    private final Dictionary dictionary;

    public Translator(Dictionary dictionary)
    {
        this.dictionary = dictionary;
    }

    public Dictionary getDictionary()
    {
        return dictionary;
    }

    public String translate(String text)
    {
        List<Word> words = findWords(text);
        StringBuilder result = new StringBuilder();
        int position = 0;
        int index = 0;

        while (index < words.size())
        {
            Word word = words.get(index);
            result.append(text, position, word.start());

            int count = findLongestMatch(text, words, index);
            if (count > 0)
            {
                String phrase = joinWords(words, index, count);
                result.append(dictionary.find(phrase).orElse(phrase));
            }
            else
            {
                count = 1;
                result.append(word.text());
            }

            position = words.get(index + count - 1).end();
            index += count;
        }
        result.append(text.substring(position));
        return result.toString();
    }

    private List<Word> findWords(String text)
    {
        List<Word> words = new ArrayList<>();
        Matcher matcher = WORD_PATTERN.matcher(text);
        while (matcher.find())
        {
            words.add(new Word(matcher.group(), matcher.start(), matcher.end()));
        }
        return words;
    }

    private int findLongestMatch(String text, List<Word> words, int index)
    {
        for (int count = countJoinedWords(text, words, index); count > 0; count--)
        {
            if (dictionary.find(joinWords(words, index, count)).isPresent())
            {
                return count;
            }
        }
        return 0;
    }

    private int countJoinedWords(String text, List<Word> words, int index)
    {
        int limit = Math.min(dictionary.getMaxWordCount(), words.size() - index);
        int count = 1;
        while (count < limit)
        {
            Word previous = words.get(index + count - 1);
            Word next = words.get(index + count);
            if (!text.substring(previous.end(), next.start()).isBlank())
            {
                break;
            }
            count++;
        }
        return count;
    }

    private String joinWords(List<Word> words, int index, int count)
    {
        return words.subList(index, index + count).stream()
                .map(Word::text)
                .collect(Collectors.joining(" "));
    }

    private record Word(String text, int start, int end)
    {
    }
}
