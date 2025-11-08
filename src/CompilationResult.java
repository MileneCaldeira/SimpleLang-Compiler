import java.util.ArrayList;
import java.util.List;

public class CompilationResult {
    private boolean success;
    private List<String> errors;
    private String javaCode;
    
    public CompilationResult() {
        this.success = false;
        this.errors = new ArrayList<>();
        this.javaCode = "";
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void addError(String error) {
        this.errors.add(error);
        this.success = false;
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    public String getJavaCode() {
        return javaCode;
    }
    
    public void setJavaCode(String javaCode) {
        this.javaCode = javaCode;
    }
}