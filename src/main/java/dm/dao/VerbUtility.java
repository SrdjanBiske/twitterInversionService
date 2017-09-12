package dm.dao;

import static dm.data.Modals.getPositiveBe;
import static dm.data.Modals.getNegativeModalVerbs;
import static dm.data.Modals.getNegativeBe;
import static dm.data.Modals.getNegativeDo;
import static dm.data.Modals.getNegativeHave;
import static dm.data.Modals.getPositiveDo;
import static dm.data.Modals.getPositiveHave;
import static dm.data.Modals.getPositiveModalVerbs;
import static dm.data.Verbs.getInfinitivePerfectMapping;
import static dm.data.Verbs.getInfinitiveThirdPersonMapping;
import static dm.data.Verbs.getNewSentenceVerbs;
import static dm.data.Verbs.getStartStopMapping;
import static dm.data.Verbs.getTwoPartVerbs;
import static dm.data.Verbs.getVerbs;
import static dm.data.Verbs.getVerbTenses;

/**
 * Methods for operations on data structures
 * containing verbs.
 */
public class VerbUtility extends Utility {

    /**
     * Mapping for perfect.
     */
    private static final int PERFECT_POS = 1;

    /**
     * Mapping for past participle.
     */
    private static final int PARTICIPLE_POS = 2;

    /**
     * Mapping for third person.
     */
    private static final int THIRD_PERS_POS = 3;

    /**
     * Mapping for gerund.
     */
    private static final int GERUND_POS = 4;

    /**
     * Checks if the word is in infinitive form of the verb.
     * @param word - the word to be checked
     * @return true if the word is in infinitive form of the
     *         verb, false otherwise
     */
    public static boolean isInfinitive(final String word) {
        String cleanCopy = clean(word);

        if (cleanCopy.equals("be")) {
            return true;
        }

        return getTense(cleanCopy) != null
                && getTense(cleanCopy) == 0;
    }

    /**
     * Checks if the word is in perfect form of the verb.
     * @param word - the word to be checked
     * @return true if the word is in perfect form of the
     *         verb, false otherwise
     */
    public static boolean isPerfect(final String word) {
        String cleanCopy = clean(word);

        if (cleanCopy.equals("was")
                || cleanCopy.equals("were")) {
            return true;
        }

        return (getTense(cleanCopy) != null
                && getTense(cleanCopy) == PERFECT_POS)
                || (getTense(cleanCopy + PERFECT_POS) != null
                && getTense(cleanCopy + PERFECT_POS) == PERFECT_POS);
    }

    /**
     * Checks if the word is in past participle form of the
     * verb.
     * @param word - the word to be checked
     * @return true if the word is in past participle form of
     *         the verb, false otherwise
     */
    public static boolean isPastParticiple(final String word) {
        String cleanCopy = clean(word);

        if (cleanCopy.equals("been")) {
            return true;
        }

        return (getTense(cleanCopy) != null
                && getTense(cleanCopy) == PARTICIPLE_POS)
                || (getTense(cleanCopy + PARTICIPLE_POS) != null
                && getTense(cleanCopy + PARTICIPLE_POS) == PARTICIPLE_POS);
    }

    /**
     * Checks if the word is in third person form of the
     * verb.
     * @param word - the word to be checked
     * @return true if the word is in third person form of
     *         the verb, false otherwise
     */
    public static boolean isThirdPerson(final String word) {
        String cleanCopy = clean(word);

        if (cleanCopy.equals("is")) {
            return true;
        }

        return (getTense(cleanCopy) != null
                && getTense(cleanCopy) == THIRD_PERS_POS)
                || (getTense(cleanCopy + THIRD_PERS_POS) != null
                && getTense(cleanCopy + THIRD_PERS_POS) == THIRD_PERS_POS);
    }

    /**
     * Checks if the word is in gerund form of the verb.
     * @param word - the word to be checked
     * @return true if the word is in gerund form of
     *         the verb, false otherwise
     */
    public static boolean isGerund(final String word) {
        String cleanCopy = clean(word);

        if (cleanCopy.equals("being")) {
            return true;
        }

        return (getTense(cleanCopy) != null
                && getTense(cleanCopy) == GERUND_POS)
                || (getTense(cleanCopy + GERUND_POS) != null
                && getTense(cleanCopy + GERUND_POS) == GERUND_POS);
    }

    /**
     * Checks if the word is a form of the verb "to do".
     * @param word - the word to be checked
     * @return true if the word is a form of the verb
     *         "to do", false otherwise
     */
    public static boolean isFormOfDo(final String word) {
        String cleanCopy = clean(word);
        return getPositiveDo().containsKey(cleanCopy)
                || getNegativeDo().containsKey(cleanCopy);
    }

    /**
     * Checks if the word is a form of the verb "to be".
     * @param word - the word to be checked
     * @return true if the word is a form of the verb
     *         "to be", false otherwise
     */
    public static boolean isFormOfBe(final String word) {
        String cleanCopy = clean(word);
        return getPositiveBe().containsKey(cleanCopy)
                || getNegativeBe().containsKey(cleanCopy);
    }

