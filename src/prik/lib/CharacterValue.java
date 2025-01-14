package prik.lib;

/**
 *
 * @author Professional
 */
public class CharacterValue implements Value {
    public final char value;

    public CharacterValue(char value) {
        this.value = value;
    }
    
    @Override
    public double asNumber() {
        return value;
    }

    @Override
    public String asString() {
        return Character.toString(value);
    }

    @Override
    public int asInt() {
        return value;
    }

    @Override
    public int type() {
        return Types.CHARACTER;
    }

    @Override
    public Object raw() {
        return value;
    }

    @Override
    public int compareTo(Value o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String toString() {
        return asString();
    }
}
