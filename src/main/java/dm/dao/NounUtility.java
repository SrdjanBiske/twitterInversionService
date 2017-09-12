package dm.dao;

import static dm.data.Nouns.getArticles;
import static dm.data.Nouns.getFemaleNames;
import static dm.data.Nouns.getLastNames;
import static dm.data.Nouns.getMaleNames;
import static dm.data.Nouns.getPluralNouns;
import static dm.data.Nouns.getSingularNouns;

/**
 * Methods for operations on data structures
 * containing nouns.
 */
public class NounUtility extends Utility {

    /**
     * Checks if the word is a noun. This check
     * does not include people names.
     * @param word - word to be checked
     * @return true if the word is a noun, false
     *         otherwise
     */
    public static boolean isNoun(final String word) {
        String cleanCopy = clean(word);
        return getSingularNouns().contains(cleanCopy)
                || getPluralNouns().contains(cleanCopy);
    }

    /**
     * Checks if the word is a name.
     * @param word - word to be checked
     * @return true if the word is a name, false
     *         otherwise
     */
    public static boolean isName(final String word) {
        String cleanCopy = clean(word);
        return getLastNames().contains(cleanCopy)
                || getMaleNames().contains(cleanCopy)
                || getFemaleNames().contains(cleanCopy);
    }

    /**
     * Checks if the word is an article.
     * @param word - word to be checked
     * @return true if the word is an article, false
     *         otherwise
     */
    public static boolean isArticle(final String word) {
        String cleanCopy = clean(word);
        return getArticles().contains(cleanCopy);
    }

    /**
     * Checks if the word is a singular noun.
     * @param word - word to be checked
     * @return true if the word is a singular noun,
     *         false otherwise
     */
    public static boolean isSingular(final String word) {
        String cleanCopy = clean(word);
        return getSingularNouns().contains(cleanCopy);
    }

    /**
     * Checks if the word is a plural noun.
     * @param word - word to be checked
     * @return true if the word is a plural noun,
     *         false otherwise
     */
    public static boolean isPlural(final String word) {
        String cleanCopy = clean(word);
        return getPluralNouns().contains(cleanCopy);
    }

}
