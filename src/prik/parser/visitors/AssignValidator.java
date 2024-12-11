package prik.parser.visitors;

import prik.exceptions.CannotAssignValueToConstantException;
import prik.exceptions.PrikException;
import prik.lib.Variables;
import prik.parser.ast.*;

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
                throw new CannotAssignValueToConstantException(variable);
            }
        }
        if (s.target instanceof ContainerAccessExpression) {
            final Expression variable = ((ContainerAccessExpression) s.target).root;
            if (Variables.isExists(variable.toString())) {
                throw new CannotAssignValueToConstantException(variable.toString() + 
                        ((ContainerAccessExpression) s.target).indices);
            }
        }
    }

    @Override
    public void visit(ImportStatement st) {
        super.visit(st);
        st.execute();
    }
}
