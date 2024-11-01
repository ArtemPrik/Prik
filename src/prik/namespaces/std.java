package prik.namespaces;

import prik.lib.Function;
import prik.lib.Functions;
import prik.lib.Value;

/**
 *
 * @author Professional
 */
public class std implements Namespace {
    @Override
    public void init() {
        Functions.set("stdout", new Function() {
            @Override
            public Value execute(Value... args) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        });
    }
}
