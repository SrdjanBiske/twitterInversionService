package dm.dao;

import static dm.data.Conjunctions.getOtherConjunctions;
import static dm.data.Conjunctions.getSentenceConjunctions;

/**
 * Methods for operations on data structures
 * containing conjunctions.
 */
public class ConjunctionUtility extends Utility {

    /**
     * Checks if the word is a sentence conjunction.
     * @param word - word to be checked
     * @return true if the word is a sentence conjunction,
     *         false otherwise
     */
    public static boolean isSentenceConjunction(final String word) {
        String cleanWord = clean(word);
        return getSentenceConjunctions().contains(cleanWord);
    }

    /**
     * Checks if the word is any conjunction.
     * @param word - word to be checked
     * @return true if the word is any conjunction,
     *         false otherwise
     */
    public static boolean isConjunction(final String word) {
        String cleanWord = clean(word);
        return getSentenceConjunctions().contains(cleanWord)
                || getOtherConjunctions().contains(cleanWord);
    }

}
