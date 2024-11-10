package prik.modules.prik.awt;

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
public final class Colors implements Module {
    @Override
    public void init() {
        MapValue map = new MapValue(5);
        
        map.set("reset", new StringValue("\\u001b[10m"));
        map.set("red", new StringValue("\\u001b[31m"));
        map.set("green", new StringValue("\\u001b[32m"));
        map.set("blue", new StringValue("\\u001b[34m"));
        map.set("while", new StringValue("\\u001b[37m"));
        map.set("black", new StringValue("\\u001b[40m"));
        map.set("purple", new StringValue("\\u001b[35m"));
        map.set("yellow", new StringValue("\\u001b[33m"));
        map.set("cyan", new StringValue("\\u001b[36m"));
        map.set("clear", (Value... args) -> {
            System.out.print("\u001b[H\u001b[2J");
            System.out.flush();
            return NumberValue.ZERO;
        });
        
        Variables.define("Colors", map);
    }
}
