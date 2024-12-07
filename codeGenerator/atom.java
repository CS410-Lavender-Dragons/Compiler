package codeGenerator;


//reference
// 0 - always
// 1 - equal
// 2 - lesser
// 3 - greater
// 4 - lesser or equal
// 5 - greater or equal
// 6 - unequal


//data holder 
public class atom {
    // Based on (name, left, right, result, cmp, dest). 
    
    // Data
    public String name, left, right, result, dest;
    public int cmp;
    
    // Constructor
    public atom(String name, String left, String right, String result, int cmp, String dest){
        this.name = name; 
        this.left = left; 
        this.right = right; 
        this.result = result; 
        this.cmp = cmp; 
        this.dest = dest; 
    }

    @Override
    // Print atom by Phase 2 specification
    public String toString() {
        String str = "";

        switch(name) {
            case "ADD": // (ADD, left, right, result)
                str = "(ADD, " + left + ", " + right + ", " + result + ")";
                break;
            case "SUB": // (SUB, left, right, result)
                str = "(SUB, " + left + ", " + right + ", " + result + ")";    
                break;
            case "MUL": // (MUL, left, right, result)
                str = "(MUL, " + left + ", " + right + ", " + result + ")";    
                break;
            case "DIV": // (DIV, left, right, result)
                str = "(DIV, " + left + ", " + right + ", " + result + ")";
                break;
            case "JMP": // (JMP, , , , , dest)
                str = "(JMP, , , , " + dest + ")";
                break;
            case "NEG": // (NEG, left, , result)
                str = "(NEG, " + left + ", , " + result + ")";
                break;
            case "LBL": // (LBL, , , , , dest)
                str = "(LBL, , , , , " + dest + ")";    
                break;
            case "TST": // (TST, left, right, , cmp, dest)
                str = "(TST, " + left + ", " + right + ", , " + cmp + ", " + dest + ")";    
                break;
            case "MOV": // (MOV, s, , d)
                str = "(MOV, " + left + ", , " + dest + ")";
                break;
            default:
                // Name doesn't match any cases
                System.err.println("Atom name does not match");
        }
        
        return str;
    }
}    
