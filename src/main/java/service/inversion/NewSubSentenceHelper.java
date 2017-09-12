package service.inversion;

import static dm.dao.ConjunctionUtility.isConjunction;
import static dm.dao.ConjunctionUtility.isSentenceConjunction;
import static dm.dao.InterrogativeUtility.isQuestionWord;
import static dm.dao.PhraseUtility.isSubSentencePhrase;
import static dm.dao.Utility.clean;
import static dm.dao.VerbUtility.isVerb;
import static dm.dao.VerbUtility.sameTense;
import static service.inversion.SkipHelper.shouldSkip;

/**
 * Helper class for managing decisions
 * about new sub-sentences.
 */
public class NewSubSentenceHelper {

    /**
     * When the word iterator encounters a sub-sentence
     * separator, this method should decide if that's
     * really the start of a new sub-sentence. For example,
     * The conjunction "and" in the following two sentences
     * is used in two different purposes. In the first case
     * it's between the nouns and in the second one, it's
     * between the sub-sentences: "I like dogs and cats."
     * and "I like dogs and I don't like cats." This method
     * should return false for the first sentence because we
     * don't have a new sub-sentence after the word "and",
     * but will return true for the second one because we
     * have two sub-sentences which are separated with "and".
     * This is being done by checking if there is a verb
     * after the sub-sentence separator.
     * @param words - words in the sentence
     * @param tense - complete form of the verb which is
     *              being considered
     * @param start - position of the separator. We should
     *              search for the verbs after this position
     * @return true if this is really the beginning of a new
     *         subsentence, false otherwise
     */
    public static boolean startNewSentence(
            final String[] words,
            final Tense tense,
            final int start) {

        if (start == 0) {
            return false;
        }

        if (isSubSentenceCase(words, start)) {
            String word = clean(words[start]);

            if (surelyNewSentence(words, tense, word, start)) {
                return true;
            }

            Tense tenseCopy = tense.copy();
            for (int pos = start + 1; pos < words.length; pos++) {

                word = clean(words[pos]);
                if (isVerb(word) && !shouldSkip(words, tense, pos)) {

                    if (tenseCopy.levelAddSuccess(word, pos)) {
                        return false;

                    } else {
                        if (isSentenceConjunction(words[pos - 1])
                                && sameTense(words[tense.getLastLvlPos()],
                                word)) {
                            return false;
                        }
                        return true;
                    }
                }

                if (isSubSentenceCase(words, pos)) {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * If one og these conditions are fulfilled,
     * it's definitely a new sub-sentence.
     * @param words - words in the sentence
     * @param tense - complete form of the verb which is
     *              being considered
     * @param word - current word being processed
     * @param start - position of the separator. We should
     *              search for the verbs after this position
     * @return true if it's definitely a new sub-sentence,
     *         false otherwise
     */
    private static boolean surelyNewSentence(
            final String[] words,
            final Tense tense,
            final String word,
            final int start) {

        return isQuestionWord(word)
                || isSubSentencePhrase(words, start)
                || word.equals("but")
                || tense.getLevel1() == null
                || hasComma(words[start])
                || (isSentenceConjunction(word)
                && start + 1 < words.length
                && isVerb(words[start + 1]));
    }

    /**
     * Sentences are usually formed of several smaller
     * sentences (sub-sentences), which are separated
     * with commas, conjunctions or interrogatives.
     * Sometimes, we have quotes or brackets in the
     * middle od the sentence that contain a whole new
     * sentence. This method checks if the word is one
     * of the possible sentence separators.
     * @param words - words in the sentence
     * @param position - position of the word which is
     *                 currently being processed
     * @return true if the currently processed word is
     *         a sub-sentence case, false otherwise
     */
    private static boolean isSubSentenceCase(
            final String[] words,
            final int position) {

        return (hasComma(words[position]))
                || isQuestionWord(words[position])
                || isQuote(words[position])
                || words[position].equals("&")
                || isConjunction(words[position])
                || isSubSentencePhrase(words, position);
    }

    /**
     * The method checks if the word contains any quotes
     * either on its start or on its end.
     * @param word - word to be checked
     * @return true if the word contains quotes, false
     *         otherwise
     */
    private static boolean isQuote(final String word) {
        return isQuoteStart(word) || isQuoteEnd(word);
    }

    /**
     * The method checks if the word has opening quotes
     * on its start.
     * @param word - word to be checked
     * @return true if the word contains opening quotes,
     *         false otherwise
     */
    private static boolean isQuoteStart(final String word) {
        return word.charAt(0) == '\"'
                || word.charAt(0) == '\'';
    }

    /**
     * The method checks if the word has closing quotes
     * on its end.
     * @param word - word to be checked
     * @return true if the word contains closing quotes,
     *         false otherwise
     */
    private static boolean isQuoteEnd(final String word) {
        return word.charAt(word.length() - 1) == '\"'
                || word.charAt(word.length() - 1) == '\'';
    }

    /**
     * In same cases, words that separate the sub-sentences
     * are also included in one of two sentences. If the
     * word contains an interrogative or it starts with a
     * quote, it's included in the next sentence. This method
     * checks this case.
     * @param word - the word to be checked
     * @return true if the word is an interrogative or starts
     *         with a quote and therefore is included in the
     *         next sentence, false otherwise
     */
    public static boolean includedInNext(final String word) {
        return isQuestionWord(word) || isQuoteStart(word);
    }

    /**
     * In same cases, words that separate the sub-sentences
     * are also included in one of two sentences. If the
     * word contains a comma on its end, it's included in the
     * previous sentence. This method checks this case.
     * @param word - word to be checked
     * @return true if the word has comma and therefore is
     *         included in the previous sentence, false
     *         otherwise
     */
    public static boolean includedInPrevious(final String word) {
        return hasComma(word);
    }

    /**
     * The method checks if the word has a comma on its
     * end.
     * @param word - word to be checked
     * @return true if the word contains a comma, false
     *         otherwise
     */
    private static boolean hasComma(final String word) {
        return word.length() > 1
                && word.charAt(word.length() - 1) == ',';
    }

}
