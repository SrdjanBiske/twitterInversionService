package service.inversion;

import service.util.CustomStringBuilder;
import service.formatter.PostFormatter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static dm.dao.CharUtility.isSpecialChar;
import static dm.dao.NounUtility.isName;
import static dm.dao.NounUtility.isSingular;
import static dm.dao.PronounUtility.isNegIndefinitePronoun;
import static dm.dao.PronounUtility.getOppositePronoun;
import static dm.dao.PronounUtility.isIndefinitePronoun;
import static dm.dao.PronounUtility.isSingularPronoun;
import static dm.dao.PronounUtility.getOppositeNegPronoun;
import static dm.dao.VerbUtility.isVerb;
import static dm.dao.VerbUtility.isModal;
import static dm.dao.VerbUtility.isPerfect;
import static dm.dao.VerbUtility.isInfinitive;
import static dm.dao.VerbUtility.isFormOfHave;
import static dm.dao.VerbUtility.isThirdPerson;
import static dm.dao.VerbUtility.isGerund;
import static dm.dao.VerbUtility.isPastParticiple;
import static dm.dao.VerbUtility.isStartOrStop;
import static dm.dao.VerbUtility.getInfinitive;
import static dm.dao.VerbUtility.isFormOfBe;
import static dm.dao.VerbUtility.isTwoPartVerb;
import static dm.dao.VerbUtility.isPositiveBe;
import static dm.dao.VerbUtility.isPositiveModal;
import static dm.dao.VerbUtility.isNegativeModal;
import static dm.dao.VerbUtility.isAdverb;
import static dm.dao.VerbUtility.isNegativeFormOfDo;
import static dm.dao.VerbUtility.getThirdPerson;
import static dm.dao.VerbUtility.getPerfect;
import static dm.dao.VerbUtility.getStartStop;
import static dm.dao.VerbUtility.getOppositeHave;
import static dm.dao.VerbUtility.getOppositeBe;
import static dm.dao.VerbUtility.isPositiveHave;
import static dm.dao.VerbUtility.isPositiveDo;
import static dm.dao.VerbUtility.isNewSubSentenceVerb;
import static dm.dao.VerbUtility.getOppositeModal;
import static dm.dao.VerbUtility.clearSpecChars;
import static dm.dao.VerbUtility.clean;
import static dm.dao.VerbUtility.isNegativeBe;
import static dm.dao.VerbUtility.isNegativeHave;
import static service.inversion.ClearanceHelper.clearAfter;
import static service.inversion.ClearanceHelper.isThereVerbAfter;
import static service.inversion.ClearanceHelper.checkNonButCase;
import static service.inversion.ClearanceHelper.checkSomeAnyCase;
import static service.inversion.NewSubSentenceHelper.includedInNext;
import static service.inversion.NewSubSentenceHelper.includedInPrevious;
import static service.inversion.NewSubSentenceHelper.startNewSentence;
import static service.inversion.QuestionHelper.isPushQuestion;
import static service.inversion.QuestionHelper.isSkipQuestion;
import static service.inversion.ShortModalHelper.fixShortModals;
import static service.inversion.SkipHelper.isPossession;
import static service.inversion.SkipHelper.shouldSkip;

/**
 * A class used for inverting the posts.
 */
public final class InversionService {

    /**
     * Used for logging of events happening in this class.
     */
    private static final Logger LOGGER;

    static {
        LOGGER = LogManager.getLogger(InversionService.class);
    }

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private InversionService() {
    }

    /**
    * Takes the original tweet as an argument and
    * creates a negated version of it by negating
    * the main verb of the sentence.
    * @param original - tweet which will be inverted
    * @return negated version of the original
    */
    public static String createInverseVersion(
            final String original) {

        try {
            String copy = PostFormatter.format(original);
            copy = formatModals(copy);

            CustomStringBuilder resultingStatus = new CustomStringBuilder();

            String[] sentences
                    = copy.split("(?<=[.?!;])\\s+(?=\\p{Lu})");

            for (String sentence : sentences) {
                invertSentence(resultingStatus, sentence);
            }

            return resultingStatus.toString();

        } catch (Exception exception) {

            LOGGER.error("Tweet not inverted.");
            return original;
        }
    }

