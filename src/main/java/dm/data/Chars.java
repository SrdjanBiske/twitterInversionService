package dm.data;

import java.util.HashSet;

/**
 * Used for storing different types of chars.
 * They are used while formatting the posts into
 * the form that we can perform operations on.
 * We use HashSet since the amount of data is
 * not too huge and we want to increase the
 * performance as much as possible by avoiding
 * iteration costs through lists and arrays.
 */
public final class Chars {

    /**
     * Set for storing characters that should have a space
     * behind them.
     */
    private static HashSet<Character> charsWithSpaceAfter;

    /**
     * Set for storing characters that should have a space
     * before them.
     */
    private static HashSet<Character> charsWithSpaceBefore;

    /**
     * Set for storing characters that should have a spaces
     * around them.
     */
    private static HashSet<Character> charsWithSpacesAround;

    /**
     * Set for storing punctuation characters.
     */
    private static HashSet<Character> punctuation;

    /**
     * Set for storing quote characters.
     */
    private static HashSet<Character> quotes;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Chars() {
    }

    /**
     * Stores the characters with a space behind them
     * in a HashSet.
     */
    private static void setCharsWithSpaceAfter() {
        charsWithSpaceAfter = new HashSet<>();
        charsWithSpaceAfter.add(',');
        charsWithSpaceAfter.add(';');
        charsWithSpaceAfter.add(':');
        charsWithSpaceAfter.add(')');
        charsWithSpaceAfter.add(']');
        charsWithSpaceAfter.add('}');
        charsWithSpaceAfter.add('%');

        if (punctuation == null) {
            setPunctuation();
        }

        charsWithSpaceAfter.addAll(punctuation);
    }

    /**
     * Stores the characters with a space before them
     * in a HashSet.
     */
    private static void setCharsWithSpaceBefore() {
        charsWithSpaceBefore = new HashSet<>();
        charsWithSpaceBefore.add('{');
        charsWithSpaceBefore.add('(');
        charsWithSpaceBefore.add('[');
    }

    /**
     * Stores the characters with a spaces around them
     * in a HashSet.
     */
    private static void setCharsWithSpacesAround() {
        charsWithSpacesAround = new HashSet<>();
        charsWithSpacesAround.add('&');
        charsWithSpacesAround.add('+');
        charsWithSpacesAround.add('*');
        charsWithSpacesAround.add('-');
        charsWithSpacesAround.add('/');
    }

    /**
     * Stores the quote characters in a HashSet.
     */
    private static void setQuotes() {
        quotes = new HashSet<>();
        quotes.add('\'');
        quotes.add('\"');
    }

    /**
     * Stores the punctuation characters in a HashSet.
     */
    private static void setPunctuation() {
        punctuation = new HashSet<>();
        punctuation.add('.');
        punctuation.add('!');
        punctuation.add('?');
    }

    /**
     * Gives back chars-with-space-after HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the chars-with-space-after HashSet
     */
    public static HashSet<Character> getCharsWithSpaceAfter() {
        if (charsWithSpaceAfter == null) {
            setCharsWithSpaceAfter();
        }
        return charsWithSpaceAfter;
    }

    /**
     * Gives back chars-with-space-before HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the chars-with-space-before HashSet
     */
    public static HashSet<Character> getCharsWithSpaceBefore() {
        if (charsWithSpaceBefore == null) {
            setCharsWithSpaceBefore();
        }
        return charsWithSpaceBefore;
    }

    /**
     * Gives back chars-with-spaces-around HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the chars-with-spaces-around HashSet
     */
    public static HashSet<Character> getCharsWithSpacesAround() {
        if (charsWithSpacesAround == null) {
            setCharsWithSpacesAround();
        }
        return charsWithSpacesAround;
    }

    /**
     * Gives back punctuation-chars HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the punctuation-chars HashSet
     */
    public static HashSet<Character> getPunctuation() {
        if (punctuation == null) {
            setPunctuation();
        }
        return punctuation;
    }

    /**
     * Gives back quote-chars HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the quote-chars HashSet
     */
    public static HashSet<Character> getQuotes() {
        if (quotes == null) {
            setQuotes();
        }
        return quotes;
    }

}
