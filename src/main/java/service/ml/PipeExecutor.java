package service.ml;

import com.aliasi.util.Files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.formatter.Pipe;
import service.formatter.PipeMode;

/**
 * Class for filtering of statuses and
 * manipulation with folders which store
 * these statuses.
 */
public final class PipeExecutor {

    /**
     * Used for logging events that happen in this class.
     */
    private static final Logger LOGGER;

    static {
        LOGGER = LogManager.getLogger(PipeExecutor.class);
    }

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private PipeExecutor() {
    }

    /**
     * This method takes data from 'TrainingDataUnresolved,
     * filters it and stores it to 'PipedTrainingData'.
     * This data is used for training.
     *
     * @return - true the whole process passes without
     *           problems and exceptions,
     *           false if that's not the case
     */
    public static boolean prepareDataForTraining() {

        File originalDirTrue = new File("src/main/resources/TrainingData");
        File pipedDir = new File("src/main/resources/TrainingDataPiped");

        String[] twitterAccounts = originalDirTrue.list();

        for (String twitterAccountIterator : twitterAccounts) {

            (new File(pipedDir, twitterAccountIterator)).mkdir();
            File twitterAccountFolder = new File(originalDirTrue,
                    twitterAccountIterator);
            String[] files = twitterAccountFolder.list();
            assert files != null;

            for (String filesIterator : files) {
                try {
                    File file = new File(twitterAccountFolder, filesIterator);
                    String original = Files.readFromFile(file, "UTF-8");
                    String filtered = Pipe.pipe(original, PipeMode.TRAINING);

                    Writer writer = new BufferedWriter(
                            new FileWriter(pipedDir + "/"
                            + twitterAccountIterator + "/" + filesIterator));

                    writer.write(filtered);
                    writer.close();
                } catch (IOException e) {
                    LOGGER.error("File or folder cannot be opened.");
                    return false;
                }
            }

            if (!twitterAccountFolder.delete()) {
                LOGGER.error("Problem occurred while deleting a directory "
                        + twitterAccountFolder.getName() + ".");
            }
        }
        return true;
    }

    /**
     * This method takes data from 'TestDataUnresolved,
     * filters it and stores it to 'TestData'.
     * This data is used for testing how precise the ML is.
     *
     * @return - true the whole process passes without
     *           problems and exceptions,
     *           false if that's not the case
     */
    public static boolean prepareDataForTesting() {
        File originalDirTrue = new File("src/main/resources/TestData/true");
        File originalDirFalse = new File("src/main/resources/TestData/false");
        File pipedDir = new File("src/main/resources/TestDataPiped");

        String[] twitterAccounts = originalDirTrue.list();
        assert twitterAccounts != null;
        for (String twitterAccountIterator : twitterAccounts) {

            File twitterAccountFolder
                    = new File(originalDirTrue, twitterAccountIterator);

            String[] files = twitterAccountFolder.list();
            assert files != null;
            for (String filesIterator : files) {
                try {
                    File file = new File(twitterAccountFolder, filesIterator);
                    String original = Files.readFromFile(file, "UTF-8");
                    String filtered = Pipe.pipe(original, PipeMode.TESTING);

                    Writer writer = new BufferedWriter(
                            new FileWriter(pipedDir
                                    + "/true/" + filesIterator));

                    writer.write(filtered);
                    writer.close();
                } catch (IOException e) {
                    LOGGER.error("File or folder cannot be opened.");
                    return false;
                }
            }
            if (!twitterAccountFolder.delete()) {
                LOGGER.error("Problem occurred while deleting a directory "
                        + twitterAccountFolder.getName() + ".");
            }
        }

        twitterAccounts = originalDirFalse.list();
        assert twitterAccounts != null;
        for (String twitterAccountIterator : twitterAccounts) {

            File twitterAccountFolder
                    = new File(originalDirFalse, twitterAccountIterator);
            String[] files = twitterAccountFolder.list();
            assert files != null;
            for (String filesIterator : files) {
                try {
                    File file = new File(twitterAccountFolder, filesIterator);
                    String original = Files.readFromFile(file, "UTF-8");
                    String filtered = Pipe.pipe(original, PipeMode.TESTING);

                    Writer writer = new BufferedWriter(
                            new FileWriter(pipedDir + "/false/"
                            + filesIterator));

                    writer.write(filtered);
                    writer.close();
                } catch (IOException e) {
                    LOGGER.error("File or folder cannot be opened.");
                    return false;
                }
            }
            if (!twitterAccountFolder.delete()) {
                LOGGER.error("Problem occurred while deleting a directory "
                        + twitterAccountFolder.getName() + ".");
            }
        }

        return true;
    }

}
