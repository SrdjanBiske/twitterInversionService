package dm.data;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Used for storing pronouns.
 * We use HashSet since the amount of data is
 * not too huge and we want to increase the
 * performance as much as possible by avoiding
 * iteration costs through lists and arrays.
 */
public final class Pronouns {

    /**
     * Set for storing basic pronouns.
     */
    private static HashSet<String> basicPronouns;

    /**
     * Set for storing possessive pronouns.
     */
    private static HashSet<String> possessivePronouns;

    /**
     * Set for storing all other pronouns.
     */
    private static HashSet<String> otherPronouns;

    /**
     * Set for storing singular pronouns.
     */
    private static HashSet<String> singularPronouns;

    /**
     * Set for storing plural pronouns.
     */
    private static HashSet<String> pluralPronouns;

    /**
     * HashMap for storing indefinite pronouns.
     */
    private static HashMap<String, String> indefinitePronouns;

    /**
     * HashMap for storing negative indefinite pronouns.
     */
    private static HashMap<String, String> negIndefinitePronouns;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Pronouns() {
    }

    /**
     * Stores the singular pronouns in a HashSet.
     */
    private static void setSingularPronouns() {
        singularPronouns = new HashSet<>();
        singularPronouns.add("he");
        singularPronouns.add("she");
        singularPronouns.add("it");
        singularPronouns.add("there");
        singularPronouns.add("that");
        singularPronouns.add("here");
    }

    /**
     * Stores the plural pronouns in a HashSet.
     */
    private static void setPluralPronouns() {
        pluralPronouns = new HashSet<>();
        pluralPronouns.add("i");
        pluralPronouns.add("you");
        pluralPronouns.add("we");
        pluralPronouns.add("they");
    }

    /**
     * Stores the basic pronouns in a HashSet.
     */
    private static void setBasicPronouns() {
        basicPronouns = new HashSet<>();

        if (singularPronouns == null) {
            setSingularPronouns();
        }

        if (pluralPronouns == null) {
            setPluralPronouns();
        }

        basicPronouns.addAll(singularPronouns);
        basicPronouns.addAll(pluralPronouns);
    }

    /**
     * Stores the possessive pronouns in a HashSet.
     */
    private static void setPossessivePronouns() {
        possessivePronouns = new HashSet<>();
        possessivePronouns.add("my");
        possessivePronouns.add("his");
        possessivePronouns.add("her");
        possessivePronouns.add("its");
        possessivePronouns.add("their");
        possessivePronouns.add("theirs");
        possessivePronouns.add("mine");
        possessivePronouns.add("your");
        possessivePronouns.add("yours");
        possessivePronouns.add("hers");
        possessivePronouns.add("our");
        possessivePronouns.add("ours");
    }

    /**
     * Stores the other pronouns in a HashSet.
     */
    private static void setOtherPronouns() {
        otherPronouns = new HashSet<>();
        otherPronouns.add("me");
        otherPronouns.add("them");
        otherPronouns.add("him");
        otherPronouns.add("us");
    }

    /**
     * Stores the indefinite pronouns in a HashMap.
     */
    private static void setIndefinitePronouns() {
        indefinitePronouns = new HashMap<>();
        indefinitePronouns.put("everybody", "nobody");
        indefinitePronouns.put("everything", "nothing");
        indefinitePronouns.put("everyone", "no one");
        indefinitePronouns.put("everywhere", "nowhere");
    }

    /**
     * Stores the negative indefinite pronouns in a HashMap.
     */
    private static void setNegIndefinitePronouns() {
        negIndefinitePronouns = new HashMap<>();
        negIndefinitePronouns.put("nothing", "something");
        negIndefinitePronouns.put("nobody", "somebody");
        negIndefinitePronouns.put("nowhere", "somewhere");
        negIndefinitePronouns.put("none", "some");
    }

    /**
     * Gives back singular pronouns HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the singular pronouns HashSet
     */
    public static HashSet<String> getSingularPronouns() {
        if (singularPronouns == null) {
            setSingularPronouns();
        }
        return singularPronouns;
    }

    /**
     * Gives back plural pronouns HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the plural pronouns HashSet
     */
    public static HashSet<String> getPluralPronouns() {
        if (pluralPronouns == null) {
            setPluralPronouns();
        }
        return pluralPronouns;
    }

    /**
     * Gives back basic pronouns HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the basic pronouns HashSet
     */
    public static HashSet<String> getBasicPronouns() {
        if (basicPronouns == null) {
            setBasicPronouns();
        }
        return basicPronouns;
    }

    /**
     * Gives back possessive pronouns HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the possessive pronouns HashSet
     */
    public static HashSet<String> getPossessivePronouns() {
        if (possessivePronouns == null) {
            setPossessivePronouns();
        }
        return possessivePronouns;
    }

    /**
     * Gives back other pronouns HashSet. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the other pronouns HashSet
     */
    public static HashSet<String> getOtherPronouns() {
        if (otherPronouns == null) {
            setOtherPronouns();
        }
        return otherPronouns;
    }

    /**
     * Gives back indefinite pronouns HashMap. If
     * it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the indefinite pronouns HashMap
     */
    public static HashMap<String, String> getIndefinitePronouns() {
        if (indefinitePronouns == null) {
            setIndefinitePronouns();
        }
        return indefinitePronouns;
    }

    /**
     * Gives back negative indefinite pronouns HashMap.
     * If it's not set at the moment of the method call,
     * it will be set and then returned.
     * @return the negative indefinite pronouns HashMap
     */
    public static HashMap<String, String> getNegIndefinitePronouns() {
        if (negIndefinitePronouns == null) {
            setNegIndefinitePronouns();
        }
        return negIndefinitePronouns;
    }

}
