package dm.dao;

import static dm.dao.VerbUtility.isTwoPartVerb;
import static dm.data.Phrase.getBeforePreviousPreviousSkipPhrases;
import static dm.data.Phrase.getCurrentNextSkipPhrases;
import static dm.data.Phrase.getPreviousCurrentSkipPhrases;
import static dm.data.Phrase.getSubSentencePhrases;

/**
 * Methods for operations on data structures
 * containing phrases.
 */
public class PhraseUtility extends Utility {

    /**
     * Checks if the two words are on the positions that
     * are before previous and previous of the current
     * position.
     * @param words - words of the sentence
     * @param position - position that is currently being
     *                 processed
     * @param word1 - before previous word
     * @param word2 - previous word
     * @return true if the two given words are on the
     *         specified positions, false otherwise
     */
    private static boolean beforePreviousPrevious(final String[] words,
                                                  final int position,
                                                  final String word1,
                                                  final String word2) {

        if (position < 2 || position > words.length - 1) {
            return false;
        }

        String beforePrevious = clean(words[position - 2]);
        String previous = clean(words[position - 1]);

        return beforePrevious.equals(word1) && previous.equals(word2);
    }

    /**
     * Checks if the two words are on the current and next
     * position.
     * @param words - words of the sentence
     * @param position - position that is currently being
     *                 processed
     * @param word1 - current word
     * @param word2 - next word
     * @return true if the two given words are on the
     *         specified positions, false otherwise
     */
    private static boolean currentNext(final String[] words,
                                       final int position,
                                       final String word1,
                                       final String word2) {

        if (position < 0 || position > words.length - 2) {
            return false;
        }

        String current = clean(words[position]);
        String next = clean(words[position + 1]);

        return current.equals(word1) && next.equals(word2);
    }

    /**
     * Checks if the two words are on the previous and current
     * position.
     * @param words - words of the sentence
     * @param position - position that is currently being
     *                 processed
     * @param word1 - previous word
     * @param word2 - current word
     * @return true if the two given words are on the
     *         specified positions, false otherwise
     */
    private static boolean previousCurrent(final String[] words,
                                           final int position,
                                           final String word1,
                                           final String word2) {

        if (position < 1 || position > words.length - 1) {
            return false;
        }

        String previous = clean(words[position - 1]);
        String current = clean(words[position]);

        return previous.equals(word1) && current.equals(word2);
    }

    /**
     * Checks if we have a phrase that should be skipped.
     * @param words - words of the sentence
     * @param position - position that is currently being
     *                 processed
     * @return true if we have a skip phrase, false otherwise
     */
    public static boolean isPhraseToSkip(final String[] words,
                                         final int position) {

        return isCurrentNextSkipPhrase(words, position)
                || isPreviousCurrentSkipPhrase(words, position)
                || isBeforePreviousPreviousSkipPhrase(words, position);
    }

    /**
     * Checks if we have a current-next phrase that should be skipped.
     * @param words - words of the sentence
     * @param position - position that is currently being
     *                 processed
     * @return true if we have a current-next skip phrase, false otherwise
     */
    private static boolean isCurrentNextSkipPhrase(final String[] words,
                                                   final int position) {
        for (String currentPhrase : getCurrentNextSkipPhrases()) {
            String phrase = currentPhrase;

            String word1 = phrase.split("\\s+")[0];
            String word2 = phrase.split("\\s+")[1];

            if (currentNext(words, position, word1, word2)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if we have a previous-current phrase that should be skipped.
     * @param words - words of the sentence
     * @param position - position that is currently being
     *                 processed
     * @return true if we have a previous-current skip phrase, false otherwise
     */
    private static boolean isPreviousCurrentSkipPhrase(final String[] words,
                                                       final int position) {
        for (String currentPhrase : getPreviousCurrentSkipPhrases()) {
            String phrase = currentPhrase;

            String word1 = phrase.split("\\s+")[0];
            String word2 = phrase.split("\\s+")[1];

            if (previousCurrent(words, position, word1, word2)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if we have a before-previous-previous phrase
     * that should be skipped.
     * @param words - words of the sentence
     * @param position - position that is currently being
     *                 processed
     * @return true if we have a before-previous-previous
     *         skip phrase, false otherwise
     */
    private static boolean isBeforePreviousPreviousSkipPhrase(
            final String[] words,
            final int position) {

        for (String currentPhrase : getBeforePreviousPreviousSkipPhrases()) {
            String phrase = currentPhrase;

            String word1 = phrase.split("\\s+")[0];
            String word2 = phrase.split("\\s+")[1];

            if (beforePreviousPrevious(words, position, word1, word2)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if we have a phrase that starts a new sub sentence.
     * @param words - words of the sentence
     * @param position - position that is currently being
     *                 processed
     * @return true if we have a sub-sentence phrase, false otherwise
     */
    public static boolean isSubSentencePhrase(final String[] words,
                                               final int position) {
        for (String currentPhrase : getSubSentencePhrases()) {
            String phrase = currentPhrase;

            String word1 = phrase.split("\\s+")[0];
            String word2 = phrase.split("\\s+")[1];

            if (currentNext(words, position, word1, word2)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if we have a phrase that should be negated instead of
     * skipped.
     * @param words - words of the sentence
     * @param position - position that is currently being
     *                 processed
     * @return true if we have a phrase that should be negated,
     *         false otherwise
     */
    public static boolean isNotSkipPhrase(final String[] words,
                                          final int position) {

        return isTwoPartVerb(words, position) != null;
    }

}
