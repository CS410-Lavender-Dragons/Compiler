package phase3;
import codeGenerator.atom;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class codeGenerator {

    // machineCode queue for our functions to populate with machineCode
    //private Queue<String> machineQueue;
    private Queue<Integer> machineQueue;
    // labelTable to store labels' associated address (pc value)
    private Hashtable<String, Integer> labelTable;
    // memoryTable to store identifier's associated memory address
    private LinkedHashMap<String, Integer> memoryTable;
    // tracks memory address generation
    private int memAddr;
    private int pc;

    public Queue<Integer> generateCode(Queue<atom> atoms){
        // Initialize labelTable, machineQueue, memoryTable, memAddr here so reusable for multiple passes
        labelTable = new Hashtable<>();
        machineQueue = new LinkedList<>();
        memoryTable = new LinkedHashMap<>();
        memAddr = 0;
        pc = 0;
        buildLabelsAndMem(atoms);
        adjustMemAddr();
        System.out.println(memoryTable);
        generate(atoms);
        machineQueue.addAll(genMemArea());
       
        return machineQueue;
    }

    // First pass - builds label and mem tables
    public void buildLabelsAndMem(Queue<atom> atoms) {
        for (atom atom : atoms) {
            switch(atom.name){
                case "ADD":
                case "SUB":
                case "MUL":
                case "DIV":
                case "NEG":
                    addToMemTable(atom.result);
                    addToMemTable(atom.left);
                    pc += 12;
                    break;
                case "LBL":
                    labelTable.put(atom.dest, Integer.valueOf(pc));
                    break;
                case "JMP":
                    pc += 8;
                    break;
                case "MOV":
                    addToMemTable(atom.dest);
                    addToMemTable(atom.left);
                    pc += 8;
                    break;
                default:
                    pc += 12;
            }
        }
        pc += 4; // Account for hlt instruction
    }

    private void adjustMemAddr(){
        for (Map.Entry<String, Integer> mem : memoryTable.entrySet())
            mem.setValue(mem.getValue() * 4 + pc);
    }

    // Second pass - generate machine code
    private void generate(Queue<atom> atoms){
        int initializeSize = atoms.size();
        for (int i = 0; i < initializeSize; i++) {
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
                    machineQueue.add(trueCmpCode.combineParameters()); // Set flag to true
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
        hlt();
    }

    private Queue<Integer> genMemArea(){
        Queue<Integer> memStrings = new LinkedList<>();
        for (Map.Entry<String, Integer> mem : memoryTable.entrySet()) {
            //memStrings.add(Character.isAlphabetic(mem.getKey().charAt(0)) ? "00000000000000000000000000000000" : String.format("%32s", Integer.toBinaryString(Float.floatToIntBits(Float.parseFloat(mem.getKey())))).replace(' ', '0') );
            memStrings.add(Float.floatToIntBits(Float.parseFloat(Character.isAlphabetic(mem.getKey().charAt(0)) ? "00000000000000000000000000000000" : mem.getKey())));
        }
        return memStrings;
    }

    private void clr(int register){
        machineCode clrCode = new machineCode(0, 0, register, 0);
        machineQueue.add(clrCode.combineParameters());
    }

    private void add(int register, int addr){
        machineCode addCode = new machineCode(1,0,register,addr);
        machineQueue.add(addCode.combineParameters());
    }

    private void sub(int register, int addr){
        machineCode subCode = new machineCode(2, 0,register,addr);
        machineQueue.add(subCode.combineParameters());
    }

    private void mul(int register, int addr){
        machineCode mulCode = new machineCode(3, 0,register,addr);
        machineQueue.add(mulCode.combineParameters());
    }

    private void div(int register, int addr){
        machineCode divCode = new machineCode(4, 0,register,addr);
        machineQueue.add(divCode.combineParameters());
    }
    
    private void jmp(atom atom){
        machineCode jmpCode = new machineCode(0, 0, 0, 0);
        jmpCode.opcode = 5;
        jmpCode.a = labelTable.get(atom.dest);
        machineQueue.add(jmpCode.combineParameters());
    }

    private void cmp(int register, int addr, int cmp){
        machineCode cmpCode = new machineCode(6, cmp, register, addr);
        machineQueue.add(cmpCode.combineParameters());
    }

    private void lod(int register, int addr){
        machineCode lodCode = new machineCode(7, 0, register, addr);
        machineQueue.add(lodCode.combineParameters());
    }

    private void sto(int register, int addr){
        machineCode stoCode = new machineCode(8, 0, register, addr);
        machineQueue.add(stoCode.combineParameters());
    }

    private void hlt(){
        machineCode hltCode = new machineCode(9, 0, 0, 0);
        machineQueue.add(hltCode.combineParameters());
    }

    private int getRegister(){
        return 1;
    }

    private void addToMemTable(String val){
        if (memoryTable.putIfAbsent(val, memAddr) == null)
            memAddr++;
    }
}
