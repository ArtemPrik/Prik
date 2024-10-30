package prik.lib.modules.prik.util;

import prik.lib.Arguments;
import prik.lib.BooleanValue;
import prik.lib.Function;
import prik.lib.Functions;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Value;
import prik.lib.Variables;
import prik.lib.modules.Module;

/**
 *
 * @author Professional
 */
public final class Scanner implements Module {
    @Override
    public void init() {
        MapValue scanner = new MapValue(10);
        
        java.util.Scanner sc = new java.util.Scanner(System.in);
        
        Functions.set("scanDouble", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue(sc.nextDouble());
        });
        scanner.set("scanInt", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue(sc.nextInt());
        });
        scanner.set("scanFloat", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue(sc.nextFloat());
        });
        scanner.set("scanLong", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue(sc.nextLong());
        });
        scanner.set("scanShort", (Value... args) -> {
            Arguments.check(0, args.length);
            return new NumberValue(sc.nextShort());
        });
        scanner.set("scanBoolean", (Value... args) -> {
            Arguments.check(0, args.length);
            return new BooleanValue(sc.nextBoolean());
        });
        
        scanner.set("scanString", (Value... args) -> {
            Arguments.check(0, args.length);
            return new StringValue(sc.nextLine());
        });
        
        Variables.define("Scanner", scanner);
    }
}
