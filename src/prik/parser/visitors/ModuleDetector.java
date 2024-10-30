package prik.parser.visitors;

import java.util.HashSet;
import java.util.Set;
import prik.parser.ast.ArrayExpression;
import prik.parser.ast.Expression;
import prik.parser.ast.Statement;
import prik.parser.ast.UseStatement;
import prik.parser.ast.ValueExpression;

/**
 *
 * @author Professional
 */
public class ModuleDetector extends AbstractVisitor {
    private Set<String> modules;

    public ModuleDetector() {
        modules = new HashSet<>();
    }

    public Set<String> detect(Statement s) {
        s.accept(this);
        return modules;
    }

    @Override
    public void visit(UseStatement st) {
        if (st.expression instanceof ArrayExpression) {
            ArrayExpression ae = (ArrayExpression) st.expression;
            for (Expression expr : ae.elements) {
                modules.add(expr.eval().asString());
            }
        }
        if (st.expression instanceof ValueExpression) {
            ValueExpression ve = (ValueExpression) st.expression;
            modules.add(ve.value.asString());
        }
        super.visit(st);
    }
}
