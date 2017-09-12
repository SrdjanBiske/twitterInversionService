package service.inversion;

import static dm.dao.Utility.clean;
import static dm.dao.VerbUtility.isVerb;
import static service.inversion.InversionService.finalizeWord;

/**
 * Methods for clearing the sufficient words
 * after the inversion has been performed.
 */
public final class ClearanceHelper {

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private ClearanceHelper() {
    }

    /**
     * Uses the deleteWord(String[], int, String)
     * method to remove any occurrence of the
     * word time stamps around the current position.
     * This is done because when the sentence is
     * being negated, the time stamps lose their
     * meaning.
     * @param words - words of the sentence
     * @param position - position of the word
     *                 which is currently being
     *                 processed
     */
    public static void clearAfter(
            final String[] words,
            final int position) {

        clearAlready(words, position);
        clearAlways(words, position);
        clearJust(words, position);
        clearNow(words, position);
    }

    /**
     * Deletes the word if it's one place before or
     * after the specified position.
     * @param words - words of the sentence
     * @param position - position of the word which
     *                 is currently being processed
     * @param word - word that should be deleted if
     *             it exists
     */
    private static void deleteWord(
            final String[] words,
            final int position,
            final String word) {

        String copy = clean(word);
        if (position > 0
                && clean(words[position - 1])
                .equals(copy)) {

            words[position - 1] = "";

        } else if (position < words.length - 1
                && clean(words[position + 1])
                .equals(copy)) {

            words[position + 1] = "";
        }
    }

    /**
     * Clears the word "just".
     * @param words - words of the sentence
     * @param position - position of the word
     *                 which is currently being
     *                 processed
     */
    private static void clearJust(
            final String[] words,
            final int position) {

        deleteWord(words, position, "just");
    }

    /**
     * Clears the word "already".
     * @param words - words of the sentence
     * @param position - position of the word which is
     *                 currently being processed
     */
    private static void clearAlready(
            final String[] words,
            final int position) {

        deleteWord(words, position, "already");
    }

    /**
     * Clears the word "always".
     * @param words - words of the sentence
     * @param position - position of the word which is
     *                 currently being processed
     */
    private static void clearAlways(
            final String[] words,
            final int position) {

        deleteWord(words, position, "always");
    }

    /**
     * Clears the word "now".
     * @param words - words of the sentence
     * @param position - position of the word which is
     *                 currently being processed
     */
    private static void clearNow(
            final String[] words,
            final int position) {

        deleteWord(words, position, "now");
    }

    /**
     * There are posts that say "It's not this, but that."
     * or "Do this, not that". If we negate just the verb,
     * the sentence won't keep the sense so we also have to
     * change "not" to "but" and the other way around. This
     * method checks if our post contains this scenario and
     * replaces these words.
     * @param words - words of the sentence
     * @param tense - complete form of the verb which is
     *              being checked
     */
    public static void checkNonButCase(
            final String[] words,
            final Tense tense) {

        if (tense.getLastLvlPos() > -1) {
            for (int position = tense.getLastLvlPos() + 1;
                 position < words.length; position++) {

                String word = clean(words[position]);

                if (isVerb(word)) {
                    break;
                }

                if (tense.isToNegative()
                        && !isThereVerbAfter(words, position)
                        && word.equals("not")) {

                    words[position] = finalizeWord(words[position],
                            "but");
                    break;

                } else if (tense.isToPositive()
                        && !isThereVerbAfter(words, position)
                        && word.equals("but")) {

                    words[position] = finalizeWord(words[position],
                            "not");
                    break;
                }
            }
        }
    }

    /**
     * Checks if there is a verb after the current position.
     * @param words - words of the sentence
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if there is a verb after the current
     *         position, false otherwise
     */
    public static boolean isThereVerbAfter(
            final String[] words,
            final int position) {

        for (int pos = position + 1;
             pos < words.length; pos++) {

            if (isVerb(words[pos])) {
                return true;
            }
        }

        return false;
    }


    /**
     * Many sentences contain the words "some" and "any"
     * in order to strengthen their meaning (i.e. "There
     * wasn't any proof."). If we turn the verb in the
     * previous example to the positive form like "There
     * was any proof.", the word "any" doesn't make sense
     * any more. That's why we should turn it to the word
     * "some" and get "There was some proof." This method
     * inverts these two words if they are contained
     * within the sentence.
     * @param words - words of the sentence
     * @param tense - complete form of the verb which is
     *              being checked
     */
    public static void checkSomeAnyCase(
            final String[] words,
            final Tense tense) {

        for (int position = tense.getPosition1() + 1;
             position < words.length; position++) {

            String word = clean(words[position]);

            if (tense.isToNegative()
                    && word.equals("some")) {

                words[position] = finalizeWord(words[position],
                        "any");
                break;

            } else if (tense.isToPositive()
                    && word.equals("any")) {

                words[position] = finalizeWord(words[position],
                        "some");
                break;
            }
        }
    }

}
