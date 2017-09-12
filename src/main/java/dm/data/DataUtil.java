package dm.data;

import com.aliasi.util.Files;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.File;

/**
 * Class containing utility methods for reading the
 * data from the disk.
 */
final class DataUtil {

    /**
     * Event logger in the DataUtil class.
     */
    private static final Logger LOGGER;

    static {
        LOGGER = LogManager.getLogger(DataUtil.class);
    }

    /**
     * Empty private constructor for preventing
     * public or default constructor.
     */
    private DataUtil() {
    }

    /**
     * The method reads the file with the given fileName,
     * recognizes all the words in the file and returns
     * them separated in the String[].
     * @param fileName - name of the file which will be read
     * @return a String[] containing all the words from the file
     */
    public static String[] readFileToStringArray(final String fileName) {
        try {
            File file = new File(
                    "src/main/resources/wordCollections/" + fileName);
            String text = Files.readFromFile(file, "UTF-8");

            LOGGER.info("File " + fileName + " successfully read.");

            return text.split("\\s+");
        } catch (IOException exception) {
            LOGGER.error("File " + fileName + " cannot be read.");
            return null;
        }
    }

}
