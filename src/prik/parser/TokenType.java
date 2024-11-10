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
    
    LONG_NUMBER,
    HEX_LONG_NUMBER,
    INT_NUMBER,
    DOUBLE_NUMBER,
    FLOAT_NUMBER,
    BYTE_NUMBER,
    SHORT_NUMBER,
    DECIMAL_NUMBER,
    
    TRUE,
    FALSE,
    
    INTEGER_DATA,
    DOUBLE_DATA,
    FLOAT_DATA,
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
    EXTRACT,
    
    IMPORT,
    AS,
    USING,
    
    TRY,
    CATCH,
    THROW,
    
    NULL,
    REPEAT,
    READLN,
    VAR,
    CONST,
    MACRO,
    LIB,
    ARROW,
    
    ASSERT,
    
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
