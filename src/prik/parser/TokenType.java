package prik.parser;

/**
 *
 * @author Professional
 */
public enum TokenType {
    NUMBER,
    HEX_NUMBER,
    WORD,
    TEXT,
    
    TRUE,
    FALSE,
    
    NUMBER_DATA,
    STRING_DATA,
    BOOLEAN_DATA,
    ANY_DATA,
    
    PRINT,
    PRINTLN,
    IF,
    ELSE,
    WHILE,
    FOR,
    DO,
    BREAK,
    CONTINUE,
    DEF,
    RETURN,
    CLASS,
    NEW,
    IMPORT,
    
    NULL,
    REPEAT,
    READLN,
    VAR,
    CONST,
    
    ASSERT,
    
    USING,
    NAMESPACE,
    
    PLUS,
    MINUS, 
    STAR, 
    SLASH, 
    PERCENT,
    AT,
    
    EQ,
    EQEQ,
    EXCL,
    EXCLEQ,
    LTEQ,
    LT,
    GT,
    GTEQ,
    
    PLUSEQ, // +=
    MINUSEQ, // -=
    STAREQ, // *=
    SLASHEQ, // /=
    PERCENTEQ, // %=
    AMPEQ, // &=
    CARETEQ, // ^=
    BAREQ, // |=
    COLONCOLONEQ, // ::=
    LTLTEQ, // <<=
    GTGTEQ, // >>=
    GTGTGTEQ, // >>>=
    
    PLUSPLUS,
    MINUSMINUS,
    
    LTLT,
    GTGT,
    GTGTGT,
    
    DOTDOT, // ..
    STARSTAR, // **
    QUESTIONCOLON, // ?:
    QUESTIONQUESTION,
    
    TILDE,
    CARET,
    CARETCARET,
    BAR,
    BARBAR,
    AMP,
    AMPAMP,
    
    QUESTION, 
    COLON,
    COLONCOLON,
    SEMICOLON,
    
    LPAREN, 
    RPAREN, 
    LBRACKET, 
    RBRACKET, 
    LBRACE, 
    RBRACE, 
    COMMA,
    DOT,
    
    EOF
}
