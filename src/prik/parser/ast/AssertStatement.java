package prik.parser.ast;

import prik.PrikException;


/**
 *
 * @author Professional
 */
public final class AssertStatement implements Statement {
    public final Expression condition;

    public AssertStatement(Expression expression) {
        this.condition = expression;
    }
    
    @Override
    public void execute() {
        if (condition.eval().asNumber() != 1) {
            throw new PrikException("AssertionError", "Assertion failed: " + condition);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "assert ";
    }
}
