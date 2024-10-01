package prik.lib.modules.prik.util;

import prik.lib.Arguments;
import prik.lib.BooleanValue;
import prik.lib.Function;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.Value;
import prik.lib.Variables;
import prik.lib.modules.Module;

/**
 *
 * @author Professional
 */
public final class Random implements Module {
    @Override
    public void init() {
        MapValue map = new MapValue(0);
        
        java.util.Random random = new java.util.Random();
        
        map.set("nextBoolean", (Value... args) -> {
            Arguments.check(0, args.length);
            return new BooleanValue(random.nextBoolean());
        });
        
        map.set("nextFloat", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue(random.nextFloat());
        });
        map.set("nextDouble", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue(random.nextDouble());
        });
        map.set("nextGaussian", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue(random.nextGaussian());
        });
        
        Variables.define("Random", map);
    }
}
