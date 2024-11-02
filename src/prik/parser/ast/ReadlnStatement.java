package prik.parser.ast;

import java.util.Scanner;
import prik.lib.StringValue;
import prik.lib.Value;


/**
 *
 * @author Professional
 */
public final class ReadlnStatement implements Statement, Expression {
    private final Scanner sc = new Scanner(System.in);
    
    @Override
    public void execute() {
        eval();
    }

    @Override
    public Value eval() {
        return new StringValue(sc.nextLine());
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
        return "readln";
    }
}
