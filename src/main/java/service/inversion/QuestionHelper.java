package service.inversion;

import static dm.dao.InterrogativeUtility.isPushQuestionWord;
import static dm.dao.InterrogativeUtility.isQuestionWord;
import static dm.dao.InterrogativeUtility.isSkipQuestionWord;

/**
 * Helper class for managing decisions if
 * the sentence is a question or not.
 */
public class QuestionHelper {

    /**
     * There is no point whatsoever in negating
     * questions in future tense (i.e. "When won't
     * it be?" doesn't make sense at all). This method
     * checks if the sentence in the post has this form.
     *
     * @param words - words of the sentence
     * @param tense - complete form of the verb which
     *              is being checked
     * @return true if the sentence is a question in
     *         future tense, false otherwise
     */
    private static boolean isFutureQuestion(
            final String[] words,
            final Tense tense) {

        return tense.getLevel1() != null
                && tense.getLevel1().equals("will")
                && (tense.isQuestion()
                || (tense.getPosition1() > 0
                && isQuestionWord(
                        words[tense.getPosition1() - 1])));
    }

    /**
     * Apart from the future questions, there are also
     * other questions that are impossible to negate
     * while maintaining the logic. These questions are
     * the ones that starts with "when", "where" or
     * similar interrogatives. This method assesses if
     * the question should be skipped from negating.
     * @param words - words of the sentence
     * @param tense - complete form of the verb which
     *              is being checked
     * @return true if the question should be skipped,
     *         false otherwise
     */
    public static boolean isSkipQuestion(
            final String[] words,
            final Tense tense) {

        return isFutureQuestion(words, tense)
                || (tense.getSubSentenceStart() > -1
                && isSkipQuestionWord(
                        words[tense.getSubSentenceStart()]));
    }

    /**
     * By push-question, we mean the questions where it
     * doesn't make sense to negate the modal verb but
     * the main verb (i.e. "Which one do you like?"
     * would be converted to "Which one do you not like?"
     * and not "Which one don't you like?", since the
     * second option doesn't make much sense). Therefore,
     * we push to the back. If we use a tense containing
     * only one verb form (i.e. past simple), the negation
     * is not performed. These questions are the one that
     * start with "who", "which", "whom", "what" and "whose".
     * @param words - words of the sentence
     * @param tense - complete form of the verb which is
     *              being checked
     * @return true if the sentence is a push question,
     *         false otherwise
     */
    public static boolean isPushQuestion(
            final String[] words,
            final Tense tense) {

        return tense.getSubSentenceStart() > -1
                && isPushQuestionWord(
                        words[tense.getSubSentenceStart()]);
    }

}
