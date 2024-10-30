package prik.parser.visitors;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import prik.Console;
import prik.lib.Function;
import prik.lib.FunctionValue;
import prik.lib.Types;
import prik.lib.UserDefinedFunction;
import prik.parser.ast.*;

/**
 *
 * @author Professional
 */
public class PrintVisitor implements ResultVisitor<StringBuilder, StringBuilder> {
    private int indent;

    public PrintVisitor() {
        indent = -2;
    }

    @Override
    public StringBuilder visit(ArrayExpression s, StringBuilder t) {
        t.append('[');
        final Iterator<Expression> it = s.elements.iterator();
        if (it.hasNext()) {
            it.next().accept(this, t);
            while (it.hasNext()) {
                t.append(", ");
                it.next().accept(this, t);
            }
        }
        t.append(']');
        return t;
    }

    @Override
    public StringBuilder visit(AssertStatement s, StringBuilder t) {
        t.append("assert ");
        t.append(s.condition);
        return t;
    }

    @Override
    public StringBuilder visit(AssignmentExpression s, StringBuilder t) {
        s.target.accept(this, t);
        t.append(' ').append((s.operation == null) ? "" : s.operation);
        t.append("= ");
        s.expression.accept(this, t);
        return t;
    }

    @Override
    public StringBuilder visit(BinaryExpression s, StringBuilder t) {
        s.expr1.accept(this, t);
        t.append(' ').append(s.operation).append(' ');
        s.expr2.accept(this, t);
        return t;
    }

    @Override
    public StringBuilder visit(BlockStatement s, StringBuilder t) {
        decreaseIndent();
        if (indent >= 0) {
            t.append('{');
        }
        increaseIndent();

        for (Statement statement : s.statements) {
            newLine(t);
            printIndent(t);
            statement.accept(this, t);
        }

        decreaseIndent();
        if (indent >= 0) {
            newLine(t);
            printIndent(t);
            t.append('}');
        }
        increaseIndent();
        return t;
    }

    @Override
    public StringBuilder visit(BreakStatement s, StringBuilder t) {
        t.append("break");
        return t;
    }

    @Override
    public StringBuilder visit(ClassDeclarationStatement s, StringBuilder t) {
        t.append("class ").append(s.name).append(" {");
        newLine(t);

        increaseIndent();
        for (AssignmentExpression field : s.fields) {
            field.accept(this, t);
        }
        for (FunctionDefineStatement method : s.methods) {
            method.accept(this, t);
        }
        decreaseIndent();
        t.append("}");
        return t;
    }

    @Override
    public StringBuilder visit(ConditionalExpression s, StringBuilder t) {
        s.expr1.accept(this, t);
        t.append(' ').append(s.operation.getName()).append(' ');
        s.expr2.accept(this, t);
        return t;
    }

    @Override
    public StringBuilder visit(ContainerAccessExpression s, StringBuilder t) {
        s.root.accept(this, t);
        for (Expression index : s.indices) {
            t.append('[');
            index.accept(this, t);
            t.append(']');
        }
        return t;
    }

    @Override
    public StringBuilder visit(ContinueStatement s, StringBuilder t) {
        t.append("continue");
        return t;
    }

    @Override
    public StringBuilder visit(DeclareConstStatement s, StringBuilder t) {
        t.append("const ");
        t.append(s.name);
        t.append(" : ");
        t.append(s.type);
        t.append(" = ");
        s.expression.accept(this, t);
        return t;
    }

    @Override
    public StringBuilder visit(DeclareVarStatement s, StringBuilder t) {
        t.append("var ");
        t.append(s.name);
        if (s.expression != null) {
            t.append(" = ");
            s.expression.accept(this, t);
        }
        return t;
    }
    

    @Override
    public StringBuilder visit(DoWhileStatement s, StringBuilder t) {
        t.append("do ");

        increaseIndent();
        s.statement.accept(this, t);
        decreaseIndent();

        t.append(" while (");
        s.condition.accept(this, t);
        t.append(")");
        return t;
    }

