package prik.parser;

import java.util.*;
import java.util.stream.Collectors;
import prik.PrikException;
import prik.lib.NullValue;
import prik.lib.UserDefinedFunction;
import prik.parser.ast.*;

/**
 *
 * @author Professional
 */
public class Parser {
    public static Statement parse(List<Token> tokens) {
        return new Parser(tokens).parse();
    }
    
    private static final Token EOF = new Token(TokenType.EOF, "", -1, -1);

    private final List<Token> tokens;
    private final int size;
    
    private final ParseErrors parseErrors;
    
    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        size = tokens.size();
        parseErrors = new ParseErrors();
    }
    
    public ParseErrors getParseErrors() {
        return parseErrors;
    }
    
    public Statement parse() {
        final BlockStatement result = new BlockStatement();
        while (!match(TokenType.EOF)) {
            result.add(statement());
        }
        return result;
    }
    
    private Statement block() {
        final BlockStatement block = new BlockStatement();
        consume(TokenType.LBRACE);
        while (!match(TokenType.RBRACE)) {
            block.add(statement());
        }
        return block;
    }
    
    private Statement statementOrBlock() {
        if (lookMatch(0, TokenType.LBRACE)) return block();
        return statement();
    }
    
    private Statement statement() {
        if (match(TokenType.PRINT)) {
            return new PrintStatement(expression());
        }
        if (match(TokenType.PRINTLN)) {
            return new PrintlnStatement(expression());
        }
        if (match(TokenType.IF)) {
            return ifElse();
        }
        if (match(TokenType.WHILE)) {
            return whileStatement();
        }
        if (match(TokenType.DO)) {
            return doWhileStatement();
        }
        if (match(TokenType.FOR)) {
            return forStatement();
        }
        if (match(TokenType.BREAK)) {
            return new BreakStatement();
        }
        if (match(TokenType.CONTINUE)) {
            return new ContinueStatement();
        }
        if (match(TokenType.RETURN)) {
            return new ReturnStatement(expression());
        }
        if (match(TokenType.USE)) {
            return new UseStatement(expression());
//            return useStatement();
        }
        if (match(TokenType.DEF)) {
            return functionDefine();
        }
        if (match(TokenType.REPEAT)) {
            return repeatStatement();
        }
        
        if (match(TokenType.VAR)) {
            return declareVar();
        }
        
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LPAREN)) {
            return new FunctionStatement(function());
        }
        
        return assignmentStatement();
    }
    
