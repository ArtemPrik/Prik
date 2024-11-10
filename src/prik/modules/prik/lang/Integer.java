package prik.modules.prik.lang;

import prik.lib.Arguments;
import prik.lib.Function;
import prik.lib.Functions;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Types;
import prik.lib.Value;
import prik.lib.Variables;
import prik.modules.Module;

/**
 *
 * @author Professional
 */
public class Integer implements Module {
    @Override
    public void init() {
        MapValue map = new MapValue(11);
        
        map.set("MIN_VALUE", new NumberValue(java.lang.Integer.MIN_VALUE));
        map.set("MAX_VALUE", new NumberValue(java.lang.Integer.MAX_VALUE));
        map.set("toHexString", (Value... args) -> {
            Arguments.check(1, args.length);
            int value;
            if (args[0].type() == Types.NUMBER) {
                value = args[0].asInt();
            } else {
                value = args[0].asInt();
            }
            return new StringValue(java.lang.Integer.toHexString(value));
        });
        map.set("toOctalString", (Value... args) -> {
            Arguments.check(1, args.length);
            int value;
            if (args[0].type() == Types.NUMBER) {
                value = args[0].asInt();
            } else {
                value = args[0].asInt();
            }
            return new StringValue(java.lang.Integer.toOctalString(value));
        });
        map.set("toBinaryString", (Value... args) -> {
            Arguments.check(1, args.length);
            int value;
            if (args[0].type() == Types.NUMBER) {
                value = args[0].asInt();
            } else {
                value = args[0].asInt();
            }
            return new StringValue(java.lang.Integer.toBinaryString(value));
        });
        map.set("parseInt", (Value... args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            final int radix = (args.length == 2) ? args[1].asInt() : 10;
            return NumberValue.of(java.lang.Integer.parseInt(args[0].asString(), radix));
        });
        map.set("valueOf", (Value... args) -> {
            Arguments.checkOrOr(1, 2, args.length);
            final int radix = (args.length == 2) ? args[1].asInt() : 10;
            return NumberValue.of(java.lang.Integer.parseInt(args[0].asString(), radix));
        });
        map.set("max", (Value... args) -> {
            Arguments.check(2, args.length);
            return new NumberValue(java.lang.Integer.max(
                    args[0].asInt(), args[1].asInt()));
        });
        map.set("min", (Value... args) -> {
            Arguments.check(2, args.length);
            return new NumberValue(java.lang.Integer.min(
                    args[0].asInt(), args[1].asInt()));
        });
        map.set("signum", (Value... args) -> {
            Arguments.check(1, args.length);
            return NumberValue.of(java.lang.Integer.signum(args[0].asInt()));
        });
        map.set("bitCount", (Value... args) -> {
            Arguments.check(1, args.length);
            return NumberValue.of(java.lang.Integer.signum(args[0].asInt()));
        });
        
        Variables.define("Integer", map);
    }
}
