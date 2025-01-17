package prik.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import prik.lib.BooleanValue;
import prik.lib.Function;
import prik.lib.FunctionValue;
import prik.lib.Functions;
import prik.lib.MapValue;
import prik.lib.NumberValue;
import prik.lib.StringValue;
import prik.lib.Types;
import prik.lib.Value;
import prik.lib.Variables;


/**
 *
 * @author Professional
 */
public final class Lexer {
    private static final String OPERATOR_CHARS = "+-*/%()[]{}=<>!&|,.^~?:";
    
    private static final Map<String, TokenType> OPERATORS;
    static {
        OPERATORS = new HashMap<>();
        OPERATORS.put("+", TokenType.PLUS);
        OPERATORS.put("-", TokenType.MINUS);
        OPERATORS.put("*", TokenType.STAR);
        OPERATORS.put("/", TokenType.SLASH);
        OPERATORS.put("%", TokenType.PERCENT);
        OPERATORS.put("(", TokenType.LPAREN);
        OPERATORS.put(")", TokenType.RPAREN);
        OPERATORS.put("[", TokenType.LBRACKET);
        OPERATORS.put("]", TokenType.RBRACKET);
        OPERATORS.put("{", TokenType.LBRACE);
        OPERATORS.put("}", TokenType.RBRACE);
        OPERATORS.put("=", TokenType.EQ);
        OPERATORS.put("<", TokenType.LT);
        OPERATORS.put(">", TokenType.GT);
        OPERATORS.put(",", TokenType.COMMA);
        OPERATORS.put(".", TokenType.DOT);
        OPERATORS.put("^", TokenType.CARET);
        OPERATORS.put("~", TokenType.TILDE);
        OPERATORS.put("?", TokenType.QUESTION);
        OPERATORS.put(":", TokenType.COLON);
        OPERATORS.put(";", TokenType.SEMICOLON);
        
        OPERATORS.put("!", TokenType.EXCL);
        OPERATORS.put("&", TokenType.AMP);
        OPERATORS.put("|", TokenType.BAR);
        
        OPERATORS.put("==", TokenType.EQEQ);
        OPERATORS.put("!=", TokenType.EXCLEQ);
        OPERATORS.put("<=", TokenType.LTEQ);
        OPERATORS.put(">=", TokenType.GTEQ);
        
        OPERATORS.put("++", TokenType.PLUSPLUS);
        OPERATORS.put("--", TokenType.MINUSMINUS);
        
        OPERATORS.put("+=", TokenType.PLUSEQ);
        OPERATORS.put("-=", TokenType.MINUSEQ);
        OPERATORS.put("*=", TokenType.STAREQ);
        OPERATORS.put("/=", TokenType.SLASHEQ);
        OPERATORS.put("%=", TokenType.PERCENTEQ);
        OPERATORS.put("&=", TokenType.AMPEQ);
        OPERATORS.put("^=", TokenType.CARETEQ);
        OPERATORS.put("|=", TokenType.BAREQ);
        OPERATORS.put("::=", TokenType.COLONCOLONEQ);
        OPERATORS.put("<<=", TokenType.LTLTEQ);
        OPERATORS.put(">>=", TokenType.GTGTEQ);
        OPERATORS.put(">>>=", TokenType.GTGTGTEQ);
        
        OPERATORS.put("::", TokenType.COLONCOLON);
        
        OPERATORS.put("&&", TokenType.AMPAMP);
        OPERATORS.put("||", TokenType.BARBAR);
        
        OPERATORS.put("<<", TokenType.LTLT);
        OPERATORS.put(">>", TokenType.GTGT);
        OPERATORS.put(">>>", TokenType.GTGTGT);
        
        OPERATORS.put("@", TokenType.AT);
        OPERATORS.put("..", TokenType.DOTDOT);
        OPERATORS.put("**", TokenType.STARSTAR);
        OPERATORS.put("^^", TokenType.CARETCARET);
        OPERATORS.put("?:", TokenType.QUESTIONCOLON);
        OPERATORS.put("??", TokenType.QUESTIONQUESTION);
        
        OPERATORS.put("->", TokenType.ARROW);
    }
    
