package phase3;
import codeGenerator.atom;
import java.util.LinkedList;
import java.util.Queue;

public class codeGenerator {

    // machineCode queue for our functions to populate with machineCode
    Queue<String> machineQueue = new LinkedList<>();

    public void generate(Queue<atom> atoms){
        long labels[] = new long[100]; //TODO magic number
        int pc = 0; //TODO is it an int?
        
        // First pass to build lable table
        for (int i = 0; i < atoms.size(); i++) {
            atom atom = atoms.remove();
            String command = atom.name;

            if (command == "LBL") {
                labels[Integer.parseInt(atom.dest)] = pc;
            } else if (command == "MOV" || command == "JMP") {
                pc += 2;
            } else {
                pc += 3;
            }
        }

        //TODO create bin file

        pc = 0; // Reset the PC for second pass
        for (int i = 0; i < atoms.size(); i++) { //TODO now in this second pass, use the lable table and actually gen the code
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
                case "LBL": //we end up ignoring these in second pass
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

        //TODO flush and close bin file
    }

    public void clr(atom atom){
        machineCode clrCode = new machineCode(0, 0, 0, 0);
        machineQueue.add(clrCode.toString());
    }

    public void add(atom atom){
        machineCode addCode = new machineCode(1,0,0,0);
        addCode.r = Integer.parseInt(atom.result);
        addCode.a = Integer.parseInt(atom.left);
        
        machineQueue.add(addCode.toString());
    }

    public void sub(atom atom){
        machineCode subCode = new machineCode(2, 0,0,0);
        subCode.r = Integer.parseInt(atom.result);
        subCode.a = Integer.parseInt(atom.left);

        machineQueue.add(subCode.toString());
    }

    public void mul(atom atom){
        machineCode mulCode = new machineCode(3, 0,0,0);
        mulCode.r = Integer.parseInt(atom.left);
        mulCode.a = Integer.parseInt(atom.right);

        machineQueue.add(mulCode.toString());
    }

    public void div(atom atom){
        machineCode divCode = new machineCode(4, 0,0,0);

        divCode.r = Integer.parseInt(atom.left);
        divCode.a = Integer.parseInt(atom.right);

        machineQueue.add(divCode.toString());
    }

    public void jmp(atom atom){
        machineCode jmpCode = new machineCode(0, 0, 0, 0);
        jmpCode.opcode = 5;
        jmpCode.a = Integer.parseInt(atom.dest);
        machineQueue.add(jmpCode.toString());
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
