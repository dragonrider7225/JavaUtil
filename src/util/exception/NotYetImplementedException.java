package util.exception;

/**
 * An exception that represents a function that has not yet been implemented. Should only be thrown when the functionality that it replaces
 * is planned to be implemented in the future. If no such plans exist, throw {@link UnsupportedOperationException} instead.
 */
public final class NotYetImplementedException extends RuntimeException {
    /**
     * Structure v1.0
     */
    private static final long serialVersionUID = -986228182356444401L;

    @SuppressWarnings("javadoc")
    public NotYetImplementedException() {
        super();
    }

    @SuppressWarnings("javadoc")
    public NotYetImplementedException(final String message) {
        super(message);
    }

    @SuppressWarnings("javadoc")
    public NotYetImplementedException(final Throwable cause) {
        super(cause);
    }

    @SuppressWarnings("javadoc")
    public NotYetImplementedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @SuppressWarnings("javadoc")
    public NotYetImplementedException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
