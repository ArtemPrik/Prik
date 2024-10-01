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
    
    VAR,
    
    TRUE,
    FALSE,
    
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
    
    NULL,
    REPEAT,
    
    USE,
    USING,
    
    PLUS,
    MINUS, 
    STAR, 
    SLASH, 
    PERCENT,
    
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
    
    LTLT,
    GTGT,
    GTGTGT,
    
    PLUSPLUS,
    MINUSMINUS,
    
    TILDE, 
    CARET, 
    BAR, 
    BARBAR, 
    AMP, 
    AMPAMP, 
    
    QUESTION, 
    COLON,
    COLONCOLON,
    SEMICOLON,
    ARROW,
    
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
