package prik.parser.ast;

import prik.lib.Value;

/**
 *
 * @author Professional
 */
public final class ArrayAssignmentExpression implements Expression {
    public final ArrayAccessExpression array;
    public final Expression expression;

    public ArrayAssignmentExpression(ArrayAccessExpression array, Expression expression) {
        this.array = array;
        this.expression = expression;
    }
    
    @Override
    public Value eval() {
        array.getArray().set(array.lastIndex(), expression.eval());
        return expression.eval();
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", array, expression);
    }
}
