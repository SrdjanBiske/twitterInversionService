package service.ml;

import com.aliasi.classify.BinaryLMClassifier;
import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.JointClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.util.Files;
import exception.ClassifierCompilationException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

/**
 * All methods related to machine learning.
 */
public final class MachineLearning {

    /**
     * Used for logging events that happen in this class.
     */
    private static final Logger LOGGER;

    static {
        LOGGER = LogManager.getLogger(MachineLearning.class);
    }

    /**
     * Directory for training data.
     */
    private static final File TRAINING_DIR
            = new File("src/main/resources/TrainingDataPiped");

    /**
     * Directory for testing data.
     */
    private static final File TESTING_DIR
            = new File("src/main/resources/TestDataPiped");

    /**
     * Path to the Classifier file.
     */
    private static final String PATH
            = "src/main/resources/classification/Classifier";

    /**
     * NGram size.
     */
    private static final int NGRAM_SIZE = 4;

    /**
     * Entropy threshold.
     */
    private static final double THRESHOLD = 100.0;

    /**
     * Score.
     */
    private static final double SCORE = -2.5;

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private MachineLearning() {
    }

    /**
     * This method takes the training data and executes
     * the machine learning process on that data.
     */
    public static void trainData() {
        NGramProcessLM langModel = new NGramProcessLM(NGRAM_SIZE);
        BinaryLMClassifier classifier
                = new BinaryLMClassifier(langModel, THRESHOLD);

        String[] trainingFolders = TRAINING_DIR.list();
        assert trainingFolders != null;
        for (String trainingFolder : trainingFolders) {
            File dir = new File(TRAINING_DIR, trainingFolder);
            String[] trainingFiles = dir.list();
            assert trainingFiles != null;
            for (String trainingFile : trainingFiles) {
                File file = new File(dir, trainingFile);
                String text;
                try {
                    text = Files.readFromFile(file, "ISO-8859-1");

                    Classification classification
                            = new Classification("true");
                    Classified<CharSequence> classified
                            = new Classified<>(text, classification);
                    classifier.handle(classified);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            ObjectOutputStream os = new ObjectOutputStream(
                    new FileOutputStream(PATH));
            classifier.compileTo(os);
            os.close();
        } catch (IOException e) {
            System.out.println("Error!");
        }
    }


    /**
     * Takes a single tweet and decides if it's
     * on the subject "USA - Russia relation" or
     * not.
     * @param text - text of the tweet to be checked
     * @return true if the tweet is about the subject
     *         "USA - Russia relation", false otherwise
     */
    public static boolean classifyTweet(
            final String text) {
        try {

            ObjectInputStream inputStream = new ObjectInputStream(
                    new FileInputStream(PATH));
            LMClassifier compiledClassifier
                    = (LMClassifier) inputStream.readObject();

            inputStream.close();

            String bestCategory;
            if (text.equals("")) {
                bestCategory = "false";
            } else {
                JointClassification jc = compiledClassifier.classifyJoint(
                        text.toCharArray(), 0, text.length());

                if ((jc.score(0) < SCORE)) {
                    bestCategory = "false";
                } else {
                    bestCategory = "true";
                }
            }

            return bestCategory.equals("true");

        } catch (IOException | ClassNotFoundException exception) {
            LOGGER.error("Classifier file cannot be found.");
            return false;
        }
    }

    /**
     * A method that runs an analysis of the machine learning
     * accuracy on both positive and negative posts.
     * @throws ClassifierCompilationException - if there is a
     *         problem with classifier loading
     */
    public static void classifyAllTweets()
            throws ClassifierCompilationException {

        double falseSum = (new File(TESTING_DIR, "false")).list().length;
        double trueSum = (new File(TESTING_DIR, "true")).list().length;
        double falseCounter = 0;
        double trueCounter = 0;

        try {

            ObjectInputStream inputStream = new ObjectInputStream(
                    new FileInputStream(PATH));
            LMClassifier compiledClassifier
                    = (LMClassifier) inputStream.readObject();

            inputStream.close();

            String[] trainingFolders = TESTING_DIR.list();
            assert trainingFolders != null;
            for (String trainingFolder : trainingFolders) {
                File dir = new File(TESTING_DIR, trainingFolder);
                String[] trainingFiles = dir.list();
                assert trainingFiles != null;
                for (String trainingFile : trainingFiles) {
                    File file = new File(dir, trainingFile);
                    String text;
                    try {
                        text = Files.readFromFile(file, "ISO-8859-1");
                    } catch (IOException e) {
                        throw new FileNotFoundException(
                                "Files could not be read from the directory.");
                    }

                    String bestCategory
                            = bestCategory(compiledClassifier, text);

                    if (dir.getName().equals("true")
                            && bestCategory.equals("true")) {
                        trueCounter += 1.0;

                    } else if (dir.getName().equals("false")
                            && bestCategory.equals("false")) {
                        falseCounter += 1.0;
                    }
                }
            }

            DecimalFormat df = new DecimalFormat("#.00");
            System.out.println("All posts score: " + df.format(THRESHOLD
                    * (trueCounter + falseCounter) / (trueSum + falseSum)));

        } catch (Exception e) {
            System.out.println("Couldn't finish...");
        }
    }

    /**
     * A method that runs an analysis of the machine learning
     * accuracy on both positive only.
     * @throws ClassifierCompilationException - if there is a
     *         problem with classifier loading
     */
    public static void classifyPositiveTweets() {
        double trueSum = (new File(TESTING_DIR, "true")).list().length;

        double trueCounter = 0;

        try {

            ObjectInputStream inputStream = new ObjectInputStream(
                    new FileInputStream(PATH));
            LMClassifier compiledClassifier
                    = (LMClassifier) inputStream.readObject();

            inputStream.close();

            String[] trainingFolders = TESTING_DIR.list();
            assert trainingFolders != null;
            for (String trainingFolder : trainingFolders) {
                File dir = new File(TESTING_DIR, trainingFolder);
                String[] trainingFiles = dir.list();
                assert trainingFiles != null;
                for (String trainingFile : trainingFiles) {
                    File file = new File(dir, trainingFile);
                    String text;
                    try {
                        text = Files.readFromFile(file, "ISO-8859-1");
                    } catch (IOException e) {
                        throw new FileNotFoundException(
                                "Files could not be read from the directory.");
                    }

                    String bestCategory
                            = bestCategory(compiledClassifier, text);

                    if (dir.getName().equals("true")
                            && bestCategory.equals("true")) {
                        trueCounter += 1.0;
                    }
                }
            }

            DecimalFormat df = new DecimalFormat("#.00");

            System.out.println("Positive posts score: "
                    + df.format(THRESHOLD * trueCounter / trueSum));

        } catch (Exception e) {
            System.out.println("Couldn't finish...");
        }
    }

    /**
     * A method that runs an analysis of the machine learning
     * accuracy on both negative only.
     * @throws ClassifierCompilationException - if there is a
     *         problem with classifier loading
     */
    public static void classifyNegativeTweets() {
        double falseSum = (new File(TESTING_DIR, "false")).list().length;
        double falseCounter = 0;

        try {

            ObjectInputStream inputStream = new ObjectInputStream(
                    new FileInputStream(PATH));
            LMClassifier compiledClassifier
                    = (LMClassifier) inputStream.readObject();

            inputStream.close();

            String[] trainingFolders = TESTING_DIR.list();
            assert trainingFolders != null;
            for (String trainingFolder : trainingFolders) {
                File dir = new File(TESTING_DIR, trainingFolder);
                String[] trainingFiles = dir.list();
                assert trainingFiles != null;
                for (String trainingFile : trainingFiles) {
                    File file = new File(dir, trainingFile);
                    String text;
                    try {
                        text = Files.readFromFile(file, "ISO-8859-1");
                    } catch (IOException e) {
                        throw new FileNotFoundException(
                                "Files could not be read from the directory.");
                    }

                    String bestCategory
                            = bestCategory(compiledClassifier, text);

                    if (dir.getName().equals("false")
                            && bestCategory.equals("false")) {
                        falseCounter += 1.0;
                    }
                }
            }

            DecimalFormat df = new DecimalFormat("#.00");
            System.out.println("Negative posts score: "
                    + df.format(THRESHOLD * falseCounter / falseSum));

        } catch (Exception e) {
            System.out.println("Couldn't finish...");
        }
    }

    /**
     * Decides what group the text belongs to.
     * @param compiledClassifier - used for classification
     * @param text - text to be classified
     * @return "true" if the text is on "USA - Russia
     *         relations" topic, "false" otherwise
     */
    private static String bestCategory(
            final LMClassifier compiledClassifier,
            final String text) {

        String bestCategory;
        if (text.equals("")) {
            bestCategory = "false";
        } else {
            JointClassification jc
                    = compiledClassifier.classifyJoint(
                    text.toCharArray(), 0, text.length());

            if (jc.score(0) < SCORE) {
                bestCategory = "false";
            } else {
                bestCategory = "true";
            }
        }

        return bestCategory;
    }

}
