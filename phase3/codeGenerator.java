package phase3;
import phase3.machineCode;

import java.util.LinkedList;
import java.util.Queue;

import codeGenerator.atom;

public class codeGenerator {

    //creating a machineCode queue for our functions to populate with machineCode
    Queue<String> machieQueue = new LinkedList<>();

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
                    cmp(atom);
                    break;
                case "MOV":
                    sto(atom);
                    break;
            }

            //TODO add to bin file
        }
=
        //TODO flush and close bin file
    }

    public void clr(atom atom){
        machineCode clrCode = new machineCode(0, 0, Integer.parseInt(atom.result), 0);
        machineQueue.add(clrCode.toString());
        
    }



    public void add(atom atom){
        machineCode addCode = new machineCode(1, 0, Integer.parseInt(atom.result), Integer.parseInt(atom.left));
        machineQueue.add(addCode.toString());
      
    }

    public void sub(atom atom){
        machineCode subCode = new machineCode(2, 0, Integer.parseInt(atom.result), Integer.parseInt(atom.left));
        
    }

    public void mul(atom atom){
        
    }

    public void div(atom atom){
        
    }

    public void jmp(atom atom){
        machineCode jmpCode = new machineCode(0, 0, 0, 0);
        jmpCode.opcode = 5;
        jmpCode.a = Integer.parseInt(atom.dest);
        machieQueue.add(jmpCode.toString());
    }

    public void cmp(atom atom){
        machineCode cmpCode = new machineCode(0, 0, 0, 0);
        cmpCode.opcode = 6;
        cmpCode.cmp = atom.cmp;
        cmpCode.a = Integer.parseInt(atom.dest);
        
    }

    public void lod(atom atom){
        machineCode lodCode = new machineCode(7, 0, 0, 0);
        lodCode.a = Integer.parseInt(atom.dest); //not sure if these lines are right 
        lodCode.r = Integer.parseInt(atom.result); //
        machineQueue.add(lodCode.toString());         
    }

    public void sto(atom atom){
        machineCode stoCode = new machineCode(8, 0, 0, 0);
        stoCode.a = Integer.parseInt(atom.dest);//
        stoCode.r = Integer.parseInt(atom.result);//
        machineQueue.add(stoCode.toString());        
    }

    public void hlt(atom atom){
        machineCode hltCode = new machineCode(9, 0, 0, 0);
        machineQueue.add(hltCode.toString());        
    }
}
