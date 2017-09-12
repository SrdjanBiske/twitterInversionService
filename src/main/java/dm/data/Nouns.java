package dm.data;

import java.util.HashSet;

import static dm.data.DataUtil.readFileToStringArray;

/**
 * Used for storing singular and plural nouns.
 * We use HashSet since the amount of data is
 * not too huge and we want to increase the
 * performance as much as possible by avoiding
 * iteration costs through lists and arrays.
 */
public final class Nouns {

    /**
     * Set for storing nouns in singular form.
     */
    private static HashSet<String> singularNouns;

    /**
     * Set for storing nouns in plural form.
     * Due the large number of nouns, we used an
     * algorithm for automatically turning the
     * singular to plural, meaning that not all
     * plural nouns are necessarily correct.
     */
    private static HashSet<String> pluralNouns;

    /**
     * Set for storing English male first names.
     */
    private static HashSet<String> maleNames;

    /**
     * Set for storing English female first names.
     */
    private static HashSet<String> femaleNames;

    /**
     * Set for storing English last names.
     */
    private static HashSet<String> lastNames;

    /**
     * Set for storing articles.
     */
    private static HashSet<String> articles;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Nouns() {
    }

    /**
     * Reads the singular nouns from the file and
     * stores them in a HashSet.
     */
    private static void setSingularNouns() {
        singularNouns = new HashSet<>();
        String[] words = readFileToStringArray("SingularNouns.txt");
        assert words != null;
        for (String word : words) {
            singularNouns.add(word);
        }
    }

    /**
     * Reads the plural nouns from the file and
     * stores them in a HashSet.
     */
    private static void setPluralNouns() {
        pluralNouns = new HashSet<>();
        String[] words = readFileToStringArray("PluralNouns.txt");
        assert words != null;
        for (String word : words) {
            pluralNouns.add(word);
        }
    }

    /**
     * Reads the English male first names from the file and
     * stores them in a HashSet.
     */
    private static void setMaleNames() {
        maleNames = new HashSet<>();
        String[] words = readFileToStringArray("MaleNames.txt");
        assert words != null;
        for (String word : words) {
            maleNames.add(word.toLowerCase());
        }
    }

    /**
     * Reads the English female first names from the file and
     * stores them in a HashSet.
     */
    private static void setFemaleNames() {
        femaleNames = new HashSet<>();
        String[] words = readFileToStringArray("FemaleNames.txt");
        assert words != null;
        for (String word : words) {
            femaleNames.add(word.toLowerCase());
        }
    }

    /**
     * Reads the English last names from the file and
     * stores them in a HashSet.
     */
    private static void setLastNames() {
        lastNames = new HashSet<>();
        String[] words = readFileToStringArray("LastNames.txt");
        assert words != null;
        for (String word : words) {
            lastNames.add(word.toLowerCase());
        }
    }

    /**
     * Stores the sentence articles in a HashSet.
     */
    private static void setArticles() {
        articles = new HashSet<>();
        articles.add("a");
        articles.add("an");
        articles.add("the");
    }

    /**
     * Gives back the singular nouns HashSet. If it's not
     * set at the moment of the method call, it will be
     * set and then returned.
     * @return the singular nouns HashSet
     */
    public static HashSet<String> getSingularNouns() {
        if (singularNouns == null) {
            setSingularNouns();
        }
        return singularNouns;
    }

    /**
     * Gives back the plural nouns HashSet. If it's not
     * set at the moment of the method call, it will be
     * set and then returned.
     * @return the plural nouns HashSet
     */
    public static HashSet<String> getPluralNouns() {
        if (pluralNouns == null) {
            setPluralNouns();
        }
        return pluralNouns;
    }

    /**
     * Gives back the English male first names HashSet.
     * If it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the English male first names HashSet
     */
    public static HashSet<String> getMaleNames() {
        if (maleNames == null) {
            setMaleNames();
        }
        return maleNames;
    }

    /**
     * Gives back the English female first names HashSet.
     * If it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the English female first names HashSet
     */
    public static HashSet<String> getFemaleNames() {
        if (femaleNames == null) {
            setFemaleNames();
        }
        return femaleNames;
    }

    /**
     * Gives back the English last names HashSet.
     * If it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the English last names HashSet
     */
    public static HashSet<String> getLastNames() {
        if (lastNames == null) {
            setLastNames();
        }
        return lastNames;
    }

    /**
     * Gives back the articles HashSet.
     * If it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the articles HashSet
     */
    public static HashSet<String> getArticles() {
        if (articles == null) {
            setArticles();
        }
        return articles;
    }
}
