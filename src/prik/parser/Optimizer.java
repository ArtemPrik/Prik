package prik.parser;

import prik.Console;
import prik.parser.ast.Node;
import prik.parser.ast.Statement;
import prik.parser.optimization.ConstantFolding;
import prik.parser.optimization.ConstantPropagation;
import prik.parser.optimization.DeadCodeElimination;
import prik.parser.optimization.ExpressionSimplification;
import prik.parser.optimization.InstructionCombining;
import prik.parser.optimization.Optimizable;
import prik.parser.optimization.SummaryOptimization;

/**
 *
 * @author Professional
 */
public class Optimizer {
    private Optimizer() { }

    public static Statement optimize(Statement statement, int level, boolean showSummary) {
        if (level == 0) return statement;

        final Optimizable optimization = new SummaryOptimization(new Optimizable[] {
            new ConstantFolding(),
            new ConstantPropagation(),
            new DeadCodeElimination(),
            new ExpressionSimplification(),
            new InstructionCombining()
        });

        Node result = statement;
        if (level >= 9) {
            int iteration = 0, lastModifications = 0;
            do {
                lastModifications = optimization.optimizationsCount();
                result = optimization.optimize(result);
                iteration++;
            } while (lastModifications != optimization.optimizationsCount());
            if (showSummary)
                Console.print("Performs " + iteration + " optimization iterations");
        } else {
            for (int i = 0; i < level; i++) {
                result = optimization.optimize(result);
            }
        }
        if (showSummary) {
            Console.println(optimization.summaryInfo());
        }
        return (Statement) result;
    }
}
