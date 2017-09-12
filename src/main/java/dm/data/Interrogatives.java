package dm.data;

import java.util.HashSet;

/**
 * Used for storing interrogative words.
 * We use HashSet since the amount of data is
 * not too huge and we want to increase the
 * performance as much as possible by avoiding
 * iteration costs through lists and arrays.
 */
public final class Interrogatives {

    /**
     * Set for storing skip interrogatives words.
     * Questions beginning with these verbs will
     * not be negated.
     */
    private static HashSet<String> skipInterrogatives;

    /**
     * Set for storing push interrogatives. Questions
     * beginning with these verbs will be negated by
     * pushing the negation to the main and not modal
     * verb.
     */
    private static HashSet<String> pushInterrogatives;

    /**
     * Set for storing negate interrogatives. Questions
     * beginning with these verbs will be negated in a
     * normal way, like any other sentence.
     */
    private static HashSet<String> negateInterrogatives;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Interrogatives() {
    }

    /**
     * Stores the skip-interrogative words in a HashSet.
     */
    private static void setSkipInterrogatives() {
        skipInterrogatives = new HashSet<>();
        skipInterrogatives.add("when");
        skipInterrogatives.add("where");
        skipInterrogatives.add("whither");
        skipInterrogatives.add("whence");
        skipInterrogatives.add("whether");
        skipInterrogatives.add("whatsoever");
    }

    /**
     * Stores the push-interrogative words in a HashSet.
     */
    private static void setPushInterrogatives() {
        pushInterrogatives = new HashSet<>();
        pushInterrogatives.add("what");
        pushInterrogatives.add("which");
        pushInterrogatives.add("whose");
        pushInterrogatives.add("who");
        pushInterrogatives.add("whom");
    }

    /**
     * Stores the negate-interrogative words in a HashSet.
     */
    private static void setNegateInterrogatives() {
        negateInterrogatives = new HashSet<>();
        negateInterrogatives.add("how");
        negateInterrogatives.add("why");
    }

    /**
     * Gives back skip-interrogatives HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the skip-interrogatives HashSet
     */
    public static HashSet<String> getSkipInterrogatives() {
        if (skipInterrogatives == null) {
            setSkipInterrogatives();
        }
        return skipInterrogatives;
    }

    /**
     * Gives back push-interrogatives HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the push-interrogatives HashSet
     */
    public static HashSet<String> getPushInterrogatives() {
        if (pushInterrogatives == null) {
            setPushInterrogatives();
        }
        return pushInterrogatives;
    }

    /**
     * Gives back negate-interrogatives HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the negate-interrogatives HashSet
     */
    public static HashSet<String> getNegateInterrogatives() {
        if (negateInterrogatives == null) {
            setNegateInterrogatives();
        }
        return negateInterrogatives;
    }

}
