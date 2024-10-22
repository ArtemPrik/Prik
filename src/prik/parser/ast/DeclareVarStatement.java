package prik.parser.ast;

import prik.lib.NullValue;
import prik.lib.Variables;


/**
 *
 * @author Professional
 */
public final class DeclareVarStatement implements Statement {
    public final String name;
    public final Expression expression;

    public DeclareVarStatement(String name) {
        this.name = name;
        this.expression = null;
    }
    
    public DeclareVarStatement(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }
    
    @Override
    public void execute() {
        if (expression != null) Variables.set(name, expression.eval());
        else Variables.set(name, new NullValue());
    }

    @Override
    public void accept(Visitor visitor) {
//        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "var " + name + (expression == null ? "" : " = " + expression);
    }
}
