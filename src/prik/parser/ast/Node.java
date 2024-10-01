package prik.parser.ast;

/**
 *
 * @author Professional
 */
public interface Node {
    void accept(Visitor visitor);
    
    
//    <R, T> R accept(ResultVisitor<R, T> visitor, T input);
}
