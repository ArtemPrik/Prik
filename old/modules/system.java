package prik.lib.modules;

import java.util.Objects;
import prik.lib.Arguments;
import prik.lib.Function;
import prik.lib.Functions;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Value;
import prik.lib.Variables;


/**
 *
 * @author Professional
 */
public final class system implements Module {
    @Override
    public void init() {
//        Variables.set("time", NumberValue.of((double)System.currentTimeMillis()));
//        Variables.set("currentTimeMillis", NumberValue.of((double)System.currentTimeMillis()));
//        Variables.set("nanoTime", NumberValue.of((double) System.nanoTime()));

        Functions.set("currentTimeMillis", args -> {
            Arguments.check(0, args.length);
            return NumberValue.of((double)System.currentTimeMillis());
        });
        
        Functions.set("nanoTime", args -> {
            Arguments.check(0, args.length);
            return NumberValue.of((double)System.nanoTime());
        });
        
        Functions.set("exit", (Value... args) -> {
            Arguments.check(1, args.length);
            try {
                System.exit(args[0].asInt());
            } finally {
                Thread.currentThread().interrupt();
            }
            return NumberValue.MINUS_ONE;
        });
        
        
/*        MapValue system = new MapValue(1);
        system.set("exit", (Value... args) -> {
            Arguments.check(1, args.length);
            try {
                System.exit(args[0].asInt());
            } finally {
                Thread.currentThread().interrupt();
            }
            return NumberValue.MINUS_ONE;
        });
        Variables.define("system", system);*/
        
        
        Functions.set("getProperty", (Value... args) -> {
            Arguments.check(1, args.length);
            if(Objects.equals(args[0].asString(), "prik.version")){
                return new StringValue("beta_001");
            }
            return new StringValue(System.getProperty(args[0].asString()));
        });
        
        Functions.set("getUsedMemory", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        });
        Functions.set("getTotalMemory", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue((Runtime.getRuntime().totalMemory()));
        });
        Functions.set("getMaxMemory", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue((Runtime.getRuntime().maxMemory()));
        });
        Functions.set("getFreeMemory", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue((Runtime.getRuntime().freeMemory()));
        });
        Functions.set("availableProcessors", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue((Runtime.getRuntime().availableProcessors()));
        });
    }
}
