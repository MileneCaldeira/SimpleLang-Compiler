import java.io.*;
import java.util.List;

public class Compiler {
    
    public static CompilationResult compile(String sourceCode) {
        CompilationResult result = new CompilationResult();
        
        try {
            Lexer lexer = new Lexer(sourceCode);
            List<Token> tokens = lexer.tokenize();
            
            for (Token token : tokens) {
                if (token.getType() == Token.TokenType.INVALID) {
                    result.addError(String.format("Token inválido '%s' na linha %d, coluna %d",
                            token.getValue(), token.getLine(), token.getColumn()));
                }
            }
            
            if (result.hasErrors()) {
                return result;
            }
            
            Parser parser = new Parser(tokens);
            parser.parse();
            
            if (parser.hasErrors()) {
                for (String error : parser.getErrors()) {
                    result.addError(error);
                }
            } else {
                result.setSuccess(true);
                result.setJavaCode(parser.getJavaCode());
            }
            
        } catch (Exception e) {
            result.addError("Erro durante a compilação: " + e.getMessage());
        }
        
        return result;
    }
    
    public static CompilationResult compileFile(String inputFilePath) {
        try {
            String sourceCode = readFile(inputFilePath);
            return compile(sourceCode);
        } catch (IOException e) {
            CompilationResult result = new CompilationResult();
            result.addError("Erro ao ler arquivo: " + e.getMessage());
            return result;
        }
    }
    
    public static void compileAndSave(String inputFilePath, String outputFilePath) {
        CompilationResult result = compileFile(inputFilePath);
        
        System.out.println("=== RESULTADO DA COMPILAÇÃO ===");
        
        if (result.isSuccess()) {
            System.out.println("✓ Compilação realizada com sucesso!");
            System.out.println("\nCódigo Java gerado salvo em: " + outputFilePath);
            
            try {
                writeFile(outputFilePath, result.getJavaCode());
            } catch (IOException e) {
                System.err.println("Erro ao salvar arquivo: " + e.getMessage());
            }
        } else {
            System.out.println("✗ Compilação falhou com os seguintes erros:\n");
            for (String error : result.getErrors()) {
                System.out.println("  " + error);
            }
        }
        
        System.out.println("================================");
    }
    
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        
        reader.close();
        return content.toString();
    }
    
    private static void writeFile(String filePath, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(content);
        writer.close();
    }
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Compiler <arquivo_entrada> [arquivo_saida]");
            System.out.println("Exemplo: java Compiler programa.sl GeneratedCode.java");
            return;
        }
        
        String inputFile = args[0];
        String outputFile = args.length > 1 ? args[1] : "GeneratedCode.java";
        
        compileAndSave(inputFile, outputFile);
    }
}