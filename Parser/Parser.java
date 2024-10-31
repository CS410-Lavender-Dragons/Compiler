package Parser;

import Core.Token;
import Core.TokenName;

import java.util.Queue;
import codeGenerator.atomGen;

public class Parser {
    Queue<Token> tokenQueue;

    Object destroyed = null; 
    int destroyedNum = 0; 

    String left; 
    String right; 

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

    void ASSIGNMENT_EXPR(){
        expect(TokenName.ASSIGN_OP);
        ARITHMETIC_EXPR();
        expect(TokenName.SEMICOLON);
    }

    //baking in at this level for now 
    void LET_ASSIGN(){
        if (accept(TokenName.IDENTIFIER)){
            var left = destroyed; 
            TYPE_ASSIGN();
            expect(TokenName.ASSIGN_OP);
            ARITHMETIC_EXPR();
            //String right = destroyed; 
            expect(TokenName.SEMICOLON);
            
        }
        //worked on, day 2 
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

    void IF_EXPR(){
        COMPARISON_EXPR();
        expect(TokenName.OPEN_BRACKET);
        STATEMENTS();
        expect(TokenName.CLOSE_BRACKET);
        ELSE_CLAUSE();
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

    void WHILE_EXPR(){
        COMPARISON_EXPR();
        expect(TokenName.OPEN_BRACKET);
        STATEMENTS();
        expect(TokenName.CLOSE_BRACKET);
    }

    void FOR_EXPR(){
        expect(TokenName.IDENTIFIER);
        expect(TokenName.IN_KW);
        ARITHMETIC_EXPR();
        RANGE();
        ARITHMETIC_EXPR();
        expect(TokenName.OPEN_BRACKET);
        STATEMENTS();
        expect(TokenName.CLOSE_BRACKET);
    }

    void RANGE(){
        if (!accept(TokenName.RANGE_OP))
            expect(TokenName.INCLUSIVERANGE_OP);
    }

    //lower levels in progress
    void ARITHMETIC_EXPR(){
            TERM();

            ARITH_LIST();
    }

    //I apologize if this isn't what we wanted
    void ARITH_LIST(){
        if (accept(TokenName.ADD_OP)){
            ARITHMETIC_EXPR();
        }
        else if (accept(TokenName.SUB_OP)){
            ARITHMETIC_EXPR();
        }
    }

    //no bueno
    void TERM(){
        VALUE();

        TERM_LIST();  
    }

    //Another apology here since this was done similarly to arith_list
    void TERM_LIST(){
        if (accept(TokenName.MULT_OP)){
            TERM();
        }
        else if (accept(TokenName.DIV_OP)){
            TERM();
        }
    }

    //in progress 
    Object VALUE(){

        //NOT worked on, no return? 
        if (accept(TokenName.OPEN_PAREN)){
            //String result = ARITHMETIC_EXPR(); no directly return arith expr
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

        //in progress, returns data type of obj to cast to Integer (?)
        else if(accept(TokenName.SUB_OP)){
            return NEGATED_VALUE();
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

    //not done; this can return to while or a number of other things 
    void COMPARISON_EXPR(){
        //what can this be? can o' worms
        ARITHMETIC_EXPR();
        
        Integer comparison = COMPARISON();
        
        //same issue, take care of atoms at lower level for arth expr 
        ARITHMETIC_EXPR();
    }

    //99% sure done; where this returns to: comparison_expression 
    //Returns int representing complement operation of comparison token
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