    private static final Map<String, TokenType> KEYWORDS;
    static {
        KEYWORDS = new HashMap<>();

        KEYWORDS.put("true", TokenType.TRUE);
        KEYWORDS.put("false", TokenType.FALSE);
        
        KEYWORDS.put("array", TokenType.ARRAY_DATA);
        KEYWORDS.put("boolean", TokenType.BOOLEAN_DATA);
        KEYWORDS.put("char", TokenType.CHAR_DATA);
        KEYWORDS.put("function", TokenType.ANONIMOUS_FN_DATA);
        KEYWORDS.put("map", TokenType.MAP_DATA);
        KEYWORDS.put("number", TokenType.NUMBER_DATA);
        KEYWORDS.put("string", TokenType.STRING_DATA);
        KEYWORDS.put("any", TokenType.ANY_DATA);
        
        KEYWORDS.put("print", TokenType.PRINT);
        KEYWORDS.put("println", TokenType.PRINTLN);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("do", TokenType.DO);
        KEYWORDS.put("break", TokenType.BREAK);
        KEYWORDS.put("continue", TokenType.CONTINUE);
        KEYWORDS.put("def", TokenType.DEF);
        KEYWORDS.put("return", TokenType.RETURN);
        KEYWORDS.put("null", TokenType.NULL);
        KEYWORDS.put("class", TokenType.CLASS);
        KEYWORDS.put("new", TokenType.NEW);
        KEYWORDS.put("extract", TokenType.EXTRACT);
        
        KEYWORDS.put("import", TokenType.IMPORT);
        KEYWORDS.put("as", TokenType.AS);
        KEYWORDS.put("using", TokenType.USING);
        
        KEYWORDS.put("repeat", TokenType.REPEAT);
        KEYWORDS.put("assert", TokenType.ASSERT);
        KEYWORDS.put("readln", TokenType.READLN);
        KEYWORDS.put("var", TokenType.VAR);
        KEYWORDS.put("const", TokenType.CONST);
        KEYWORDS.put("macro", TokenType.MACRO);
        KEYWORDS.put("lib", TokenType.LIB);
        KEYWORDS.put("enum", TokenType.ENUM);
        
        KEYWORDS.put("throw", TokenType.THROW);
        KEYWORDS.put("try", TokenType.TRY);
        KEYWORDS.put("catch", TokenType.CATCH);
    }
    
    public static Set<String> getKeywords() {
        return KEYWORDS.keySet();
    }

    private final String input;
    private final int length;
    
    private final List<Token> tokens;
    
    private int pos;
    public static int row, col;
    
    public Lexer() {
        this.input = null;
        length = input.length();
        
        tokens = new ArrayList<>();
        row = col = 1;
        typesModule();
    }

    public Lexer(String input) {
        this.input = input;
        length = input.length();
        
        tokens = new ArrayList<>();
        row = col = 1;
        typesModule();
    }
    
    public static List<Token> tokenize(String input) {
        return new Lexer(input).tokenize();
    }
    
    public List<Token> tokenize() {
        while (pos < length) {
            final char current = peek(0);
            if (Character.isDigit(current)) tokenizeNumber();
            else if (Character.isLetter(current)) tokenizeWord();
            else if (current == '`') tokenizeExtendedWord();
            else if (current == '"') tokenizeText();
            else if (current == '\'') tokenizeChar();
            else if (current == '#') {
                next();
                tokenizeHexNumber();
            }
            else if (OPERATOR_CHARS.indexOf(current) != -1) {
                tokenizeOperator();
            } else {
                next();
            }
        }
        return tokens;
    }
    