    /**
     * Checks if the word is a positive form of the verb
     * "to be".
     * @param word - the word to be checked
     * @return true if the word is a positive form of the
     *         verb "to be", false otherwise
     */
    public static boolean isPositiveBe(final String word) {
        String cleanCopy = clean(word);
        return getPositiveBe().containsKey(cleanCopy);
    }

    /**
     * Checks if the word is a positive form of the verb
     * "to have".
     * @param word - the word to be checked
     * @return true if the word is a positive form of the
     *         verb "to have", false otherwise
     */
    public static boolean isPositiveHave(final String word) {
        String cleanCopy = clean(word);
        return getPositiveHave().containsKey(cleanCopy);
    }

    /**
     * Checks if the word is a negative form of the verb
     * "to be".
     * @param word - the word to be checked
     * @return true if the word is a negative form of the
     *         verb "to be", false otherwise
     */
    public static boolean isNegativeBe(final String word) {
        String cleanCopy = clean(word);
        return getNegativeBe().containsKey(cleanCopy);
    }

    /**
     * Checks if the word is a negative form of the verb
     * "to have".
     * @param word - the word to be checked
     * @return true if the word is a negative form of the
     *         verb "to have", false otherwise
     */
    public static boolean isNegativeHave(final String word) {
        String cleanCopy = clean(word);
        return getNegativeHave().containsKey(cleanCopy);
    }

    /**
     * Checks if the word is a positive form of the verb
     * "to do".
     * @param word - the word to be checked
     * @return true if the word is a positive form of the
     *         verb "to do", false otherwise
     */
    public static boolean isPositiveDo(final String word) {
        String cleanCopy = clean(word);
        return getPositiveDo().containsKey(cleanCopy);
    }

    /**
     * Checks if the word is a form of the verb "to have".
     * @param word - the word to be checked
     * @return true if the word is a form of the verb
     *         "to have", false otherwise
     */
    public static boolean isFormOfHave(final String word) {
        String cleanCopy = clean(word);
        return getPositiveHave().containsKey(cleanCopy)
                || getNegativeHave().containsKey(cleanCopy);
    }

    /**
     * Checks if the word is a modal verb.
     * @param word - the word to be checked
     * @return true if the word is a modal verb,
     *         false otherwise
     */
    public static boolean isModal(final String word) {
        String cleanCopy = clean(word);
        return getPositiveModalVerbs().containsKey(cleanCopy)
                || getNegativeModalVerbs().containsKey(cleanCopy);
    }

    /**
     * Checks if the word is a verb in any form.
     * @param word - the word to be checked
     * @return - true if the word is a verb in any form,
     *           false otherwise
     */
    public static boolean isVerb(final String word) {
        String cleanCopy = clean(word);
        return isModal(cleanCopy) || getVerbs().containsKey(cleanCopy)
                || isFormOfBe(cleanCopy) || isFormOfDo(cleanCopy)
                || isFormOfHave(cleanCopy);
    }

    /**
     * Checks if the word is a negative form of the verb
     * "to do".
     * @param word - the word to be checked
     * @return true if the word is a negative form of the
     *         verb "to do", false otherwise
     */
    public static boolean isNegativeFormOfDo(final String word) {
        String cleanCopy = clean(word);
        return getNegativeDo().containsKey(cleanCopy);
    }

    /**
     * Finds the third person form of the infinitive.
     * @param infinitive - verb whose third person form we
     *                     need
     * @return the third person form of the infinitive if
     *         it exists, null otherwise
     */
    public static String getThirdPerson(final String infinitive) {
        String cleanCopy = clean(infinitive);
        return getInfinitiveThirdPersonMapping().get(cleanCopy);
    }

    /**
     * Finds the perfect form of the infinitive.
     * @param infinitive - verb whose perfect form we
     *                     need
     * @return the perfect form of the infinitive if
     *         it exists, null otherwise
     */
    public static String getPerfect(final String infinitive) {
        String cleanCopy = clean(infinitive);
        return getInfinitivePerfectMapping().get(cleanCopy);
    }

    /**
     * Finds the infinitive form of the given verb.
     * @param verb - verb whose infinitive form we need
     * @return the infinitive form of the given verb,
     *         null if it doesn't exist
     */
    public static String getInfinitive(final String verb) {
        String cleanCopy = clean(verb);

        if (isModal(cleanCopy)) {
            return cleanCopy;
        }

        if (isFormOfBe(cleanCopy)) {
            return "be";
        }

        return getVerbs().get(cleanCopy);
    }

