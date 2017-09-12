package dm.data;

import java.util.HashSet;

/**
 * Used for storing conjunction words.
 * We use HashSet since the amount of data is
 * not too huge and we want to increase the
 * performance as much as possible by avoiding
 * iteration costs through lists and arrays.
 */
public final class Conjunctions {

    /**
     * Set for storing only sentence conjunction words
     * (and, or, nor, but).
     */
    private static HashSet<String> sentenceConjunctions;

    /**
     * Set for storing other conjunction words.
     */
    private static HashSet<String> otherConjunctions;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Conjunctions() {
    }

    /**
     * Stores the sentence conjunction words in a HashSet.
     */
    private static void setSentenceConjunctions() {
        sentenceConjunctions = new HashSet<>();
        sentenceConjunctions.add("and");
        sentenceConjunctions.add("or");
        sentenceConjunctions.add("but");
        sentenceConjunctions.add("nor");
    }

    /**
     * Stores the other conjunction words in a HashSet.
     */
    private static void setOtherConjunctions() {
        otherConjunctions = new HashSet<>();
        otherConjunctions.add("yet");
        otherConjunctions.add("so");
        otherConjunctions.add("for");
        otherConjunctions.add("off");
    }

    /**
     * Gives back sentence-conjunction-words HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the sentence-conjunction-words HashSet
     */
    public static HashSet<String> getSentenceConjunctions() {
        if (sentenceConjunctions == null) {
            setSentenceConjunctions();
        }
        return sentenceConjunctions;
    }

    /**
     * Gives back other-conjunction-words HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the all-conjunction-words HashSet
     */
    public static HashSet<String> getOtherConjunctions() {
        if (otherConjunctions == null) {
            setOtherConjunctions();
        }
        return otherConjunctions;
    }

}
