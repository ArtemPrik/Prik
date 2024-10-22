package prik.exceptions;

/**
 *
 * @author Professional
 */
public class VariableDoesNotExistsException extends PrikException {
    private final String variable;

    public VariableDoesNotExistsException(String variable) {
        super("prik.errors.VariableDoesNotExistsError", "Variable \"" + variable + "\" does not exists");
        this.variable = variable;
    }

    public String getVariable() {
        return variable;
    }
}
