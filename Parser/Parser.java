package Parser;

import Core.Token;
import Core.TokenName;

import java.util.Queue;
import codeGenerator.atomGen;
import Core.Variable;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Parser {
    Queue<Token> tokenQueue;
    Dictionary<String, Variable> lookupTable = new Hashtable<>();

    Object destroyed = null; 
    int destroyedNum = 0; 

    //helper: double can cast itself automatically into an obj (as with any primitive)
    double floatCalculator(Integer left, Integer right){
        //abracadabra
        double answer =  (left) + (right / Math.pow(10, right.toString().length()));
        return answer;
    }

    //helper
    public Queue<Token> getQueue(){
        return this.tokenQueue;
    }

   

    public void parse(Queue<Token> tokenQueue){
        this.tokenQueue = tokenQueue;
        STATEMENTS();
        expect(TokenName.EOI);
    }

    boolean accept(TokenName tokenName){
        if (tokenQueue.peek().getName() == tokenName){
            destroyed = tokenQueue.remove().getValue();

            return true;
        }
        return false;
    }

    void expect(TokenName tokenName){
        if (tokenQueue.peek().getName() != tokenName)
            throw new RuntimeException("Expected: " + tokenName + ", but found: " + tokenQueue.peek().getName());
        //save
        destroyed = tokenQueue.remove().getValue();
    }

    void STATEMENTS(){
        STATEMENT();
        if (tokenQueue.peek().getName() != TokenName.EOI && tokenQueue.peek().getName() != TokenName.CLOSE_BRACKET) {
            STATEMENTS();
        }
    }

    void STATEMENT(){
        if (accept(TokenName.IF_KW))
            IF_EXPR();
        else if (accept(TokenName.WHILE_KW))
            WHILE_EXPR();
        else if (accept(TokenName.FOR_KW))
            FOR_EXPR();
        else if (accept(TokenName.IDENTIFIER))
            ASSIGNMENT_EXPR();
        else {
            expect(TokenName.LET_KW);
            LET_ASSIGN();
        }
    }

    //Can generate: MOV atoms
    void ASSIGNMENT_EXPR(){
        expect(TokenName.ASSIGN_OP);
        ARITHMETIC_EXPR();
        expect(TokenName.SEMICOLON);

        //Generate a new Variable with name, mutable, type - add to lookup table
        //Generate a MOV Atom where we place the result from ARITHMETIC_EXPR into the identifier 
    }

    //Can generate: MOV atoms
    void LET_ASSIGN(){
        if (accept(TokenName.IDENTIFIER)){
            var left = destroyed; 
            TYPE_ASSIGN();
            expect(TokenName.ASSIGN_OP);
            ARITHMETIC_EXPR();
            String right = destroyed; 
            expect(TokenName.SEMICOLON);

            Variable temp;
            temp.setName(left);
            temp.setMutable(false);
            temp.setType();
            
        }
        else {
            expect(TokenName.MUT_KW);
            expect(TokenName.IDENTIFIER);
            var left = destroyed; 
            TYPE_ASSIGN();
            expect(TokenName.ASSIGN_OP);
            ARITHMETIC_EXPR();
            expect(TokenName.SEMICOLON);
        }

    }

    void TYPE_ASSIGN(){
        expect(TokenName.COLON);
        TYPE();
    }

    void TYPE(){
        if (!accept(TokenName.BIT_8_FLOAT_OP) && !accept(TokenName.BIT_8_INT_OP) && !accept(TokenName.BIT_16_FLOAT_OP) && !accept(TokenName.BIT_16_INT_OP) && !accept(TokenName.BIT_32_FLOAT_OP) && !accept(TokenName.BIT_32_INT_OP) && !accept(TokenName.BIT_64_FLOAT_OP) && !accept(TokenName.BIT_64_INT_OP) && !accept(TokenName.BIT_128_FLOAT_OP))
            expect(TokenName.BIT_128_INT_OP);
    }
    
    //Can generate: JMP, LBL, MOV atoms
    void IF_EXPR(){
        COMPARISON_EXPR();
        expect(TokenName.OPEN_BRACKET);
        STATEMENTS();
        //Ensure the identifier doesn’t exist in lookup table OR that is it mutable
        expect(TokenName.CLOSE_BRACKET);
        ELSE_CLAUSE();
        //Identify comparison operator and then use the complement
    }

    void ELSE_CLAUSE(){
        if (accept(TokenName.ELSE_KW))
            ELSE_NESTED();
    }

    void ELSE_NESTED(){
        if (accept(TokenName.IF_KW))
            IF_EXPR();
        else {
            expect(TokenName.OPEN_BRACKET);
            STATEMENTS();
            expect(TokenName.CLOSE_BRACKET);
        }
    }

    //Can generate: JMP, LBL, MOV atoms
    void WHILE_EXPR(){
        COMPARISON_EXPR();
        expect(TokenName.OPEN_BRACKET);
        STATEMENTS();
        expect(TokenName.CLOSE_BRACKET);
    }

    //Can generate: JMP, LBL, MOV atoms
    void FOR_EXPR(){
        expect(TokenName.IDENTIFIER);
        expect(TokenName.IN_KW);
        ARITHMETIC_EXPR();
        //Ensure Identifier being used is not already in Lookup table; if so, raise exception
        //Generate new Variable with name, yes mutable, type - add to lookup table
        //Generate MOV atom placing initial value of first ARITHMETIC_EXPR into identifier
        //Generate LBL atom with String label1 = labelVar + labelNum++;
        //Make temp variable with String labelAfterName = labelVar + labelNum++;
        RANGE();
        //Generate TST atom using cmp of 5 for “..” and cmp of 3 for “..=” which jumps to labelAfterName
        ARITHMETIC_EXPR();
        expect(TokenName.OPEN_BRACKET);
        //Go into STATEMENTS() - which generates those
        STATEMENTS();
        //Generate ADD atom which adds 1 to identifier and stores result in identifier
        //Generate JMP atom which goes to label1
        //Generate LBL atom with labelAfterName
        expect(TokenName.CLOSE_BRACKET);

        //At end of function, remove Variable from lookup table

    }

    void RANGE(){
        if (!accept(TokenName.RANGE_OP))
            expect(TokenName.INCLUSIVERANGE_OP);
    }

    void ARITHMETIC_EXPR(){
            TERM();

            ARITH_LIST();
    }

    //Can generate: ADD, SUB atoms
    void ARITH_LIST(){
        if (accept(TokenName.ADD_OP)){
            ARITHMETIC_EXPR();
        }
        else if (accept(TokenName.SUB_OP)){
            ARITHMETIC_EXPR();
        }
    }

    //no
    void TERM(){
        VALUE();

        TERM_LIST();  
    }

    //Can generate: MUL, DIV atoms
    void TERM_LIST(){
        if (accept(TokenName.MULT_OP)){
            TERM();
        }
        else if (accept(TokenName.DIV_OP)){
            TERM();
        }
    }

    //in progress 
    //Can generate: NEG atoms
    Object VALUE(){

        //NOT worked in 
        if (accept(TokenName.OPEN_PAREN)){
            ARITHMETIC_EXPR();
            expect(TokenName.CLOSE_PAREN);
        }

        //worked on
        else if (accept(TokenName.NUMERIC)){
            Integer num1 = (Integer) destroyed; 
            Integer floatResult = FLOAT();

            //return witchcraft
            return floatResult == null ? num1 : floatCalculator(num1, floatResult);
            
            //math magic to calculate out decimal to return up the tree
            //return (Integer)(num1) + (floatResult / Math.pow(10, floatResult.toString().length()));//whatever the result of float is ; 
            //return added 
        }

        //NOT worked on
        else if(accept(TokenName.SUB_OP)){
            NEGATED_VALUE();
            
        }

        //worked on 
        else {
            //terminal 
            expect(TokenName.IDENTIFIER);
            //return up the tree 
            return destroyed; 
        }
        //placeholder 
        return ""; 
    }
    
    //worked on, probably done 
    Integer FLOAT(){
        if(accept(TokenName.DECIMAL)){
            expect(TokenName.NUMERIC);
            //grab
            return (Integer) destroyed;  
        }
        return null; 
    }

    
    //in progress 
    Integer NEGATED_VALUE(){
        //in progress
        if(accept(TokenName.NUMERIC)){
            return FLOAT(); 
        }
        //not touched... yet
        else{
            expect(TokenName.IDENTIFIER);
        }
        //placeholder
        return null; 
    }

    //this can return to while
    //Can generate: TST atoms
    void COMPARISON_EXPR(){
        //what can this be? can o worms
        ARITHMETIC_EXPR();
        
        Integer comparison = COMPARISON();
        
        //same issue, take care of atoms at lower level for arth expr 
        ARITHMETIC_EXPR();
    }

    //Returns int representing complement operation of comparison token
    //where this returns to: comparison_expression 
    // //Can generate: NEG atoms
    Integer COMPARISON(){
        if (accept(TokenName.EQ_OP))
            return 6;
        else if (accept(TokenName.GREATER_EQ_OP))
            return 2;
        else if (accept(TokenName.LESS_EQ_OP))
            return 3;
        else if (accept(TokenName.GREATER_OP))
            return 4;
        else if (accept(TokenName.LESS_OP))
            return 5;
        else{
            expect(TokenName.UNEQUAL_OP);
            return 1;
        }
    }
}
