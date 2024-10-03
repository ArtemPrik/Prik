package prik.parser.ast;

/**
 *
 * @author Professional
 */
public interface Visitor {
    void visit(ArrayAccessExpression s);
    void visit(ArrayAssignmentExpression s);
    void visit(ArrayAssignmentStatement s);
    void visit(ArrayExpression s);
    void visit(AssignmentStatement s);
    void visit(AssignmentExpression s);
    void visit(BinaryExpression s);
    void visit(BlockStatement s);
    void visit(BreakStatement s);
    void visit(ConditionalExpression s);
    void visit(ContainerAccessExpression s);
    void visit(ContinueStatement s);
    void visit(DeclareVarStatement s);
    void visit(DoWhileStatement s);
    void visit(ExprStatement s);
    void visit(ForStatement s);
    void visit(ForeachArrayStatement s);
    void visit(ForeachMapStatement s);
    void visit(FunctionDefineStatement s);
    void visit(FunctionReferenceExpression s);
    void visit(FunctionalExpression s);
    void visit(IfStatement s);
    void visit(MapExpression s);
    void visit(PrintStatement s);
    void visit(PrintlnStatement s);
    void visit(RepeatStatement s);
    void visit(ReturnStatement s);
    void visit(TernaryExpression s);
    void visit(UnaryExpression s);
    void visit(UseStatement st);
    void visit(ValueExpression s);
    void visit(VariableExpression s);
    void visit(WhileStatement st);
}
