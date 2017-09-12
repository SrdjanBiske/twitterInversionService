package service.inversion;

import static dm.dao.AdjectiveUtility.isAdjective;
import static dm.dao.ConjunctionUtility.isConjunction;
import static dm.dao.NounUtility.isArticle;
import static dm.dao.NounUtility.isName;
import static dm.dao.NounUtility.isNoun;
import static dm.dao.NounUtility.isPlural;
import static dm.dao.NounUtility.isSingular;
import static dm.dao.PhraseUtility.isNotSkipPhrase;
import static dm.dao.PhraseUtility.isPhraseToSkip;
import static dm.dao.PrepositionUtility.isPreposition;
import static dm.dao.PronounUtility.isSingularPronoun;
import static dm.dao.PronounUtility.isPluralPronoun;
import static dm.dao.PronounUtility.isBasicPronoun;
import static dm.dao.PronounUtility.isOtherPronoun;
import static dm.dao.PronounUtility.isPossessivePronoun;
import static dm.dao.Utility.clean;
import static dm.dao.VerbUtility.isPastParticiple;
import static dm.dao.VerbUtility.isGerund;
import static dm.dao.VerbUtility.isThirdPerson;
import static dm.dao.VerbUtility.isInfinitive;
import static dm.dao.VerbUtility.isPerfect;
import static dm.dao.VerbUtility.isModal;
import static dm.dao.VerbUtility.isVerb;
import static dm.dao.VerbUtility.isFormOfDo;
import static dm.dao.VerbUtility.isFormOfBe;
import static dm.dao.VerbUtility.isFormOfHave;

/**
 * Class with methods for the recognition of
 * false positives.
 */
public final class SkipHelper {

    /**
     * Three positions before.
     */
    private static final int THREE_POS_BEF = 3;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private SkipHelper() {
    }

    /**
     * This method collects all the rules that
     * say that the word which has been recognized
     * as a verb should be skipped and not negated.
     * @param words - words of the sentence
     * @param position - position of the word which
     *                 is currently being processed
     * @param tense - complete form of the verb
     *              which is being checked
     * @return true if the word should be skipped
     *         and not negated, false otherwise
     */
    public static boolean shouldSkip(final String[] words,
                                      final Tense tense,
                                      final int position) {

        return isPastParticipleAfterGerund(words, position)
                || isArticleBefore(words, tense, position)
                || isFalsePositive(words, tense, position)
                || isConjunctionBefore(words, tense, position)
                || isAdjectiveBefore(words, tense, position)
                || firstPlaceError(words, position)
                || isPrepositionAfter(words, position)
                || isPhraseToSkip(words, position)
                || isPronounBefore(words, tense, position)
                || isPrepositionBefore(words, tense, position)
                || isPastParticipleWithBy(words, position)
                || isGerundBefore(words, position)
                || isPossibleName(words, position);
    }

    /**
     * Checks if the word is actually a name.
     *  @param words - words of the sentence
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if the word is surely a name,
     *         false otherwise
     */
    private static boolean isPossibleName(
            final String[] words,
            final int position) {

        return isName(words[position])
                && ((position > 0
                && isName(words[position - 1]))
                || position + 1 < words.length
                && isName(words[position + 1]));
    }

    /**
     * This is detects false positives by checking if a
     * gerund is on a previous position (i.e. int the
     * phrases like "taking hold", the word "hold" should
     * not be negated).
     * @param words - words of the sentence
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if a gerund is on the previous position,
     *         false otherwise
     */
    private static boolean isGerundBefore(
            final String[] words,
            final int position) {

        return position > 0
                && isGerund(words[position - 1]);
    }

    /**
     * This is one way of detecting false positives. If
     * we have a past participle which is followed by the
     * word "by", it is very likely that it's used as an
     * adjective. This method checks this scenario.
     * @param words - words of the sentence
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if the verb is followed by the word
     *         "by", false otherwise
     */
    private static boolean isPastParticipleWithBy(
            final String[] words,
            final int position) {

        return isPastParticiple(words[position])
                && (((position + 1 < words.length)
                && clean(words[position + 1]).equals("by"))
                || ((position + 2 < words.length)
                && clean(words[position + 2]).equals("by")));
    }