    /**
     * Checks if the two verbs are in the same tense.
     * @param verb1 - first verb for comparison
     * @param verb2 - second verb for comparison
     * @return true if the verbs are in the same tense,
     *         false otherwise
     */
    public static boolean sameTense(final String verb1,
                                    final String verb2) {
        String cleanCopy1 = clean(verb1);
        String cleanCopy2 = clean(verb2);
        return (isInfinitive(cleanCopy1)
                && isInfinitive(cleanCopy2))
                || (isPerfect(cleanCopy1)
                && isPerfect(cleanCopy2))
                || (isPastParticiple(cleanCopy1)
                && isPastParticiple(cleanCopy2))
                || (isThirdPerson(cleanCopy1)
                && isThirdPerson(cleanCopy2))
                || (isGerund(cleanCopy1)
                && isGerund(cleanCopy2));
    }

    /**
     * Checks if the word is a positive modal verb.
     * @param word - the word to be checked
     * @return true if the word is a positive modal verb,
     *         false otherwise
     */
    public static boolean isPositiveModal(final String word) {
        String cleanCopy = clean(word);
        return getPositiveModalVerbs().containsKey(cleanCopy);
    }

    /**
     * Checks if the word is a negative modal verb.
     * @param word - the word to be checked
     * @return true if the word is a negative modal verb,
     *         false otherwise
     */
    public static boolean isNegativeModal(final String word) {
        String cleanCopy = clean(word);
        return getNegativeModalVerbs().containsKey(cleanCopy);
    }

    /**
     * Checks if the word is any form of the verbs "start"
     * or "stop".
     * @param word - the word to be checked
     * @return true if the word is any form of the verbs
     *         "start" or "stop", false otherwise
     */
    public static boolean isStartOrStop(final String word) {
        String cleanCopy = clean(word);
        return getStartStopMapping().containsKey(cleanCopy);
    }

    /**
     * Gets the tense of the given word, if it's a verb.
     * @param word - verb whose tense we need
     * @return tense if the word is a verb, null otherwise
     */
    public static Integer getTense(final String word) {
        String cleanCopy = clean(word);
        return getVerbTenses().get(cleanCopy);
    }

    /**
     * Gives back the opposite version of the given word
     * from the start-stop mapping if it exists.
     * @param word - whose opposite version we need
     * @return opposite version of the word, null if it
     *         doesn't exist in the mapping
     */
    public static String getStartStop(final String word) {
        String cleanCopy = clean(word);
        return getStartStopMapping().get(cleanCopy);
    }

    /**
     * Checks if the verb is a two-part verb.
     * @param words - words of the sentence
     * @param position - position which is currently being
     *                 processed
     * @return true if the two-part-verb is placed on this
     *         position, false otherwise
     */
    public static String isTwoPartVerb(final String[] words,
                                       final int position) {
        String mainVerb = getInfinitive(words[position]);

        if (position > 0) {
            String previous = clean(words[position - 1]);
            if (getTwoPartVerbs().containsKey(previous + " " + mainVerb)) {
                return getTwoPartVerbs().get(previous + " " + mainVerb);
            }
        }

        return null;
    }

    /**
     * Checks if the word is a new-sub-sentence verb.
     * @param word - word to be checked
     * @return true if the word is a new-sub-sentence
     *         verb, false otherwise
     */
    public static boolean isNewSubSentenceVerb(final String word) {
        String cleanCopy = clean(word);
        return getNewSentenceVerbs().contains(cleanCopy);
    }

    /**
     * Performs the very basic check if the word is an
     * adverb.
     * @param word - word to be checked
     * @return true if the word ends with "ly" or is
     *         "hereby", false otherwise
     */
    public static boolean isAdverb(final String word) {
        String cleanCopy = clean(word);
        int wordLen = cleanCopy.length();
        return wordLen > 2 && cleanCopy
                .substring(wordLen - 2, wordLen).equals("ly")
                || cleanCopy.equals("hereby");
    }

    /**
     * Gives back the opposite modal verb.
     * @param word - word whose opposite we need
     * @return the opposite modal verb
     */
    public static String getOppositeModal(final String word) {
        String cleanCopy = clean(word);

        if (isPositiveModal(cleanCopy)) {
            return getPositiveModalVerbs().get(cleanCopy);
        } else {
            return getNegativeModalVerbs().get(cleanCopy);
        }
    }

    /**
     * Gives back the opposite form of the verb "to be".
     * @param word - word whose opposite we need
     * @return the opposite form of the verb "to be"
     */
    public static String getOppositeBe(final String word) {
        String cleanCopy = clean(word);

        if (isPositiveBe(cleanCopy)) {
            return getPositiveBe().get(cleanCopy);
        } else {
            return getNegativeBe().get(cleanCopy);
        }
    }

    /**
     * Gives back the opposite form of the verb "to have".
     * @param word - word whose opposite we need
     * @return the opposite form of the verb "to have"
     */
    public static String getOppositeHave(final String word) {
        String cleanCopy = clean(word);

        if (isPositiveHave(cleanCopy)) {
            return getPositiveHave().get(cleanCopy);
        } else {
            return getNegativeHave().get(cleanCopy);
        }
    }

}
