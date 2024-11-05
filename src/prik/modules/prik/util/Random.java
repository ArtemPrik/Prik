package prik.modules.prik.util;

import prik.lib.Arguments;
import prik.lib.BooleanValue;
import prik.lib.Function;
import prik.lib.Functions;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.Value;
import prik.lib.Variables;
import prik.modules.Module;

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
        
        Functions.set("rand", new Function() {
            private static final java.util.Random RND = new java.util.Random();
            
            @Override
            public Value execute(Value... args) {
                if (args.length == 0) return new NumberValue(RND.nextDouble());
            
                int from = 0;
                int to = 100;
                if (args.length == 1) {
                    to = (int) args[0].asNumber();
                } else if (args.length == 2) {
                    from = (int) args[0].asNumber();
                    to = (int) args[1].asNumber();
                }
                return new NumberValue(RND.nextInt(to - from) + from);
            }
        });
    }
}
