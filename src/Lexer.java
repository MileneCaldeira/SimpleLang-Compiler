import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private String input;
    private int position;
    private int line;
    private int column;
    private char currentChar;
    
    private static final Map<String, Token.TokenType> keywords = new HashMap<>();
    
    static {
        keywords.put("int", Token.TokenType.INT);
        keywords.put("float", Token.TokenType.FLOAT);
        keywords.put("string", Token.TokenType.STRING);
        keywords.put("if", Token.TokenType.IF);
        keywords.put("else", Token.TokenType.ELSE);
        keywords.put("while", Token.TokenType.WHILE);
        keywords.put("do", Token.TokenType.DO);
        keywords.put("for", Token.TokenType.FOR);
        keywords.put("print", Token.TokenType.PRINT);
        keywords.put("scan", Token.TokenType.SCAN);
    }
    
    public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.line = 1;
        this.column = 1;
        this.currentChar = input.length() > 0 ? input.charAt(0) : '\0';
    }
    
    private void advance() {
        if (currentChar == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
        
        position++;
        if (position < input.length()) {
            currentChar = input.charAt(position);
        } else {
            currentChar = '\0';
        }
    }
    
    private char peek() {
        int peekPos = position + 1;
        if (peekPos < input.length()) {
            return input.charAt(peekPos);
        }
        return '\0';
    }
    
    private void skipWhitespace() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            advance();
        }
    }
    
    private void skipComment() {
        if (currentChar == '/' && peek() == '/') {
            while (currentChar != '\0' && currentChar != '\n') {
                advance();
            }
        } else if (currentChar == '/' && peek() == '*') {
            advance();
            advance();
            while (currentChar != '\0') {
                if (currentChar == '*' && peek() == '/') {
                    advance();
                    advance();
                    break;
                }
                advance();
            }
        }
    }
    
    private Token readNumber() {
        int startLine = line;
        int startColumn = column;
        StringBuilder number = new StringBuilder();
        boolean isFloat = false;
        
        while (currentChar != '\0' && (Character.isDigit(currentChar) || currentChar == '.')) {
            if (currentChar == '.') {
                if (isFloat) {
                    break;
                }
                isFloat = true;
            }
            number.append(currentChar);
            advance();
        }
        
        Token.TokenType type = isFloat ? Token.TokenType.NUMBER_FLOAT : Token.TokenType.NUMBER_INT;
        return new Token(type, number.toString(), startLine, startColumn);
    }
    
    private Token readString() {
        int startLine = line;
        int startColumn = column;
        StringBuilder string = new StringBuilder();
        
        advance();
        
        while (currentChar != '\0' && currentChar != '"') {
            if (currentChar == '\\' && peek() == '"') {
                advance();
                string.append('"');
                advance();
            } else {
                string.append(currentChar);
                advance();
            }
        }
        
        if (currentChar == '"') {
            advance();
        }
        
        return new Token(Token.TokenType.STRING_LITERAL, string.toString(), startLine, startColumn);
    }
    
    private Token readIdentifier() {
        int startLine = line;
        int startColumn = column;
        StringBuilder id = new StringBuilder();
        
        while (currentChar != '\0' && (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
            id.append(currentChar);
            advance();
        }
        
        String identifier = id.toString();
        Token.TokenType type = keywords.getOrDefault(identifier, Token.TokenType.IDENTIFIER);
        
        return new Token(type, identifier, startLine, startColumn);
    }
    
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        
        while (currentChar != '\0') {
            int startLine = line;
            int startColumn = column;
            
            skipWhitespace();
            
            if (currentChar == '\0') {
                break;
            }
            
            if (currentChar == '/' && (peek() == '/' || peek() == '*')) {
                skipComment();
                continue;
            }
            
            if (Character.isDigit(currentChar)) {
                tokens.add(readNumber());
                continue;
            }
            
            if (currentChar == '"') {
                tokens.add(readString());
                continue;
            }
            
            if (Character.isLetter(currentChar) || currentChar == '_') {
                tokens.add(readIdentifier());
                continue;
            }
            
            switch (currentChar) {
                case '+':
                    tokens.add(new Token(Token.TokenType.PLUS, "+", startLine, startColumn));
                    advance();
                    break;
                case '-':
                    tokens.add(new Token(Token.TokenType.MINUS, "-", startLine, startColumn));
                    advance();
                    break;
                case '*':
                    tokens.add(new Token(Token.TokenType.MULTIPLY, "*", startLine, startColumn));
                    advance();
                    break;
                case '/':
                    tokens.add(new Token(Token.TokenType.DIVIDE, "/", startLine, startColumn));
                    advance();
                    break;
                case '%':
                    tokens.add(new Token(Token.TokenType.MODULO, "%", startLine, startColumn));
                    advance();
                    break;
                case '=':
                    if (peek() == '=') {
                        tokens.add(new Token(Token.TokenType.EQUALS, "==", startLine, startColumn));
                        advance();
                        advance();
                    } else {
                        tokens.add(new Token(Token.TokenType.ASSIGN, "=", startLine, startColumn));
                        advance();
                    }
                    break;
                case '!':
                    if (peek() == '=') {
                        tokens.add(new Token(Token.TokenType.NOT_EQUALS, "!=", startLine, startColumn));
                        advance();
                        advance();
                    } else {
                        tokens.add(new Token(Token.TokenType.NOT, "!", startLine, startColumn));
                        advance();
                    }
                    break;
                case '<':
                    if (peek() == '=') {
                        tokens.add(new Token(Token.TokenType.LESS_EQUAL, "<=", startLine, startColumn));
                        advance();
                        advance();
                    } else {
                        tokens.add(new Token(Token.TokenType.LESS, "<", startLine, startColumn));
                        advance();
                    }
                    break;
                case '>':
                    if (peek() == '=') {
                        tokens.add(new Token(Token.TokenType.GREATER_EQUAL, ">=", startLine, startColumn));
                        advance();
                        advance();
                    } else {
                        tokens.add(new Token(Token.TokenType.GREATER, ">", startLine, startColumn));
                        advance();
                    }
                    break;
                case '&':
                    if (peek() == '&') {
                        tokens.add(new Token(Token.TokenType.AND, "&&", startLine, startColumn));
                        advance();
                        advance();
                    } else {
                        tokens.add(new Token(Token.TokenType.INVALID, "&", startLine, startColumn));
                        advance();
                    }
                    break;
                case '|':
                    if (peek() == '|') {
                        tokens.add(new Token(Token.TokenType.OR, "||", startLine, startColumn));
                        advance();
                        advance();
                    } else {
                        tokens.add(new Token(Token.TokenType.INVALID, "|", startLine, startColumn));
                        advance();
                    }
                    break;
                case '(':
                    tokens.add(new Token(Token.TokenType.LPAREN, "(", startLine, startColumn));
                    advance();
                    break;
                case ')':
                    tokens.add(new Token(Token.TokenType.RPAREN, ")", startLine, startColumn));
                    advance();
                    break;
                case '{':
                    tokens.add(new Token(Token.TokenType.LBRACE, "{", startLine, startColumn));
                    advance();
                    break;
                case '}':
                    tokens.add(new Token(Token.TokenType.RBRACE, "}", startLine, startColumn));
                    advance();
                    break;
                case ';':
                    tokens.add(new Token(Token.TokenType.SEMICOLON, ";", startLine, startColumn));
                    advance();
                    break;
                case ',':
                    tokens.add(new Token(Token.TokenType.COMMA, ",", startLine, startColumn));
                    advance();
                    break;
                default:
                    tokens.add(new Token(Token.TokenType.INVALID, String.valueOf(currentChar), startLine, startColumn));
                    advance();
            }
        }
        
        tokens.add(new Token(Token.TokenType.EOF, "", line, column));
        return tokens;
    }
}