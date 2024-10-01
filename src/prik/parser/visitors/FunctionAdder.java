package prik.parser.visitors;

import prik.parser.ast.*;

/**
 *
 * @author Professional
 */
public final class FunctionAdder extends AbstractVisitor {
    @Override
    public void visit(FunctionDefineStatement s) {
        super.visit(s);
        s.execute();
    }
}