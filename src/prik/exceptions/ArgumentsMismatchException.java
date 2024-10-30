package prik.exceptions;

/**
 *
 * @author Professional
 */
public final class ArgumentsMismatchException extends RuntimeException {
    public ArgumentsMismatchException() {
        super();
    }

    public ArgumentsMismatchException(String message) {
//        super("prik.errors.ArgumentsMistmatchError", message);
        super(message);
    }
}
