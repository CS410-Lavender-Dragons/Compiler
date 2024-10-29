package Compiler;

import Lexer.Lexer;
import Parser.Parser;

public class Compiler {
    public static void main(String[] args){
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        var tokens1 = lexer.tokenize("x = 89 + a;");
        parser.parse(tokens1);
        var tokens2 = lexer.tokenize("for n in 1..100{x=x+y-100*20/20;}");
        parser.parse(tokens2);
        var tokens3 = lexer.tokenize("for x in 23..=  4912 { x = 99;}   y = (2 + 5) * 7;");
        parser.parse(tokens3);
        var tokens4 = lexer.tokenize("let x = 10; let y =  x;");
        parser.parse(tokens4);
    }
}
