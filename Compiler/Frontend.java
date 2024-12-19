package Compiler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Queue;

import Lexer.Lexer;
import Parser.Parser;
import Core.Atom;

public class Frontend {
    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();

        // Parse arguments
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

        Path file = Path.of(inputFile);
        String inputCode = Files.readString(file);
        var tokenizedCode = lexer.tokenize(inputCode);

        Queue<Atom> atomList = parser.parse(tokenizedCode, optimizedFlag);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))){
            int listSize = atomList.size();
            for(int i = 0; i < listSize; i++){
                String atom = atomList.remove().toString();
                writer.write(atom);
                writer.newLine();
            }
        }
    }
}