    /**
     * Method for the inversion of a single sentence. It
     * saves its results in the CustomStringBuilder.
     * The method cannot be separated into sub-methods,
     * since it works as a pipeline that the words of
     * the sentence go through. While they go through
     * the pipeline, various checks and operations are
     * performed on them.
     * @param resultingStatus - where the results will
     *                        be stored
     * @param sentence - sentence to be inverted
     */
    private static void invertSentence(
            final CustomStringBuilder resultingStatus,
            final String sentence) {

        String[] words = sentence.split("\\s+");

        int subSentenceStart = 0;
        boolean shouldInvert = true;
        Tense tense = new Tense(words[subSentenceStart],
                subSentenceStart, words.length);
        tense.setChanged(false);

        for (int position = 0; position < words.length; position++) {

            String word = words[position];
            String cleanWord = clean(word);
            int senLen = words.length;

            if (startNewSentence(words, tense, position)) {

                if (includedInPrevious(words[position])) {
                    if (shouldInvert) {
                        if (isVerb(cleanWord)
                                && !shouldSkip(words, tense, position)) {
                            tense.levelAddSuccess(cleanWord, position);
                        }
                        invert(words, tense);
                    }

                    if (position + 1 < senLen) {
                        tense = new Tense(words[position + 1],
                                position + 1, words.length);
                        subSentenceStart = position + 1;
                    } else {
                        tense = new Tense(words[position],
                                position, words.length);
                        subSentenceStart = position;
                    }

                    shouldInvert = true;
                    continue;

                } else if (includedInNext(words[position])) {
                    if (shouldInvert) {
                        invert(words, tense);
                    }

                    tense = new Tense(words[position],
                            position, words.length);
                    subSentenceStart = position;
                    shouldInvert = true;

                } else {
                    if (shouldInvert) {
                        invert(words, tense);
                    }

                    if (position + 1 < senLen) {
                        tense = new Tense(words[position + 1],
                                position + 1, words.length);
                        subSentenceStart = position + 1;
                    } else {
                        tense = new Tense(words[position],
                                position, words.length);
                        subSentenceStart = position;
                    }

                    shouldInvert = true;
                    continue;
                }
            }

            if (isVerb(cleanWord)) {

                if (shouldSkip(words, tense, position)) {
                    if (shouldInvert && position + 1 == senLen) {
                        invert(words, tense);
                    }
                    continue;
                }

                if (tense.getLastLvlPos() > -1
                        && isPossessionBetween(words,
                        tense.getLastLvlPos(), position)) {
                    if (shouldInvert && position + 1 == senLen) {
                        invert(words, tense);
                    }
                    continue;
                }

                if (isNewSubSentenceVerb(cleanWord)) {
                    shouldInvert = true;
                }

                if (!tense.levelAddSuccess(cleanWord, position)) {
                    if (isGerund(cleanWord) || (tense.getLevel1() == null
                            && isPastParticiple(cleanWord))) {
                        continue;

                    } else if (shouldInvert) {
                        if (invert(words, tense)) {
                            shouldInvert = false;
                        }
                        tense = new Tense(words[subSentenceStart],
                                subSentenceStart, words.length);
                        --position;
                    }
                }
            }

            if (cleanWord.equals("to") && position + 1 < senLen
                    && isInfinitive(words[position + 1])) {
                tense.activateInfinitive();
            }

            if (cleanWord.equals("after") || cleanWord.equals("before")
                    && isThereVerbAfter(words, position)) {
                if (shouldInvert) {
                    invert(words, tense);
                }
                tense = new Tense(cleanWord, position, words.length);
                subSentenceStart = position;
                shouldInvert = false;
            }

            if (shouldInvert && position + 1 == senLen) {
                invert(words, tense);
            }
        }

        for (String word : words) {
            resultingStatus.append(word);
        }
    }

    /**
     * Makes sure that there are no short modal
     * forms like "it's".
     * @param post - post that possibly has short
     *             modals
     * @return post without short modals
     */
    private static String formatModals(final String post) {
        String[] allWords = post.split("\\s+");
        allWords = fixShortModals(allWords);

        CustomStringBuilder fixedPost
                = new CustomStringBuilder();

        for (String word : allWords) {
            fixedPost.append(word);
        }

        return fixedPost.toString();
    }

