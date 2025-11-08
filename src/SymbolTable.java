import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SymbolTable {
    public static class Symbol {
        private String name;
        private String type;
        private int scope;
        
        public Symbol(String name, String type, int scope) {
            this.name = name;
            this.type = type;
            this.scope = scope;
        }
        
        public String getName() {
            return name;
        }
        
        public String getType() {
            return type;
        }
        
        public int getScope() {
            return scope;
        }
    }
    
    private Stack<Map<String, Symbol>> scopes;
    private int currentScope;
    
    public SymbolTable() {
        scopes = new Stack<>();
        currentScope = 0;
        enterScope();
    }
    
    public void enterScope() {
        scopes.push(new HashMap<>());
        currentScope++;
    }
    
    public void exitScope() {
        if (!scopes.isEmpty()) {
            scopes.pop();
            currentScope--;
        }
    }
    
    public void addSymbol(String name, String type) throws Exception {
        Map<String, Symbol> currentScopeMap = scopes.peek();
        if (currentScopeMap.containsKey(name)) {
            throw new Exception("Variável '" + name + "' já declarada no escopo atual");
        }
        currentScopeMap.put(name, new Symbol(name, type, currentScope));
    }
    
    public Symbol lookup(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Map<String, Symbol> scope = scopes.get(i);
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        return null;
    }
    
    public boolean isDeclared(String name) {
        return lookup(name) != null;
    }
    
    public String getType(String name) {
        Symbol symbol = lookup(name);
        return symbol != null ? symbol.getType() : null;
    }
}