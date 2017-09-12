package exception;

/**
 * Thrown if there's any problem with reading
 * the generated classifier.
 */
public class ClassifierCompilationException extends Exception {

    /**
     * Exception constructor.
     * @param message - error message
     */
    public ClassifierCompilationException(final String message) {
        super(message);
    }

}
