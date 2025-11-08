import java.util.List;
import java.util.ArrayList;

public class Parser {
    private List<Token> tokens;
    private int position;
    private Token currentToken;
    private SymbolTable symbolTable;
    private List<String> errors;
    private StringBuilder javaCode;
    
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
        this.currentToken = tokens.get(0);
        this.symbolTable = new SymbolTable();
        this.errors = new ArrayList<>();
        this.javaCode = new StringBuilder();
    }
    
    private void advance() {
        position++;
        if (position < tokens.size()) {
            currentToken = tokens.get(position);
        }
    }
    
    private void expect(Token.TokenType type) throws Exception {
        if (currentToken.getType() == type) {
            advance();
        } else {
            throw new Exception(String.format("Erro de sintaxe na linha %d, coluna %d: esperado %s, encontrado %s",
                    currentToken.getLine(), currentToken.getColumn(), type, currentToken.getType()));
        }
    }
    
    private boolean check(Token.TokenType type) {
        return currentToken.getType() == type;
    }
    
    private boolean match(Token.TokenType... types) {
        for (Token.TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }
    
    public void parse() {
        javaCode.append("import java.util.Scanner;\n\n");
        javaCode.append("public class GeneratedCode {\n");
        javaCode.append("    public static void main(String[] args) {\n");
        javaCode.append("        Scanner scanner = new Scanner(System.in);\n");
        
        try {
            while (!check(Token.TokenType.EOF)) {
                statement();
            }
            javaCode.append("        scanner.close();\n");
            javaCode.append("    }\n");
            javaCode.append("}\n");
        } catch (Exception e) {
            errors.add(e.getMessage());
        }
    }
    
    private void statement() throws Exception {
        if (check(Token.TokenType.INT) || check(Token.TokenType.FLOAT) || check(Token.TokenType.STRING)) {
            declaration();
        } else if (check(Token.TokenType.IF)) {
            ifStatement();
        } else if (check(Token.TokenType.WHILE)) {
            whileStatement();
        } else if (check(Token.TokenType.DO)) {
            doWhileStatement();
        } else if (check(Token.TokenType.FOR)) {
            forStatement();
        } else if (check(Token.TokenType.PRINT)) {
            printStatement();
        } else if (check(Token.TokenType.SCAN)) {
            scanStatement();
        } else if (check(Token.TokenType.IDENTIFIER)) {
            assignment();
        } else if (check(Token.TokenType.LBRACE)) {
            block();
        } else {
            throw new Exception(String.format("Instrução inválida na linha %d, coluna %d",
                    currentToken.getLine(), currentToken.getColumn()));
        }
    }
    
    private void declaration() throws Exception {
        Token typeToken = currentToken;
        String type = typeToken.getValue();
        String javaType = convertType(type);
        advance();
        
        Token idToken = currentToken;
        expect(Token.TokenType.IDENTIFIER);
        String varName = idToken.getValue();
        
        try {
            symbolTable.addSymbol(varName, type);
        } catch (Exception e) {
            errors.add(e.getMessage());
            throw e;
        }
        
        javaCode.append("        ").append(javaType).append(" ").append(varName);
        
        if (check(Token.TokenType.ASSIGN)) {
            advance();
            javaCode.append(" = ");
            String exprType = expression();
            
            if (!isCompatibleType(type, exprType)) {
                errors.add(String.format("Tipo incompatível na atribuição da variável '%s' na linha %d",
                        varName, idToken.getLine()));
            }
        }
        
        expect(Token.TokenType.SEMICOLON);
        javaCode.append(";\n");
    }
    
    private void assignment() throws Exception {
        Token idToken = currentToken;
        String varName = idToken.getValue();
        expect(Token.TokenType.IDENTIFIER);
        
        if (!symbolTable.isDeclared(varName)) {
            errors.add(String.format("Variável '%s' não declarada na linha %d", varName, idToken.getLine()));
        }
        
        String varType = symbolTable.getType(varName);
        
        expect(Token.TokenType.ASSIGN);
        javaCode.append("        ").append(varName).append(" = ");
        
        String exprType = expression();
        
        if (varType != null && !isCompatibleType(varType, exprType)) {
            errors.add(String.format("Tipo incompatível na atribuição da variável '%s' na linha %d",
                    varName, idToken.getLine()));
        }
        
        expect(Token.TokenType.SEMICOLON);
        javaCode.append(";\n");
    }
    
    private void ifStatement() throws Exception {
        expect(Token.TokenType.IF);
        expect(Token.TokenType.LPAREN);
        javaCode.append("        if (");
        expression();
        expect(Token.TokenType.RPAREN);
        javaCode.append(") ");
        
        symbolTable.enterScope();
        statement();
        symbolTable.exitScope();
        
        if (check(Token.TokenType.ELSE)) {
            advance();
            javaCode.append(" else ");
            symbolTable.enterScope();
            statement();
            symbolTable.exitScope();
        }
    }
    
    private void whileStatement() throws Exception {
        expect(Token.TokenType.WHILE);
        expect(Token.TokenType.LPAREN);
        javaCode.append("        while (");
        expression();
        expect(Token.TokenType.RPAREN);
        javaCode.append(") ");
        
        symbolTable.enterScope();
        statement();
        symbolTable.exitScope();
    }
    
    private void doWhileStatement() throws Exception {
        expect(Token.TokenType.DO);
        javaCode.append("        do ");
        
        symbolTable.enterScope();
        statement();
        symbolTable.exitScope();
        
        expect(Token.TokenType.WHILE);
        expect(Token.TokenType.LPAREN);
        javaCode.append(" while (");
        expression();
        expect(Token.TokenType.RPAREN);
        expect(Token.TokenType.SEMICOLON);
        javaCode.append(");\n");
    }
    
    private void forStatement() throws Exception {
        expect(Token.TokenType.FOR);
        expect(Token.TokenType.LPAREN);
        javaCode.append("        for (");
        
        symbolTable.enterScope();
        
        if (check(Token.TokenType.INT) || check(Token.TokenType.FLOAT) || check(Token.TokenType.STRING)) {
            Token typeToken = currentToken;
            String type = typeToken.getValue();
            String javaType = convertType(type);
            advance();
            
            Token idToken = currentToken;
            expect(Token.TokenType.IDENTIFIER);
            String varName = idToken.getValue();
            
            try {
                symbolTable.addSymbol(varName, type);
            } catch (Exception e) {
                errors.add(e.getMessage());
            }
            
            javaCode.append(javaType).append(" ").append(varName);
            
            if (check(Token.TokenType.ASSIGN)) {
                advance();
                javaCode.append(" = ");
                expression();
            }
        } else if (check(Token.TokenType.IDENTIFIER)) {
            Token idToken = currentToken;
            String varName = idToken.getValue();
            expect(Token.TokenType.IDENTIFIER);
            
            if (!symbolTable.isDeclared(varName)) {
                errors.add(String.format("Variável '%s' não declarada na linha %d", varName, idToken.getLine()));
            }
            
            expect(Token.TokenType.ASSIGN);
            javaCode.append(varName).append(" = ");
            expression();
        }
        
        expect(Token.TokenType.SEMICOLON);
        javaCode.append("; ");
        
        expression();
        expect(Token.TokenType.SEMICOLON);
        javaCode.append("; ");
        
        if (check(Token.TokenType.IDENTIFIER)) {
            Token idToken = currentToken;
            String varName = idToken.getValue();
            expect(Token.TokenType.IDENTIFIER);
            
            if (!symbolTable.isDeclared(varName)) {
                errors.add(String.format("Variável '%s' não declarada na linha %d", varName, idToken.getLine()));
            }
            
            expect(Token.TokenType.ASSIGN);
            javaCode.append(varName).append(" = ");
            expression();
        }
        
        expect(Token.TokenType.RPAREN);
        javaCode.append(") ");
        
        statement();
        symbolTable.exitScope();
    }
    
    private void printStatement() throws Exception {
        expect(Token.TokenType.PRINT);
        expect(Token.TokenType.LPAREN);
        javaCode.append("        System.out.println(");
        expression();
        expect(Token.TokenType.RPAREN);
        expect(Token.TokenType.SEMICOLON);
        javaCode.append(");\n");
    }
    
    private void scanStatement() throws Exception {
        expect(Token.TokenType.SCAN);
        expect(Token.TokenType.LPAREN);
        
        Token idToken = currentToken;
        String varName = idToken.getValue();
        expect(Token.TokenType.IDENTIFIER);
        
        if (!symbolTable.isDeclared(varName)) {
            errors.add(String.format("Variável '%s' não declarada na linha %d", varName, idToken.getLine()));
        }
        
        String varType = symbolTable.getType(varName);
        
        javaCode.append("        ").append(varName).append(" = ");
        
        if (varType != null) {
            switch (varType) {
                case "int":
                    javaCode.append("scanner.nextInt()");
                    break;
                case "float":
                    javaCode.append("scanner.nextFloat()");
                    break;
                case "string":
                    javaCode.append("scanner.nextLine()");
                    break;
            }
        }
        
        expect(Token.TokenType.RPAREN);
        expect(Token.TokenType.SEMICOLON);
        javaCode.append(";\n");
    }
    
    private void block() throws Exception {
        expect(Token.TokenType.LBRACE);
        javaCode.append("{\n");
        
        symbolTable.enterScope();
        
        while (!check(Token.TokenType.RBRACE) && !check(Token.TokenType.EOF)) {
            statement();
        }
        
        symbolTable.exitScope();
        
        expect(Token.TokenType.RBRACE);
        javaCode.append("        }\n");
    }
    private String expression() throws Exception {
        return logicalOr();
    }
    
    private String logicalOr() throws Exception {
        String type = logicalAnd();
        
        while (check(Token.TokenType.OR)) {
            advance();
            javaCode.append(" || ");
            logicalAnd();
        }
        
        return type;
    }
    
    private String logicalAnd() throws Exception {
        String type = equality();
        
        while (check(Token.TokenType.AND)) {
            advance();
            javaCode.append(" && ");
            equality();
        }
        
        return type;
    }
    
    private String equality() throws Exception {
        String type = relational();
        
        while (check(Token.TokenType.EQUALS) || check(Token.TokenType.NOT_EQUALS)) {
            Token op = currentToken;
            advance();
            javaCode.append(" ").append(op.getValue()).append(" ");
            relational();
        }
        
        return type;
    }
    
    private String relational() throws Exception {
        String type = additive();
        
        while (check(Token.TokenType.LESS) || check(Token.TokenType.GREATER) ||
               check(Token.TokenType.LESS_EQUAL) || check(Token.TokenType.GREATER_EQUAL)) {
            Token op = currentToken;
            advance();
            javaCode.append(" ").append(op.getValue()).append(" ");
            additive();
        }
        
        return type;
    }
    
    private String additive() throws Exception {
        String type = multiplicative();
        
        while (check(Token.TokenType.PLUS) || check(Token.TokenType.MINUS)) {
            Token op = currentToken;
            advance();
            javaCode.append(" ").append(op.getValue()).append(" ");
            String rightType = multiplicative();
            type = promoteType(type, rightType);
        }
        
        return type;
    }
    
    private String multiplicative() throws Exception {
        String type = unary();
        
        while (check(Token.TokenType.MULTIPLY) || check(Token.TokenType.DIVIDE) || check(Token.TokenType.MODULO)) {
            Token op = currentToken;
            advance();
            javaCode.append(" ").append(op.getValue()).append(" ");
            String rightType = unary();
            type = promoteType(type, rightType);
        }
        
        return type;
    }
    
    private String unary() throws Exception {
        if (check(Token.TokenType.MINUS) || check(Token.TokenType.NOT)) {
            Token op = currentToken;
            advance();
            javaCode.append(op.getValue());
            return unary();
        }
        return primary();
    }
    
    private String primary() throws Exception {
        if (check(Token.TokenType.NUMBER_INT)) {
            javaCode.append(currentToken.getValue());
            advance();
            return "int";
        } else if (check(Token.TokenType.NUMBER_FLOAT)) {
            javaCode.append(currentToken.getValue()).append("f");
            advance();
            return "float";
        } else if (check(Token.TokenType.STRING_LITERAL)) {
            javaCode.append("\"").append(currentToken.getValue()).append("\"");
            advance();
            return "string";
        } else if (check(Token.TokenType.IDENTIFIER)) {
            String varName = currentToken.getValue();
            
            if (!symbolTable.isDeclared(varName)) {
                errors.add(String.format("Variável '%s' não declarada na linha %d",
                        varName, currentToken.getLine()));
            }
            
            javaCode.append(varName);
            advance();
            return symbolTable.getType(varName);
        } else if (check(Token.TokenType.LPAREN)) {
            advance();
            javaCode.append("(");
            String type = expression();
            expect(Token.TokenType.RPAREN);
            javaCode.append(")");
            return type;
        } else {
            throw new Exception(String.format("Expressão inválida na linha %d, coluna %d",
                    currentToken.getLine(), currentToken.getColumn()));
        }
    }
    
    private String convertType(String type) {
        switch (type) {
            case "int":
                return "int";
            case "float":
                return "float";
            case "string":
                return "String";
            default:
                return type;
        }
    }
    
    private boolean isCompatibleType(String targetType, String sourceType) {
        if (targetType == null || sourceType == null) {
            return true;
        }
        
        if (targetType.equals(sourceType)) {
            return true;
        }
        
        if (targetType.equals("float") && sourceType.equals("int")) {
            return true;
        }
        
        return false;
    }
    
    private String promoteType(String type1, String type2) {
        if (type1 == null || type2 == null) {
            return "int";
        }
        
        if (type1.equals("float") || type2.equals("float")) {
            return "float";
        }
        
        if (type1.equals("int") || type2.equals("int")) {
            return "int";
        }
        
        return "string";
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public String getJavaCode() {
        return javaCode.toString();
    }
}