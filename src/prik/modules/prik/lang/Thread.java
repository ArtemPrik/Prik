package prik.modules.prik.lang;

import prik.lib.Arguments;
import prik.lib.Functions;
import prik.lib.NumberValue;
import prik.modules.Module;

/**
 *
 * @author Professional
 */
public final class Thread implements Module {
    @Override
    public void init() {
        Functions.set("sleep", args -> {
            Arguments.check(1, args.length);
            if (args.length == 1) {
                try {
                    java.lang.Thread.sleep((long) args[0].asNumber());
                } catch (InterruptedException ex) {
                    java.lang.Thread.currentThread().interrupt();
                }
            }
            return NumberValue.ZERO;
        });
        Functions.set("thread", args -> {
            if (args.length == 1) {
                new java.lang.Thread(() -> {
                    Functions.get(args[0].asString()).execute();
                }).start();
            }
            return NumberValue.ZERO;
        });
    }
}
