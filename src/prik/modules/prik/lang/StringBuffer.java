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
public final class StringBuffer implements Module {
    static java.lang.StringBuffer buffer = new java.lang.StringBuffer();
    
    @Override
    public void init() {
        MapValue bufferMap = new MapValue(4);
        
        bufferMap.set("append", (Value... args) -> {
            Arguments.check(1, args.length);
            buffer.append(args[0]);
            return NumberValue.ZERO;
        });
        
        bufferMap.set("appendCodePoint", (Value... args) -> {
            Arguments.check(1, args.length);
            buffer.appendCodePoint(args[0].asInt());
            return NumberValue.ZERO;
        });
        
        bufferMap.set("delete", (Value... args) -> {
            Arguments.check(2, args.length);
            buffer.delete(args[0].asInt(), args[1].asInt());
            return NumberValue.ZERO;
        });
        
        MapValue map = new MapValue(1);
        map.set("`new`", (Value... args) -> {
            Arguments.check(0, args.length);
            buffer = new java.lang.StringBuffer();
            return bufferMap;
        });
        Variables.define("StringBuffer", map);
    }
}