    /**
     * Method used to make negation of a verb.
     *
     * @param words - words in the sentence
     * @param tense - complete form of the verb
     *              which is being negated
     * @return words in the negated sentence
     */
    private static boolean invert(
            final String[] words,
            final Tense tense) {

        if (tense.getLevel1() == null) {
            return false;

        } else if (isNewSubSentenceVerb(words[tense.getLastLvlPos()])
                && isThereVerbAfter(words, tense.getLastLvlPos())) {
            return false;

        } else if (isSkipQuestion(words, tense)) {
            return false;

        } else if (isPushQuestion(words, tense)) {
            invertPushQuestion(words, tense);

        } else {
            boolean changed = generalCase(words, tense);
            if (!changed) {
                performInversion(words, tense);
            }
        }

        checkNonButCase(words, tense);
        checkSomeAnyCase(words, tense);

        return true;
    }

    /**
     * Inverts the push questions.
     * @param words - words of the sentence
     * @param tense - complete form of the
     *              verb which is being negated
     */
    private static void invertPushQuestion(
            final String[] words,
            final Tense tense) {

        boolean changed = false;
        if (isPositive(tense.getLevel1())) {
            changed = negationAfter(words, tense.getPosition1());
            tense.setToPositive();
            tense.setChanged(true);
        }

        if (!changed && isNegative(tense.getLevel1())) {
            invertHashSet(words, tense.getPosition1());
            tense.setChanged(true);

        } else if (!changed && tense.getLevel2() != null) {
            negateLast(words, tense);
            tense.setToNegative();
            tense.setChanged(true);
        }
    }

    /**
     * In the cases when we have sentences with
     * indefinite pronouns, the normal verb negation
     * won't have much sense and it's also very
     * likely that we'll get sentences with double
     * negations, which is not grammatically correct.
     * That's why we'll only inverse these pronouns
     * (everybody -> nobody...) and the resulting
     * sentence will still have sense.
     * @param words - words in the sentence
     * @param tense - complete form of the verb which
     *              is being negated
     * @return true if anything has changed, false
     *         otherwise (in the cases when there's
     *         no indefinite pronouns in the sentence)
     */
    private static boolean generalCase(
            final String[] words,
            final Tense tense) {

        boolean changed = everyToNo(words, tense);
        if (changed) {
            return true;
        }

        changed = noToSome(words, tense);
        if (changed) {
            return true;
        }

        return startToStopCase(words, tense);
    }

    /**
     * Inversion from indefinite pronouns which
     * contain the word "every" to their
     * counterparts that contain the word "no"
     * (i.e. "everybody" -> "nobody").
     * @param words - words in the sentence
     * @param tense - complete form of the verb which
     *              is being negated
     * @return true if anything has changed, false
     *         otherwise (in the cases when there's
     *         no such indefinite pronouns in the
     *         sentence)
     */
    private static boolean everyToNo(
            final String[] words,
            final Tense tense) {

        if (tense.getPosition1() > 0
                && isIndefinitePronoun(words[tense.getPosition1() - 1])) {

            words[tense.getPosition1() - 1]
                    = finalizeWord(words[tense.getPosition1() - 1],
                    getOppositePronoun(words[tense.getPosition1() - 1]));

            tense.setToNegative();
            return true;
        }

        return false;
    }

