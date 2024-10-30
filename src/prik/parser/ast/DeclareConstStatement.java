package prik.parser.ast;

import prik.exceptions.TypeException;
import prik.lib.BooleanValue;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Types;
import prik.lib.Variables;


/**
 *
 * @author Professional
 */
public final class DeclareConstStatement implements Statement {
    public final String name;
    public final Expression expression;
    public Datatypes.Datatype type;

    public DeclareConstStatement(String name, Expression expression, 
                                Datatypes.Datatype type) {
        this.name = name;
        this.expression = expression;
        this.type = type;
    }
    
    @Override
    public void execute() {
        switch (type) {
            case STRING:
                if (expression.eval().type() == Types.STRING) {
                    Variables.setConstant(name,
                            new StringValue(expression.eval().asString()));
                } else throw new TypeException(
                        Types.typeToString(expression.eval().type()) + " doesnt match string");
                break;
            case NUMBER:
                if (expression.eval().type() == Types.NUMBER) {
                    Variables.setConstant(name,
                            new NumberValue(expression.eval().asNumber()));
                } else throw new TypeException(
                        Types.typeToString(expression.eval().type()) + " doesnt match number");
                break;
            case BOOLEAN:
                if (expression.eval().type() == Types.BOOLEAN) {
                    Variables.setConstant(name,
                            new BooleanValue(Boolean.TRUE || Boolean.FALSE));
                } else throw new TypeException(
                        Types.typeToString(expression.eval().type()) + " doesnt match number");
                break;
            case ANY:
                Variables.setConstant(name, expression.eval());
                break;
            default:
                throw new TypeException(Types.typeToString(
                        expression.eval().type()) + " doesnt support");
        }
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
        return String.format("const %s = %s" , name, expression);
    }
}
