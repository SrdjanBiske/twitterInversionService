package dm.data;

import java.util.HashMap;
import java.util.HashSet;

import static dm.data.DataUtil.readFileToStringArray;

/**
 * Used for storing verbs.
 * We use HashMap and HashSet since the amount
 * of data is not too huge and we want to increase
 * the performance as much as possible by avoiding
 * iteration costs through lists and arrays.
 */
public final class Verbs {

    /**
     * Number of possible verb forms: Infinitive,
     * Perfect, Past Participle, Third Person and
     * Gerund.
     */
    private static final int NUM_OF_VERB_FORMS = 5;

    /**
     * Mapping for infinitive.
     */
    private static final int INFINITIVE_POS = 0;

    /**
     * Mapping for perfect.
     */
    private static final int PERFECT_POS = 1;

    /**
     * Mapping for past participle.
     */
    private static final int PARTICIPLE_POS = 2;

    /**
     * Mapping for third person.
     */
    private static final int THIRD_PERS_POS = 3;

    /**
     * Mapping for gerund.
     */
    private static final int GERUND_POS = 4;

    /**
     * HashMap for storing all non-modal verbs.
     * Key is a form of the verb and the value is
     * its infinitive form.
     */
    private static HashMap<String, String> verbs;

    /**
     * HashMap that maps the infinitive (key) form
     * of the verb to its third person form (value).
     */
    private static HashMap<String, String> infinitiveThirdPersonMapping;

    /**
     * HashMap that maps the infinitive (key) form
     * of the verb to its perfect form (value).
     */
    private static HashMap<String, String> infinitivePerfectMapping;

    /**
     * HashMap for storing verb tenses. Key is a form
     * of the verb and the value is it's tense
     * with the following mapping: 0 - Infinitive,
     * 1 - Perfect, 2 - Past Participle, 3 - Third
     * Person, 4 - Gerund.
     */
    private static HashMap<String, Integer> verbTenses;

    /**
     * There are some verbs that contain two words
     * where one of them is usually a noun. For this
     * reason, we have to store such verbs in order to
     * not recognize them as false positives in later
     * checks. Key is a whole verb phrase (i.e. trash
     * talk), and the value is the noun part of the verb.
     */
    private static HashMap<String, String> twoPartVerbs;

    /**
     * Mapping for all forms of the verb "start" to the
     * same tense of the verb "stop" and vice versa.
     */
    private static HashMap<String, String> startStopMapping;

    /**
     * Set for storing verbs which are usually
     * used for starting a new sub-sentence within the
     * original one.
     */
    private static HashSet<String> newSentenceVerbs;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Verbs() {
    }

    /**
     * A method that sets four HashMaps all at once
     * in order to avoid the costs of repetitive
     * reading of the same file. It reads the Verbs.txt
     * and sets the verbTenses, infinitivePerfectMapping,
     * verbs and infinitiveThirdPersonMapping HashMaps.
     * There are verbs that have the same form in multiple
     * tenses, which is a problem if that form is a key
     * for a HashMap. These conflicts are solved by
     * putting the tense number in the String after the
     * verb (i.e. perfect of "let" is also "let" and it
     * will be stored as "let1").
     */
    public static void setVerbs() {

        verbs = new HashMap<>();
        verbTenses = new HashMap<>();
        infinitiveThirdPersonMapping = new HashMap<>();
        infinitivePerfectMapping = new HashMap<>();

        String[] words = readFileToStringArray("Verbs.txt");
        int size = words.length;

        for (int pos = 0; pos < size; ++pos) {

            if (pos % NUM_OF_VERB_FORMS == THIRD_PERS_POS) {
                infinitiveThirdPersonMapping.
                        put(words[pos - THIRD_PERS_POS], words[pos]);
            }

            if (pos % NUM_OF_VERB_FORMS == PERFECT_POS) {
                infinitivePerfectMapping.
                        put(words[pos - PERFECT_POS], words[pos]);
            }

            String word = words[pos];
            if (verbs.containsKey(words[pos])) {
                word = words[pos] + (pos % NUM_OF_VERB_FORMS);
            }

            switch (pos % NUM_OF_VERB_FORMS) {
                case INFINITIVE_POS:
                    verbs.put(word, words[pos]);
                    break;
                case PERFECT_POS:
                    verbs.put(word, words[pos - PERFECT_POS]);
                    break;
                case PARTICIPLE_POS:
                    verbs.put(word, words[pos - PARTICIPLE_POS]);
                    break;
                case THIRD_PERS_POS:
                    verbs.put(word, words[pos - THIRD_PERS_POS]);
                    break;
                default:
                    verbs.put(word, words[pos - GERUND_POS]);
                    break;
            }

            verbTenses.put(word, pos % NUM_OF_VERB_FORMS);
        }

        infinitiveThirdPersonMapping.put("be", "is");
    }

