package Compiler;

import Core.Token;
import Lexer.Lexer;
import Parser.Parser;
import codeGenerator.atomGen;

public class Compiler {
    public static void main(String[] args){
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        //atomGen generator = new atomGen();
        var tokens1 = lexer.tokenize("x = 89 + a;");
        parser.parse(tokens1);
        var atoms = parser.getAtoms(); 
        //print
        System.out.println(atoms); 
        atoms.end();
        

        /* var tokens2 = lexer.tokenize("for n in 1..100{x=x+y-100*20/20;}");
        parser.parse(tokens2);
        var tokens3 = lexer.tokenize("for x in 23..=  4912 { x = 99;}   y = (2 + 5) * 7;");
        parser.parse(tokens3);
        var tokens4 = lexer.tokenize("let x : i32 = 10; let y : f32 =  (-x) + (8.32 * 7 - 5) + (z) / x + 0.23;");
        parser.parse(tokens4); */
    }
}
    