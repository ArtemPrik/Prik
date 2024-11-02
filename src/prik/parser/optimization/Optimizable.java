package prik.parser.optimization;

import prik.parser.ast.Node;

/**
 *
 * @author Professional
 */
public interface Optimizable {
    Node optimize(Node node);

    int optimizationsCount();

    String summaryInfo();
}
