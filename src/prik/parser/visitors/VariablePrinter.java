package prik.parser.visitors;

import prik.Console;
import prik.parser.ast.*;

/**
 *
 * @author Professional
 */
public final class VariablePrinter extends AbstractVisitor {
//    @Override
//    public void visit(ArrayAccessExpression s) {
//        super.visit(s);
//        Console.println(s.variable);
//    }
    
    @Override
    public void visit(AssignmentExpression s) {
        super.visit(s);
        Console.println(s.target);
    }

    @Override
    public void visit(VariableExpression s) {
        super.visit(s);
        Console.println(s.name);
    }
}
