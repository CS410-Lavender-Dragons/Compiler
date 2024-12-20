package CodeGenerator;
import Core.Atom;

import java.util.*;
import java.util.stream.Collectors;

public class CodeGenerator {

    // machineCode queue for our functions to populate with machineCode
    private Queue<MachineCode> instructionQueue;
    // labelTable to store labels' associated address (pc value)
    private Hashtable<String, Integer> labelTable;
    // memoryTable to store identifier's associated memory address
    private LinkedHashMap<String, Integer> memoryTable;
    // tracks memory address generation
    private int memAddr;
    // tracks # of instructions
    private int pc;
    private boolean optimizeLODSTO; // Flag for optimization
    private static final int ADDRESSING_MULTIPLIER = 1;
    private Map<Integer, Integer> registerMemoryMap = new HashMap<>(); // Tracks register-memory mappings

    public Queue<Integer> generateCode(Queue<Atom> atoms, final boolean optimized){
        optimizeLODSTO = optimized;
        generateInstructions(atoms);
        if (optimizeLODSTO)
            updateOptimizedMemAddr();
        Queue<Integer> machineQueue = instructionQueue.stream().map(instr -> instr.combineParameters()).collect(Collectors.toCollection(LinkedList::new));
        machineQueue.addAll(genMemArea());
        return machineQueue;
    }


    private Queue<MachineCode> generateInstructions(Queue<Atom> atoms){
        // Initialize pc, instructionQueue here so reusable for multiple passes
        instructionQueue = new LinkedList<>();
        pc = 0;
        buildLabelsAndMem(atoms);
        if (!optimizeLODSTO)
            adjustMemAddr();
        generate(atoms);
        return instructionQueue;
    }

    // First pass - builds label and mem tables
    private void buildLabelsAndMem(Queue<Atom> atoms) {
        // Initialize labelTable, memoryTable, memAddr here so reusable for multiple passes
        labelTable = new Hashtable<>();
        memoryTable = new LinkedHashMap<>();
        memAddr = 0;
        for (Atom atom : atoms) {
            switch(atom.name){
                case "ADD":
                case "SUB":
                case "MUL":
                case "DIV":
                case "NEG":
                    addToMemTable(atom.result);
                    addToMemTable(atom.left);
                    pc += 3 * ADDRESSING_MULTIPLIER;
                    break;
                case "LBL":
                    labelTable.put(atom.dest, Integer.valueOf(pc));
                    break;
                case "JMP":
                    pc += 2 * ADDRESSING_MULTIPLIER;
                    break;
                case "MOV":
                    addToMemTable(atom.dest);
                    addToMemTable(atom.left);
                    pc += 2 * ADDRESSING_MULTIPLIER;
                    break;
                default:
                    pc += 3 * ADDRESSING_MULTIPLIER;
            }
        }
        pc += 1 * ADDRESSING_MULTIPLIER; // Account for hlt instruction
    }

    private void adjustMemAddr(){
        for (Map.Entry<String, Integer> mem : memoryTable.entrySet())
            mem.setValue(mem.getValue() * ADDRESSING_MULTIPLIER + pc * ADDRESSING_MULTIPLIER + ADDRESSING_MULTIPLIER); // +ADDRESSING_MULTIPLIER accounts for extra CLR instr at beginning
    }
    private static <T, V> T getKeyByValue(Map<T, V> map, V value) {
        for (Map.Entry<T, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
    private void updateOptimizedMemAddr(){
        Map<String, Integer> oldMemMap = Map.copyOf(memoryTable);
        adjustMemAddr();
        for (int i = 0; i < instructionQueue.size(); i++){
            MachineCode instruction = instructionQueue.remove();
            String valueKey = getKeyByValue(oldMemMap, instruction.a);
            if (valueKey != null)
                instruction.a = memoryTable.get(valueKey);
            instructionQueue.add(instruction);
        }
    }

    // Second pass - generate machine code
    private void generate(Queue<Atom> atoms){
        int initializeSize = atoms.size();
        for (int i = 0; i < initializeSize; i++) {
            Atom atom = atoms.remove();
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
                    MachineCode trueCmpCode = new MachineCode(6, 0, 0, 0);
                    instructionQueue.add(trueCmpCode); // Set flag to true
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
        MachineCode clrCode = new MachineCode(0, 0, register, 0);
        instructionQueue.add(clrCode);
    }

    private void add(int register, int addr){
        MachineCode addCode = new MachineCode(1,0,register,addr);
        instructionQueue.add(addCode);
    }

    private void sub(int register, int addr){
        MachineCode subCode = new MachineCode(2, 0,register,addr);
        instructionQueue.add(subCode);
    }

    private void mul(int register, int addr){
        MachineCode mulCode = new MachineCode(3, 0,register,addr);
        instructionQueue.add(mulCode);
    }

    private void div(int register, int addr){
        MachineCode divCode = new MachineCode(4, 0,register,addr);
        instructionQueue.add(divCode);
    }
    
    private void jmp(Atom atom){
        MachineCode jmpCode = new MachineCode(0, 0, 0, 0);
        jmpCode.opcode = 5;
        jmpCode.a = labelTable.get(atom.dest);
        instructionQueue.add(jmpCode);
    }

    private void cmp(int register, int addr, int cmp){
        MachineCode cmpCode = new MachineCode(6, cmp, register, addr);
        instructionQueue.add(cmpCode);
    }

    private void lod(int register, int addr){
        if (optimizeLODSTO) {
            if (registerMemoryMap.getOrDefault(register, -1) == addr) {
                pc -= 1 * ADDRESSING_MULTIPLIER;
                return; // Skip redundant LOD - return & adjust mem addrs following
            }
        }
        MachineCode lodCode = new MachineCode(7, 0, register, addr);
        instructionQueue.add(lodCode);
        registerMemoryMap.put(register, addr);
    }

    private void sto(int register, int addr){
        if (optimizeLODSTO) {
            if (registerMemoryMap.getOrDefault(register, -1) == addr) {
                pc -= 4;
                return; // Skip redundant LOD
            }
        }
        MachineCode stoCode = new MachineCode(8, 0, register, addr);
        instructionQueue.add(stoCode);
        registerMemoryMap.put(register, addr);
    }

    private void hlt(){
        MachineCode hltCode = new MachineCode(9, 0, 0, 0);
        instructionQueue.add(hltCode);
    }

    private int getRegister(){
        return 1;
    }

    private void addToMemTable(String val){
        if (memoryTable.putIfAbsent(val, memAddr) == null)
            memAddr++;
    }
}
