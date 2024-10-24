package prik.lib;

/**
 *
 * @author Professional
 */
public interface Value extends Comparable<Value> {
    double asNumber();
    String asString();
    int asInt();
    int type();
    Object raw();
}
