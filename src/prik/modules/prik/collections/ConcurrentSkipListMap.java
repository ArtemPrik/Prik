package prik.modules.prik.collections;

import prik.lib.Functions;
import prik.modules.Module;
import static prik.modules.prik.collections.mapFunctions.sortedMapFunction;

/**
 *
 * @author Professional
 */
public class ConcurrentSkipListMap implements Module {
    @Override
    public void init() {
        Functions.set("concurrentSkipListMap", sortedMapFunction(
                java.util.concurrent.ConcurrentSkipListMap::new, 
                java.util.concurrent.ConcurrentSkipListMap::new));
    }
}
