package prik.exceptions;

/**
 *
 * @author Professional
 */
public class CannotAssignValueToConstantException extends RuntimeException {
    public CannotAssignValueToConstantException(String constant) {
        super(String.format("Cannot assign value to constant \"%s\"", constant));
    }
}
