package prik.modules.prik.lang;

import java.util.Objects;
import prik.lib.Arguments;
import prik.lib.Function;
import prik.lib.Functions;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Value;
import prik.lib.Variables;
import prik.modules.Module;

/**
 *
 * @author Professional
 */
public final class System implements Module {
    @Override
    public void init() {
        MapValue system = new MapValue(11);
        system.set("time", NumberValue.of((double)java.lang.System.currentTimeMillis()));

        system.set("currentTimeMillis", args -> {
            Arguments.check(0, args.length);
            return NumberValue.of((double)java.lang.System.currentTimeMillis());
        });
        
        system.set("nanoTime", args -> {
            Arguments.check(0, args.length);
            return NumberValue.of((double)java.lang.System.nanoTime());
        });
        
        system.set("exit", (Value... args) -> {
            Arguments.check(1, args.length);
            try {
                java.lang.System.exit(args[0].asInt());
            } finally {
                java.lang.Thread.currentThread().interrupt();
            }
            return NumberValue.MINUS_ONE;
        });
        
        system.set("getProperty", (Value... args) -> {
            Arguments.check(1, args.length);
            if(Objects.equals(args[0].asString(), "prik.version")){
                return new StringValue("beta_001");
            }
            return new StringValue(java.lang.System.getProperty(args[0].asString()));
        });
        
        system.set("getUsedMemory", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        });
        system.set("getTotalMemory", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue((Runtime.getRuntime().totalMemory()));
        });
        system.set("getMaxMemory", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue((Runtime.getRuntime().maxMemory()));
        });
        system.set("getFreeMemory", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue((Runtime.getRuntime().freeMemory()));
        });
        system.set("availableProcessors", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue((Runtime.getRuntime().availableProcessors()));
        });
        
        Variables.define("System", system);
    }
}