    /**
     * Inversion from indefinite pronouns which
     * contain the word "no" to their
     * counterparts that contain the word "some"
     * (i.e. "nobody" -> "somebody").
     * @param words - words in the sentence
     * @param tense - complete form of the verb which
     *              is being negated
     * @return true if anything has changed, false
     *         otherwise (in the cases when there's
     *         no such indefinite pronouns in the
     *         sentence)
     */
    private static boolean noToSome(
            final String[] words,
            final Tense tense) {
        int start;
        /*if (tense.getPosition1() > 0) {
            start = tense.getPosition1() - 1;
        } else {
            start = tense.getPosition1();
        }*/
        start = tense.getSubSentenceStart();

        int end;
        if (tense.getLastLvlPos() + 2 < words.length) {
            end = tense.getLastLvlPos() + 2;
        } else if (tense.getLastLvlPos() + 1 < words.length) {
            end = tense.getLastLvlPos() + 1;
        } else {
            end = tense.getLastLvlPos();
        }

        for (int pos = start; pos < end; pos++) {

            String word = clean(words[pos]);
            if (isNegIndefinitePronoun(word)) {

                words[pos] = finalizeWord(words[pos],
                        getOppositeNegPronoun(word));

                tense.setToPositive();
                return true;

            } else if (word.equals("no") && pos + 1 < words.length
                    && clean(words[pos + 1]).equals("one")) {

                words[pos] = finalizeWord(words[pos], "someone");
                words[pos + 1] = finalizeWord(words[pos + 1], "");

                tense.setToPositive();
                return true;
            }
        }

        return false;
    }

    /**
     * Inversion from the verb "start" to the verb "stop".
     * (i.e. "started" -> "stopped").
     * @param words - words in the sentence
     * @param tense - complete form of the verb which
     *              is being negated
     * @return true if anything has changed, false
     *         otherwise (in the cases when there's
     *         no such indefinite pronouns in the
     *         sentence)
     */
    private static boolean startToStopCase(
            final String[] words,
            final Tense tense) {

        if (tense.getBeforeLastLvlPos() > -1
                && isStartOrStop(words[tense.getBeforeLastLvlPos()])) {

            words[tense.getBeforeLastLvlPos()]
                    = finalizeWord(words[tense.getBeforeLastLvlPos()],
                    getStartStop(words[tense.getBeforeLastLvlPos()]));

            return true;
        }

        return false;
    }

    /**
     * This method is used to detect what tense are
     * the verbs in and to call the appropriate
     * negation method.
     * @param words - words in the sentence
     * @param tense - complete form of the verb which
     *              is being negated
     */
    private static void performInversion(
            final String[] words,
            final Tense tense) {

        String toNegate = tense.getLevel1();
        int position = tense.getPosition1();

        String verbPhrase = isTwoPartVerb(words,
                tense.getLastLvlPos());

        if ((isModal(toNegate) || isFormOfBe(toNegate)
                || isFormOfHave(toNegate))
                && !shouldNegateHave(words, tense)) {

            simpleInversion(words, tense, toNegate, position);
            tense.setChanged(true);

        } else if (isVerb(toNegate)) {

            complexInversion(words, tense, toNegate,
                    verbPhrase, position);
        }

        clearAfter(words, position);
    }

    /**
     * Checks if there is a negation word like
     * "not" or "no" right after the verb. If
     * that's the case, the negation word is
     * removed.
     * @param words - words in the sentence
     * @param position - position of the word
     *                 which is currently being
     *                 processed
     * @return true if a change happened, false
     *         otherwise
     */
    private static boolean negationAfter(
            final String[] words,
            final int position) {

        if (isNextWordNegation(words, position)) {
            words[position + 1] =
                    finalizeWord(words[position + 1], "");
            return true;

        } else if (isWordAfterNextNegation(words, position)) {
            words[position + 2] =
                    finalizeWord(words[position + 2], "");
            return true;
        }

        return false;
    }

    /**
     * Inverts the modals and the verbs "to be"
     * and "to have".
     * @param words - words in the sentence
     * @param tense - complete form of the verb which
     * @param toNegate - verb that will be inverted
     * @param position - position of the word
     *                 which is currently being
     *                 processed
     */
    private static void simpleInversion(
            final String[] words,
            final Tense tense,
            final String toNegate,
            final int position) {

        if (isPositive(toNegate)) {
            boolean changed
                    = negationAfter(words, position);

            if (changed) {
                tense.setToPositive();
            } else {
                invertHashSet(words, position);
                tense.setToNegative();
            }

        } else {
            invertHashSet(words, position);
            tense.setToPositive();
        }
    }

