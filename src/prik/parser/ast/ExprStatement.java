package prik.parser.ast;

import prik.lib.Value;

/**
 *
 * @author Professional
 */
public final class ExprStatement extends InterruptableNode implements Expression, Statement {
    public final Expression expr;
    
    public ExprStatement(Expression expression) {
        this.expr = expression;
    }

    @Override
    public void execute() {
        super.interruptionCheck();
        expr.eval();
    }
    
    @Override
    public Value eval() {
        return expr.eval();
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
        return expr.toString();
    }
}
