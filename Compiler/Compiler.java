package Compiler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;

import Lexer.Lexer;
import Parser.Parser;
import Core.Atom;
import CodeGenerator.*;


public class Compiler {
    public static void main(String[] args) throws IOException{
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        CodeGenerator codegen = new CodeGenerator();

        String inputFile = args[0];
        String outputFile = args[1];
        boolean optimizedFlag = false;

        try {
            if(args[2].equals("-o")){
                optimizedFlag = true;
            }
        }
        catch (IndexOutOfBoundsException e){
        }

        Path file = Path.of(inputFile);
        String sampleCode = Files.readString(file);
        var pipelineToken = lexer.tokenize(sampleCode);

        Queue<Atom> atomList = parser.parse(pipelineToken, optimizedFlag);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("IOfiles/atoms.txt"))){
            for(int i = 0; i < atomList.size(); i++){
                String atom = atomList.poll().toString();
                writer.write(atom);
                writer.newLine();
            }
        }

        //var tokens7 = lexer.tokenize("x = 5;");
        //var tokens7 = lexer.tokenize("let x = 5;");
        //var tokens7 = lexer.tokenize("let x : i32 = 6; x = 4;");
        //var tokens7 = lexer.tokenize("for x in 1..20 { let mut y = 10;");
        //var tokens7 = lexer.tokenize("for x in 1 { let mut y = 10; }");
        //var tokens7 = lexer.tokenize("loop y {let mut x = 10; }");
        //var tokens7 = lexer.tokenize("let y : i32 = 10; loop y < {let mut x = 10; }");
        //var tokens7 = lexer.tokenize("let x : i2 = 10;");
        //var tokens7 = lexer.tokenize("loop 1 7 { let mut x = 2;}");
        //parser.parse(tokens7);

        // var tokens1 = lexer.tokenize("let x : i32 = 89 + 0;");
        // parser.parse(tokens1);
        
        // System.out.println("\n");
        // var tokens2 = lexer.tokenize("let mut x : i32 = 0; let y :i8 = 10; for n in 1..100{x=x+y-100*20/20;}");
        // parser.parse(tokens2);

        // System.out.println("\n");
        // var tokens3 = lexer.tokenize("for x in 23..=  4912 { x = 99;}   let y : f32 = (2 + 5) * 7;");
        // parser.parse(tokens3);

        // System.out.println("\n");
        // var tokens4 = lexer.tokenize("let x : i32 = 10; let mut y:i32 = 0; if x > 10 { y = -x; } else { y = x / 0.8; }");
        // parser.parse(tokens4);

        // System.out.println("\n");
        // var tokens5 = lexer.tokenize("let x : i64 = 5; let mut y : f32 = 0; if x < 5 { y = 2; } else if x > 10 { y = 3; } else { y = 4; }");
        // parser.parse(tokens5);

        //System.out.println("\n");
        //var tokens6 = lexer.tokenize("let x : i8 = 1; let mut y : f32 = 0; while x < 10 { y = x * 3 + 2; }");
        //var tokens6 = lexer.tokenize("let mut x : i8 = 2; x = 2 * 3; x = 3;");
        //Queue<atom> atoms = parser.parse(tokens6, 0);

        // Read atoms in from intermediary file and pass to generateCode
        Queue<Atom> atoms = readAtomsFromFile("IOfiles/atoms.txt"); //TODO add correct file name

        // Check for optimization flag
        Queue<Integer> machineCodes;
        if (args.length > 0 && args[0].equals("-o")) {
            System.out.println("Optimized arg");
            machineCodes = codegen.generateCode(atoms, true);
        } else {
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
    