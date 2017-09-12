package dm.dao;

import java.util.HashSet;

import static dm.data.MlData.getAbbreviations;
import static dm.data.MlData.getIgnoreWords;
import static dm.data.MlData.getRussiaKeywords;
import static dm.data.MlData.getTestingWords;
import static dm.data.MlData.getTrainingWords;
import static service.formatter.Pipe.stemWord;

/**
 * Methods for operations on data structures
 * containing machine learning data.
 */
public class MlUtility extends Utility {

    /**
     * Gives back the abbreviations array.
     * @return the abbreviations array
     */
    public static String[] abbreviations() {
        return getAbbreviations();
    }

    /**
     * This method  is checking if a String is about Russia
     * by looking for any relevant words within the String.
     *
     * @param token - word that is being examined
     * @return true if the word contains any term related to
     *         Russia, false otherwise
     */
    public static boolean isAboutRussia(final String token) {
        String copy = clean(token);
        HashSet<String> russiaKeywords = getRussiaKeywords();
        for (String keyword : russiaKeywords) {
            if (copy.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method  is checking if a String should be ignored
     * because it contains one of the ignore words.
     *
     * @param token - word that is being examined
     * @return true if the word should be ignore, false
     *         otherwise
     */
    public static boolean shouldIgnore(final String token) {
        String copy = clean(token);
        HashSet<String> ignoreWords = getIgnoreWords();
        for (String word : ignoreWords) {
            if (copy.contains(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the word belongs to the testing words list.
     * @param word - word to be checked
     * @return true if the word belongs to the testing words
     *         list, false otherwise
     */
    public static boolean isTestingWord(final String word) {
        return getTestingWords().contains(word)
                || getTestingWords().contains(stemWord(word));
    }

    /**
     * Checks if the word belongs to the training words list.
     * @param word - word to be checked
     * @return true if the word belongs to the training words
     *         list, false otherwise
     */
    public static boolean isTrainingWord(final String word) {
        return getTrainingWords().contains(word)
                || getTrainingWords().contains(stemWord(word));
    }

}
