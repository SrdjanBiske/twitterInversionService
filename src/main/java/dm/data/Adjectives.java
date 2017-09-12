package dm.data;

import java.util.HashSet;

import static dm.data.DataUtil.readFileToStringArray;

/**
 * Used for storing adjectives. We only store
 * the adjectives which cannot simultaneously
 * be an adverb, since that would lead to
 * confusion.
 * We use HashSet since the amount of data is
 * not too huge and we want to increase the
 * performance as much as possible by avoiding
 * iteration costs through lists and arrays.
 */
public final class Adjectives {

    /**
     * Set for adjectives.
     */
    private static HashSet<String> adjectives;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Adjectives() {
    }

    /**
     * Reads the adjectives from the file and
     * stores them in a HashSet.
     */
    private static void setAdjectives() {
        adjectives = new HashSet<>();
        String[] words = readFileToStringArray(
                "Adjectives.txt");
        assert words != null;
        for (String word : words) {
            adjectives.add(word.toLowerCase());
        }
    }

    /**
     * Gives back the adjectives HashSet.
     * If it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the adjectives HashSet
     */
    public static HashSet<String> getAdjectives() {
        if (adjectives == null) {
            setAdjectives();
        }
        return adjectives;
    }
}
