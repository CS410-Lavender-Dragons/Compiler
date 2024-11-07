package Parser;

import Core.Token;
import Core.TokenName;

import java.util.Queue;
import codeGenerator.atomGen;
import Core.Variable;
import Core.Variable.Type;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

//casting needs to be done right before use to avoid data types issues 

public class Parser {
    Queue<Token> tokenQueue;
    Dictionary<String, Variable> lookupTable;
    Object destroyed = null;
    atomGen atomList;
    int lblCounter = 0;
    int tRegCount = 0;
    // atomGen
    String result;

    // helper
    public atomGen getAtoms() {
        System.out.println("Here");
        return this.atomList;
    }

    // helper, post-increment
    public String newTReg() {
        return "T" + tRegCount++;
    }

    /**
     * @param left
     * @param right
     * @return double
     *         helper: double can cast itself automatically into an obj (as with any
     *         primitive)
     */
    double floatCalculator(Integer left, Integer right) {
        // abracadabra
        double answer = (left) + (right / Math.pow(10, right.toString().length()));
        return answer;
    }

    /**
     * @param token
     * @return Variable
     * 
     *         Helper: Take instance of token and populate variable
     */
    public Variable TokVarConversion(Token token) {

        return null;
    }

    /**
     * @return String
     * 
     *         helper for generate labels
     */
    public String generateLabel() {
        return "LBL " + lblCounter++;
    }

    /**
     * @return Queue<Token>
     * 
     *         helper
     */
    public Queue<Token> getQueue() {
        return this.tokenQueue;
    }

    /**
     * @param tokenQueue
     */
    public void parse(Queue<Token> tokenQueue) {
        this.tokenQueue = tokenQueue;
        this.lookupTable = new Hashtable<>();
        this.atomList = new atomGen();
        STATEMENTS();
        expect(TokenName.EOI);
        atomList.end();
    }

    /**
     * @param tokenName
     * @return boolean
     */
    boolean accept(TokenName tokenName) {
        if (tokenQueue.peek().getName() == tokenName) {
            destroyed = tokenQueue.remove().getValue();

            return true;
        }
        return false;
    }
    
    /**
     * @param tokenName
     */
    void expect(TokenName tokenName) {
        if (tokenQueue.peek().getName() != tokenName)
            throw new RuntimeException("Expected: " + tokenName + ", but found: " + tokenQueue.peek().getName());
        // save
        destroyed = tokenQueue.remove().getValue();
    }

    void STATEMENTS() {
        STATEMENT();
        if (tokenQueue.peek().getName() != TokenName.EOI && tokenQueue.peek().getName() != TokenName.CLOSE_BRACKET) {
            STATEMENTS();
        }
    }

