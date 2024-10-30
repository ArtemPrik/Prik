package prik.parser.visitors;

import java.util.Map;
import prik.parser.ast.*;

/**
 *
 * @author Professional
 */
public abstract class AbstractVisitor implements Visitor {

    @Override
    public void visit(ArrayExpression s) {
        for (Expression index : s.elements) {
            index.accept(this);
        }
    }

    @Override
    public void visit(AssertStatement s) {
        s.condition.accept(this);
    }

    @Override
    public void visit(AssignmentExpression s) {
        s.expression.accept(this);
    }

    @Override
    public void visit(BinaryExpression s) {
        s.expr1.accept(this);
        s.expr2.accept(this);
    }

    @Override
    public void visit(BlockStatement s) {
        for (Statement statement : s.statements) {
            statement.accept(this);
        }
    }

    @Override
    public void visit(BreakStatement s) {
    }
    
    @Override
    public void visit(ClassDeclarationStatement s) {
    }

    @Override
    public void visit(ConditionalExpression s) {
        s.expr1.accept(this);
        s.expr2.accept(this);
    }
    
    @Override
    public void visit(ContainerAccessExpression s) {
        s.root.accept(this);
        for (Expression index : s.indices) index.accept(this);
    }

    @Override
    public void visit(ContinueStatement s) {
    }

    @Override
    public void visit(DeclareConstStatement s) {
        s.expression.accept(this);
    }
    
    @Override
    public void visit(DeclareVarStatement s) {
        if (s.expression != null) s.expression.accept(this);
    }

    @Override
    public void visit(DoWhileStatement s) {
        s.condition.accept(this);
        s.statement.accept(this);
    }

    @Override
    public void visit(ExprStatement s) {
        s.expr.accept(this);
    }

    @Override
    public void visit(ForStatement s) {
        s.initialization.accept(this);
        s.termination.accept(this);
        s.increment.accept(this);
        s.statement.accept(this);
    }

    @Override
    public void visit(ForeachArrayStatement s) {
        s.body.accept(this);
        s.container.accept(this);
    }

    @Override
    public void visit(ForeachMapStatement s) {
        s.body.accept(this);
        s.container.accept(this);
    }

    @Override
    public void visit(FunctionDefineStatement s) {
        s.body.accept(this);
    }

    @Override
    public void visit(FunctionReferenceExpression s) {
    }

    @Override
    public void visit(FunctionalExpression s) {
        for (Expression argument : s.arguments) {
            argument.accept(this);
        }
    }

    @Override
    public void visit(IfStatement s) {
        s.expression.accept(this);
        s.ifStatement.accept(this);
        if (s.elseStatement != null) {
            s.elseStatement.accept(this);
        }
    }

    @Override
    public void visit(ImportStatement s) {
        s.expression.accept(this);
    }
    
    @Override
    public void visit(MapExpression s) {
        for (Map.Entry<Expression, Expression> entry : s.elements.entrySet()) {
            entry.getKey().accept(this);
            entry.getValue().accept(this);
        }
    }
    
    @Override
    public void visit(ObjectCreationExpression s) {
        for (Expression argument : s.constructorArguments) {
            argument.accept(this);
        }
    }

    @Override
    public void visit(PrintStatement s) {
        s.expression.accept(this);
    }
    
    @Override
    public void visit(PrintlnStatement s) {
        s.expression.accept(this);
    }

    @Override
    public void visit(ReadlnStatement s) {
    }

    @Override
    public void visit(RepeatStatement s) {
        s.condition.accept(this);
        s.body.accept(this);
    }

    @Override
    public void visit(ReturnStatement s) {
        s.expression.accept(this);
    }

    @Override
    public void visit(TernaryExpression s) {
        s.condition.accept(this);
        s.trueExpr.accept(this);
        s.falseExpr.accept(this);
    }
    
    @Override
    public void visit(UnaryExpression s) {
        s.expr1.accept(this);
    }
    
    @Override
    public void visit(UseStatement s) {
        if (s.expression != null) s.expression.accept(this);
    }

    @Override
    public void visit(ValueExpression s) {
    }

    @Override
    public void visit(VariableExpression s) {
    }

    @Override
    public void visit(WhileStatement s) {
        s.condition.accept(this);
        s.statement.accept(this);
    }
}
