package prik.lib.modules.prik.lang;

import prik.lib.MapValue;
import prik.lib.Variables;
import prik.lib.modules.Module;

/**
 *
 * @author Professional
 */
public final class Number implements Module {
    @Override
    public void init() {
        MapValue map = new MapValue(1);
        
        
        
        Variables.define("Number", map);
    }
}
