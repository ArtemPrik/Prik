package prik.lib;

import java.util.Map;
import prik.exceptions.TypeException;

/**
 *
 * @author Professional
 */
public class EnumValue implements Value {
    public final MapValue enumMap;

    public EnumValue(MapValue enumMap) {
        this.enumMap = enumMap;
    }
    
    @Override
    public double asNumber() {
        throw new TypeException("Cannot cast enum to number");
    }

    @Override
    public String asString() {
        return enumMap.asString();
    }

    @Override
    public int asInt() {
        throw new TypeException("Cannot cast enum to integer");
    }

    @Override
    public int type() {
        return Types.ENUM;
    }

    @Override
    public Object raw() {
        return enumMap;
    }

    @Override
    public int compareTo(Value o) {
        return asString().compareTo(o.asString());
    }
    
    public ArrayValue toPairs(Map<Value, Value> map) {
        final int size = map.size();
        final ArrayValue result = new ArrayValue(size);
        int index = 0;
        for (Map.Entry<Value, Value> entry : map.entrySet()) {
            result.set(index++, 
                entry.getKey()
            );
        }
        return result;
    }

    @Override
    public String toString() {
        return asString();
    }
}