    /**
     * Method performs all the inversions that are
     * more complex than just taking the opposite
     * form from the HashSet.
     * @param words - words in the sentence
     * @param tense - complete form of the verb which
     *              is being negated
     * @param toNegate - verb that will be inverted
     * @param verbPhrase - in verb phrases like "trash
     *                   talk", "trash" will be saved
     *                   as verbPhrase
     * @param position - position of the word which is
     *                 currently being processed
     */
    private static void complexInversion(
            final String[] words,
            final Tense tense,
            final String toNegate,
            final String verbPhrase,
            final int position) {

        tense.setChanged(true);

        if (isPositiveDo(toNegate)
                && negationAfter(words, position)) {
            tense.setToPositive();

        } else if (doAndDidSpecialCase(words, toNegate, tense)
                != null) {

            words[position] = finalizeWord(words[position],
                    doAndDidSpecialCase(words, toNegate, tense));
            tense.setToNegative();

        } else if (isInfinitive(toNegate)) {
            negateInfinitive(words, tense, toNegate,
                    verbPhrase, position);
            tense.setToNegative();

        } else if (isThirdPerson(toNegate)) {
            negateThirdPerson(words, tense, toNegate,
                    verbPhrase, position);
            tense.setToNegative();

        } else if (isPerfect(toNegate)) {
            negatePast(words, tense, toNegate,
                    verbPhrase, position);
            tense.setToNegative();

        } else if (isNegativeFormOfDo(toNegate)) {
            turnDoPositive(words, tense, toNegate,
                    verbPhrase, position);
            tense.setToPositive();
        } else {
            tense.setChanged(false);
        }
    }

    /**
     * If the word on a given position is a
     * modal, a verb "to be" or a verb "to have",
     * it will be turned to its opposite form.
     * @param words - words in the sentence
     * @param position - position of the word which is
     *                 currently being processed
     */
    private static void invertHashSet(
            final String[] words,
            final int position) {

        if (isModal(words[position])) {
            words[position] = finalizeWord(words[position],
                    getOppositeModal(words[position]));

        } else if (isFormOfBe(words[position])) {
            words[position] = finalizeWord(words[position],
                    getOppositeBe(words[position]));

        } else if (isFormOfHave(words[position])) {
            words[position] = finalizeWord(words[position],
                    getOppositeHave(words[position]));
        }
    }

    /**
     * Checks if the verb is a positive form
     * of a modal verb or of the verbs "to be"
     * or "to have".
     * @param toNegate - verb to check
     * @return true if toNegate is a positive
     *         form, false otherwise
     */
    private static boolean isPositive(
            final String toNegate) {

        return isPositiveModal(toNegate)
                || isPositiveBe(toNegate)
                || isPositiveHave(toNegate);
    }

    /**
     * Checks if the verb is a negative form
     * of a modal verb or of the verbs "to be"
     * or "to have".
     * @param toNegate - verb to check
     * @return true if toNegate is a negative
     *         form, false otherwise
     */
    private static boolean isNegative(
            final String toNegate) {

        return isNegativeModal(toNegate)
                || isNegativeBe(toNegate)
                || isNegativeHave(toNegate);
    }

    /**
     * Method used for turning any negative form of
     * the verb "to do" to its positive form. (i.e.
     * from "doesn't sleep" to "sleeps").
     * @param words - words in the sentence
     * @param tense - complete form of the verb which
     *              is being negated
     * @param toNegate - verb that will be inverted
     * @param verbPhrase - in verb phrases like "trash
     *                   talk", "trash" will be saved
     *                   as verbPhrase
     * @param position - position of the word which is
     *                 currently being processed
     */
    private static void turnDoPositive(
            final String[] words,
            final Tense tense,
            final String toNegate,
            final String verbPhrase,
            final int position) {

        if (toNegate.equals("doesn't")) {

            turnToPositive(words, tense, toNegate, verbPhrase,
                    "does", getThirdPerson(tense.getLevel2()),
                    position);

        } else if (toNegate.equals("don't")) {

            if (!tense.isQuestion()) {
                words[position] = "";
            } else {
                words[position] = finalizeWord(words[position],
                        "do");
            }

        } else  if (toNegate.equals("didn't")) {

            turnToPositive(words, tense, toNegate, verbPhrase,
                    "did", getPerfect(tense.getLevel2()),
                    position);
        }
    }

