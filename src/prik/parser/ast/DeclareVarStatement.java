package prik.parser.ast;

import prik.exceptions.TypeException;
import prik.lib.BooleanValue;
import prik.lib.NullValue;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Types;
import prik.lib.Value;
import prik.lib.Variables;


/**
 *
 * @author Professional
 */
public final class DeclareVarStatement implements Statement {
    public final String name;
    public final Expression expression;
    public prik.lib.Datatypes type;

    public DeclareVarStatement(String name) {
        this.name = name;
        this.expression = null;
    }
    
    public DeclareVarStatement(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    public DeclareVarStatement(String name, prik.lib.Datatypes type) {
        this.name = name;
        this.expression = null;
        this.type = type;
    }
    
    public DeclareVarStatement(String name, Expression expression, 
                                prik.lib.Datatypes type) {
        this.name = name;
        this.expression = expression;
        this.type = type;
    }
    
    @Override
    public void execute() {
        if (expression != null) Variables.set(name, expression.eval());
        else Variables.set(name, new NullValue());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }

    @Override
    public String toString() {
        return "var " + name + (expression == null ? "" : " = " + expression);
    }
}
