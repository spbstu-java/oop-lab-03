package translator;

/** A contiguous chunk of source text: either a word or a separator (whitespace/punctuation). */
record Token(String value, boolean isWord) {
}