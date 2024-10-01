package prik.parser.ast;

import java.util.logging.Level;
import java.util.logging.Logger;
import prik.Console;


/**
 *
 * @author Professional
 */
public final class ThrowStatement implements Statement {
    public final Expression expression;

    public ThrowStatement(Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public void execute() {
        try {
            //        Console.error(expression.eval().asString());
            throw new Throwable(expression.eval().asString());
        } catch (Throwable ex) {
            Logger.getLogger(ThrowStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "throw " + expression;
    }
}
