package service.inversion;

import static dm.dao.PronounUtility.isBasicPronoun;
import static dm.dao.Utility.clean;
import static dm.dao.VerbUtility.isPastParticiple;

/**
 * Helper class for retrieving the full forms
 * of short modals.
 */
public final class ShortModalHelper {

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private ShortModalHelper() {
    }

    /**
     * This method goes through all words in the
     * post and changes short modal verb forms to
     * the long ones (i.e. "I'm" will be set to
     * "I am").
     * @param words - words in the sentence
     * @return the array of all words in the post
     *         with fixed short modal verbs
     */
    public static String[] fixShortModals(
            final String[] words) {

        for (int pos = 0; pos < words.length; pos++) {
            String result = getShortModal(words, pos);

            if (result != null) {
                words[pos] = words[pos].substring(0,
                        words[pos].lastIndexOf("'"))
                        + " " + result;
            }
        }

        return words;
    }

    /**
     * This method recognizes the short form of a modal verb
     * and returns its full form (i.e. "I'm" will return the
     * String "am").
     * @param words - words in the sentence
     * @param position - position of the word which is
     *                 currently being processed
     * @return the full form of the modal verb, null if the
     *         verb cannot be recognized
     */
    public static String getShortModal(
            final String[] words,
            final int position) {

        String word = clean(words[position]);
        String prevWord;

        if (word.contains("'")) {
            prevWord = word.substring(0,
                    word.lastIndexOf("'"));

            word = word.substring(
                    word.lastIndexOf("'"));

        } else {
            return null;
        }

        if (isBasicPronoun(prevWord)) {
            switch (word) {
                case "'m":
                    return "am";

                case "'s":
                    if (isPastParticipleAfter(words,
                            position)) {
                        return "has";

                    } else {
                        return "is";
                    }

                case "'ll":
                    return "will";

                case "'d":
                    if (isPastParticipleAfter(words,
                            position)) {
                        return "had";

                    } else {
                        return "would";
                    }

                case "'re":
                    return "are";

                case "'ve":
                    return "have";

                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    /**
     * The method checks if there is a past participle form
     * of the verb in the next two words of the sentence. It
     * only checks two words because the english grammar
     * doesn't allow the main verb to be too far away from
     * the modal verb.
     * @param words - words in the sentence
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if one of the following two words is past
     *         participle, false otherwise
     */
    private static boolean isPastParticipleAfter(
            final String[] words,
            final int position) {

        return (position + 1 < words.length
                && isPastParticiple(words[position + 1]))
                || (position + 2 < words.length
                && isPastParticiple(words[position + 2]));
    }

}
