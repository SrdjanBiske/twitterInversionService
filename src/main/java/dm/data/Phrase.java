package dm.data;

import java.util.HashSet;

/**
 * Data structures for storing
 * well-known phrases.
 */
public final class Phrase {

    /**
     * Set for storing skip phrases.
     * For these set, we have to check current
     * and next position.
     */
    private static HashSet<String> currentNextSkipPhrases;

    /**
     * Set for storing skip phrases.
     * For these set, we have to check previous
     * and current position.
     */
    private static HashSet<String> previousCurrentSkipPhrases;

    /**
     * Set for storing skip phrases.
     * For these set, we have to check before previous
     * and previous position.
     */
    private static HashSet<String> beforePreviousPreviousSkipPhrases;

    /**
     * Set for sub-sentence phrases.
     */
    private static HashSet<String> subSentencePhrases;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Phrase() {
    }

    /**
     * Stores the current-next skip phrases.
     */
    private static void setCurrentNextSkipPhrases() {
        currentNextSkipPhrases = new HashSet<>();
        currentNextSkipPhrases.add("last year");
        currentNextSkipPhrases.add("last night");
        currentNextSkipPhrases.add("deal on");
        currentNextSkipPhrases.add("deal that");
        currentNextSkipPhrases.add("close to");
        currentNextSkipPhrases.add("charge for");
        currentNextSkipPhrases.add("is that");
        currentNextSkipPhrases.add("post editorial");
        currentNextSkipPhrases.add("support for");
        currentNextSkipPhrases.add("made in");
    }

    /**
     * Stores the previous-current skip phrases.
     */
    private static void setPreviousCurrentSkipPhrases() {
        previousCurrentSkipPhrases = new HashSet<>();
        previousCurrentSkipPhrases.add("for fear");
        previousCurrentSkipPhrases.add("- called");
        previousCurrentSkipPhrases.add("here is");
        previousCurrentSkipPhrases.add("here are");
    }

    /**
     * Stores the before-previous-previous skip phrases.
     */
    private static void setBeforePreviousPreviousSkipPhrases() {
        beforePreviousPreviousSkipPhrases = new HashSet<>();
        beforePreviousPreviousSkipPhrases.add("according to");
    }

    /**
     * Stores the sub-sentence phrases.
     */
    private static void setSubSentencePhrases() {
        subSentencePhrases = new HashSet<>();
        subSentencePhrases.add("is that");
    }

    /**
     * Gives back the current-next skip phrase HashSet.
     * If it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the current-next skip phrase HashSet
     */
    public static HashSet<String> getCurrentNextSkipPhrases() {
        if (currentNextSkipPhrases == null) {
            setCurrentNextSkipPhrases();
        }
        return currentNextSkipPhrases;
    }

    /**
     * Gives back the previous-current skip phrase HashSet.
     * If it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the previous-current skip phrase HashSet
     */
    public static HashSet<String> getPreviousCurrentSkipPhrases() {
        if (previousCurrentSkipPhrases == null) {
            setPreviousCurrentSkipPhrases();
        }
        return previousCurrentSkipPhrases;
    }

    /**
     * Gives back the before-previous-previous skip
     * phrase HashSet. If it's not set at the moment
     * of the method call, it will be set and then
     * returned.
     * @return the before-previous-previous skip
     *         phrase HashSet
     */
    public static HashSet<String> getBeforePreviousPreviousSkipPhrases() {
        if (beforePreviousPreviousSkipPhrases == null) {
            setBeforePreviousPreviousSkipPhrases();
        }
        return beforePreviousPreviousSkipPhrases;
    }

    /**
     * Gives back the sub-sentence phrase HashSet.
     * If it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the sub-sentence phrase HashSet
     */
    public static HashSet<String> getSubSentencePhrases() {
        if (subSentencePhrases == null) {
            setSubSentencePhrases();
        }
        return subSentencePhrases;
    }

}
