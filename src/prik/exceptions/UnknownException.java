package prik.exceptions;

import prik.PrikException;

/**
 *
 * @author Professional
 */
public final class UnknownException extends PrikException {
    public UnknownException(String unk, Object message) {
        super("prik.errors.Unknown", unk, message);
    }
}
