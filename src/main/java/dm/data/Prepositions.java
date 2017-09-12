package dm.data;

import java.util.HashSet;

/**
 * Used for storing prepositions.
 * We use HashSet since the amount of data is
 * not too huge and we want to increase the
 * performance as much as possible by avoiding
 * iteration costs through lists and arrays.
 */
public final class Prepositions {

    /**
     * Set for prepositions.
     */
    private static HashSet<String> prepositions;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Prepositions() {
    }

    /**
     * Reads the prepositions from the file and
     * stores them in a HashSet.
     */
    private static void setPrepositions() {
        prepositions = new HashSet<>();
        prepositions.add("in");
        prepositions.add("no");
        prepositions.add("on");
        prepositions.add("at");
        prepositions.add("from");
        prepositions.add("of");
    }

    /**
     * Gives back the prepositions HashSet.
     * If it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the prepositions HashSet
     */
    public static HashSet<String> getPrepositions() {
        if (prepositions == null) {
            setPrepositions();
        }
        return prepositions;
    }

}
