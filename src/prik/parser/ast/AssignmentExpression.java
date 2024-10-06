package prik.parser.ast;

import prik.exceptions.VariableDoesNotExistsException;
import prik.lib.Value;
import prik.lib.Variables;

/**
 *
 * @author Professional
 */
public final class AssignmentExpression implements Expression {
//    public final String variable;
    public final Accessible target;
    public final BinaryExpression.Operator operation;
    public final Expression expression;

    public AssignmentExpression(BinaryExpression.Operator operation, Accessible target, Expression expr) {
        this.operation = operation;
        this.target = target;
        this.expression = expr;
    }
    
    @Override
    public Value eval() {
//        if (Variables.isExists(variable)) {
//            Variables.set(variable, result);
//        } else throw new VariableDoesNotExistsException(variable);
        if (operation == null) {
            // Simple assignment
            return target.set(expression.eval());
        }
        final Expression expr1 = new ValueExpression(target.get());
        final Expression expr2 = new ValueExpression(expression.eval());
        return target.set(new BinaryExpression(operation, expr1, expr2).eval());
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
        final String op = (operation == null) ? "" : operation.toString();
        return String.format("%s %s= %s", target, op, expression);
    }
}
