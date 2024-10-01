package prik.parser.ast;

import prik.exceptions.VariableDoesNotExistsException;
import prik.lib.Value;
import prik.lib.Variables;

/**
 *
 * @author Professional
 */
public final class AssignmentStatement implements Statement {
    public final String variable;
    public final Expression expression;

    public AssignmentStatement(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }
    
    @Override
    public void execute() {
        final Value result = expression.eval();
        if (Variables.isExists(variable)) {
            Variables.set(variable, result);
        } else throw new VariableDoesNotExistsException(variable);
//        Variables.set(variable, result);
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
//    public <R, T> R accept(ResultVisitor<R, T> visitor, T t) {
//        return visitor.visit(this, t);
//    }

    @Override
    public String toString() {
        return String.format("%s = %s", variable, expression);
    }
}
