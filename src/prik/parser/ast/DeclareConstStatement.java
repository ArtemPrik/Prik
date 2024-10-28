package prik.parser.ast;

import prik.lib.Variables;


/**
 *
 * @author Professional
 */
public final class DeclareConstStatement implements Statement {
    public final String name;
    public final Expression expression;
    public Datatypes.Datatype type;
    
    public DeclareConstStatement(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    public DeclareConstStatement(String name, Expression expression, 
                                Datatypes.Datatype type) {
        this.name = name;
        this.expression = expression;
        this.type = type;
    }
    
    @Override
    public void execute() {
        Variables.setConstant(name, expression.eval());
    }

    @Override
    public void accept(Visitor visitor) {
//        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("const %s = %s" , name, expression);
    }
}
