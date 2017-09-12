package service.formatter;

import opennlp.tools.stemmer.PorterStemmer;
import org.apache.commons.lang.StringEscapeUtils;

import static dm.dao.MlUtility.abbreviations;
import static dm.dao.MlUtility.isAboutRussia;
import static dm.dao.MlUtility.isTestingWord;
import static dm.dao.MlUtility.isTrainingWord;
import static dm.dao.MlUtility.shouldIgnore;
import static dm.dao.VerbUtility.getInfinitive;
import static dm.dao.VerbUtility.isVerb;

/**
 * Class used for preparing posts for the
 * machine learning processing.
 */
public final class Pipe {

    /**
     * Minimal number of word in piped post.
     */
    private static final int MIN_WORD_NUM = 3;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Pipe() {
    }

    /**
     * This method takes the original text of the twitter status
     * and passes it through different kinds of filters:
     *      Step 1: If some characters are escaped within the HTML,
     *              they will be fixed.
     *      Step 2: If there's any slash abbreviations, they will be
     *              expanded to full words.
     *      Step 3: All letters will be put in lower case.
     *      Step 4: If there is hyphen ('-') connecting two words,
     *              the words will be separated and the hyphen will
     *              be deleted.
     *      Step 5: All special characters will be deleted.
     *      Step 6: All verbs are going to be shortened to their
     *              infinitive form.
     *      Step 7: If there's a word which is too common or which is
     *              irrelevant, it will be skipped.
     *      Step 8: Remaining words will be stemmed.
     * @param original - original status in text form
     * @param mode - execution mode for this method (training or testing)
     * @return filtered status, ready for training or classification
     */
    public static String pipe(final String original, final PipeMode mode) {
        String copy = original;

        // Step 1
        copy = StringEscapeUtils.unescapeHtml(copy);

        // Step 2
        copy = fixAbbreviations(copy);

        StringBuilder result = new StringBuilder();
        String[] tokens = copy.split("[\\s]+");
        for (String current : tokens) {
            String token = current;

            // Step 3
            token = token.toLowerCase();

            // Step 4
            token = token.replaceAll("-",
                    " ");

            // Step 5
            token = token.replaceAll("[^A-Za-z0-9]+",
                    "");

            // Step 6
            if (isVerb(token)) {
                token = getInfinitive(token);
            }

            // Step 7
            if (shouldIgnore(token)) {
                continue;

            } else if (mode == PipeMode.TRAINING
                    && !isTrainingWord(token)) {
                continue;

            } else if (mode == PipeMode.TESTING
                    && !isTestingWord(token)) {
                continue;
            }

            // Step 8
            token = stemWord(token);

            result.append(token + " ");
        }

        String[] finalTokens = result.toString().split("\\s+");

        if (finalTokens.length < MIN_WORD_NUM
                && !isAboutRussia(result.toString())) {

            return "";
        } else {
            return result.toString();
        }
    }

    /**
     * Replaces the slash abbreviations with their full versions.
     * @param original - post with slash abbreviations
     * @return post with no slash abbreviations
     */
    public static String fixAbbreviations(final String original) {
        String copy = original;

        for (int i = 0; i < abbreviations().length; i += 2) {
            if (copy.contains(abbreviations()[i])) {

                copy = copy.replace(abbreviations()[i],
                        abbreviations()[i + 1] + " ");
            }
        }

        return copy;
    }

    /**
     * This method takes the word and stems it.
     * @param original - original word
     * @return stemmed word
     */
    public static String stemWord(final String original) {
        PorterStemmer stemmer = new PorterStemmer();
        return stemmer.stem(original);
    }

}
