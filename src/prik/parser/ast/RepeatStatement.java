package prik.parser.ast;

import prik.exceptions.TypeException;
import prik.lib.Types;


/**
 *
 * @author Professional
 */
public final class RepeatStatement implements Statement/*, Expression*/ {
    public final Expression condition;
    public final Statement body;

    public RepeatStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }
    
    @Override
    public void execute() {
        if (condition.eval().type() == Types.NUMBER) {
            for (int i = 0; i < condition.eval().asNumber(); i++) {
                body.execute();
            }
        } else throw new TypeException(Types.typeToString(condition.eval().type()) +
                            " cannot match number");
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
        return "repeat " + condition + " {\n" + body + "}";
    }
}
