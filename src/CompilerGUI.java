import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class CompilerGUI extends JFrame {
    private JTextArea sourceCodeArea;
    private JTextArea outputArea;
    private JTextArea javaCodeArea;
    private JButton compileButton;
    private JButton compileAndRunButton;
    private JButton openButton;
    private JButton saveButton;
    private JButton clearButton;
    private JLabel statusLabel;
    
    public CompilerGUI() {
        setTitle("SimpleLang Compiler");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBackground(new Color(240, 240, 240));
        
        openButton = new JButton("Abrir Arquivo");
        openButton.addActionListener(e -> openFile());
        
        saveButton = new JButton("Salvar Java");
        saveButton.addActionListener(e -> saveJavaCode());
        
        compileButton = new JButton("Compilar");
        compileButton.setBackground(new Color(76, 175, 80));
        compileButton.setForeground(Color.WHITE);
        compileButton.setFocusPainted(false);
        compileButton.addActionListener(e -> compile());
        
        compileAndRunButton = new JButton("Compilar e Executar");
        compileAndRunButton.setBackground(new Color(33, 150, 243));
        compileAndRunButton.setForeground(Color.WHITE);
        compileAndRunButton.setFocusPainted(false);
        compileAndRunButton.setToolTipText("Compila e executa em terminal externo");
        compileAndRunButton.addActionListener(e -> compileAndRun());
        
        clearButton = new JButton("Limpar");
        clearButton.addActionListener(e -> clearAll());
        
        topPanel.add(openButton);
        topPanel.add(saveButton);
        topPanel.add(compileButton);
        topPanel.add(compileAndRunButton);
        topPanel.add(clearButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        
        JPanel sourcePanel = new JPanel(new BorderLayout());
        sourcePanel.setBorder(BorderFactory.createTitledBorder("Código SimpleLang"));
        sourceCodeArea = new JTextArea();
        sourceCodeArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        sourceCodeArea.setLineWrap(false);
        sourceCodeArea.setTabSize(4);
        JScrollPane sourceScroll = new JScrollPane(sourceCodeArea);
        sourcePanel.add(sourceScroll, BorderLayout.CENTER);
        
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Resultado da Compilação"));
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputPanel.add(outputScroll, BorderLayout.CENTER);
        
        JPanel javaPanel = new JPanel(new BorderLayout());
        javaPanel.setBorder(BorderFactory.createTitledBorder("Código Java Gerado"));
        javaCodeArea = new JTextArea();
        javaCodeArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        javaCodeArea.setEditable(false);
        javaCodeArea.setLineWrap(false);
        JScrollPane javaScroll = new JScrollPane(javaCodeArea);
        javaPanel.add(javaScroll, BorderLayout.CENTER);
        
        centerPanel.add(sourcePanel);
        centerPanel.add(outputPanel);
        centerPanel.add(javaPanel);
        
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(240, 240, 240));
        statusLabel = new JLabel("Pronto");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        bottomPanel.add(statusLabel);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        sourceCodeArea.setText(getExampleCode());
    }
    
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("SimpleLang Files (*.sl)", "sl"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                sourceCodeArea.read(reader, null);
                reader.close();
                statusLabel.setText("Arquivo aberto: " + file.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir arquivo: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void saveJavaCode() {
        if (javaCodeArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum código Java para salvar. Compile primeiro!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Java Files (*.java)", "java"));
        fileChooser.setSelectedFile(new File("GeneratedCode.java"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".java")) {
                file = new File(file.getAbsolutePath() + ".java");
            }
            
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(javaCodeArea.getText());
                writer.close();
                statusLabel.setText("Código Java salvo: " + file.getName());
                JOptionPane.showMessageDialog(this, "Código Java salvo com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void compile() {
        String sourceCode = sourceCodeArea.getText();
        
        if (sourceCode.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite ou carregue um código fonte!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        outputArea.setText("Compilando...\n");
        javaCodeArea.setText("");
        statusLabel.setText("Compilando...");
        
        SwingWorker<CompilationResult, Void> worker = new SwingWorker<CompilationResult, Void>() {
            @Override
            protected CompilationResult doInBackground() {
                return Compiler.compile(sourceCode);
            }
            
            @Override
            protected void done() {
                try {
                    CompilationResult result = get();
                    displayResult(result);
                } catch (Exception e) {
                    outputArea.setText("Erro durante a compilação: " + e.getMessage());
                    statusLabel.setText("Erro");
                }
            }
        };
        
        worker.execute();
    }
    
    private void compileAndRun() {
        String sourceCode = sourceCodeArea.getText();
        
        if (sourceCode.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite ou carregue um código fonte!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Aviso se tiver scan()
        if (sourceCode.contains("scan(")) {
            int option = JOptionPane.showConfirmDialog(
                this,
                "Este programa contém scan() para entrada de dados.\n" +
                "Será executado em um TERMINAL SEPARADO onde você\n" +
                "poderá digitar os valores solicitados.\n\n" +
                "Continuar?",
                "Entrada de Dados Detectada",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );
            
            if (option != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        outputArea.setText("Compilando...\n");
        javaCodeArea.setText("");
        statusLabel.setText("Compilando...");
        
        SwingWorker<CompilationResult, Void> worker = new SwingWorker<CompilationResult, Void>() {
            @Override
            protected CompilationResult doInBackground() {
                return Compiler.compile(sourceCode);
            }
            
            @Override
            protected void done() {
                try {
                    CompilationResult result = get();
                    
                    if (result.isSuccess()) {
                        javaCodeArea.setText(result.getJavaCode());
                        
                        try {
                            File javaFile = new File("GeneratedCode.java");
                            BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile));
                            writer.write(result.getJavaCode());
                            writer.close();
                            
                            outputArea.setText("✓ Compilação SimpleLang bem-sucedida!\n\n");
                            outputArea.append("Salvando código Java...\n");
                            outputArea.append("✓ Arquivo: GeneratedCode.java\n\n");
                            outputArea.append("Compilando código Java...\n");
                            
                            Process compileProcess = Runtime.getRuntime().exec("javac GeneratedCode.java");
                            compileProcess.waitFor();
                            
                            if (compileProcess.exitValue() == 0) {
                                outputArea.append("✓ Código Java compilado!\n\n");
                                outputArea.append("========================================\n");
                                outputArea.append("  ABRINDO TERMINAL PARA EXECUÇÃO\n");
                                outputArea.append("========================================\n\n");
                                outputArea.append("O programa está sendo executado em uma\n");
                                outputArea.append("janela de terminal separada.\n\n");
                                
                                if (sourceCode.contains("scan(")) {
                                    outputArea.append("⚠ ATENÇÃO: Este programa solicita entrada!\n");
                                    outputArea.append("   Digite os valores no TERMINAL que abriu.\n\n");
                                }
                                
                                String os = System.getProperty("os.name").toLowerCase();
                                
                                if (os.contains("win")) {
                                    Runtime.getRuntime().exec("cmd /c start cmd /k java GeneratedCode");
                                    outputArea.append("✓ Terminal aberto (Windows CMD)\n");
                                } else if (os.contains("mac")) {
                                    Runtime.getRuntime().exec(new String[]{
                                        "osascript", "-e",
                                        "tell application \"Terminal\" to do script \"cd " + 
                                        System.getProperty("user.dir") + " && java GeneratedCode\""
                                    });
                                    outputArea.append("✓ Terminal aberto (Mac Terminal)\n");
                                } else {
                                    try {
                                        Runtime.getRuntime().exec(new String[]{
                                            "gnome-terminal", "--", "bash", "-c",
                                            "java GeneratedCode; read -p 'Pressione Enter...'"
                                        });
                                        outputArea.append("✓ Terminal aberto (Linux)\n");
                                    } catch (Exception e1) {
                                        try {
                                            Runtime.getRuntime().exec(new String[]{
                                                "xterm", "-hold", "-e", "java GeneratedCode"
                                            });
                                            outputArea.append("✓ Terminal aberto (xterm)\n");
                                        } catch (Exception e2) {
                                            outputArea.append("\n⚠ Execute manualmente:\n");
                                            outputArea.append("   java GeneratedCode\n");
                                        }
                                    }
                                }
                                
                                statusLabel.setText("✓ Executando em terminal externo");
                                
                            } else {
                                outputArea.append("❌ Erro ao compilar Java\n");
                                BufferedReader errorReader = new BufferedReader(
                                    new InputStreamReader(compileProcess.getErrorStream()));
                                String line;
                                while ((line = errorReader.readLine()) != null) {
                                    outputArea.append(line + "\n");
                                }
                                statusLabel.setText("❌ Erro");
                            }
                            
                        } catch (Exception ex) {
                            outputArea.append("❌ Erro: " + ex.getMessage() + "\n\n");
                            outputArea.append("Execute manualmente:\n");
                            outputArea.append("javac GeneratedCode.java\n");
                            outputArea.append("java GeneratedCode\n");
                            statusLabel.setText("⚠ Compilado (execute manualmente)");
                        }
                        
                    } else {
                        displayResult(result);
                    }
                    
                } catch (Exception e) {
                    outputArea.setText("❌ Erro: " + e.getMessage());
                    statusLabel.setText("❌ Erro");
                }
            }
        };
        
        worker.execute();
    }
    
    private void displayResult(CompilationResult result) {
        outputArea.setText("");
        
        if (result.isSuccess()) {
            outputArea.setForeground(new Color(0, 128, 0));
            outputArea.setText("✓ COMPILAÇÃO BEM-SUCEDIDA!\n\n");
            outputArea.append("O código está sintaticamente e semanticamente correto.\n");
            outputArea.append("O código Java foi gerado com sucesso.\n\n");
            outputArea.append("Use 'Compilar e Executar' para rodar o programa\n");
            outputArea.append("ou 'Salvar Java' para salvar o código gerado.");
            
            javaCodeArea.setText(result.getJavaCode());
            statusLabel.setText("Compilação bem-sucedida");
            
        } else {
            outputArea.setForeground(new Color(200, 0, 0));
            outputArea.setText("✗ COMPILAÇÃO FALHOU\n\n");
            outputArea.append("Erros encontrados:\n\n");
            
            int errorCount = 1;
            for (String error : result.getErrors()) {
                outputArea.append(errorCount + ". " + error + "\n");
                errorCount++;
            }
            
            statusLabel.setText("Compilação falhou - " + result.getErrors().size() + " erro(s)");
        }
    }
    
    private void clearAll() {
        sourceCodeArea.setText("");
        outputArea.setText("");
        javaCodeArea.setText("");
        statusLabel.setText("Pronto");
    }
    
    private String getExampleCode() {
        return "// Exemplo de código SimpleLang\n\n" +
               "int numero;\n" +
               "print(\"Digite um numero:\");\n" +
               "scan(numero);\n\n" +
               "if (numero > 0) {\n" +
               "    print(\"O numero e positivo\");\n" +
               "} else {\n" +
               "    print(\"O numero e negativo ou zero\");\n" +
               "}\n\n" +
               "print(\"Programa finalizado!\");\n";
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            CompilerGUI gui = new CompilerGUI();
            gui.setVisible(true);
        });
    }
}