    @Override
    public StringBuilder visit(ExprStatement s, StringBuilder t) {
        s.expr.accept(this, t);
        return t;
    }

    @Override
    public StringBuilder visit(ForStatement s, StringBuilder t) {
        t.append("for (");
        s.initialization.accept(this, t);
        t.append(", ");
        s.termination.accept(this, t);
        t.append(", ");
        s.increment.accept(this, t);
        t.append(") ");

        increaseIndent();
        s.statement.accept(this, t);
        decreaseIndent();
        return t;
    }

    @Override
    public StringBuilder visit(ForeachArrayStatement s, StringBuilder t) {
        t.append("for (");
        visitVariable(s.variable, t);
        t.append(" : ");
        s.container.accept(this, t);
        t.append(") ");

        increaseIndent();
        s.body.accept(this, t);
        decreaseIndent();
        return t;
    }

    @Override
    public StringBuilder visit(ForeachMapStatement s, StringBuilder t) {
        t.append("for (");
        visitVariable(s.key, t);
        t.append(", ");
        visitVariable(s.value, t);
        t.append(" : ");
        s.container.accept(this, t);
        t.append(") ");

        increaseIndent();
        s.body.accept(this, t);
        decreaseIndent();
        return t;
    }

    @Override
    public StringBuilder visit(FunctionDefineStatement s, StringBuilder t) {
        t.append("def ");
        visitVariable(s.name, t);
        t.append(s.arguments);
        return visitFunctionBody(s.body, t);
    }

    @Override
    public StringBuilder visit(FunctionReferenceExpression s, StringBuilder t) {
        t.append("::");
        visitVariable(s.name, t);
        return t;
    }

    @Override
    public StringBuilder visit(FunctionalExpression s, StringBuilder t) {
        if (s.name instanceof ValueExpression
                && ((ValueExpression)s.name).value.type() == Types.STRING) {
            t.append(((ValueExpression)s.name).value.asString());
        } else {
            s.name.accept(this, t);
        }
        printArgs(t, s.arguments);
        return t;
    }

    @Override
    public StringBuilder visit(IfStatement s, StringBuilder t) {
        t.append("if (");
        s.expression.accept(this, t);
        t.append(") ");

        increaseIndent();
        s.ifStatement.accept(this, t);
        decreaseIndent();

        if (s.elseStatement != null) {
            newLine(t);
            printIndent(t);
            t.append("else ");
            s.elseStatement.accept(this, t);
        }
        return t;
    }

    @Override
    public StringBuilder visit(ImportStatement s, StringBuilder t) {
        t.append("import ");
        s.expression.accept(this, t);
        return t;
    }

    @Override
    public StringBuilder visit(MapExpression s, StringBuilder t) {
        if (s.elements.isEmpty()) {
            t.append("{ }");
            return t;
        }
        t.append('{');
        increaseIndent();
        boolean firstElement = true;
        for (Map.Entry<Expression, Expression> entry : s.elements.entrySet()) {
            if (firstElement) firstElement = false;
            else t.append(",");
            newLine(t);
            printIndent(t);
            entry.getKey().accept(this, t);
            t.append(" : ");
            entry.getValue().accept(this, t);
        }
        decreaseIndent();
        newLine(t);
        printIndent(t);
        t.append('}');
        return t;
    }
    
    @Override
    public StringBuilder visit(ObjectCreationExpression s, StringBuilder t) {
        t.append("new ").append(s.className);
        printArgs(t, s.constructorArguments);
        return t;
    }

    @Override
    public StringBuilder visit(PrintStatement s, StringBuilder t) {
        t.append("print ");
        s.expression.accept(this, t);
        return t;
    }

    @Override
    public StringBuilder visit(PrintlnStatement s, StringBuilder t) {
        t.append("println ");
        s.expression.accept(this, t);
        return t;
    }

    @Override
    public StringBuilder visit(ReadlnStatement s, StringBuilder t) {
        t.append("readln");
        return t;
    }

