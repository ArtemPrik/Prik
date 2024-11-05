package prik.modules.prik.util;

import prik.lib.ArrayValue;
import prik.lib.Function;
import prik.lib.Functions;
import prik.lib.NumberValue;
import prik.lib.Value;
import prik.modules.Module;

/**
 *
 * @author Professional
 */
public final class Arrays implements Module {
    @Override
    public void init() {
        Functions.set("newArray", new Function() {
            @Override
            public Value execute(Value... args) {
                return createArray(args, 0);
            }
            private ArrayValue createArray(Value[] args, int index) {
                final int size = (int) args[index].asNumber();
                final int last = args.length - 1;
                ArrayValue array = new ArrayValue(size);
                if (index == last) {
                    for (int i = 0; i < size; i++) {
                        array.set(i, NumberValue.ZERO);
                    }
                } else if (index < last) {
                    for (int i = 0; i < size; i++) {
                        array.set(i, createArray(args, index + 1));
                    }
                }
                return array;
            }
        });
    }
}
