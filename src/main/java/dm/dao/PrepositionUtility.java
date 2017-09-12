package dm.dao;

import static dm.data.Prepositions.getPrepositions;

/**
 * Methods for operations on data structures
 * containing prepositions.
 */
public class PrepositionUtility extends Utility {

    /**
     * Checks if the word is a preposition.
     * @param word - word to be checked
     * @return true if the word is a preposition,
     *         false otherwise
     */
    public static boolean isPreposition(final String word) {
        String cleanCopy = clean(word);
        return getPrepositions().contains(cleanCopy);
    }

}
