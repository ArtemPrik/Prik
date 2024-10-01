package prik.parser.ast;

import prik.exceptions.VariableDoesNotExistsException;
import prik.lib.*;

/**
 *
 * @author Professional
 */
public final class VariableExpression implements Expression {
    public final String name;
    
    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        // if (!Variables.isExists(name)) throw new RuntimeException("Variable does not exists: " + name);
        if (!Variables.isExists(name)) throw new VariableDoesNotExistsException(name);
        return Variables.get(name);
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
