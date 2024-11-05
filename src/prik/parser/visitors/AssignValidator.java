package prik.parser.visitors;

import prik.Console;
import prik.exceptions.CannotAssignValueToConstantException;
import prik.exceptions.PrikException;
import prik.lib.Variables;
import prik.modules.Module;
import prik.parser.ast.*;
import prik.parser.visitors.AbstractVisitor;

/**
 *
 * @author Professional
 */
public final class AssignValidator extends AbstractVisitor {
    
    @Override
    public void visit(AssignmentExpression s) {
        super.visit(s);
        if (s.target instanceof VariableExpression) {
            final String variable = ((VariableExpression) s.target).name;
            if (Variables.isExists(variable)) {
//                throw new RuntimeException(String.format("Warning: variable \"%s\" overrides constant", variable));
                throw new CannotAssignValueToConstantException(variable);
            }
        }
    }

    @Override
    public void visit(ImportStatement st) {
        super.visit(st);
        st.execute();
    }
}
