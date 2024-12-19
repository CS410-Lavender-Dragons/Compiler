package Compiler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Queue;

import Lexer.Lexer;
import Parser.Parser;
import codeGenerator.atom;

public class Frontend {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();

        // Parse arguments
        String inputFile = args[0];
        String outputFile = args[1];
        int optimizedFlag = 0;

        try {
            if(args[2] == "-o"){
                optimizedFlag = 1;
            }
        }
        catch (IndexOutOfBoundsException e){
            optimizedFlag = 0;
        }

        Path file = Path.of(inputFile);
        String inputCode = Files.readString(file);
        var tokenizedCode = lexer.tokenize(inputCode);

        Queue<atom> atomList = parser.parse(tokenizedCode, optimizedFlag);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))){
            for(int i = 0; i < atomList.size(); i++){
                String atom = atomList.poll().toString();
                writer.write(atom);
                writer.newLine();
            }
        }
    }
}
