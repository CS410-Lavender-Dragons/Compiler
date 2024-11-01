package Parser;

import Core.Token;
import Core.TokenName;

import java.util.Queue;
import codeGenerator.atomGen;
import Core.Variable;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

//casting needs to be done right before use to avoid data types issues 

public class Parser {
    Queue<Token> tokenQueue;
    Dictionary<String, Variable> lookupTable = new Hashtable<>();

    Object destroyed = null; 
    int destroyedNum = 0; 

    atomGen atomList = new atomGen();


    //starting on null

    //this will have to be tied into the logic of the new tReg stuff, method in atomGen
    String result; 

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
        //Ensure variable exists in lookup table AND is mutable
        expect(TokenName.ASSIGN_OP);
        ARITHMETIC_EXPR();
        expect(TokenName.SEMICOLON);
        //Generate a MOV Atom where we place the result from ARITHMETIC_EXPR into the identifier 
    }

    //Can generate: MOV atoms
    void LET_ASSIGN(){
        if (accept(TokenName.IDENTIFIER)){
            String left = (String)destroyed; 
            TYPE_ASSIGN();
            //Generate a new Variable with name, mutable false, type - add to lookup table
            expect(TokenName.ASSIGN_OP);
            ARITHMETIC_EXPR();
            String right = (String)destroyed;
            expect(TokenName.SEMICOLON);
            //Generate a MOV Atom where we place the result from ARITHMETIC_EXPR into the identifier

            //should have everything in...

            //what is this 
            /* Variable temp;
            temp.setName(left);
            temp.setMutable(false);
            temp.setType(); */
            
        }
        else {
            expect(TokenName.MUT_KW);
            expect(TokenName.IDENTIFIER);
            var left = destroyed; 
            TYPE_ASSIGN();
            //Generate a new Variable with name, mutable true, type - add to lookup table
            expect(TokenName.ASSIGN_OP);
            ARITHMETIC_EXPR();
            expect(TokenName.SEMICOLON);
            //Generate a MOV Atom where we place the result from ARITHMETIC_EXPR into the identifier
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
        expect(TokenName.CLOSE_BRACKET);
        //Generate LBL atom with label value from COMPARISON_EXPR
        ELSE_CLAUSE();
    }

    void ELSE_CLAUSE(){
        if (accept(TokenName.ELSE_KW))
            //Generate a JMP atom to a temporary label, pass this label name to nested
            ELSE_NESTED();
    }

    void ELSE_NESTED(){
        if (accept(TokenName.IF_KW))
            IF_EXPR();
            //Generate LBL atom with passed in parameter
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
        //Ensure Identifier being used is not already in Lookup table; if so, raise exception
        //Generate new Variable with name, yes mutable, type - add to lookup table
        expect(TokenName.IN_KW);
        ARITHMETIC_EXPR();
        //Generate MOV atom placing initial value of first ARITHMETIC_EXPR into identifier
        //Generate LBL atom with String label1 = labelVar + labelNum++;
        //Make temp variable with String labelAfterName = labelVar + labelNum++;
        RANGE();
        ARITHMETIC_EXPR();
        //Generate TST atom using cmp of 5 for “..” and cmp of 3 for “..=” which jumps to labelAfterName (left value is identifier, right is second ARITHMETIC_EXPR return)
        expect(TokenName.OPEN_BRACKET);
        //Go into STATEMENTS() - which generates those associated atoms
        STATEMENTS();
        //Generate ADD atom which adds 1 to identifier and stores result in identifier
        //Generate JMP atom which goes to label1
        expect(TokenName.CLOSE_BRACKET);
        //Generate LBL atom with labelAfterName
        //At end of function, remove Variable from lookup table

    }

    void RANGE(){
        if (!accept(TokenName.RANGE_OP))
            expect(TokenName.INCLUSIVERANGE_OP);
    }

    //lower levels in progress, there may be something wrong with the line ordering in here 
    Object ARITHMETIC_EXPR(){
            //is this left or right? since it gets processed twice, it should be able to be both 
            Object left = TERM();
            
            //

            //operator and operator in progress 
            ARITH_LIST(left);

    }

    //worked on, done? 
    //Can generate: ADD, SUB atoms
    void ARITH_LIST(Object left){
        //buck stops here
        if (accept(TokenName.ADD_OP)){
            //this is the right term
            String right = (String)ARITHMETIC_EXPR();
            atomList.addAtom((String)left, right, result);
        }
        else if (accept(TokenName.SUB_OP)){
            String right = (String)ARITHMETIC_EXPR();
            atomList.subAtom((String)left, right, result); 
        }
    }

    //worked on, done?, no bueno
    Object TERM(){
        //left HAS to come out of here, for mul operator. can be x + y, or 1 + y
        Object left = VALUE();

        //feed left into here, if not null 
        if(left != null){
            TERM_LIST(left); 
        }  
        //this returns the right one 
        return left;
    }

    //done? Can generate: MUL, DIV atoms
    void TERM_LIST(Object left){
        //left term is input

        if (accept(TokenName.MULT_OP)){
            Object right = TERM();
            //create mult atom, logic will handle dest logic 
            atomList.mulAtom((String)left, (String)right, (String)result);
        }
        else if (accept(TokenName.DIV_OP)){
            Object right = TERM();
            //create div atom 
            atomList.mulAtom((String)left, (String)right, (String)result);
        }
    }

    //in progress 
    //Can generate: NEG atoms
    //MUST return a numeric or char value to punt up tree 
    Object VALUE(){

        //NOT worked on, no return? 
        if (accept(TokenName.OPEN_PAREN)){
            Object result = ARITHMETIC_EXPR();
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
    //this can return to while
    //Can generate: TST atoms
    void COMPARISON_EXPR(){
        //what can this be? can o' worms
        ARITHMETIC_EXPR();
        
        Integer comparison = COMPARISON();
        //same issue, take care of atoms at lower level for arth expr 
        ARITHMETIC_EXPR();

        //Generate TST atom using comparison variable value which jumps to a label
    }

    //99% sure done; where this returns to: comparison_expression 
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