    /**
     * Method used for turning the negative forms
     * of Third Person and Past Simple of the verb
     * "to do" to their positive versions.
     * @param words - words in the sentence
     * @param tense - complete form of the verb which
     *              is being negated
     * @param toNegate - verb that will be inverted
     * @param verbPhrase - in verb phrases like "trash
     *                   talk", "trash" will be saved
     *                   as verbPhrase
     * @param doesOrDid - "does" or "did", depending on
     *                  the used tense
     * @param mainVerb - main verb in appropriate tense
     * @param position - position of the word which is
     *                 currently being processed
     */
    private static void turnToPositive(
            final String[] words,
            final Tense tense,
            final String toNegate,
            final String verbPhrase,
            final String doesOrDid,
            final String mainVerb,
            final int position) {

        if (!tense.isQuestion()) {
            if (verbPhrase != null) {
                words[position] = finalizeWord(words[position],
                        verbPhrase + " " + mainVerb);

                words[tense.getPosition2()] = "";
                words[tense.getPosition2() - 1] = "";

            } else {
                words[position] = finalizeWord(words[position],
                        mainVerb);

                words[tense.getPosition2()] = "";
            }
        } else {
            words[position] = finalizeWord(words[position], doesOrDid);
        }
    }

    /**
     * Method used for negation of Present Simple and Past
     * Simple tense.
     *
     * @param words - words in the sentence
     * @param tense - complete form of the verb which is
     *              being negated
     * @param toNegate - verb that will be inverted
     * @param verbPhrase - in verb phrases like "trash talk",
     *                   "trash" will be saved as verbPhrase
     * @param negDo - depends on the tense being negated
     *              (don't, doesn't or didn't)
     * @param position - position of the word which is
     *                 currently being processed
     */
    private static void negate(
            final String[] words,
            final Tense tense,
            final String toNegate,
            final String verbPhrase,
            final String negDo,
            final int position) {

        if (isAdverbBefore(words, position)) {

            words[position] = finalizeWord(words[position],
                    negDo + " " + words[position - 1]
                            + " " + getInfinitive(toNegate));

            words[position - 1] = "";

        } else {
            if (tense.isQuestion() && isPositiveDo(toNegate)
                    && !isOnTheEnd(words, position)) {

                words[position] = finalizeWord(words[position],
                        negDo);

            } else {
                if (verbPhrase != null) {

                    words[position] = finalizeWord(words[position],
                            negDo + " " + verbPhrase + " "
                                    + getInfinitive(toNegate));

                    words[tense.getLastLvlPos() - 1] = "";

                } else {
                    words[position] = finalizeWord(words[position],
                            negDo + " " + getInfinitive(toNegate));
                }
            }
        }
    }

    /**
     * Method used for negation of Past Simple tense
     * (i.e. from "slept" to "didn't sleep").
     *
     * @param words - words in the sentence
     * @param tense - complete form of the verb which is
     *              being negated
     * @param toNegate - verb that will be inverted
     * @param verbPhrase - in verb phrases like "trash talk",
     *                   "trash" will be saved as verbPhrase
     * @param position - position of the word which is
     *                 currently being processed
     */
    private static void negatePast(
            final String[] words,
            final Tense tense,
            final String toNegate,
            final String verbPhrase,
            final int position) {

        if (isInfinitiveAfterPastParticiple(tense)) {
            return;
        }

        if (toNegate.equals("did")
                && isInfinitive(tense.getLevel2())) {

            words[position] = finalizeWord(words[position],
                    "didn't");
            return;
        }

        negate(words, tense, toNegate,
                verbPhrase, "didn't", position);
    }

    /**
     * Method used for negation of first person and
     * plural of Present Simple tense (i.e. from
     * "sleeps" to "don't sleep").
     * @param words - words in the sentence
     * @param tense - complete form of the verb which
     *              is being negated
     * @param toNegate - verb that will be inverted
     * @param verbPhrase - in verb phrases like "trash
     *                   talk", "trash" will be saved
     *                   as verbPhrase
     * @param position - position of the word which is
     *                 currently being processed
     */
    private static void negateInfinitive(
            final String[] words,
            final Tense tense,
            final String toNegate,
            final String verbPhrase,
            final int position) {

        String[] copy = words;

        if (tense.getLevel2() == null && position > 1
                && clean(copy[position - 1]).equals("to")) {

            copy[position - 1] = "to not";

        } else if (position > 0
                && (isSingular(copy[position - 1])
                || isName(copy[position - 1])
                || isSingularPronoun(copy[position - 1]))
                && isPerfect(toNegate)) {

            negatePast(copy, tense, toNegate,
                    verbPhrase, position);

        } else {
            negate(words, tense, toNegate,
                    verbPhrase, "don't", position);
        }
    }