    private void tokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        if (current == '0' && (peek(1) == 'x' || (peek(1) == 'X'))) {
            next();
            next();
            tokenizeHexNumber();
            return;
        }
        boolean decimal = false;
        while (true) {
            if (current == '_') {
                current = next();
                continue;
            }
            if (current == '.') {
                if (buffer.indexOf(".") != -1) throw error("Invalid float number");
            } else if (current == 'e' || current == 'E') {
                decimal = true;
                int exp = subTokenizeScientificNumber();
                buffer.append(current).append(exp);
                current = next();
                break;
            } else if (!Character.isDigit(current)) break;
            buffer.append(current);
            current = next();
        }
        if (current == 'f' || current == 'F') {
            next();
            addToken(TokenType.FLOAT_NUMBER, buffer.toString());
        } else if (current == 'b' || current == 'B') {
            next();
            addToken(TokenType.BYTE_NUMBER, buffer.toString());
        } else if (current == 'l' || current == 'L') {
            next();
            addToken(TokenType.LONG_NUMBER, buffer.toString());
        } else if (current == 'i' || current == 'I') {
            next();
            addToken(TokenType.INT_NUMBER, buffer.toString());
        } else if ((current == 'd' || current == 'D') || decimal) {
            next();
            addToken(TokenType.DOUBLE_NUMBER, buffer.toString());
        } else if (current == 's' || current == 'S') {
            next();
            addToken(TokenType.SHORT_NUMBER, buffer.toString());
        } else {
            addToken(TokenType.NUMBER, buffer.toString());
        }
    }
    
    private int subTokenizeScientificNumber() {
        int sign = switch (next()) {
            case '-' -> { skip(); yield -1; }
            case '+' -> { skip(); yield 1; }
            default -> 1;
        };

        boolean hasValue = false;
        char current = peek(0);
        while (current == '0') {
            hasValue = true;
            current = next();
        }
        int result = 0;
        int position = 0;
        while (Character.isDigit(current)) {
            result = result * 10 + (current - '0');
            current = next();
            position++;
        }
        if (position == 0 && !hasValue) throw error("Empty floating point exponent");
        if (position >= 4) {
            if (sign > 0) throw error("Float number too large");
            else throw error("Float number too small");
        }
        return sign * result;
    }
    
    private void tokenizeHexNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (isHexNumber(current) || current == '_') {
            if (current != '_') {
                buffer.append(current);
            }
            current = next();
        }
        if (buffer.isEmpty()) throw error("Empty HEX value");
        if (peek(-1) == '_') throw error("HEX value cannot end with _");
        if (current == 'L') {
            skip();
            addToken(TokenType.HEX_LONG_NUMBER, buffer.toString());
        } else {
            addToken(TokenType.HEX_NUMBER, buffer.toString());
        }
    }

    private static boolean isHexNumber(char current) {
        return Character.isDigit(current)
                || ('a' <= current && current <= 'f')
                || ('A' <= current && current <= 'F');
    }
    
    private void tokenizeOperator() {
        char current = peek(0);
        if (current == '/') {
            if (peek(1) == '/') {
                next();
                next();
                tokenizeComment();
                return;
            } else if (peek(1) == '*') {
                next();
                next();
                tokenizeMultilineComment();
                return;
            }
        }
        final StringBuilder buffer = new StringBuilder();
        while (true) {
            final String text = buffer.toString();
            if (!OPERATORS.containsKey(text + current) && !text.isEmpty()) {
                addToken(OPERATORS.get(text));
                return;
            }
            buffer.append(current);
            current = next();
        }
    }
    
    private void tokenizeWord() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (!Character.isLetterOrDigit(current) && (current != '_')  && (current != '$')) {
                break;
            }
            buffer.append(current);
            current = next();
        }
        
        final String word = buffer.toString();
        if (KEYWORDS.containsKey(word)) {
            addToken(KEYWORDS.get(word));
        } else {
            addToken(TokenType.WORD, word);
        }
    }
    
    private void tokenizeExtendedWord() {
        next();// skip `
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (current == '`') break;
            if (current == '\0') throw error("Reached end of file while parsing extended word.");
            if (current == '\n' || current == '\r') throw error("Reached end of line while parsing extended word.");
            buffer.append(current);
            current = next();
        }
        next(); // skip closing `
        addToken(TokenType.WORD, buffer.toString());
    }
    
    private void tokenizeText() {
        skip();// skip "
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (current == '\\') {
                current = next();
                if ("\r\n\0".indexOf(current) != -1) {
                    throw error("Reached end of line while parsing text");
                }

                int idx = "\\0\"bfnrts".indexOf(current);
                if (idx != -1) {
                    current = next();
                    buffer.append("\\\0\"\b\f\n\r\t\s".charAt(idx));
                    continue;
                }
                if (current == 'u') {
                    // http://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.3
                    int rollbackPosition = pos;
                    while (current == 'u') current = next();
                    int escapedValue = 0;
                    for (int i = 12; i >= 0 && escapedValue != -1; i -= 4) {
                        if (isHexNumber(current)) {
                            escapedValue |= (Character.digit(current, 16) << i);
                        } else {
                            escapedValue = -1;
                        }
                        current = next();
                    }
                    if (escapedValue >= 0) {
                        buffer.append((char) escapedValue);
                    } else {
                        // rollback
                        buffer.append("\\u");
                        pos = rollbackPosition;
                    }
                    continue;
                }
                buffer.append('\\');
                continue;
            }
            if (current == '"') break;
            if (current == '\0') throw error("Reached end of file while parsing text string.");
            buffer.append(current);
            current = next();
        }
        next(); // skip closing "
        
        addToken(TokenType.TEXT, buffer.toString());
    }
    
    private void tokenizeChar() {
        skip();// skip '
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (current == '\\') {
                current = next();
                if ("\r\n\0".indexOf(current) != -1) {
                    throw error("Reached end of line while parsing character");
                }

                int idx = "\\0\"bfnrts".indexOf(current);
                if (idx != -1) {
                    current = next();
                    buffer.append("\\\0\"\b\f\n\r\t\s".charAt(idx));
                    continue;
                }
                buffer.append('\\');
                continue;
            }
            if (current == '\'') break;
            if (current == '\0') throw error("Reached end of file while parsing character.");
            buffer.append(current);
            current = next();
        }
        next(); // skip closing '
        if (buffer.length() != 1) throw error("Length of character doesn`t correct");
        addToken(TokenType.CHAR, buffer.toString());
    }
    
    private void tokenizeComment() {
        char current = peek(0);
        while ("\r\n\0".indexOf(current) == -1) {
            current = next();
        }
     }
    
    private void tokenizeMultilineComment() {
        char current = peek(0);
        while (true) {
            if (current == '\0') throw error("Reached end of file while parsing multiline comment");
            if (current == '*' && peek(1) == '/') break;
            current = next();
        }
        next(); // *
        next(); // /
    }
    
    private void skip() {
        if (pos >= length) return;
        final char result = input.charAt(pos);
        if (result == '\n') {
            row++;
            col = 1;
        } else col++;
        pos++;
    }
    
    private char next() {
        skip();
        return peek(0);
    }
    
    private char peek(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= length) return '\0';
        return input.charAt(position);
    }
    
    private void addToken(TokenType type) {
        addToken(type, "");
    }
    
    private void addToken(TokenType type, String text) {
        tokens.add(new Token(type, text, row, col));
    }
    
    private LexerException error(String text) {
        return new LexerException(row, col, text);
    }
    
    private void typesModule() {
        MapValue types = new MapValue(8);
        
        types.set("string", args -> new StringValue(args[0].asString()));
        types.set("number", args -> NumberValue.of(args[0].asNumber()));
        
        types.set("byte", args -> NumberValue.of((byte)args[0].asInt()));
        types.set("short", args -> NumberValue.of((short)args[0].asInt()));
        types.set("int", args -> NumberValue.of(args[0].asInt()));
        types.set("long", args -> NumberValue.of((long)args[0].asNumber()));
        types.set("float", args -> NumberValue.of((float)args[0].asInt()));
        Functions.set("digit", args -> NumberValue.of(args[0].asNumber()));
        
        types.set("isString", (Value... args) -> {
            prik.lib.Arguments.check(1, args.length);
            if (args[0].type() == Types.STRING) return new BooleanValue(true);
            else return new BooleanValue(false);
        });
        types.set("isNumber", (Value... args) -> {
            prik.lib.Arguments.check(1, args.length);
            if (args[0].type() == Types.NUMBER) return new BooleanValue(true);
            else return new BooleanValue(false);
        });
        types.set("isBoolean", (Value... args) -> {
            prik.lib.Arguments.check(1, args.length);
            if (args[0].type() == Types.BOOLEAN) return new BooleanValue(true);
            else return new BooleanValue(false);
        });
        
        Variables.define("Types", types);
    }
}
