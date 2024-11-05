package prik.modules.prik.collections;

import prik.lib.Functions;
import prik.modules.Module;
import static prik.modules.prik.collections.mapFunctions.mapFunction;

/**
 *
 * @author Professional
 */
public class LinkedHashMap implements Module {
    @Override
    public void init() {
        Functions.set("linkedHashMap", mapFunction(java.util.LinkedHashMap::new));
    }
}
