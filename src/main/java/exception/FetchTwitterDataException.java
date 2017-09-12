package exception;

/**
 * Thrown if there's any problem with fetching
 * the data from Twitter.
 */
public class FetchTwitterDataException extends Exception {

    /**
    * Exception constructor.
    * @param message - error message
    */
    public FetchTwitterDataException(final String message) {
        super(message);
    }

}
