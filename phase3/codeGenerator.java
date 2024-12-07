package phase3;
import codeGenerator.atom;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class codeGenerator {

    // machineCode queue for our functions to populate with machineCode
    private Queue<String> machineQueue;
    // labelTable to store labels' associated address (pc value)
    private Hashtable<String, Integer> labelTable;
    // memoryTable to store identifier's associated memory address
    private Hashtable<String, Integer> memoryTable;
    // constantsTable to store literal constants memory addresses
    private Hashtable<Integer, Integer> constantsTable;
    // tracks memory address generation
    private int memAddr;
    private int pc;
    private int constantsBaseAddress = 100; 
    private int constantsAddress;

    public Queue<String> generateCode(Queue<atom> atoms){
        // Initialize labelTable, machineQueue, memoryTable, memAddr here so reusable for multiple passes
        labelTable = new Hashtable<>();
        machineQueue = new LinkedList<>();
        memoryTable = new Hashtable<>();
        constantsTable = new Hashtable<>();
        constantsAddress = constantsBaseAddress;
        memAddr = 0;
        pc = 0;
        //writeConstantsToBin();
        buildLabelsAndMem(atoms);
        System.out.println(pc);
        adjustMemAddr();
        System.out.println(memoryTable);
        generate(atoms);
        machineQueue.addAll(genMemArea());
       
        return machineQueue;
    }

    // First pass - builds label table
    public void buildLabelsAndMem(Queue<atom> atoms) {
        for (atom atom : atoms) {
            switch(atom.name){
                case "ADD":
                case "SUB":
                case "MUL":
                case "DIV":
                case "NEG":
                    memoryTable.putIfAbsent(atom.result, memAddr++);
                    memoryTable.putIfAbsent(atom.left, memAddr++);
                    pc += 12;
                    break;
                case "LBL":
                    labelTable.put(atom.dest, Integer.valueOf(pc));
                    break;
                case "JMP":
                    pc += 8;
                    break;
                case "MOV":
                    memoryTable.putIfAbsent(atom.dest, memAddr++);
                    memoryTable.putIfAbsent(atom.left, memAddr++);
                    pc += 8;
                    break;
                default:
                    pc += 12;
            }
        }
    }

    private void adjustMemAddr(){
        for (Map.Entry<String, Integer> mem : memoryTable.entrySet())
            mem.setValue(mem.getValue() * 4 + pc);
    }

    // Second pass - generate machine code
    private void generate(Queue<atom> atoms){
        for (int i = 0; i < atoms.size(); i++) {
            atom atom = atoms.remove();
            switch(atom.name){
                case "ADD":
                    int register = getRegister();
                    lod(register, memoryTable.get(atom.left));
                    add(register, memoryTable.get(atom.right));
                    sto(register, memoryTable.get(atom.result));
                    break;
                case "SUB":
                    int sub_register = getRegister();
                    lod(sub_register, memoryTable.get(atom.left));
                    sub(sub_register, memoryTable.get(atom.right));
                    sto(sub_register, memoryTable.get(atom.result));
                    break;
                case "MUL":
                    int mul_register = getRegister();
                    lod(mul_register, memoryTable.get(atom.left));
                    mul(mul_register, memoryTable.get(atom.right));
                    sto(mul_register, memoryTable.get(atom.result));
                    break;
                case "DIV":
                    int div_register = getRegister();
                    lod(div_register, memoryTable.get(atom.left));
                    div(div_register, memoryTable.get(atom.right));
                    sto(div_register, memoryTable.get(atom.result));
                    break;
                case "JMP":
                    machineCode trueCmpCode = new machineCode(6, 0, 0, 0);
                    machineQueue.add(trueCmpCode.toString()); // Set flag to true
                    jmp(atom);
                    break;
                case "NEG":
                    int neg_register = getRegister();
                    clr(neg_register);
                    sub(neg_register, memoryTable.get(atom.left));
                    sto(neg_register, memoryTable.get(atom.result));
                    break;
                case "TST":
                    int tst_register = getRegister();
                    lod(tst_register, memoryTable.get(atom.left));
                    cmp(tst_register, memoryTable.get(atom.right), atom.cmp);
                    jmp(atom);
                    break;
                case "MOV":
                    int mov_register = getRegister();
                    lod(mov_register, memoryTable.get(atom.left));
                    sto(mov_register, memoryTable.get(atom.dest));
                    break;
            }
        }
    }

    public Queue<String> genMemArea(){
        Queue<String> memStrings = new LinkedList<>();
        for (Map.Entry<String, Integer> mem : memoryTable.entrySet()) {
            memStrings.add(Character.isAlphabetic(mem.getKey().charAt(0)) ? "00000000000000000000000000000000" : Integer.toBinaryString(Float.floatToIntBits(Float.parseFloat(mem.getKey()))));
        }
        return memStrings;
    }

    public void clr(int register){
        machineCode clrCode = new machineCode(0, 0, register, 0);
        machineQueue.add(clrCode.toString());
    }

    public void add(int register, int addr){
        machineCode addCode = new machineCode(1,0,register,addr);
        machineQueue.add(addCode.toString());
    }

    public void sub(int register, int addr){
        machineCode subCode = new machineCode(2, 0,register,addr);
        machineQueue.add(subCode.toString());
    }

    public void mul(int register, int addr){
        machineCode mulCode = new machineCode(3, 0,register,addr);
        machineQueue.add(mulCode.toString());
    }

    public void div(int register, int addr){
        machineCode divCode = new machineCode(4, 0,register,addr);
        machineQueue.add(divCode.toString());
    }
    
    public void jmp(atom atom){
        machineCode jmpCode = new machineCode(0, 0, 0, 0);
        jmpCode.opcode = 5;
        jmpCode.a = labelTable.get(atom.dest);
        machineQueue.add(jmpCode.toString());
    }

    public void cmp(int register, int addr, int cmp){
        machineCode cmpCode = new machineCode(6, cmp, register, addr);
        machineQueue.add(cmpCode.toString());
    }

    public void lod(int register, int addr){
        machineCode lodCode = new machineCode(7, 0, register, addr);
        machineQueue.add(lodCode.toString());         
    }

    public void sto(int register, int addr){
        machineCode stoCode = new machineCode(8, 0, register, addr);
        machineQueue.add(stoCode.toString());        
    }

    public void hlt(atom atom){
        machineCode hltCode = new machineCode(9, 0, 0, 0);
        machineQueue.add(hltCode.toString());        
    }

    private int getRegister(){
        return 1;
    }

  
}
