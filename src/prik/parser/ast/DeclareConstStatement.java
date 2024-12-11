package prik.parser.ast;

import prik.exceptions.TypeException;
import prik.lib.CharacterValue;
import prik.lib.Types;
import prik.lib.Value;
import prik.lib.Variables;


/**
 *
 * @author Professional
 */
public final class DeclareConstStatement implements Statement, Expression {
    public final String name;
    public final Expression expression;
    public final prik.lib.Datatypes type;

    public DeclareConstStatement(String name, Expression expression, 
                                prik.lib.Datatypes type) {
        this.name = name;
        this.expression = expression;
        this.type = type;
    }
    
    @Override
    public void execute() {
        eval();
    }

    @Override
    public Value eval() {
        final Value value = expression.eval();
        switch (type) {
            case ARRAY:
                if (value.type() == Types.ARRAY) {
                    Variables.setConstant(name, value);
                } else throw new TypeException(
                        Types.typeToString(value.type()) + " doesnt match array");
                break;
            case BOOLEAN:
                if (value.type() == Types.BOOLEAN) {
                    Variables.setConstant(name, value);
                } else throw new TypeException(
                        Types.typeToString(value.type()) + " doesnt match boolean");
                break;
            case CHAR:
                if (value.type() == Types.CHARACTER) {
                    Variables.setConstant(name,
                            new CharacterValue(value.asString().charAt(0)));
                } else throw new TypeException(
                        Types.typeToString(value.type()) + " doesnt match character");
                break;
            case FUNCTION:
                if (value.type() == Types.FUNCTION) {
                    Variables.setConstant(name, value);
                } else throw new TypeException(
                        Types.typeToString(value.type()) + " doesnt match function");
                break;
            case MAP:
                if (value.type() == Types.MAP) {
                    Variables.setConstant(name, value);
                } else throw new TypeException(
                        Types.typeToString(value.type()) + " doesnt match map");
                break;
                case NUMBER:
                if (value.type() == Types.NUMBER) {
                    Variables.setConstant(name, value);
                } else throw new TypeException(
                        Types.typeToString(value.type()) + " doesnt match number");
                break;
            case STRING:
                if (value.type() == Types.STRING) {
                    Variables.setConstant(name, value);
                } else throw new TypeException(
                        Types.typeToString(value.type()) + " doesnt match string");
                break;
            case ANY:
                Variables.setConstant(name, value);
                break;
            default:
                throw new TypeException(Types.typeToString(
                        value.type()) + " doesnt support");
        }
        return value;
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
        return String.format("const %s: %s = %s" , name, type, expression);
    }
}
