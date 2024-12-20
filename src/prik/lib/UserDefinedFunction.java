package prik.lib;

import java.util.List;
import prik.exceptions.ArgumentsMismatchException;
import prik.parser.ast.Argument;
import prik.parser.ast.ReturnStatement;
import prik.parser.ast.Statement;

/**
 *
 * @author Professional
 */
public class UserDefinedFunction implements Function {
    public final prik.parser.ast.Arguments arguments;
    public final Statement body;
    
    public UserDefinedFunction(prik.parser.ast.Arguments argNames, Statement body) {
        this.arguments = argNames;
        this.body = body;
    }
    
    public int getArgsCount() {
        return arguments.size();
    }
    
    public String getArgsName(int index) {
        if (index < 0 || index >= getArgsCount()) return "";
        return arguments.get(index).getName();
    }

    @Override
    public Value execute(Value... args) {
        final int size = args.length;
        if (size != getArgsCount()) throw new RuntimeException("Args count mismatch");
        try {
            Variables.push();
            for (int i = 0; i < size; i++) {
                Variables.set(getArgsName(i), args[i]);
            }
            body.execute();
            return NumberValue.ZERO;
        } catch (ReturnStatement rt) {
            return rt.getResult();
        } finally {
            Variables.pop();
        }
    }

    @Override
    public String toString() {
        if (body instanceof ReturnStatement) {
            return String.format("def%s -> %s", arguments, ((ReturnStatement)body).expression);
        }
        return String.format("def%s {%s}", arguments, body);
    }
}
