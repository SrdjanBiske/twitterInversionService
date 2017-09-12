package dm.data;

import java.util.HashMap;

/**
 * Used for storing modal verbs and other verbs
 * that are used for building complex verb forms.
 * We use HashMap and HashSet since the amount
 * of data is not too huge and we want to increase
 * the performance as much as possible by avoiding
 * iteration costs through lists and arrays.
 */
public final class Modals {

    /**
     * HashMap for storing positive forms of modal verbs
     * in all tenses. Key is a positive form and the value
     * is a negative form of the verb in the same tense.
     */
    private static HashMap<String, String> positiveModalVerbs;

    /**
     * HashMap for storing negative forms of modal verbs
     * in all tenses. Key is a negative form and the value
     * is a positive form of the verb in the same tense.
     */
    private static HashMap<String, String> negativeModalVerbs;

    /**
     * HashMap for storing positive forms of the verb "to do"
     * in all tenses. Key is a positive form and the value
     * is a negative form of the verb in the same tense.
     */
    private static HashMap<String, String> positiveDo;

    /**
     * HashMap for storing negative forms of the verb "to do"
     * in all tenses. Key is a negative form and the value
     * is a positive form of the verb in the same tense.
     */
    private static HashMap<String, String> negativeDo;

    /**
     * HashMap for storing positive forms of the verb "to be"
     * in all tenses. Key is a positive form and the value
     * is a negative form of the verb in the same tense.
     */
    private static HashMap<String, String> positiveBe;

    /**
     * HashMap for storing negative forms of the verb "to be"
     * in all tenses. Key is a negative form and the value
     * is a positive form of the verb in the same tense.
     */
    private static HashMap<String, String> negativeBe;

    /**
     * HashMap for storing positive forms of the verb "to have"
     * in all tenses. Key is a positive form and the value
     * is a negative form of the verb in the same tense.
     */
    private static HashMap<String, String> positiveHave;

    /**
     * HashMap for storing negative forms of the verb "to have"
     * in all tenses. Key is a negative form and the value
     * is a positive form of the verb in the same tense.
     */
    private static HashMap<String, String> negativeHave;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Modals() {
    }

    /**
     * Stores the positive modal verbs in a HashMap.
     */
    private static void setPositiveModalVerbs() {
        positiveModalVerbs = new HashMap<>();
        positiveModalVerbs.put("will", "won't");
        positiveModalVerbs.put("would", "wouldn't");
        positiveModalVerbs.put("can", "can't");
        positiveModalVerbs.put("could", "couldn't");
        positiveModalVerbs.put("must", "must not");
        positiveModalVerbs.put("should", "shouldn't");
        positiveModalVerbs.put("shall", "shall not");
        positiveModalVerbs.put("ought", "ought not");
        positiveModalVerbs.put("may", "may not");
        positiveModalVerbs.put("might", "might not");
    }

    /**
     * Stores the negative modal verbs in a HashMap.
     */
    private static void setNegativeModalVerbs() {
        negativeModalVerbs = new HashMap<>();
        negativeModalVerbs.put("can't", "can");
        negativeModalVerbs.put("couldn't", "could");
        negativeModalVerbs.put("shouldn't", "should");
        negativeModalVerbs.put("won't", "will");
        positiveModalVerbs.put("cannot", "can");
        negativeModalVerbs.put("wouldn't", "would");
    }

    /**
     * Stores the positive forms of the verb "to do"
     * in a HashMap.
     */
    private static void setPositiveDo() {
        positiveDo = new HashMap<>();
        positiveDo.put("do", "don't");
        positiveDo.put("did", "didn't");
        positiveDo.put("does", "doesn't");
        positiveDo.put("done", "not done");
    }

    /**
     * Stores the negative forms of the verb "to do"
     * in a HashMap.
     */
    private static void setNegativeDo() {
        negativeDo = new HashMap<>();
        negativeDo.put("don't", "do");
        negativeDo.put("didn't", "did");
        negativeDo.put("doesn't", "does");
    }

