package prik.exceptions;

/**
 *
 * @author Professional
 */
public final class UnknownException extends RuntimeException {
    public UnknownException(String unk, Object message) {
//        super("prik.errors.Unknown", String.format("Unknown %s \"%s\"", unk, message));
        super(String.format("Unknown %s \"%s\"", unk, message));
    }
}
