========================================
COMPILADOR SIMPLELANG
========================================

PROJETO: Trabalho A3 - Teoria da Computação e Compiladores
DATA ENTREGA: 25/11/2025
LINGUAGEM: SimpleLang
IMPLEMENTAÇÃO: Java (sem ANTLR - implementação manual)

========================================
INTEGRANTES DO GRUPO
========================================

1. Milene dos Santos Caldeira - RA: [número]

========================================
COMO EXECUTAR
========================================

OPÇÃO 1: Pela IDE NetBeans
---------------------------
1. Abrir o projeto no NetBeans
2. Executar CompilerGUI.java (Shift+F6)
3. A interface gráfica será aberta

OPÇÃO 2: Pelo Executável JAR
-----------------------------
1. Ir na pasta dist/
2. Executar: java -jar CompiladorSimpleLang.jar
   OU duplo clique em CompiladorSimpleLang.jar

========================================
ARQUIVOS DO PROJETO
========================================

CÓDIGO FONTE (7 arquivos):
- Token.java              → Definição de tokens
- Lexer.java              → Analisador léxico
- SymbolTable.java        → Tabela de símbolos
- Parser.java             → Analisador sintático/semântico
- CompilationResult.java  → Resultado da compilação
- Compiler.java           → Compilador principal
- CompilerGUI.java        → Interface gráfica (MAIN)

ARQUIVOS DE TESTE (3 arquivos):
- teste_correto.sl        → Programa válido completo
- teste_com_erros.sl      → Testa detecção de erros
- teste_avancado.sl       → Programa complexo (calculadora)

DOCUMENTAÇÃO:
- README.txt              → Este arquivo
- DOCUMENTACAO.txt        → Documentação da linguagem
- GRAMATICA_SIMPLELANG.txt → Gramática formal

========================================
REQUISITOS ATENDIDOS
========================================

REQUISITOS MÍNIMOS:
✓ 3 tipos de variáveis (int, float, string)
✓ Estrutura if...else
✓ 2 estruturas de repetição (while, do-while, for = 3!)
✓ Precedência de operadores matemáticos correta
✓ Atribuições
✓ Comandos print() e scan()
✓ Números decimais com ponto
✓ Eliminação de espaços, tabs e enters
✓ Mensagens de erro detalhadas
✓ Mensagem de sucesso
✓ Gramática sem recursividade à esquerda
✓ Sem produções vazias

DIFERENCIAIS IMPLEMENTADOS:
✓ Verificação de tipos nas operações
✓ Verificação de declaração de variáveis
✓ Gerenciamento de escopo de variáveis
✓ Conversão automática para código Java
✓ Interface gráfica profissional
✓ Sistema de salvamento de arquivos

========================================
CARACTERÍSTICAS DA LINGUAGEM SIMPLELANG
========================================

TIPOS DE DADOS:
- int: números inteiros
- float: números decimais
- string: texto entre aspas duplas

ESTRUTURAS DE CONTROLE:
- if (condição) { ... } else { ... }

ESTRUTURAS DE REPETIÇÃO:
- while (condição) { ... }
- do { ... } while (condição);
- for (inicialização; condição; incremento) { ... }

OPERADORES ARITMÉTICOS:
+ - * / %

OPERADORES RELACIONAIS:
== != < > <= >=

OPERADORES LÓGICOS:
&& || !

ENTRADA/SAÍDA:
- print(expressão)
- scan(variável)

COMENTÁRIOS:
// comentário de linha
/* comentário de bloco */

========================================
EXEMPLO DE CÓDIGO SIMPLELANG
========================================

int numero;
print("Digite um numero:");
scan(numero);

if (numero > 0) {
    print("Positivo");
} else {
    print("Negativo ou zero");
}

for (int i = 1; i <= 5; i = i + 1) {
    print(i);
}

========================================
ANÁLISES REALIZADAS
========================================

1. ANÁLISE LÉXICA (Lexer.java)
   - Reconhecimento de tokens
   - Remoção de espaços, tabs e enters
   - Suporte a comentários
   - Identificação de erros léxicos

2. ANÁLISE SINTÁTICA (Parser.java)
   - Validação da estrutura do programa
   - Respeito à precedência de operadores
   - Detecção de erros sintáticos

3. ANÁLISE SEMÂNTICA (Parser.java + SymbolTable.java)
   - Verificação de declaração de variáveis
   - Verificação de tipos
   - Gerenciamento de escopo
   - Detecção de redeclarações

4. GERAÇÃO DE CÓDIGO (Parser.java)
   - Conversão para código Java
   - Código Java compilável e executável

========================================
IMPLEMENTAÇÃO SEM ANTLR
========================================

Este projeto foi implementado MANUALMENTE em Java puro,
sem uso de frameworks como ANTLR.

JUSTIFICATIVA:
- Demonstra domínio completo dos conceitos
- Implementação de autômatos finitos
- Construção manual de parsers
- Maior controle sobre o processo de compilação
- Implementação educacional mais profunda

========================================
TESTANDO O COMPILADOR
========================================

1. Execute CompilerGUI.java
2. Carregue teste_correto.sl
3. Clique em "Compilar"
4. Resultado: "Compilação bem-sucedida!"
5. Veja o código Java gerado

Para testar erros:
1. Carregue teste_com_erros.sl
2. Clique em "Compilar"
3. Veja os erros detectados

========================================
ESTRUTURA DO COMPILADOR
========================================

[Código Fonte SimpleLang]
         ↓
    [Lexer.java]
    (Análise Léxica)
         ↓
    [Lista de Tokens]
         ↓
    [Parser.java]
    (Análise Sintática/Semântica)
         ↓
    [Código Java Gerado]
         ↓
    [Arquivo .java]

========================================
TECNOLOGIAS UTILIZADAS
========================================

- Linguagem: Java
- IDE: NetBeans
- Interface: Swing (GUI)
- Compilador: Implementação manual (sem ANTLR)
- Estruturas de dados: ArrayList, HashMap, Stack

========================================
RECURSOS DA INTERFACE GRÁFICA
========================================

BOTÕES:
- Abrir Arquivo: Carrega arquivos .sl
- Salvar Java: Salva código Java gerado
- Compilar: Realiza a compilação
- Limpar: Limpa todas as áreas

ÁREAS DE TEXTO:
1. Código SimpleLang (esquerda)
2. Resultado da Compilação (centro)
3. Código Java Gerado (direita)

========================================
COMPILANDO O CÓDIGO JAVA GERADO
========================================

Após gerar o código Java:
1. Salve como GeneratedCode.java
2. Compile: javac GeneratedCode.java
3. Execute: java GeneratedCode

========================================
OBSERVAÇÕES FINAIS
========================================

- O compilador gera mensagens de erro claras
- Indica linha e coluna dos erros
- Interface intuitiva e profissional
- Código Java gerado é funcional
- Todos os requisitos foram atendidos
- Diferenciais foram implementados

========================================
