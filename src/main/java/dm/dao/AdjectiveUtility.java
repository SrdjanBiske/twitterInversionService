package dm.dao;

import static dm.data.Adjectives.getAdjectives;

/**
 * Methods for operations on data structures
 * containing adjectives.
 */
public class AdjectiveUtility extends Utility {

    /**
     * Checks if the word is an adjective.
     * @param word - word to be checked
     * @return true if the word is an adjective,
     *         false otherwise
     */
    public static boolean isAdjective(final String word) {
        String cleanCopy = clean(word);
        return getAdjectives().contains(cleanCopy);
    }

}
