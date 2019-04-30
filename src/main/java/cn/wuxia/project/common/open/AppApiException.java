package cn.wuxia.project.common.open;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppApiException extends Exception {
    private static final long serialVersionUID = -4702141480760911518L;

    public Logger logger = LoggerFactory.getLogger(getClass());

    public AppApiException(String msg) {
        super(msg);
        logger.error(msg);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public AppApiException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message, cause);
    }

    /** Constructs a new runtime exception with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public AppApiException(Throwable cause) {
        super(cause);
        logger.error("", cause);
    }
}
