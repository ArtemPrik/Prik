package prik.parser.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import prik.exceptions.UnknownFunctionException;
import prik.lib.*;

/**
 *
 * @author Professional
 */
public final class FunctionalExpression extends InterruptableNode implements Expression, Statement {
    public final Expression name;
    public final List<Expression> arguments;
    
    public FunctionalExpression(Expression functionExpr) {
        this.name = functionExpr;
        arguments = new ArrayList<>();
    }
    
    public FunctionalExpression(Expression name, List<Expression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }
    
    public void addArgument(Expression arg) {
        arguments.add(arg);
    }
    
    @Override
    public void execute() {
        eval();
    }
    
    @Override
    public Value eval() {
        super.interruptionCheck();
        final int size = arguments.size();
        final Value[] values = new Value[size];
        for (int i = 0; i < size; i++) {
            values[i] = arguments.get(i).eval();
        }
        final Function f = consumeFunction(name);
        CallStack.enter(name.toString(), f);
        final Value result = f.execute(values);
        CallStack.exit();
        return result;
    }
    
    private Function consumeFunction(Expression expr) {
        final Value value = expr.eval();
        if (value.type() == Types.FUNCTION) {
            return ((FunctionValue) value).getValue();
        }
        return getFunction(value.asString());
    }
    
    private Function getFunction(String key) {
        if (Functions.isExists(key)) {
            return Functions.get(key);
        }
        if (Variables.isExists(key)) {
            final Value variable = Variables.get(key);
            if (variable.type() == Types.FUNCTION) {
                return ((FunctionValue)variable).getValue();
            }
        }
        throw new UnknownFunctionException(key);
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T t) {
        return visitor.visit(this, t);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (name instanceof ValueExpression valueExpr && (valueExpr.value.type() == Types.STRING)) {
            sb.append(valueExpr.value.asString()).append('(');
        } else {
            sb.append(name).append('(');
        }
        final Iterator<Expression> it = arguments.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(", ").append(it.next());
            }
        }
        sb.append(')');
        return sb.toString();
    }
}
