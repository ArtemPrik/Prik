package prik.lib;

import java.util.Objects;
import prik.exceptions.TypeException;


/**
 *
 * @author Professional
 */
public class ClassInstanceValue implements Value {
    private final String className;
    private final MapValue thisMap;
    private ClassMethod constructor;

    public ClassInstanceValue(String name) {
        this.className = name;
        thisMap = new MapValue(10);
    }

    public MapValue getThisMap() {
        return thisMap;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void addField(String name, Value value) {
        thisMap.set(name, value);
    }
    
    public void addMethod(String name, ClassMethod method) {
        thisMap.set(name, method);
        if (name.equals(className)) {
            constructor = method;
        }
    }
    
    public void callConstructor(Value[] args) {
        if (constructor != null) {
            constructor.execute(args);
        }
    }
    
    public Value access(Value value) {
        return thisMap.get(value);
    }

    @Override
    public Object raw() {
        return null;
    }

    @Override
    public int asInt() {
        throw new TypeException("Cannot cast class to integer");
    }

    @Override
    public double asNumber() {
        throw new TypeException("Cannot cast class to integer");
    }

    @Override
    public String asString() {
        return "class " + className + "@" + thisMap;
    }

    @Override
    public int type() {
        return Types.CLASS;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hash(className, thisMap);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final ClassInstanceValue other = (ClassInstanceValue) obj;
        return Objects.equals(this.className, other.className)
                && Objects.equals(this.thisMap, other.thisMap);
    }

    @Override
    public int compareTo(Value o) {
        return asString().compareTo(o.asString());
    }
    
    @Override
    public String toString() {
        return asString();
    }
}
