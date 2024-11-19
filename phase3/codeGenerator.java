package phase3;
import codeGenerator.atom;

public class codeGenerator {
    

    public void generate(atom atom){
        String command = atom.name;

        switch(command){
            case "ADD":
                add(atom);
                break;
            case "SUB":
                sub(atom);
                break;
            case "MUL":
                mul(atom);
                break;
            case "DIV":
                div(atom);
                break;
            case "JMP":
                jmp(atom);
                break;
            case "NEG":
                break;
            case "LBL":
                break;
            case "TST":
                break;
            case "MOV":
                break;
        }

        
    }

    public void add(atom atom){

    }

    public void sub(atom atom){
        
    }

    public void mul(atom atom){
        
    }

    public void div(atom atom){
        
    }

    public void jmp(atom atom){
        
    }

    public void cmd(atom atom){
        
    }

    public void lod(atom atom){
        
    }

    public void sto(atom atom){
        
    }

    public void hlt(atom atom){
        
    }
}
