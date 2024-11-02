package prik.parser.ast;

import java.util.ArrayList;
import java.util.List;
import prik.exceptions.UnknownFunctionException;
import prik.lib.*;

/**
 *
 * @author Professional
 */
public final class FunctionalExpression implements Expression, Statement {
    public final Expression name;
    public final List<Expression> arguments;
    
    public FunctionalExpression(Expression name) {
        this.name = name;
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
        final int size = arguments.size();
        final Value[] values = new Value[size];
        for (int i = 0; i < size; i++) {
            values[i] = arguments.get(i).eval();
        }
        
        final Function function = getFunction(name);
        if (function instanceof UserDefinedFunction) {
            final UserDefinedFunction userFunction = (UserDefinedFunction) function;
//            if (size != userFunction.getArgsCount()) throw new RuntimeException("Args count mismatch");
            
            Variables.push();
            for (int i = 0; i < size; i++) {
                Variables.set(userFunction.getArgsName(i), values[i]);
            }
            final Value result = userFunction.execute(values);
            Variables.pop();
            return result;
        }
        return function.execute(values);
    }
    
    private Function getFunction(Expression key) {
        if (Functions.isExists(key.eval().asString())) return Functions.get(key.eval().asString());
        if (Variables.isExists(key.eval().asString())) {
            final Value variable = Variables.get(key.eval().asString());
            if (variable.type() == Types.FUNCTION) return ((FunctionValue)variable).getValue();
        }
        throw new UnknownFunctionException(key.eval().asString());
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
        return name + "(" + arguments.toString() + ")";
    }
}
