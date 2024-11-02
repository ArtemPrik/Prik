package prik.parser.optimization;

import prik.lib.Value;

/**
 *
 * @author Professional
 */
public class VariableInfo {
    public Value value;
    int modifications;

    @Override
    public String toString() {
        return (value == null ? "?" : value) + " (" + modifications + " mods)";
    }
}