    /**
     * If a past participle form of the verb is after
     * gerund form, it doesn't make much sense to negate
     * it since the sentence very often loses its meaning.
     * This method if the verb on the specified position
     * is a past participle after gerund.
     * @param words - words of the sentence
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if the past participle is after
     *         gerund, false otherwise
     */
    private static boolean isPastParticipleAfterGerund(
            final String[] words,
            final int position) {

        return position > 0
                && isGerund(words[position - 1])
                && isPastParticiple(words[position]);
    }

    /**
     * Checks if the previous word is an article. It is
     * used for checking if the word which is recognized
     * as a verb is actually an adjective (i.e. in the
     * phrase 'the United Nations', we know because of
     * 'the' that 'United' is used as adjective).
     * @param words - words in the sentence
     * @param tense - complete form of the verb which is
     *              being checked
     * @param position - position of the word which is
     *                 currently being processed
     * @return - true if an article is before the current
     *           word, false otherwise
     */
    private static boolean isArticleBefore(final String[] words,
                                           final Tense tense,
                                           final int position) {

        return position > tense.getSubSentenceStart()
                && (isArticle(words[position - 1])
                || (position > 1
                && isArticle(words[position - 2])
                && isName(words[position - 1])));
    }

    /**
     * There are some pronouns which if are surely a sign
     * for a false positive, if they are put directly
     * before the verb (i.e. "your", "his", "me", "us").
     * This method detects them.
     * @param words  - words of the sentence
     * @param tense - complete form of the verb which is
     *              being checked
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if there is a non-personal pronoun
     *         before the verb, false otherwise
     */
    private static boolean isPronounBefore(final String[] words,
                                           final Tense tense,
                                           final int position) {

        return position > tense.getSubSentenceStart()
                && (isOtherPronoun(words[position - 1])
                || isPossessivePronoun(words[position - 1]));
    }

    /**
     * In order to avoid many occurrences of the words
     * which are falsely recognized as verbs (we call
     * these verbs "False positives", i.e. "part" can
     * be a noun and a verb), we check if the verb tense
     * is adapted to its subject noun. If a noun is in
     * singular, the verb has to end with 's'. If the
     * noun is in plural, the verb has to be in its
     * infinitive form. This method checks if that's the
     * case.
     * @param words - words of the sentence
     * @param tense - complete form of the verb which is
     *              being checked
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if the composition is a false positive,
     *         false otherwise
     */
    private static boolean isFalsePositive(final String[] words,
                                           final Tense tense,
                                           final int position) {

        return  tense.getPosition1() == null
                && (position > tense.getSubSentenceStart()

                && ((multiplicityError(words, position)
                && !isNotSkipPhrase(words, position)
                && !nounsConnectedWithAnd(words, position))

                || isThatVerbCase(words, position)

                || isPossession(words[position - 1])));
    }

    /**
     * Looks for the multiplicity error by checking if there is
     * a verb in third person form after a plural or in an
     * infinitive form after a singular.
     * @param words - words of the sentence
     * @param position - position of the word which is currently
     *                 being processed
     * @return true if there is an error, false otherwise
     */
    private static boolean multiplicityError(
            final String[] words,
            final int position) {

        return (isInfinitive(words[position])
                && !isPerfect(words[position])
                && (isName(words[position - 1])
                || isSingular(words[position - 1])
                || isSingularPronoun(words[position - 1])))
                || (isThirdPerson(words[position])
                && (isPlural(words[position - 1])
                || isPluralPronoun(words[position - 1])));
    }

    /**
     * Checks if there are two nouns or names just before
     * the position that are connected with "and" or "&".
     * @param words - words of the sentence
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if there are nouns connected, false
     *         otherwise
     */
    private static boolean nounsConnectedWithAnd(
            final String[] words,
            final int position) {

        return position > 2
                && isInfinitive(words[position])
                && (isName(words[position - 1])
                || isNoun(words[position - 1])
                || isBasicPronoun(words[position - 1]))
                && (words[position - 2].equals("and")
                || words[position - 2].equals("&"))
                && (isName(words[position - THREE_POS_BEF])
                || isNoun(words[position - THREE_POS_BEF])
                || isBasicPronoun(words[position - THREE_POS_BEF]));
    }

