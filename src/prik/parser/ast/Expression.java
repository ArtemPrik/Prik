package prik.parser.ast;

import prik.lib.Value;


/**
 *
 * @author Professional
 */
public interface Expression extends Node {
    Value eval();
}