//    private UseStatement useStatement() {
//        final var modules = new HashSet<String>();
//        do {
//            modules.add(consume(TokenType.WORD).getText());
//        } while (match(TokenType.COMMA));
//        return new UseStatement(modules);
//    }
    
    private Statement assignmentStatement() {
//        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.EQ)) {
//            final String variable = consume(TokenType.WORD).getText();
//            consume(TokenType.EQ);
//            return new AssignmentStatement(variable, expression());
//        }
//        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LBRACKET)) {
//            final ArrayAccessExpression array = element();
//            consume(TokenType.EQ);
//            return new ArrayAssignmentStatement(array, expression());
//        }
//        throw new ParseException("Unknown statement: " + get(0));
        final Expression assignment = assignmentStrict();
        if (assignment != null) {
            return new ExprStatement(assignment);
        }
        throw new ParseException("Unknown statement: " + get(0));
    }
    
    private DeclareVarStatement declareVar() {
        String name = consume(TokenType.WORD).getText();
        if (match(TokenType.EQ)) return new DeclareVarStatement(name, expression());
        return new DeclareVarStatement(name);
//        if(match(TokenType.VAR)) return new DeclareVarStatement(consume(TokenType.WORD).getText());
//        if(match(TokenType.VAR)) {
//            String name = consume(TokenType.WORD).getText();
//            consume(TokenType.EQ);
//            return new DeclareVarStatement(name, expression());
//        }
    }
    
    private Statement repeatStatement() {
        boolean openParen = match(TokenType.LPAREN);
        final Expression condition = expression();
        if (openParen) consume(TokenType.RPAREN);
        final Statement body = statementOrBlock();
        return new RepeatStatement(condition, body);
    }
    
    private Statement ifElse() {
        final Expression condition = expression();
        final Statement ifStatement = statementOrBlock();
        final Statement elseStatement;
        if (match(TokenType.ELSE)) {
            elseStatement = statementOrBlock();
        } else {
            elseStatement = null;
        }
         return new IfStatement(condition, ifStatement, elseStatement);
    }
    
    private Statement whileStatement() {
        final Expression condition = expression();
        final Statement statement = statementOrBlock();
        return new WhileStatement(condition, statement);
    }
    
    private Statement doWhileStatement() {
        final Statement statement = statementOrBlock();
        consume(TokenType.WHILE);
        final Expression condition = expression();
        return new DoWhileStatement(condition, statement);
    }
    
    private Statement forStatement() {
        int foreachIndex = lookMatch(0, TokenType.LPAREN) ? 1 : 0;
        if (lookMatch(foreachIndex, TokenType.WORD)
                && lookMatch(foreachIndex + 1, TokenType.COLON)) {
            // for v : arr || for (v : arr)
            return foreachArrayStatement();
        }
        if (lookMatch(foreachIndex, TokenType.WORD)
                && lookMatch(foreachIndex + 1, TokenType.COMMA)
                && lookMatch(foreachIndex + 2, TokenType.WORD)
                && lookMatch(foreachIndex + 3, TokenType.COLON)) {
            // for key, value : arr || for (key, value : arr)
            return foreachMapStatement();
        }
        
        boolean openParen = match(TokenType.LPAREN); // необязательные скобки
//        match(TokenType.LPAREN); // необязательные скобки
        final Statement initialization = assignmentStatement();
//        final Statement initialization = assignmentOrDeclareNumber();
        consume(TokenType.COMMA);
        final Expression termination = expression();
        consume(TokenType.COMMA);
        final Statement increment = assignmentStatement();
//        match(TokenType.RPAREN); // необязательные скобки
        if (openParen) consume(TokenType.RPAREN); // скобки
        final Statement statement = statementOrBlock();
        return new ForStatement(initialization, termination, increment, statement);
    }
    
    private ForeachArrayStatement foreachArrayStatement() {
        // for x : arr
        boolean optParentheses = match(TokenType.LPAREN);
        final String variable = consume(TokenType.WORD).getText();
        consume(TokenType.COLON);
        final Expression container = expression();
        if (optParentheses) {
            consume(TokenType.RPAREN); // close opt parentheses
        }
        final Statement statement = statementOrBlock();
        return new ForeachArrayStatement(variable, container, statement);
    }

    private ForeachMapStatement foreachMapStatement() {
        // for k, v : map
        boolean optParentheses = match(TokenType.LPAREN);
        final String key = consume(TokenType.WORD).getText();
        consume(TokenType.COMMA);
        final String value = consume(TokenType.WORD).getText();
        consume(TokenType.COLON);
        final Expression container = expression();
        if (optParentheses) {
            consume(TokenType.RPAREN); // close opt parentheses
        }
        final Statement statement = statementOrBlock();
        return new ForeachMapStatement(key, value, container, statement);
    }
    
    private FunctionDefineStatement functionDefine() {
        final String name = consume(TokenType.WORD).getText();
        consume(TokenType.LPAREN);
        final List<String> argNames = new ArrayList<>();
        while (!match(TokenType.RPAREN)) {
            argNames.add(consume(TokenType.WORD).getText());
            match(TokenType.COMMA);
        }
        final Statement body = statementBody();
        return new FunctionDefineStatement(name, argNames, body);
    }
    
    private Arguments arguments() {
        // (arg1, arg2, arg3 = expr1, arg4 = expr2)
        final Arguments arguments = new Arguments();
        boolean startsOptionalArgs = false;
        consume(TokenType.LPAREN);
        while (!match(TokenType.RPAREN)) {
            final String name = consume(TokenType.WORD).getText();
            if (match(TokenType.EQ)) {
                startsOptionalArgs = true;
//                arguments.addOptional(name, variable());
                arguments.addOptional(name, expression());
            } else if (!startsOptionalArgs) {
                arguments.addRequired(name);
            } else {
                throw new ParseException("Required argument cannot be after optional");
            }
            match(TokenType.COMMA);
        }
        return arguments;
    }
    
    private Statement statementBody() {
        if (match(TokenType.EQ) || match(TokenType.ARROW)) {
            return new ReturnStatement(expression());
        }
        return statementOrBlock();
    }
    
    private FunctionalExpression function() {
        final String name = consume(TokenType.WORD).getText();
        consume(TokenType.LPAREN);
        final FunctionalExpression function = new FunctionalExpression(name);
        while (!match(TokenType.RPAREN)) {
            function.addArgument(expression());
            match(TokenType.COMMA);
        }
        return function;
    }
    
    private Expression array() {
        consume(TokenType.LBRACKET);
        final List<Expression> elements = new ArrayList<>();
        while (!match(TokenType.RBRACKET)) {
            elements.add(expression());
            match(TokenType.COMMA);
        }
        return new ArrayExpression(elements);
    }
    
    private ArrayAccessExpression element() {
        final String variable = consume(TokenType.WORD).getText();
        final List<Expression> indices = new ArrayList<>();
        do {
            consume(TokenType.LBRACKET);
            indices.add(expression());
            consume(TokenType.RBRACKET);
        } while(lookMatch(0, TokenType.LBRACKET));
        return new ArrayAccessExpression(variable, indices);
    }
    
    private Expression expression() {
//        return ternary();
        return assignment();
    }
    
    private Expression assignment() {
        final Expression assignment = assignmentStrict();
        if (assignment != null) {
            return assignment;
        }
        return ternary();
    }

    private Expression assignmentStrict() {
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.EQ)) {
            final String variable = consume(TokenType.WORD).getText();
            consume(TokenType.EQ);
            return new AssignmentExpression(variable, expression());
        }
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LBRACKET)) {
            final ArrayAccessExpression array = element();
            consume(TokenType.EQ);
            return new ArrayAssignmentExpression(array, expression());
        }
        
        return null;
    }
    
    private MapExpression map() {
        // {key1 : value1, key2 : value2, ...}
        consume(TokenType.LBRACE);
        final Map<Expression, Expression> elements = new HashMap<>();
        while (!match(TokenType.RBRACE)) {
            final Expression key = primary();
            consume(TokenType.COLON);
            final Expression value = expression();
            elements.put(key, value);
            match(TokenType.COMMA);
        }
        return new MapExpression(elements);
    }
    
    private Expression ternary() {
        Expression result = logicalOr();
        
        if (match(TokenType.QUESTION)) {
            final Expression trueExpr = expression();
            consume(TokenType.COLON);
            final Expression falseExpr = expression();
            return new TernaryExpression(result, trueExpr, falseExpr);
        }
        
        return result;
    }
    
    private Expression logicalOr() {
        Expression result = logicalAnd();
        
        while (true) {
            if (match(TokenType.BARBAR)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.OR, result, logicalAnd());
                continue;
            }
            break;
        }
        
        return result;
    }
    
    private Expression logicalAnd() {
        Expression result = bitwiseOr();
        
        while (true) {
            if (match(TokenType.AMPAMP)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.AND, result, bitwiseOr());
                continue;
            }
            break;
        }
        
        return result;
    }
    
    private Expression bitwiseOr() {
        Expression expression = bitwiseXor();
        
        while (true) {
            if (match(TokenType.BAR)) {
                expression = new BinaryExpression(BinaryExpression.Operator.OR, expression, bitwiseXor());
                continue;
            }
            break;
        }
        
        return expression;
    }
    
    private Expression bitwiseXor() {
        Expression expression = bitwiseAnd();
        
        while (true) {
            if (match(TokenType.CARET)) {
                expression = new BinaryExpression(BinaryExpression.Operator.XOR, expression, bitwiseAnd());
                continue;
            }
            break;
        }
        
        return expression;
    }
    
    private Expression bitwiseAnd() {
        Expression expression = equality();
        
        while (true) {
            if (match(TokenType.AMP)) {
                expression = new BinaryExpression(BinaryExpression.Operator.AND, expression, equality());
                continue;
            }
            break;
        }
        
        return expression;
    }
    
    private Expression equality() {
        Expression result = conditional();
        
        if (match(TokenType.EQEQ)) {
            return new ConditionalExpression(ConditionalExpression.Operator.EQUALS, result, conditional());
        }
        if (match(TokenType.EXCLEQ)) {
            return new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS, result, conditional());
        }
        
        return result;
    }
    
    private Expression conditional() {
        Expression result = shift();
        
        while (true) {
            if (match(TokenType.LT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LT, result, shift());
                continue;
            }
            if (match(TokenType.LTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LTEQ, result, shift());
                continue;
            }
            if (match(TokenType.GT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GT, result, shift());
                continue;
            }
            if (match(TokenType.GTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GTEQ, result, shift());
                continue;
            }
            break;
        }
        
        return result;
    }
    
    private Expression shift() {
        Expression expression = additive();
        
        while (true) {
            if (match(TokenType.LTLT)) {
                expression = new BinaryExpression(BinaryExpression.Operator.LSHIFT, expression, additive());
                continue;
            }
            if (match(TokenType.GTGT)) {
                expression = new BinaryExpression(BinaryExpression.Operator.RSHIFT, expression, additive());
                continue;
            }
            if (match(TokenType.GTGTGT)) {
                expression = new BinaryExpression(BinaryExpression.Operator.URSHIFT, expression, additive());
                continue;
            }
            break;
        }
        
        return expression;
    }
    
    private Expression additive() {
        Expression result = multiplicative();
        
        while (true) {
            if (match(TokenType.PLUS)) {
                result = new BinaryExpression(BinaryExpression.Operator.ADD, result, multiplicative());
                continue;
            }
            if (match(TokenType.MINUS)) {
                result = new BinaryExpression(BinaryExpression.Operator.SUBTRACT, result, multiplicative());
                continue;
            }
//            if (match(TokenType.COLONCOLON)) {
//                result = new BinaryExpression(BinaryExpression.Operator.PUSH, result, multiplicative());
//                continue;
//            }
            break;
        }
        
        return result;
    }
    
    private Expression multiplicative() {
        Expression result = unary();
        
        while (true) {
            if (match(TokenType.STAR)) {
                result = new BinaryExpression(BinaryExpression.Operator.MULTIPLY, result, unary());
                continue;
            }
            if (match(TokenType.SLASH)) {
                result = new BinaryExpression(BinaryExpression.Operator.DIVIDE, result, unary());
                continue;
            }
            if (match(TokenType.PERCENT)) {
                result = new BinaryExpression(BinaryExpression.Operator.REMAINDER, result, unary());
                continue;
            }
            break;
        }
        
        return result;
    }
    
    private Expression unary() {
        if (match(TokenType.MINUS)) {
            return new UnaryExpression(UnaryExpression.Operator.NEGATE, primary());
        }
        if (match(TokenType.EXCL)) {
            return new UnaryExpression(UnaryExpression.Operator.NOT, primary());
        }
        if (match(TokenType.TILDE)) {
            return new UnaryExpression(UnaryExpression.Operator.COMPLEMENT, primary());
        }
        if (match(TokenType.PLUS)) {
            return primary();
        }
        return primary();
    }
    
    private Expression primary() {
        final Token current = get(0);
        if (match(TokenType.NUMBER)) {
            return new ValueExpression(Double.parseDouble(current.getText()));
        }
        if (match(TokenType.HEX_NUMBER)) {
            return new ValueExpression(Long.parseLong(current.getText(), 16));
        }
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LPAREN)) {
            return function();
        }
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LBRACKET)) {
            return element();
        }
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.DOT)) {
            return qualifiedName();
        }
        if (lookMatch(0, TokenType.LBRACKET)) {
            return array();
        }
        if (lookMatch(0, TokenType.LBRACE)) {
            return map();
        }
        if (match(TokenType.COLONCOLON)) {
            // ::method reference
            final String functionName = consume(TokenType.WORD).getText();
            return new FunctionReferenceExpression(functionName);
        }
        if (match(TokenType.DEF)) {
            // anonymous function def(args) ...
//            final Arguments arguments = arguments();
            consume(TokenType.LPAREN);
            final List<String> arguments = new ArrayList();
            while (!match(TokenType.RPAREN)) {
                arguments.add(consume(TokenType.WORD).getText());
                match(TokenType.COMMA);
            }
            final Statement statement = statementBody();
            return new ValueExpression(new UserDefinedFunction(arguments, statement));
        }
        if (match(TokenType.WORD)) {
            return new VariableExpression(current.getText());
        }
        if (match(TokenType.TEXT)) {
            return new ValueExpression(current.getText());
        }
        if (match(TokenType.LPAREN)) {
            Expression result = expression();
//            match(TokenType.RPAREN);
            consume(TokenType.RPAREN);
            return result;
        }
        
        if (match(TokenType.NULL)) return new ValueExpression(new NullValue());
        if (match(TokenType.TRUE)) return new ValueExpression(true);
        if (match(TokenType.FALSE)) return new ValueExpression(false);
        
        throw new ParseException("Unknown expression: " + current);
    }
    
    private Expression qualifiedName() {
        final Token current = get(0);
        if (!match(TokenType.WORD)) return null;

        final List<Expression> indices = variableSuffix();
        if (indices == null || indices.isEmpty()) {
            return new VariableExpression(current.getText());
        }
        return new ContainerAccessExpression(current.getText(), indices);
    }

    private List<Expression> variableSuffix() {
        // .key1.arr1[expr1][expr2].key2
        if (!lookMatch(0, TokenType.DOT) && !lookMatch(0, TokenType.LBRACKET)) {
            return null;
        }
        final List<Expression> indices = new ArrayList<>();
        while (lookMatch(0, TokenType.DOT) || lookMatch(0, TokenType.LBRACKET)) {
            if (match(TokenType.DOT)) {
                final String fieldName = consume(TokenType.WORD).getText();
                final Expression key = new ValueExpression(fieldName);
                indices.add(key);
            }
            if (match(TokenType.LBRACKET)) {
                indices.add(expression());
                consume(TokenType.RBRACKET);
            }
        }
        return indices;
    }
    
    private Token consume(TokenType type) {
        final Token current = get(0);
        if (type != current.getType()) throw new ParseException("Token " + current + " doesn't match " + type);
        pos++;
        return current;
    }
    
    private boolean match(TokenType type) {
        final Token current = get(0);
        if (type != current.getType()) return false;
        pos++;
        return true;
    }
    
    private boolean lookMatch(int pos, TokenType type) {
        return get(pos).getType() == type;
    }
    
    private Token get(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= size) return EOF;
        return tokens.get(position);
    }
}
