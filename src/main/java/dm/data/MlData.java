package dm.data;

import com.aliasi.util.Files;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import static dm.data.DataUtil.readFileToStringArray;

/**
 * Data used for training and testing of
 * machine learning.
 */
public final class MlData {

    /**
     * Set for storing words that are being
     * considered while training the Naive Bayes.
     */
    private static HashSet<String> trainingWords;

    /**
     * Set for storing words that are being
     * considered in the execution of the Naive
     * Bayes.
     */
    private static HashSet<String> testingWords;

    /**
     * Set for storing Russia keywords.
     */
    private static HashSet<String> russiaKeywords;

    /**
     * Set for words that should be ignored while
     * processing.
     */
    private static HashSet<String> ignoreWords;

    /**
     * Array that stores slash abbreviations.
     */
    private static String[] abbreviations;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private MlData() {
    }

    /**
     * Reads the training words from the file and
     * stores them in a HashSet.
     */
    private static void setTrainingWords() {
        trainingWords = new HashSet<>();
        String[] words = readFileToStringArray(
                "RelevantTraining.txt");

        assert words != null;
        for (String word : words) {
            trainingWords.add(word);
        }
    }

    /**
     * Reads the testing words from the file and
     * stores them in a HashSet.
     */
    private static void setTestingWords() {
        testingWords = new HashSet<>();
        String[] words = readFileToStringArray(
                "RelevantTesting.txt");

        assert words != null;
        for (String word : words) {
            testingWords.add(word);
        }
    }

    /**
     * Sets the Russia keywords HashSet.
     */
    private static void setRussiaKeywords() {
        russiaKeywords = new HashSet<>();
        russiaKeywords.add("russia");
        russiaKeywords.add("putin");
        russiaKeywords.add("vladimir");
        russiaKeywords.add("kremlin");
        russiaKeywords.add("medvedev");
        russiaKeywords.add("lavrov");
        russiaKeywords.add("russia");
        russiaKeywords.add("moscow");
    }

    /**
     * Sets the ignore words HashSet.
     */
    private static void setIgnoreWords() {
        ignoreWords = new HashSet<>();
        ignoreWords.add("youtub");
        ignoreWords.add("http");
        ignoreWords.add("exxon");
    }


    /**
     * Reads the slash abbreviations from the file and
     * stores them in an array.
     */
    private static void setAbbreviations() {
        try {
            File abbrev = new File("Abbreviations.txt");
            String allAbbr = Files.readFromFile(abbrev, "UTF-8");
            abbreviations = allAbbr.split("\\s+");
        } catch (IOException e) {
            abbreviations = new String[0];
        }
    }

    /**
     * Gives back the training words HashSet. If it's not
     * set at the moment of the method call, it will be
     * set and then returned.
     * @return the singular nouns HashSet
     */
    public static HashSet<String> getTrainingWords() {
        if (trainingWords == null) {
            setTrainingWords();
        }
        return trainingWords;
    }

    /**
     * Gives back the testing words HashSet. If it's not
     * set at the moment of the method call, it will be
     * set and then returned.
     * @return the plural nouns HashSet
     */
    public static HashSet<String> getTestingWords() {
        if (testingWords == null) {
            setTestingWords();
        }
        return testingWords;
    }

    /**
     * Gives back the Russia keyword HashSet. If it's not
     * set at the moment of the method call, it will be
     * set and then returned.
     * @return the Russia keyword HashSet
     */
    public static HashSet<String> getRussiaKeywords() {
        if (russiaKeywords == null) {
            setRussiaKeywords();
        }
        return russiaKeywords;
    }

    /**
     * Gives back the ignore words HashSet. If it's not
     * set at the moment of the method call, it will be
     * set and then returned.
     * @return the ignore words HashSet
     */
    public static HashSet<String> getIgnoreWords() {
        if (ignoreWords == null) {
            setIgnoreWords();
        }
        return ignoreWords;
    }

    /**
     * Gives back the slash abbreviations array. If it's not
     * set at the moment of the method call, it will be
     * set and then returned.
     * @return the slash abbreviations array
     */
    public static String[] getAbbreviations() {
        if (abbreviations == null
                || abbreviations.length == 0) {
            setAbbreviations();
        }
        return abbreviations;
    }

}
