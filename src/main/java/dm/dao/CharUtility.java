package dm.dao;

import static dm.data.Chars.getCharsWithSpaceAfter;
import static dm.data.Chars.getCharsWithSpaceBefore;
import static dm.data.Chars.getCharsWithSpacesAround;
import static dm.data.Chars.getPunctuation;
import static dm.data.Chars.getQuotes;

/**
 * Methods for operations on data structures
 * containing chars.
 */
public class CharUtility extends Utility {

    /**
     * Checks if a character is a number.
     * @param character - char to be checked
     * @return true if the char is a number,
     *         false otherwise
     */
    public static boolean isNumber(final char character) {
        return character >= '0' && character <= '9';
    }

    /**
     * Checks if a char is any of the most used
     * special characters.
     * @param toCheck - char to be checked
     * @return true if the char is any of the most
     *         used special characters, false otherwise
     */
    public static boolean isSpecialChar(final char toCheck) {
        return isCharWithSpacesAround(toCheck)
                || isCharWithSpaceBefore(toCheck)
                || isCharWithSpaceAfter(toCheck)
                || isQuote(toCheck);
    }

    /**
     * Checks if a char is a punctuation.
     * @param toCheck - char to be checked
     * @return true if the char is is a punctuation,
     *         false otherwise
     */
    public static boolean isPunctuation(final char toCheck) {
        return getPunctuation().contains(toCheck);
    }

    /**
     * Checks if a char should have spaces around.
     * @param toCheck - char to be checked
     * @return true if the char should have spaces
     *         around, false otherwise
     */
    public static boolean isCharWithSpacesAround(final char toCheck) {
        return getCharsWithSpacesAround().contains(toCheck);
    }

    /**
     * Checks if a char should have space before.
     * @param toCheck - char to be checked
     * @return true if the char should have spaces
     *         before, false otherwise
     */
    public static boolean isCharWithSpaceBefore(final char toCheck) {
        return getCharsWithSpaceBefore().contains(toCheck);
    }

    /**
     * Checks if a char should have space after.
     * @param toCheck - char to be checked
     * @return true if the char should have spaces
     *         after, false otherwise
     */
    public static boolean isCharWithSpaceAfter(final char toCheck) {
        return getCharsWithSpaceAfter().contains(toCheck);
    }

    /**
     * Checks if a char is a quote.
     * @param toCheck - char to be checked
     * @return true if the char is a quote,
     *         false otherwise
     */
    public static boolean isQuote(final char toCheck) {
        return getQuotes().contains(toCheck);
    }

    /**
     * Checks if a char is a whitespace.
     * @param toCheck - char to be checked
     * @return true if the char is a whitespace,
     *         false otherwise
     */
    public static boolean isWhitespace(final char toCheck) {
        return Character.isWhitespace(toCheck);
    }

    /**
     * Checks if a char is a letter or a digit.
     * @param toCheck - char to be checked
     * @return true if the char is a letter or a
     *         digit, false, otherwise
     */
    public static boolean isLetterOrDigit(final char toCheck) {
        return Character.isLetterOrDigit(toCheck);
    }

}