    /**
     * Stores the two-part-verbs in a HashMap.
     */
    private static void setTwoPartVerbs() {
        twoPartVerbs = new HashMap<>();
        twoPartVerbs.put("trash talk", "trash");
        twoPartVerbs.put("heap praise", "heap");
    }

    /**
     * Stores the start-stop mappings in a HashMap.
     */
    private static void setStartStopMapping() {
        startStopMapping = new HashMap<>();
        startStopMapping.put("start", "stop");
        startStopMapping.put("started", "stopped");
        startStopMapping.put("starts", "stops");
        startStopMapping.put("starting", "stopping");
        startStopMapping.put("stopping", "starting");
        startStopMapping.put("stop", "start");
        startStopMapping.put("stopped", "started");
        startStopMapping.put("stops", "starts");
    }

    /**
     * Reads the new-sub-sentence verbs from a file
     * and stores them in a HashSet.
     */
    private static void setShouldNegateVerbs() {
        newSentenceVerbs = new HashSet<>();
        String[] words = readFileToStringArray("NewSubSentenceVerbs.txt");
        assert words != null;
        for (String word : words) {
            newSentenceVerbs.add(word);
        }
    }

    /**
     * Gives back the all-verbs HashMap. If it's not
     * set at the moment of the method call, it will be
     * set and then returned.
     * @return the all-verbs HashMap
     */
    public static HashMap<String, String> getVerbs() {
        if (verbs == null) {
            setVerbs();
        }
        return verbs;
    }

    /**
     * Gives back the infinitive-third person mapping
     * HashMap. If it's not set at the moment of the
     * method call, it will be set and then returned.
     * @return the infinitive-third person mapping
     *         HashMap
     */
    public static HashMap<String, String> getInfinitiveThirdPersonMapping() {
        if (infinitiveThirdPersonMapping == null) {
            setVerbs();
        }
        return infinitiveThirdPersonMapping;
    }

    /**
     * Gives back the infinitive-perfect mapping
     * HashMap. If it's not set at the moment of the
     * method call, it will be set and then returned.
     * @return the infinitive-perfect mapping HashMap
     */
    public static HashMap<String, String> getInfinitivePerfectMapping() {
        if (infinitivePerfectMapping == null) {
            setVerbs();
        }
        return infinitivePerfectMapping;
    }

    /**
     * Gives back the verb-tenses HashMap. If it's not
     * set at the moment of the method call, it will be
     * set and then returned.
     * @return the verb-tenses HashMap
     */
    public static HashMap<String, Integer> getVerbTenses() {
        if (verbTenses == null) {
            setVerbs();
        }
        return verbTenses;
    }

    /**
     * Gives back the two-part-verbs HashMap. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the two-part-verbs HashMap
     */
    public static HashMap<String, String> getTwoPartVerbs() {
        if (twoPartVerbs == null) {
            setTwoPartVerbs();
        }
        return twoPartVerbs;
    }

    /**
     * Gives back the start-stop-mapping HashMap. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the start-stop-mapping HashMap
     */
    public static HashMap<String, String> getStartStopMapping() {
        if (startStopMapping == null) {
            setStartStopMapping();
        }
        return startStopMapping;
    }

    /**
     * Gives back the new-sentence-verbs HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the new-sentence-verbs HashSet
     */
    public static HashSet<String> getNewSentenceVerbs() {
        if (newSentenceVerbs == null) {
            setShouldNegateVerbs();
        }
        return newSentenceVerbs;
    }

}
