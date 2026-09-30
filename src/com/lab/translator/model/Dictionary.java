package com.lab.translator.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class Dictionary
{
    private final Map<String, String> words = new HashMap<>();

    private int maxWordCount = 0;

    public void add(String phrase, String translation)
    {
        String key = normalize(phrase);
        words.put(key, translation.trim());
        maxWordCount = Math.max(maxWordCount, key.split(" ").length);
    }

    public Optional<String> find(String phrase)
    {
        return Optional.ofNullable(words.get(normalize(phrase)));
    }

    public int getMaxWordCount()
    {
        return maxWordCount;
    }

    public int size()
    {
        return words.size();
    }

    private String normalize(String phrase)
    {
        return phrase.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    @Override
    public String toString()
    {
        return "Dictionary{size=" + words.size() + ", maxWordCount=" + maxWordCount + "}";
    }
}
