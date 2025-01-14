package prik.parser.ast;

import prik.exceptions.PrikException;


/**
 *
 * @author Professional
 */
public final class AssertStatement implements Statement {
    public final Expression condition;
    public String message;

    public AssertStatement(Expression expression) {
        this.condition = expression;
        this.message = null;
    }

    public AssertStatement(Expression condition, String message) {
        this.condition = condition;
        this.message = message;
    }
    
    @Override
    public void execute() {
        if (condition.eval().asNumber() != 1) {
            if (message != null) {
                throw new PrikException("AssertionError", message);
            }
            throw new PrikException("AssertionError", "Assertion failed: " + condition);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
//        return visitor.visit(this, input);
        throw new RuntimeException("Not supported yet");
    }

    @Override
    public String toString() {
        return "assert " + condition;
    }
}
