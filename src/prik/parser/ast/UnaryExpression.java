package prik.parser.ast;

import prik.exceptions.OperationNotSupportedException;
import prik.lib.*;

/**
 *
 * @author Professional
 */
public final class UnaryExpression implements Expression, Statement {
    public static enum Operator {
        NEGATE("-"),
        
        NOT("!"),
        COMPLEMENT("~");
        
        private final String name;

        private Operator(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    
    public final Expression expr1;
    public final Operator operation;

    public UnaryExpression(Operator operation, Expression expr1) {
        this.operation = operation;
        this.expr1 = expr1;
    }

    @Override
    public void execute() {
        eval();
    }
    
    @Override
    public Value eval() {
        final Value value = expr1.eval();
        switch (operation) {
            case NEGATE: return new NumberValue(-value.asNumber());
            case COMPLEMENT: return new NumberValue(~(int)value.asNumber());
            case NOT: return new NumberValue(value.asNumber() != 0 ? 0 : 1);
            default:
                throw new OperationNotSupportedException(operation.name);
        }
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
        return String.format("%s %s", operation, expr1);
    }
}
