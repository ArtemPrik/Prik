package prik.exceptions;

/**
 *
 * @author Professional
 */
public final class ArgumentsMismatchException extends PrikException {
    public ArgumentsMismatchException() {
        super();
    }

    public ArgumentsMismatchException(String message) {
        super("prik.errors.ArgumentsMistmatchError", message);
    }
}
