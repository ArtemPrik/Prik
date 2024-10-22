package prik.lib.modules.prik.lang;

import prik.lib.Arguments;
import prik.lib.Function;
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
public final class StringBuilder implements Module {
    static java.lang.StringBuilder builder = new java.lang.StringBuilder();
    
    @Override
    public void init() {
        MapValue builderMap = new MapValue(4);
        
        builderMap.set("append", (Value... args) -> {
            Arguments.check(1, args.length);
            builder.append(args[0]);
            return NumberValue.ZERO;
        });
        
        builderMap.set("appendCodePoint", (Value... args) -> {
            Arguments.check(1, args.length);
            builder.appendCodePoint(args[0].asInt());
            return NumberValue.ZERO;
        });
        
        builderMap.set("delete", (Value... args) -> {
            Arguments.check(1,  args.length);
            builder.delete(args[0].asInt(), args[1].asInt());
            return NumberValue.ZERO;
        });
        
        builderMap.set("deleteCharAt", (Value... args) -> {
            Arguments.check(1,  args.length);
            builder.deleteCharAt(args[0].asInt());
            return NumberValue.ZERO;
        });
        
        builderMap.set("replace", (Value... args) -> {
            Arguments.check(3, args.length);
            builder.replace(args[0].asInt(), args[1].asInt(), args[2].asString());
            return NumberValue.ZERO;
        });
        
        builderMap.set("reverse", (Value... args) -> {
            Arguments.check(0, args.length);
            builder.reverse();
            return NumberValue.ZERO;
        });
        
        builderMap.set("toString", (Value... args) -> {
            return new StringValue(builder.toString());
        });
        
        
        MapValue map = new MapValue(1);
        map.set("`new`", (Value... args) -> {
            Arguments.check(0, args.length);
            builder = new java.lang.StringBuilder();
            return builderMap;
        });
        Variables.define("StringBuilder", map);
    }
}
