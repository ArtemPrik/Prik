package prik.parser.optimization;

import java.util.HashMap;
import java.util.Map;
import prik.parser.ast.Accessible;
import prik.parser.ast.Argument;
import prik.parser.ast.Arguments;
import prik.parser.ast.AssignmentExpression;
import prik.parser.ast.ContainerAccessExpression;
import prik.parser.ast.DestructuringAssignmentStatement;
import prik.parser.ast.ForeachArrayStatement;
import prik.parser.ast.ForeachMapStatement;
import prik.parser.ast.Node;
import prik.parser.ast.UnaryExpression;
import prik.parser.ast.ValueExpression;
import prik.parser.ast.VariableExpression;
import static prik.parser.visitors.VisitorUtils.isValue;
import static prik.parser.visitors.VisitorUtils.isVariable;

/**
 *
 * @author Professional
 */
public class VariablesGrabber extends OptimizationVisitor<Map<String, VariableInfo>> {
    public static Map<String, VariableInfo> getInfo(Node node) {
        return getInfo(node, false);
    }

    public static Map<String, VariableInfo> getInfo(Node node, boolean grabModuleConstants) {
        Map<String, VariableInfo> variableInfos = new HashMap<>();
        node.accept(new VariablesGrabber(grabModuleConstants), variableInfos);
        return variableInfos;
    }

    private final boolean grabModuleConstants;

    public VariablesGrabber() {
        this(false);
    }

    public VariablesGrabber(boolean grabModuleConstants) {
        this.grabModuleConstants = grabModuleConstants;
    }

    @Override
    public Node visit(AssignmentExpression s, Map<String, VariableInfo> t) {
        if (!isVariable((Node)s.target)) {
            return super.visit(s, t);
        }

        final String variableName = ((VariableExpression) s.target).name;
        final VariableInfo var = variableInfo(t, variableName);

        if (s.operation == null && isValue(s.expression)) {
            var.value = ((ValueExpression) s.expression).value;
        }
        t.put(variableName, var);
        return super.visit(s, t);
    }

    @Override
    public Node visit(DestructuringAssignmentStatement s, Map<String, VariableInfo> t) {
        for (String variableName : s.variables) {
            if (variableName == null) continue;
            t.put(variableName, variableInfo(t, variableName));
        }
        return super.visit(s, t);
    }

    @Override
    public Node visit(ForeachArrayStatement s, Map<String, VariableInfo> t) {
        t.put(s.variable, variableInfo(t, s.variable));
        return super.visit(s, t);
    }

    @Override
    public Node visit(ForeachMapStatement s, Map<String, VariableInfo> t) {
        t.put(s.key, variableInfo(t, s.key));
        t.put(s.value, variableInfo(t, s.value));
        return super.visit(s, t);
    }

    @Override
    public Node visit(UnaryExpression s, Map<String, VariableInfo> t) {
        if (s.expr1 instanceof Accessible) {
            if (s.expr1 instanceof VariableExpression) {
                final String variableName = ((VariableExpression) s.expr1).name;
                t.put(variableName, variableInfo(t, variableName));
            }
            if (s.expr1 instanceof ContainerAccessExpression) {
                ContainerAccessExpression conExpr = (ContainerAccessExpression) s.expr1;
                if (conExpr.rootIsVariable()) {
                    final String variableName = ((VariableExpression) conExpr.root).name;
                    t.put(variableName, variableInfo(t, variableName));
                }
            }
        }
        return super.visit(s, t);
    }

    @Override
    protected boolean visit(Arguments in, Arguments out, Map<String, VariableInfo> t) {
        for (Argument argument : in) {
            final String variableName = argument.getName();
            final VariableInfo var = variableInfo(t, variableName);
            /* No need to add value - it is optional arguments
            final Expression expr = argument.getValueExpr();
            if (expr != null && isValue(expr)) {
                var.value = ((ValueExpression) expr).value;
            }*/
            t.put(variableName, var);
        }
        return super.visit(in, out, t);
    }



    private VariableInfo variableInfo(Map<String, VariableInfo> t, final String variableName) {
        final VariableInfo var;
        if (t.containsKey(variableName)) {
            var = t.get(variableName);
            var.modifications++;
        } else {
            var = new VariableInfo();
            var.modifications = 1;
        }
        return var;
    }
}