    @Override
    public StringBuilder visit(RepeatStatement s, StringBuilder t) {
        t.append("repeat (");
        s.condition.accept(this, t);
        t.append(") ");
        s.body.accept(this, t);
        return t;
    }

    @Override
    public StringBuilder visit(ReturnStatement s, StringBuilder t) {
        t.append("return ");
        s.expression.accept(this, t);
        return t;
    }

    @Override
    public StringBuilder visit(TernaryExpression s, StringBuilder t) {
        s.condition.accept(this, t);
        t.append(" ? ");
        s.trueExpr.accept(this, t);
        t.append(" : ");
        s.falseExpr.accept(this, t);
        return t;
    }

    @Override
    public StringBuilder visit(UnaryExpression s, StringBuilder t) {
        switch (s.operation) {
            case INCREMENT_POSTFIX:
            case DECREMENT_POSTFIX:
                s.expr1.accept(this, t);
                t.append(s.operation);
                break;
            default:
                t.append(s.operation);
                s.expr1.accept(this, t);
        }
        return t;
    }

    @Override
    public StringBuilder visit(UsingNamespaceStatement s, StringBuilder t) {
        t.append("using namespace ");
        t.append(s.namespace);
        return t;
    }

    @Override
    public StringBuilder visit(ValueExpression s, StringBuilder t) {
        switch (s.value.type()) {
            case Types.STRING:
                String str = s.value.raw().toString();
                str = str.replace("\n", "\\n");
                str = str.replace("\t", "\\t");
                t.append('"').append(str).append('"');
                break;
            case Types.FUNCTION:  {
                final Function function = ((FunctionValue) s.value).getValue();
                if (function instanceof UserDefinedFunction) {
                    UserDefinedFunction f = (UserDefinedFunction) function;
                    t.append("def");
                    t.append(f.arguments);
                    return visitFunctionBody(f.body, t);
                } else t.append(function);
                break;
            }
            default:
                t.append(s.value.raw());
                break;
        }
        return t;
    }

    @Override
    public StringBuilder visit(VariableExpression s, StringBuilder t) {
        return visitVariable(s.name, t);
    }

    @Override
    public StringBuilder visit(WhileStatement s, StringBuilder t) {
        t.append("while (");
        s.condition.accept(this, t);
        t.append(") ");

        increaseIndent();
        s.statement.accept(this, t);
        decreaseIndent();
        return t;
    }

    public StringBuilder visitNullableVariable(String name, StringBuilder t) {
        if (name == null) {
            t.append(' ');
            return t;
        }
        return visitVariable(name, t);
    }

    public StringBuilder visitVariable(String name, StringBuilder t) {
        boolean extendedWordVariable = false;
        for (char ch : name.toCharArray()) {
            if (!Character.isLetterOrDigit(ch)) {
                extendedWordVariable = true;
                break;
            }
        }
        if (extendedWordVariable) {
            t.append('`').append(name).append('`');
        } else {
            t.append(name);
        }
        return t;
    }

    private StringBuilder visitFunctionBody(Statement s, StringBuilder t) {
        if (s instanceof ReturnStatement) {
            t.append(" = ");
            ((ReturnStatement)s).expression.accept(this, t);
        } else {
            increaseIndent();
            s.accept(this, t);
            decreaseIndent();
        }
        return t;
    }
    
    private void printArgs(StringBuilder t, List<Expression> args) {
        t.append("(");
        boolean firstElement = true;
        for (Expression expr : args) {
            if (firstElement) firstElement = false;
            else t.append(", ");
            expr.accept(this, t);
        }
        t.append(")");
    }

    private void newLine(StringBuilder t) {
        t.append(Console.newline());
    }

    private void printIndent(StringBuilder sb) {
        for (int i = 0; i < indent; i++) {
            sb.append(' ');
        }
    }

    private void increaseIndent() {
        indent += 2;
    }

    private void decreaseIndent() {
        // Allow dedent to -2
        if (indent >= 0)
            indent -= 2;
    }
}
