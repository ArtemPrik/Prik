package prik.lib.modules.prik.lang;

import prik.lib.Arguments;
import prik.lib.BooleanValue;
import prik.lib.Function;
import prik.lib.MapValue;
import prik.lib.Value;
import prik.lib.Variables;
import prik.lib.modules.Module;

/**
 *
 * @author Professional
 */
public final class Boolean implements Module {
    @Override
    public void init() {
        MapValue map = new MapValue(1);
        
        map.set("parseBoolean", (Value... args) -> {
            Arguments.check(1, args.length);
            return new BooleanValue(
                    java.lang.Boolean.parseBoolean(args[0].asString()));
        });
        
        Variables.define("Boolean", map);
    }
}
