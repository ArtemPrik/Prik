package prik.exceptions;

/**
 *
 * @author Professional
 */
public final class OperationIsNotSupportedException extends PrikException {

    public OperationIsNotSupportedException() {
    }

    public OperationIsNotSupportedException(String message) {
        super("prik.errors.OperationIsNotSupportedException", "Operation " + message + " is not supported");
    }
    
    public OperationIsNotSupportedException(Object operation) {
        super("prik.errors.OperationIsNotSupportedException", "Operation " + operation + " is not supported");
    }
    
    public OperationIsNotSupportedException(Object operation, String message) {
        super("prik.errors.OperationIsNotSupportedException", "Operation " + operation + " is not supported " + message);
    }
}
