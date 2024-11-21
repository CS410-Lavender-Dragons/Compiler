package phase3;
import java.util.Queue;

import codeGenerator.atom;

public class codeGenerator {
    
    public void generate(Queue<atom> atoms){

        //Part B
        //Build a label table while your code generator translates label-based atoms to placeholder instructions.
        // Follow either the single-pass strategy or the multi-pass strategy.
        //  Incorporate your design into the code generator of step A above.

        //TODO create bin file


       for (int i = 0; i < atoms.size(); i++) {
            atom atom = atoms.remove();
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

            //TODO add to bin file
        }

        //TODO flush and close bin file
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