    /**
     * Method used for negation of third person of
     * Present Simple tense (i.e. from "sleeps" to
     * "doesn't sleep").
     * @param words - words in the sentence
     * @param tense - complete form of the verb which
     *              is being negated
     * @param toNegate - verb that will be inverted
     * @param verbPhrase - in verb phrases like "trash
     *                   talk", "trash" will be saved
     *                   as verbPhrase
     * @param position - position of the word which
     *                 is currently being processed
     */
    private static void negateThirdPerson(
            final String[] words,
            final Tense tense,
            final String toNegate,
            final String verbPhrase,
            final int position) {

        negate(words, tense, toNegate,
                verbPhrase, "doesn't", position);
    }

    /**
     * Checks if we're on the end of the sentence.
     *
     * @param words - words of the sentence
     * @param position - position of the word which
     *                 is currently being processed
     * @return true if we're on the last word of the
     *         sentence, false otherwise
     */
    private static boolean isOnTheEnd(
            final String[] words,
            final int position) {

        return position == words.length - 1;
    }

    /**
     * The "have" in sentences "I have a car." and
     * "I have done that." is not negated the same
     * way. This method checks if the "have" should
     * be negated in a "don't have" way or "haven't".
     * @param words - words of the sentence
     * @param tense - complete form of the verb which
     *              is being checked
     * @return true if we do the negation with
     *         "haven't", false otherwise
     */
    private static boolean shouldNegateHave(
            final String[] words,
            final Tense tense) {

        String last = words[words.length - 1];
        return tense.getLevel1() != null
                && (tense.getPosition1() != 0
                || (tense.getPosition1() == 0
                && last.charAt(last.length() - 1) != '?'))
                && (tense.getLevel1().equals("have")
                || tense.getLevel1().equals("had")
                || tense.getLevel1().equals("has"))
                && ((tense.getLevel2() != null
                && isInfinitive(tense.getLevel2()))
                || tense.getLevel2() == null)
                && !isNextWordNegation(words,
                tense.getPosition1());
    }

    /**
     * The method checks if the word before position
     * is an adverb.
     * @param words - words of the sentence
     * @param position - position of the word which
     *                 is currently being processed
     * @return true if the word before position is
     *         an adverb, false otherwise
     */
    private static boolean isAdverbBefore(
            final String[] words,
            final int position) {

        return position > 0 && isAdverb(words[position - 1]);
    }

    /**
     * In many cases, when we have the phrase of the
     * form Past Participle + "to" + Infinitive (i.e.
     * "forced to move"), it doesn't make sense to
     * invert that phrase. However, this is not the
     * case with all the verbs and this method tries
     * to recognize what verbs should be skipped for
     * the negation.
     * @param tense - complete form of the verb which
     *              is being checked
     * @return true if the tense is Past Participle
     *         + Infinitive and should be skipped,
     *         false otherwise
     */
    private static boolean isInfinitiveAfterPastParticiple(
            final Tense tense) {

        return tense.getLevel1() != null
                && isPastParticiple(tense.getLevel1())
                && tense.getLevel2() != null
                && isInfinitive(tense.getLevel2())
                && !tense.getLevel1().equals("had")
                && !tense.getLevel1().equals("voted")
                && !tense.getLevel1().equals("allowed")
                && !tense.getLevel1().equals("wanted")
                && !tense.getLevel1().equals("tried");
    }

    /**
     * A method used for the negation of the last
     * verb form in the tense if the tense contains
     * more than one verb form. Otherwise, it will
     * return the original sentence.
     * @param words - words of the sentence
     * @param tense - complete form of the verb which
     *              is being checked
     */
    private static void negateLast(
            final String[] words,
            final Tense tense) {

        String[] result = words;
        int position = tense.getLastLvlPos();

        if (tense.getLevel2() != null) {
            result[position] = "not " + result[position];
        } else {
            performInversion(result, tense);
        }
    }

