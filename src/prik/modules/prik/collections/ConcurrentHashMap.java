package prik.modules.prik.collections;

import prik.lib.Functions;
import prik.modules.Module;
import static prik.modules.prik.collections.mapFunctions.mapFunction;

/**
 *
 * @author Professional
 */
public class ConcurrentHashMap implements Module {
    @Override
    public void init() {
        Functions.set("concurrentHashMap", mapFunction(java.util.concurrent.ConcurrentHashMap::new));
    }
}
