package prik.parser.ast;

import prik.PrikException;
import prik.lib.Value;


/**
 *
 * @author Professional
 */
public final class AssertReturnStatement implements Statement, Expression {
    public final Expression condition;
    public final Expression expression;
    private final ReturnStatement rt;

    public AssertReturnStatement(Expression condition, Expression expression) {
        this.condition = condition;
        this.expression = expression;
        rt = new ReturnStatement(expression);
    }
    
    
    
    @Override
    public void execute() {
        eval();
    }

    @Override
    public Value eval() {
        if (condition.eval().asNumber() != 1) {
            throw new PrikException("AssertionError", "Assertion failed: " + condition);
        }
        return rt.getResult();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
    public <R, T> R accept(ResultVisitor<R, T> visitor, T t) {
        return visitor.visit(this, t);
    }

    @Override
    public String toString() {
        return "assert ";
    }
}
