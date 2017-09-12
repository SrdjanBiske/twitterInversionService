package dm.dao;

import static dm.data.Pronouns.getBasicPronouns;
import static dm.data.Pronouns.getOtherPronouns;
import static dm.data.Pronouns.getPossessivePronouns;
import static dm.data.Pronouns.getPluralPronouns;
import static dm.data.Pronouns.getSingularPronouns;
import static dm.data.Pronouns.getIndefinitePronouns;
import static dm.data.Pronouns.getNegIndefinitePronouns;

/**
 * Methods for operations on data structures
 * containing pronouns.
 */
public class PronounUtility extends Utility {

    /**
     * Checks if the word is a singular pronoun.
     * @param word - word to be checked
     * @return true if the word is a singular pronoun,
     *         false otherwise
     */
    public static boolean isSingularPronoun(final String word) {
        String cleanCopy = clean(word);
        return getSingularPronouns().contains(cleanCopy);
    }

    /**
     * Checks if the word is a plural pronoun.
     * @param word - word to be checked
     * @return true if the word is a plural pronoun,
     *         false otherwise
     */
    public static boolean isPluralPronoun(final String word) {
        String cleanCopy = clean(word);
        return getPluralPronouns().contains(cleanCopy);
    }

    /**
     * Checks if the word is a basic pronoun.
     * @param word - word to be checked
     * @return true if the word is a basic pronoun,
     *         false otherwise
     */
    public static boolean isBasicPronoun(final String word) {
        String cleanCopy = clean(word);
        return getBasicPronouns().contains(cleanCopy);
    }

    /**
     * Checks if the word is a possessive pronoun.
     * @param word - word to be checked
     * @return true if the word is a possessive pronoun,
     *         false otherwise
     */
    public static boolean isPossessivePronoun(final String word) {
        String cleanCopy = clean(word);
        return getPossessivePronouns().contains(cleanCopy);
    }

    /**
     * Checks if the word is any other pronoun.
     * @param word - word to be checked
     * @return true if the word is any other pronoun,
     *         false otherwise
     */
    public static boolean isOtherPronoun(final String word) {
        String cleanCopy = clean(word);
        return getOtherPronouns().contains(cleanCopy);
    }

    /**
     * Checks if the word is an indefinite pronoun.
     * @param word - word to be checked
     * @return true if the word is an indefinite
     *         pronoun, false otherwise
     */
    public static boolean isIndefinitePronoun(final String word) {
        String cleanCopy = clean(word);
        return getIndefinitePronouns().containsKey(cleanCopy);
    }

    /**
     * Gives back the opposite indefinite pronoun.
     * @param word - word whose opposite we need
     * @return the opposite indefinite pronoun
     */
    public static String getOppositePronoun(final String word) {
        String cleanCopy = clean(word);
        return getIndefinitePronouns().get(cleanCopy);
    }

    /**
     * Checks if the word is a negative indefinite
     * pronoun.
     * @param word - word to be checked
     * @return true if the word is a negative
     *         indefinite pronoun, false otherwise
     */
    public static boolean isNegIndefinitePronoun(final String word) {
        String cleanCopy = clean(word);
        return getNegIndefinitePronouns().containsKey(cleanCopy);
    }

    /**
     * Gives back the opposite of the negative
     * indefinite pronoun.
     * @param word - word whose opposite we need
     * @return the opposite of the negative
     *         indefinite pronoun.
     */
    public static String getOppositeNegPronoun(final String word) {
        String cleanCopy = clean(word);
        return getNegIndefinitePronouns().get(cleanCopy);
    }

}
