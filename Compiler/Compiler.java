package Compiler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import Lexer.Lexer;
import Parser.Parser;
import Core.Atom;
import CodeGenerator.*;


public class Compiler {
    public static void main(String[] args) throws IOException{
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        CodeGenerator codegen = new CodeGenerator();

        if (args.length < 2)
            throw new RuntimeException("Please enter the input file name, output file name, and additionally an optimization flag -o");
        String inputFile = args[0];
        String outputFile = args[1];
        boolean optimizedFlag = false;

        try {
            if(args[2].toLowerCase().trim().equals("-o")){
                optimizedFlag = true;
            }
        }
        catch (IndexOutOfBoundsException e){
        }

        File input = new File(args[0]);
        if(!input.exists()){
            throw new RuntimeException("Error: input file " + args[0] + " does not exist");
        }

        Path file = Path.of(inputFile);
        String sampleCode = Files.readString(file);

        // Lex source code file
        var pipelineToken = lexer.tokenize(sampleCode);

        // Parse tokens
        Queue<Atom> atomList = parser.parse(pipelineToken, optimizedFlag);

        // Generate machine code
        Queue<Integer> machineCodes = codegen.generateCode(atomList, optimizedFlag);

        try {
            File binF = new File(outputFile);

            if (!binF.createNewFile()) {
                System.out.println("Output file already exists. Proceed? (Y/N)");
                String response = (new Scanner(System.in)).nextLine().toLowerCase();
                if (!response.equals("y")) {
                    System.out.println("Compilation terminated.");
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile));
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
}
    