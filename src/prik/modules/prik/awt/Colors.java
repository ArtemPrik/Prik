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
        
        Variables.set("reset", new StringValue("\\u001b[10m"));
        Variables.set("red", new StringValue("\\u001b[31m"));
        Variables.set("green", new StringValue("\\u001b[32m"));
        Variables.set("blue", new StringValue("\\u001b[34m"));
        Variables.set("while", new StringValue("\\u001b[37m"));
        Variables.set("black", new StringValue("\\u001b[40m"));
        Variables.set("purple", new StringValue("\\u001b[35m"));
        Variables.set("yellow", new StringValue("\\u001b[33m"));
        Variables.set("cyan", new StringValue("\\u001b[36m"));
        Functions.set("clear", (Value... args) -> {
            System.out.print("\u001b[H\u001b[2J");
            System.out.flush();
            return NumberValue.ZERO;
        });
        
        Variables.define("Colors", map);
    }
}