    /**
     * Stores the positive forms of the verb "to be"
     * in a HashMap.
     */
    private static void setPositiveBe() {
        positiveBe = new HashMap<>();
        positiveBe.put("be", "not be");
        positiveBe.put("am", "am not");
        positiveBe.put("is", "isn't");
        positiveBe.put("are", "aren't");
        positiveBe.put("was", "wasn't");
        positiveBe.put("were", "weren't");
    }

    /**
     * Stores the negative forms of the verb "to be"
     * in a HashMap.
     */
    private static void setNegativeBe() {
        negativeBe = new HashMap<>();
        negativeBe.put("isn't", "is");
        negativeBe.put("aren't", "are");
        negativeBe.put("wasn't", "was");
        negativeBe.put("weren't", "were");
    }

    /**
     * Stores the positive forms of the verb "to have"
     * in a HashMap.
     */
    private static void setPositiveHave() {
        positiveHave = new HashMap<>();
        positiveHave.put("have", "haven't");
        positiveHave.put("has", "hasn't");
        positiveHave.put("had", "hadn't");
    }

    /**
     * Stores the negative forms of the verb "to have"
     * in a HashMap.
     */
    private static void setNegativeHave() {
        negativeHave = new HashMap<>();
        negativeHave.put("haven't", "have");
        negativeHave.put("hasn't", "has");
        negativeHave.put("hadn't", "had");
    }

    /**
     * Gives back the positive modal verbs HashMap. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the positive modal verbs HashMap
     */
    public static HashMap<String, String> getPositiveModalVerbs() {
        if (positiveModalVerbs == null) {
            setPositiveModalVerbs();
        }
        return positiveModalVerbs;
    }

    /**
     * Gives back the negative modal verbs HashMap. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the negative modal verbs HashMap
     */
    public static HashMap<String, String> getNegativeModalVerbs() {
        if (negativeModalVerbs == null) {
            setNegativeModalVerbs();
        }
        return negativeModalVerbs;
    }

    /**
     * Gives back the positive forms of the verb "to
     * do" HashMap. If it's not set at the moment of
     * the method call, it will be set and then returned.
     * @return the positive forms of the verb "to do"
     *         HashMap
     */
    public static HashMap<String, String> getPositiveDo() {
        if (positiveDo == null) {
            setPositiveDo();
        }
        return positiveDo;
    }

    /**
     * Gives back the negative forms of the verb "to
     * do" HashMap. If it's not set at the moment of
     * the method call, it will be set and then returned.
     * @return the negative forms of the verb "to do"
     *         HashMap
     */
    public static HashMap<String, String> getNegativeDo() {
        if (negativeDo == null) {
            setNegativeDo();
        }
        return negativeDo;
    }

    /**
     * Gives back the positive forms of the verb "to
     * be" HashMap. If it's not set at the moment of
     * the method call, it will be set and then returned.
     * @return the positive forms of the verb "to be"
     *         HashMap
     */
    public static HashMap<String, String> getPositiveBe() {
        if (positiveBe == null) {
            setPositiveBe();
        }
        return positiveBe;
    }

    /**
     * Gives back the negative forms of the verb "to
     * be" HashMap. If it's not set at the moment of
     * the method call, it will be set and then returned.
     * @return the negative forms of the verb "to be"
     *         HashMap
     */
    public static HashMap<String, String> getNegativeBe() {
        if (negativeBe == null) {
            setNegativeBe();
        }
        return negativeBe;
    }

    /**
     * Gives back the positive forms of the verb "to
     * have" HashMap. If it's not set at the moment of
     * the method call, it will be set and then returned.
     * @return the positive forms of the verb "to have"
     *         HashMap
     */
    public static HashMap<String, String> getPositiveHave() {
        if (positiveHave == null) {
            setPositiveHave();
        }
        return positiveHave;
    }

    /**
     * Gives back the negative forms of the verb "to
     * have" HashMap. If it's not set at the moment of
     * the method call, it will be set and then returned.
     * @return the negative forms of the verb "to have"
     *         HashMap
     */
    public static HashMap<String, String> getNegativeHave() {
        if (negativeHave == null) {
            setNegativeHave();
        }
        return negativeHave;
    }

}
