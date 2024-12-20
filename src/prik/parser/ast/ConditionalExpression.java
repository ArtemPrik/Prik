package prik.parser.ast;

import prik.exceptions.OperationIsNotSupportedException;
import prik.lib.*;


/**
 *
 * @author Professional
 */
public final class ConditionalExpression implements Expression {
    public static enum Operator {
        EQUALS("=="),
        NOT_EQUALS("!="),
        
        LT("<"),
        LTEQ("<="),
        GT(">"),
        GTEQ(">="),
        
        AND("&&"),
        OR("||"),
        
        NULL_COALESCE("??");
        
        private final String name;

        private Operator(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    
    public final Expression expr1, expr2;
    public final Operator operation;

    public ConditionalExpression(Operator operation, Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public Value eval() {
        final Value value1 = expr1.eval();
        switch (operation) {
            case AND: return new BooleanValue(
                    (value1.asNumber() != 0) && (expr2.eval().asNumber() != 0) );
            case OR: return new BooleanValue(
                    (value1.asNumber() != 0) || (expr2.eval().asNumber() != 0) );
            case NULL_COALESCE: return nullCoalesce();
        }
        
        
        final Value value2 = expr2.eval();
        
        double number1, number2;
        if (value1 instanceof StringValue) {
            number1 = value1.asString().compareTo(value2.asString());
            number2 = 0;
        } else {
            number1 = value1.asNumber();
            number2 = value2.asNumber();
        }
        
        boolean result;
        switch (operation) {
            case EQUALS: result = number1 == number2; break;
            case NOT_EQUALS: result = number1 != number2; break;
            
            case LT: result = number1 < number2; break;
            case LTEQ: result = number1 <= number2; break;
            case GT: result = number1 > number2; break;
            case GTEQ: result = number1 >= number2; break;
            
            default:
                throw new OperationIsNotSupportedException(operation);
        }
        return new BooleanValue(result);
    }
    
    private Value nullCoalesce() {
        Value value1;
        try {
            value1 = expr1.eval();
        } catch (NullPointerException npe) {
            value1 = null;
        }
        if (value1 == null) {
            return expr2.eval();
        }
        return value1;
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
        return String.format("%s %s %s", expr1, operation.getName(), expr2);
    }
}
