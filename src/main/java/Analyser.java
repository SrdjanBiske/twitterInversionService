import exception.ClassifierCompilationException;
import service.ml.PipeExecutor;
import service.ml.MachineLearning;

/**
 * Class for running the machine learning
 * analytics.
 */
public final class Analyser {

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private Analyser() {
    }

    /**
    * This method is used to run the classification
    * of tweets.
     * @param args - needed only for Maven compilation
    */
    public static void main(final String[] args) {

        PipeExecutor.prepareDataForTraining();
        PipeExecutor.prepareDataForTesting();

        try {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("TRAINING AND ANALYZING DATA");
            System.out.println();

            MachineLearning.trainData();

            System.out.println("Results:");

            MachineLearning.classifyAllTweets();
            MachineLearning.classifyPositiveTweets();
            MachineLearning.classifyNegativeTweets();

            System.out.println();
            System.out.println();
            System.out.println();
        } catch (ClassifierCompilationException e) {
            e.printStackTrace();
        }
    }
}
