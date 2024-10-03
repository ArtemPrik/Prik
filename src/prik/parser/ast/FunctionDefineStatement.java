package prik.parser.ast;

import java.util.List;
import prik.lib.Functions;
import prik.lib.UserDefinedFunction;

/**
 *
 * @author Professional
 */
public final class FunctionDefineStatement implements Statement {
    public final String name;
//    public final List<String> arguments;
    public final Arguments arguments;
    public final Statement body;
    
    public FunctionDefineStatement(String name, Arguments argNames, Statement body) {
        this.name = name;
        this.arguments = argNames;
        this.body = body;
    }

    @Override
    public void execute() {
        Functions.set(name, new UserDefinedFunction(arguments, body));
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
        if (body instanceof ReturnStatement) {
            return String.format("def %s%s = %s", name, arguments, ((ReturnStatement)body).expression);
        }
        return String.format("def %s(%s) %s", name, arguments, body);
    }
}