    /**
     * Method is detecting false positives by checking if
     * there is a word "that" on the next position after
     * the potential verb and then another verb on the
     * position after next (i.e. "part that is...", from
     * the "that is" construction, we know that the word
     * "part" is not a verb in this case).
     * @param words - words of the sentence
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if there's the word "that" on
     *         position + 1. followed by a verb
     */
    private static boolean isThatVerbCase(final String[] words,
                                          final int position) {

        return position + 2 < words.length
                && words[position + 1].equals("that")
                && isVerb(words[position + 2]);
    }

    /**
     * Checks if the a word ends with "'s", which
     * usually represents a possession.
     * @param word - word to be checked
     * @return true if the word is possession,
     *         false otherwise
     */
    public static boolean isPossession(final String word) {
        String copy = clean(word);
        return copy.length() > 2
                && copy.endsWith("\'s")
                && isName(copy.substring(0, copy.length() - 2));
    }

    /**
     * This method detects false positives by looking
     * for conjunctions before the verbs while considering
     * that there might be a conjunction between the two
     * verbs. In that case, it will return false.
     * @param words - words of the sentence
     * @param tense - complete form of the verb which is
     *              being considered
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if there is a conjunction before the
     *         verb that indicates that it's a false
     *         positive, false otherwise
     */
    private static boolean isConjunctionBefore(
            final String[] words,
            final Tense tense,
            final int position) {

        if (position < tense.getSubSentenceStart()
                || position == 0) {
            return false;
        }

        if (isConjunction(words[position - 1])) {

            if (tense.getLevel1() == null
                    && !tense.hasChanged()) {
                return true;
            }

            Tense tenseCopy = tense.copy();
            tenseCopy.undo();
            return !tenseCopy
                    .levelAddSuccess(clean(words[position]),
                            position);
        }

        return false;
    }

    /**
     * The method detects false positives by looking for
     * prepositions before the verbs which are surely
     * never used before them (i.e. "in", "from" etc.).
     * @param words - words of the sentence
     * @param tense - complete form of the verb which is
     *              being considered
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if there is is a preposition before
     *         the verb which indicates that it's a false
     *         positive, false otherwise
     */
    private static boolean isPrepositionBefore(
            final String[] words,
            final Tense tense,
            final int position) {

        if (position < tense.getSubSentenceStart()
                || position == 0) {
            return false;
        }

        return isPreposition(words[position - 1]);
    }

    /**
     * The method detects false positives by looking
     * for prepositions after the verbs which are
     * surely never used after them (i.e. "of").
     * @param words - words of the sentence
     * @param position - position of the word which
     *                 is currently being processed
     * @return true if there is is a preposition after
     *         the verb which indicates that it's a
     *         false positive, false otherwise
     */
    private static boolean isPrepositionAfter(
            final String[] words,
            final int position) {

        if (position > words.length - 2) {
            return false;
        }

        String next = clean(words[position + 1]);
        return next.equals("of");
    }

    /**
     * Checks if there is an adjective on the previous
     * position. Adjectives are used to describe nouns,
     * and not verbs, and that's how this method
     * recognizes the false positives.
     * @param words - words of the sentence
     * @param tense - complete form of the verb which
     *              is being considered
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if there is an adjective before the
     *         given position, false otherwise
     */
    private static boolean isAdjectiveBefore(
            final String[] words,
            final Tense tense,
            final int position) {

        return position > tense.getSubSentenceStart()
                && isAdjective(words[position - 1]);
    }

    /**
     * This method is used for the recognition of false
     * positives in the sentences like "Sanctions are
     * discussed." The system would recognize the word
     * "Sanctions" as a verb but this method ensures that
     * only the modal verbs and imperative infinitive can
     * be on the beginning of the sentence.
     *
     * @param words - words of the sentence
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if there is a verb form on the
     *         beginning of the sentence other than modal
     *         or infinitive, false otherwise
     */
    private static boolean firstPlaceError(
            final String[] words,
            final int position) {

        return position == 0 && ((!isModal(words[position])
                && !isFormOfDo(words[position])
                && !isInfinitive(words[position])
                && !isFormOfBe(words[position])
                && !isFormOfHave(words[position]))
                || (isInfinitive(words[position])
                && isVerb(words[1])));
    }

}
