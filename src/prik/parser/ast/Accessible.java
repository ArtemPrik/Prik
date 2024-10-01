package prik.parser.ast;

import prik.lib.Value;

/**
 *
 * @author Professional
 */
public interface Accessible extends Node {
    Value get();
    
    Value set(Value value);
}
