package prik.parser.visitors;

import prik.exceptions.PrikException;
import prik.lib.Variables;
import prik.lib.modules.Module;
import prik.parser.ast.*;

/**
 *
 * @author Professional
 */
public final class AssignValidator extends AbstractVisitor {
    @Override
    public void visit(UseStatement s) {
        super.visit(s);
        try {
//            Module module = (Module) Class.forName(s.PACKAGE + s.expression.eval().asString()).newInstance();
//            module.init();
//            s.eval();
        } catch (Exception ex) {    }
    }
    
//    @Override
//    public void visit(AssignmentExpression s) {
//        super.visit(s);
//        if (Variables.isExists(s.target.get().asString())) {
//            throw new PrikException("CannotAssignValueToConstantError", "Cannot assign value to constant \"" + s.target.get().asString() + "\"");
//        }
//    }
}
