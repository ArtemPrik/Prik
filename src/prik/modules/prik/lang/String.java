package prik.modules.prik.lang;

import prik.lib.Arguments;
import prik.lib.Function;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.Value;
import prik.lib.Variables;
import prik.modules.Module;

/**
 *
 * @author Professional
 */
public final class String implements Module {
    @Override
    public void init() {
        MapValue map = new MapValue(0);
        
        
        
        Variables.define("String", map);
    }
}
