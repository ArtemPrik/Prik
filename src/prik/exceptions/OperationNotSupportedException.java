package prik.exceptions;

import prik.PrikException;

/**
 *
 * @author Professional
 */
public final class OperationNotSupportedException extends PrikException {

    public OperationNotSupportedException() {
    }

    public OperationNotSupportedException(String message) {
        super("prik.errors.OperationNotSupportedException", "Operation " + message + " is not supported");
    }
    
    public OperationNotSupportedException(Object operation) {
        super("prik.errors.OperationNotSupportedException", "Operation " + operation + " is not supported");
    }
    
    public OperationNotSupportedException(Object operation, String message) {
        super("prik.errors.OperationNotSupportedException", "Operation " + operation + " is not supported " + message);
    }
}
