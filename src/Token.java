public class Token {
    public enum TokenType {
        // Palavras-chave
        INT, FLOAT, STRING, IF, ELSE, WHILE, DO, FOR,
        PRINT, SCAN,
        
        // Identificadores e literais
        IDENTIFIER, NUMBER_INT, NUMBER_FLOAT, STRING_LITERAL,
        
        // Operadores
        PLUS, MINUS, MULTIPLY, DIVIDE, MODULO,
        ASSIGN, EQUALS, NOT_EQUALS, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL,
        AND, OR, NOT,
        
        // Delimitadores
        LPAREN, RPAREN, LBRACE, RBRACE, SEMICOLON, COMMA,
        
        // Especiais
        EOF, INVALID
    }
    
    private TokenType type;
    private String value;
    private int line;
    private int column;
    
    public Token(TokenType type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }
    
    public TokenType getType() {
        return type;
    }
    
    public String getValue() {
        return value;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
    
    @Override
    public String toString() {
        return String.format("Token(%s, '%s', %d:%d)", type, value, line, column);
    }
}