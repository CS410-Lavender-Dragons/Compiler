package Compiler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import Core.Atom;
import CodeGenerator.CodeGenerator;

public class Backend {
    public static void main(String[] args) throws IOException {
        CodeGenerator codegen = new CodeGenerator();

        String inputFile = args[0];
        int optimizedFlag = 0;

        try {
            if(args[1] == "-o"){
                optimizedFlag = 1;
            }
        }
        catch (IndexOutOfBoundsException e){
            optimizedFlag = 0;
        }

        // Read atoms in from intermediary file and pass to generateCode
        Queue<Atom> atoms = readAtomsFromFile(inputFile);

        Queue<Integer> machineCodes = new LinkedList<Integer>();
        if (optimizedFlag == 1) {
            machineCodes = codegen.generateCode(atoms, true);
        } else if (optimizedFlag == 0) {
            machineCodes = codegen.generateCode(atoms, false);
        }

        // Create bin file
        String filename = "oxide.bin";
        try {
            File binF = new File(filename);

            if (binF.createNewFile()) {
                System.out.println("File created: " + binF.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename));
            // Loop through machine code queue and write each instruction to bin file
            dos.writeInt(0);
            while (!machineCodes.isEmpty()) {
                int currByte = machineCodes.remove();
                dos.writeInt(currByte);
            }

            dos.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static Queue<Atom> readAtomsFromFile(String filePath) throws IOException {
        // Create an empty queue for atoms
        Queue<Atom> atomsQueue = new LinkedList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Reads the each file line until the end
            while ((line = reader.readLine()) != null) {
                //parses the line into an atom using helper method
                atomsQueue.add(parseAtom(line)); 
            }
        }
        // returns the queue of atoms
        return atomsQueue;
    }

    //reads a line from file and turns it into an atom
    private static Atom parseAtom(String line) {
        // removes parentheses and split by commas
        line = line.trim().substring(1, line.length() - 1); 
        
        //string array to hold parts of the atom
        String[] parts = line.split(",\\s*");
    
        String name = parts[0];
        String left = null, right = null, result = null, dest = null;
        int cmp = -1;
        
        //gets the info for each part of the atom
        switch(name) {
            case "ADD":
            case "SUB":
            case "MUL":
            case "DIV":
                left = parts[1];
                right = parts[2];
                result = parts[3];
                break;
    
            case "JMP":
            case "LBL":
                dest = parts[4];
                break;
    
            case "NEG":
                left = parts[1];
                result = parts[3];
                break;
    
            case "TST":
                left = parts[1];
                right = parts[2];
                cmp = Integer.parseInt(parts[4]);
                dest = parts[5];
                break;
    
            case "MOV":
                left = parts[1];
                dest = parts[3];
                break;
    
            default:
                System.err.println("Unknown atom type: " + name);
        }
        
        //returns the info in an atom
        return new Atom(name, left, right, result, cmp, dest);
    }
}
