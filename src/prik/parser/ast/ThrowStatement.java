package prik.parser.ast;

import java.util.List;
import java.util.Map;
import prik.exceptions.PrikException;

/**
 *
 * @author Professional
 */
public class ThrowStatement implements Statement {
    public final String type;
    public final Expression expr;

    public ThrowStatement(String type, Expression expr) {
        this.type = type;
        this.expr = expr;
    }
    
    @Override
    public void execute() {
        switch (type) {
            case "RuntimeException": throw new RuntimeException(expr.eval().asString());
            case "ArithmeticException": throw new ArithmeticException(expr.eval().asString());
            default:
                throw new RuntimeException("Unknown exception \"" + type + "\"");
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
//        return visitor.visit(this, input);
        throw new RuntimeException("Not supported yet");
    }

    @Override
    public String toString() {
        return "throw " + type + "(" + expr.eval().asString() + ")";
    }
}
