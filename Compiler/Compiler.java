package Compiler;

import java.io.*;
import java.util.Queue;

import Lexer.Lexer;
import Parser.Parser;
import codeGenerator.atom;
import phase3.*;


public class Compiler {
    public static void main(String[] args){
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        codeGenerator codegen = new codeGenerator();

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

        System.out.println("\n");
        var tokens6 = lexer.tokenize("let x : i8 = 1; let mut y : f32 = 0; while x < 10 { y = x * 3 + 2; }");
        Queue<atom> atoms = parser.parse(tokens6);
        Queue<Integer> machineCode = codegen.generateCode(atoms);
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
            // Create File writer
            //FileWriter fw = new FileWriter(filename);
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename));
            // Loop through machine code queue and write each instruction to bin file
            while (!machineCode.isEmpty()) {
                int currByte = machineCode.remove();
                dos.writeInt(currByte);
            }
            //fw.close();
            dos.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
    