package prik.parser.optimization;

import prik.exceptions.OperationNotSupportedException;
import prik.parser.Optimizer;
import prik.parser.ast.*;

/**
 *
 * @author Professional
 */
public final class ConstantFolding extends OptimizationVisitor<Void> implements Optimizer.Info {
    private int binaryExpressionFoldingCount;
    private int conditionalExpressionFoldingCount;
    private int unaryExpressionFoldingCount;

    public ConstantFolding() {
        binaryExpressionFoldingCount = 0;
        conditionalExpressionFoldingCount = 0;
        unaryExpressionFoldingCount = 0;
    }

    @Override
    public int optimizationsCount() {
        return binaryExpressionFoldingCount
                + conditionalExpressionFoldingCount
                + unaryExpressionFoldingCount;
    }

    @Override
    public String summaryInfo() {
        if (optimizationsCount() == 0) return "";
        final StringBuilder sb = new StringBuilder();
        if (binaryExpressionFoldingCount > 0) {
            sb.append("\nBinaryExpression foldings: ").append(binaryExpressionFoldingCount);
        }
        if (conditionalExpressionFoldingCount > 0) {
            sb.append("\nConditionalExpression foldings: ").append(conditionalExpressionFoldingCount);
        }
        if (unaryExpressionFoldingCount > 0) {
            sb.append("\nUnaryExpression foldings: ").append(unaryExpressionFoldingCount);
        }
        return sb.toString();
    }

    @Override
    public Node visit(BinaryExpression s, Void t) {
        if ( (s.expr1 instanceof ValueExpression) && (s.expr2 instanceof ValueExpression) ) {
            binaryExpressionFoldingCount++;
            try {
                return new ValueExpression(s.eval());
            } catch (OperationNotSupportedException op) {
                binaryExpressionFoldingCount--;
            }
        }
        return super.visit(s, t);
    }

    @Override
    public Node visit(ConditionalExpression s, Void t) {
        if ( (s.expr1 instanceof ValueExpression) && (s.expr2 instanceof ValueExpression) ) {
            conditionalExpressionFoldingCount++;
            try {
                return new ValueExpression(s.eval());
            } catch (OperationNotSupportedException op) {
                conditionalExpressionFoldingCount--;
            }
        }
        return super.visit(s, t);
    }

    @Override
    public Node visit(UnaryExpression s, Void t) {
        if (s.expr1 instanceof ValueExpression) {
            unaryExpressionFoldingCount++;
            try {
                return new ValueExpression(s.eval());
            } catch (OperationNotSupportedException op) {
                unaryExpressionFoldingCount--;
            }
        }
        return super.visit(s, t);
    }
}
