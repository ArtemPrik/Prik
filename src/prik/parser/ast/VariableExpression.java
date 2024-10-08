package prik.parser.ast;

import prik.exceptions.VariableDoesNotExistsException;
import prik.lib.*;

/**
 *
 * @author Professional
 */
public final class VariableExpression implements Expression, Accessible {
    public final String name;
    
    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        return get();
    }

    @Override
    public Value get() {
        if (!Variables.isExists(name)) throw new VariableDoesNotExistsException(name);
        return Variables.get(name);
    }

    @Override
    public Value set(Value value) {
        Variables.set(name, value);
        return value;
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
        return name;
    }
}
