package prik.lib;

/**
 *
 * @author Professional
 */
public interface Value {
    double asNumber();
    String asString();
    int asInt();
    int type();
    Object raw();
    int compareTo(Value o);
}
