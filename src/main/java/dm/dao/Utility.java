package dm.dao;

import static dm.dao.CharUtility.isWhitespace;

/**
 * Class containing methods used by all other
 * utility classes in this package.
 */
public abstract class Utility {

    /**
     * Returns the word in lower case without special characters.
     * @param word - word to be cleaned
     * @return the word in lower case without special characters
     */
    public static String clean(final String word) {
        if (word == null) {
            return "";
        }
        return clearSpecChars(word).toLowerCase();
    }

    /**
     * Cleans all the special characters from the word.
     * @param word - original word with special characters
     * @return - clean word without special characters
     */
    public static String clearSpecChars(final String word) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < word.length(); ++i) {
            if ((word.charAt(i) >= 'a' && word.charAt(i) <= 'z')
                    || (word.charAt(i) >= 'A' && word.charAt(i) <= 'Z')
                    || (word.charAt(i) >= '0' && word.charAt(i) <= '9')
                    || (word.charAt(i) == '\'') || (word.charAt(i) == '-')
                    || isWhitespace(word.charAt(i))) {

                result.append(word.charAt(i));
            }
        }
        return result.toString();
    }

}
