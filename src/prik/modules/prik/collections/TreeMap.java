package prik.modules.prik.collections;

import prik.lib.Functions;
import prik.modules.Module;
import static prik.modules.prik.collections.mapFunctions.sortedMapFunction;

/**
 *
 * @author Professional
 */
public class TreeMap implements Module {
    @Override
    public void init() {
        Functions.set("treeMap", sortedMapFunction(
                java.util.TreeMap::new, java.util.TreeMap::new));
    }
}