    /**
     * Checks if there is a possessive noun between
     * the start and end positions.
     * @param words - words of the sentence
     * @param start - start position
     * @param end - end position
     * @return true if there is a possessive noun
     *         between the start and the end, false
     *         otherwise
     */
    private static boolean isPossessionBetween(
            final String[] words,
            final int start,
            final int end) {

        for (int pos = start + 1; pos < end; pos++) {
            if (isPossession(words[pos])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the next word is "no" or "not".
     * @param words - words of the sentence
     * @param position - position of the word which
     *                 is currently being processed
     * @return true if the next word is "no" or "not",
     *         false otherwise
     */
    private static boolean isNextWordNegation(
            final String[] words,
            final int position) {
        return position < words.length - 1
                && (clean(words[position + 1]).equals("not")
                || (clean(words[position + 1]).equals("no")
                && position < words.length - 2
                && !clean(words[position + 2]).equals("one"))
                || (clean(words[position + 1]).equals("no")
                && position == words.length - 2)
                || clean(words[position + 1]).equals("never"));
    }

    /**
     * Checks if the word after next is "no" or "not".
     * @param words - words of the sentence
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if the word after next is "no" or
     *         "not", false otherwise
     */
    private static boolean isWordAfterNextNegation(
            final String[] words,
            final int position) {

        return position < words.length - 2
                && (clean(words[position + 2]).equals("not")
                || clean(words[position + 2]).equals("no")
                || clean(words[position + 1]).equals("never"));
    }

    /**
     * 'Do' is a special verb because it can be used
     * normally as all other verbs but it can also be
     * used to form past simple. Therefore the negation
     * of 'I did.' and 'I did it.' will not be the same.
     * It will be 'I didn't.' and 'I didn't do it.' The
     * only way to differentiate is to check if the verb
     * is on the end of the sentence. This method checks
     * if 'do/did' is on the end.
     * @param words - words of the sentence
     * @param word - word to be checked
     * @param tense - complete form of the verb which is
     *              being considered
     * @return - negative version if the verb is do/did
     *           and is on the end of the sentence, null
     *           otherwise
     */
    private static String doAndDidSpecialCase(
            final String[] words,
            final String word,
            final Tense tense) {

        if (tense.getLevel2() != null
                || tense.isQuestion()) {
            return null;

        } else if (word.equals("do")
                && tense.getPosition1() == words.length - 1) {
            return "don't";

        } else if (word.equals("did")
                && tense.getPosition1() == words.length - 1) {
            return "didn't";

        } else {
            return null;
        }
    }

    /**
     * Words in the original post can be written in some
     * specific styles (i.e. upper case, with some special
     * characters etc.). This method takes the original
     * word from the post, analyzes its style and returns
     * the new word in the same style as the original one.
     * @param original - word to be analyzed
     * @param toEdit - new word which be styled in the same
     *               way as the original word
     * @return styled new word
     */
    public static String finalizeWord(
            final String original,
            final String toEdit) {

        String sample = clearSpecChars(original);
        int sLen = sample.length();
        int osLen = original.length();

        String result = clean(toEdit);
        int len = result.length();

        if (sample.equals(
                sample.toUpperCase())) {

            result = result.toUpperCase();

        } else if (sample.equals(
                sample.substring(0, 1).toUpperCase()
                        + sample.substring(1, sLen).toLowerCase())) {

            result = result.substring(0, 1).toUpperCase()
                    + result.substring(1, len).toLowerCase();
        } else {
            result = result.toLowerCase();
        }

        if (isSpecialChar(original.charAt(0))) {
            result = original.charAt(0) + result;
        }

        if (osLen > 1 && isSpecialChar(original.charAt(osLen - 2))
                && isSpecialChar(original.charAt(osLen - 1))) {
            result = result + original.charAt(osLen - 2);
        }

        if (osLen > 1 && isSpecialChar(original.charAt(osLen - 1))) {
            result = result + original.charAt(osLen - 1);
        }

        return result;
    }

}