    void STATEMENT() {
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

    /**
     * @throws Exception
     */
    // Can generate: MOV atoms
    void ASSIGNMENT_EXPR() {
        //destination identifier
        String identifier = String.valueOf(destroyed);
        // Ensure variable exists in lookup table AND is mutable
        if (lookupTable.get(identifier) == null)
            throw new RuntimeException(identifier + " identifier has not been initialized!");
        if (!lookupTable.get(identifier).isMutable())
            throw new RuntimeException(identifier + " is not mutable!");

        expect(TokenName.ASSIGN_OP);
        String result = ARITHMETIC_EXPR();
        //MOV result into identifier
        atomList.movAtom(result, identifier);

        expect(TokenName.SEMICOLON);
    }

    // Can generate: MOV atoms
    void LET_ASSIGN() {
        if (accept(TokenName.IDENTIFIER)) {
            String identifier = String.valueOf(destroyed);
            if (lookupTable.get(identifier) != null)
                throw new RuntimeException(identifier + " already exists!");
            Type varType = TYPE_ASSIGN();
            expect(TokenName.ASSIGN_OP);
            String identValue = ARITHMETIC_EXPR();
            lookupTable.put(identifier, new Variable(identifier, false, varType));
            atomList.movAtom(identValue, identifier);
            expect(TokenName.SEMICOLON);
        } else {
            expect(TokenName.MUT_KW);
            expect(TokenName.IDENTIFIER);
            String identifier = String.valueOf(destroyed);
            if (lookupTable.get(identifier) != null)
                throw new RuntimeException(identifier + " already exists!");
            Type varType = TYPE_ASSIGN();
            expect(TokenName.ASSIGN_OP);
            String identValue = ARITHMETIC_EXPR();
            lookupTable.put(identifier, new Variable(identifier, true, varType));
            atomList.movAtom(identValue, identifier);
            expect(TokenName.SEMICOLON);
        }
    }

    Type TYPE_ASSIGN() {
        expect(TokenName.COLON);
        return TYPE();
    }

    Type TYPE() {
        if (accept(TokenName.BIT_8_FLOAT_OP) || accept(TokenName.BIT_16_FLOAT_OP) || accept(TokenName.BIT_32_FLOAT_OP) || accept(TokenName.BIT_64_FLOAT_OP) || accept(TokenName.BIT_128_FLOAT_OP))
            return Type.FLOAT;
        else if (accept(TokenName.BIT_8_INT_OP) || accept(TokenName.BIT_16_INT_OP) || accept(TokenName.BIT_32_INT_OP) || accept(TokenName.BIT_64_INT_OP))
            return Type.INT;
        else
            expect(TokenName.BIT_128_INT_OP);
        return Type.INT;
    }
    /**
     * logic may be wrong
     * 
     * Can generate: JMP, LBL, MOV atoms
     */
    void IF_EXPR() {
        //Label to jump to if condition not true
        String afterIfLabel = generateLabel();
        COMPARISON_EXPR(afterIfLabel);

        expect(TokenName.OPEN_BRACKET);
        STATEMENTS();
        expect(TokenName.CLOSE_BRACKET);

        ELSE_CLAUSE(afterIfLabel);
    }

    void ELSE_CLAUSE(String afterIfLabel) {
        if (accept(TokenName.ELSE_KW))
            ELSE_NESTED(afterIfLabel);
    }

    /**
     * @param label
     * 
     *              process else stuff
     */
    void ELSE_NESTED(String afterIfLabel) {
        if (accept(TokenName.IF_KW)) {
            //LBL atom for afterIf
            atomList.lblAtom(afterIfLabel);
            IF_EXPR();
        }
        else {
            //Label for afterElse
            String afterElseLabel = generateLabel();
            //JMP atom to afterElseLabel
            atomList.jmpAtom(afterElseLabel);
            //LBL atom for else
            atomList.lblAtom(afterIfLabel);
            expect(TokenName.OPEN_BRACKET);
            STATEMENTS();
            expect(TokenName.CLOSE_BRACKET);
            //LBL atom for afterElse
            atomList.lblAtom(afterElseLabel);
        }
    }

    // Can generate: JMP, LBL, MOV atoms
    void WHILE_EXPR() {
        String beforeWhileLabel = generateLabel();
        String afterWhileLabel = generateLabel();
        atomList.lblAtom(beforeWhileLabel);
        COMPARISON_EXPR(afterWhileLabel);
        expect(TokenName.OPEN_BRACKET);
        STATEMENTS();
        expect(TokenName.CLOSE_BRACKET);
        atomList.jmpAtom(beforeWhileLabel);
        atomList.lblAtom(afterWhileLabel);
    }

    // Can generate: JMP, LBL, MOV atoms
    void FOR_EXPR() {
        expect(TokenName.IDENTIFIER);
        // right should be the identifier name, stow away for later
        String loopIdent = String.valueOf(destroyed);

        // Ensure Identifier being used is not already in Lookup table; if so, raise exception
        if (lookupTable.get(loopIdent) != null) {
            throw new RuntimeException("For-loop variable already in use!");
        }

        // Populating Variable for lookup table
        Variable loopVar = new Variable(loopIdent, true, Type.INT);
        lookupTable.put(loopIdent, loopVar);

        expect(TokenName.IN_KW);

        //Temp register storing left value
        String leftArithResult = ARITHMETIC_EXPR();

        // Generate MOV atom placing initial value of first ARITHMETIC_EXPR into identifier
        atomList.movAtom(leftArithResult, loopIdent);

        // Generate LBL atom and store value
        var label1 = generateLabel();
        atomList.lblAtom(label1);

        // Make temp variable with String labelAfterName = labelVar + labelNum++;
        var labelAfterName = generateLabel();

        int cmp = RANGE();
        
        String upperLoopVal = ARITHMETIC_EXPR();

        // Generate TST atom using cmp of 5 for “..” and cmp of 3 for “..=” which jumps
        // to labelAfterName (left value is identifier, right is second ARITHMETIC_EXPR
        // return)
        atomList.tstAtom(loopIdent, upperLoopVal, cmp, labelAfterName);

        expect(TokenName.OPEN_BRACKET);
        // Go into STATEMENTS() - which generates those associated atoms
        STATEMENTS();
        
        // Generate ADD atom which adds 1 to identifier and stores result in identifier
        atomList.addAtom(loopIdent, "1", loopIdent);

        // Generate JMP atom which goes to label1
        atomList.jmpAtom(label1);

        expect(TokenName.CLOSE_BRACKET);

        // Generate LBL atom with labelAfterName
        atomList.lblAtom(labelAfterName);

        // At end of function, remove Variable from lookup table
        lookupTable.remove(loopIdent);
    }

    int RANGE() {
        if (accept(TokenName.RANGE_OP))
            return 5;
        else
            expect(TokenName.INCLUSIVERANGE_OP);
        return 3;
    }

    /**
     * @return Object
     * 
     *         lower levels in progress, there may be something wrong with the line
     *         ordering in here
     */
    String ARITHMETIC_EXPR() {
        String resultReg = TERM();
        ARITH_LIST(resultReg);
        return resultReg;
    }

    /**
     * @param left
     * 
     *             add/sub atoms
     *             worked on, done?
     * 
     * 
     */
    void ARITH_LIST(String tReg) {
        if (accept(TokenName.ADD_OP)) {
            String right = ARITHMETIC_EXPR();
            atomList.addAtom(tReg, right, tReg);
        } else if (accept(TokenName.SUB_OP)) {
            String right = ARITHMETIC_EXPR();
            atomList.subAtom(tReg, right, tReg);
        }
    }

    /**
     * @return String, tReg
     * 
     *         worked on, done? this is highly questionable ----
     */
    String TERM() {
        String resultReg = VALUE();
        TERM_LIST(resultReg);
        return resultReg;
    }

    /**
     * @param left item
     * @return
     * 
     *         Adds mul atom or div atom depending on operator.
     *         return result back to term()?
     * 
     *         worked on, done?
     */
    void TERM_LIST(String tReg) {
        if (accept(TokenName.MULT_OP)) {
            String right = TERM();
            atomList.mulAtom(tReg, right, tReg);
        } else if (accept(TokenName.DIV_OP)) {
            String right = TERM();
            atomList.divAtom(tReg, right, tReg);
        }
    }

    /**
     * @return Object
     * 
     *         worked on, done?
     *         can generate: neg atoms
     *         must return numeric or char value to punt up tree
     */

    String VALUE() {

        // worked on, no return?
        if (accept(TokenName.OPEN_PAREN)) {
            String tReg = ARITHMETIC_EXPR();
            expect(TokenName.CLOSE_PAREN);
            // return where the value is stored
            return tReg;
        }

        // worked on
        else if (accept(TokenName.NUMERIC)) {
            Integer num1 = (Integer) destroyed;
            Integer floatResult = FLOAT();

            // witchcraft
            return String.valueOf(floatResult == null ? num1 : floatCalculator(num1, floatResult));
        }

        // in progress, returns data type of obj to cast to Integer (?)
        else if (accept(TokenName.SUB_OP)) {
            return String.valueOf(NEGATED_VALUE());
        }

        // TODO worked on, check
        else {
            // terminal
            expect(TokenName.IDENTIFIER);
            // return up the tree
            return destroyed.toString();
        }
    }

    /**
     * @return Integer
     * 
     * @return extacted value
     * 
     *         worked on, probably done
     */
    Integer FLOAT() {
        if (accept(TokenName.DECIMAL)) {
            expect(TokenName.NUMERIC);
            // grab
            return (Integer) destroyed;
        }
        return null;
    }

    /**
     * @return Integer
     * 
     *         returns negated value
     */
    Integer NEGATED_VALUE() {
        // in progress
        if (accept(TokenName.NUMERIC)) {
            return FLOAT();
        }
        // not touched... yet
        else {
            expect(TokenName.IDENTIFIER);
        }
        // placeholder
        return null;
    }

    /**
     * 
     * @param
     * @return target label
     *         Not done. Needs lookup table logic built in
     *         Can genrate TST atoms.
     *         Can return to while (anything else?).
     *         Pulls from arithmetic_expr and comparison (debugging)
     */

    void COMPARISON_EXPR(String label) {
        String left = ARITHMETIC_EXPR();
        Integer comparison = COMPARISON();
        String right = ARITHMETIC_EXPR();

        // Generate TST atom using comparison variable value which jumps to a label
        atomList.tstAtom(left, right, comparison.intValue(), label);

    }

    /**
     * @return a number
     *         99% sure done; where this returns to: comparison_expression
     *         Returns int representing complement operation of comparison token
     *         where this returns to: comparison_expression
     *         Can generate: NEG atoms (?)
     */
    Integer COMPARISON() {
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
        else {
            expect(TokenName.UNEQUAL_OP);
            return 1;
        }
    }
}
