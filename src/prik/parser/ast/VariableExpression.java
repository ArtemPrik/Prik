package prik.parser.ast;

import prik.exceptions.CannotAssignValueToConstantException;
import prik.exceptions.PrikException;
import prik.exceptions.UnknownException;
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
        if (Variables.isExists(name)) {
            if (Variables.isConstant(name)) {
                return Variables.getConstant(name);
            } else return Variables.get(name);
        } else throw new VariableDoesNotExistsException(name);
    }

    @Override
    public Value set(Value value) {
        if (Variables.isExists(name)) {
            if (!Variables.isConstant(name)) {
                Variables.set(name, value);
            } else throw new CannotAssignValueToConstantException(name);
        } else throw new UnknownException("variable", name);
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
