package prik.parser.ast;

import prik.lib.FunctionValue;
import prik.lib.Functions;


/**
 *
 * @author Professional
 */
public final class FunctionReferenceExpression extends InterruptableNode implements Expression {
    public final String name;

    public FunctionReferenceExpression(String name) {
        this.name = name;
    }

    @Override
    public FunctionValue eval() {
        super.interruptionCheck();
        return new FunctionValue(Functions.get(name));
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
        return "::" + name;
    }
}
