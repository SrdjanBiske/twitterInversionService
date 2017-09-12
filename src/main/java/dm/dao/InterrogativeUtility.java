package dm.dao;

import static dm.data.Interrogatives.getNegateInterrogatives;
import static dm.data.Interrogatives.getPushInterrogatives;
import static dm.data.Interrogatives.getSkipInterrogatives;

/**
 * Methods for operations on data structures
 * containing interrogatives.
 */
public class InterrogativeUtility extends Utility {

    /**
     * Checks if the word is a push-interrogative.
     * @param word - word to be checked
     * @return true if the word is a push-interrogative,
     *         false otherwise
     */
    public static boolean isPushQuestionWord(final String word) {
        String cleanCopy = clean(word);
        return getPushInterrogatives().contains(cleanCopy);
    }

    /**
     * Checks if the word is a skip-interrogative.
     * @param word - word to be checked
     * @return true if the word is a skip-interrogative,
     *         false otherwise
     */
    public static boolean isSkipQuestionWord(final String word) {
        String cleanCopy = clean(word);
        return getSkipInterrogatives().contains(cleanCopy);
    }

    /**
     * Checks if the word is a negate-interrogative.
     * @param word - word to be checked
     * @return true if the word is a negate-interrogative,
     *         false otherwise
     */
    public static boolean isNegateQuestionWord(final String word) {
        String cleanCopy = clean(word);
        return getNegateInterrogatives().contains(cleanCopy);
    }

    /**
     * Checks if the word is any interrogative.
     * @param word - word to be checked
     * @return true if the word is any interrogative,
     *         false otherwise
     */
    public static boolean isQuestionWord(final String word) {
        return isPushQuestionWord(word) || isNegateQuestionWord(word)
                || isSkipQuestionWord(word);
    }

}
