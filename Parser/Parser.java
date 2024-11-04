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
    Dictionary<String, Variable> lookupTable = new Hashtable<>();

    Object destroyed = null;
    int destroyedNum = 0;

    atomGen atomList = new atomGen();

    String destString;

    int lblCounter = 0;

    // starting on null

    // this will have to be tied into the logic of the new tReg stuff, method in
    // atomGen
    String result;

    /**
     * @param left
     * @param right
     * @return double
     */
    // helper: double can cast itself automatically into an obj (as with any
    // primitive)
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
        STATEMENTS();
        expect(TokenName.EOI);
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

    // Can generate: MOV atoms
    void ASSIGNMENT_EXPR() {
        // Ensure variable exists in lookup table AND is mutable
        accept(TokenName.IDENTIFIER);
        String left = String.valueOf(destroyed);
        if ((lookupTable.get(left) != null) && (lookupTable.get(left).isMutable())) {
            expect(TokenName.ASSIGN_OP);
            ARITHMETIC_EXPR();
            expect(TokenName.SEMICOLON);
        } else if (lookupTable.get(left) == null) {
            // variable has not been declared, throw error
        } else {
            // variable does exist but isn't mutable, throw error
        }

        // Generate a MOV Atom where we place the result from ARITHMETIC_EXPR into the
        // identifier
    }

    // Can generate: MOV atoms
    void LET_ASSIGN() {
        if (accept(TokenName.IDENTIFIER)) {
            String left = String.valueOf(destroyed);
            TYPE_ASSIGN();
            // Generate a new Variable with name, mutable false, type - add to lookup table
            expect(TokenName.ASSIGN_OP);
            ARITHMETIC_EXPR();
            String right = String.valueOf(destroyed);
            expect(TokenName.SEMICOLON);
            // Generate a MOV Atom where we place the result from ARITHMETIC_EXPR into the
            // identifier

            // should have everything in...

            // Populating Variable for lookup table
            Variable temp_var = new Variable(null, false, null);
            temp_var.setName(left);
            temp_var.setMutable(false);

            if (right == "INT") {
                temp_var.setType(Type.INT);
            } else if (right == "FLOAT") {
                temp_var.setType(Type.FLOAT);
            }

            lookupTable.put(temp_var.getName(), temp_var);

        } else {
            expect(TokenName.MUT_KW);
            expect(TokenName.IDENTIFIER);
            String left = String.valueOf(destroyed);
            TYPE_ASSIGN();
            // Generate a new Variable with name, mutable true, type - add to lookup table
            expect(TokenName.ASSIGN_OP);
            ARITHMETIC_EXPR();
            String right = String.valueOf(destroyed);
            expect(TokenName.SEMICOLON);
            // Generate a MOV Atom where we place the result from ARITHMETIC_EXPR into the
            // identifier

            // Populating Variable for lookup table
            Variable temp_var = new Variable(null, false, null);
            temp_var.setName(left);
            temp_var.setMutable(true);

            if (right == "INT") {
                temp_var.setType(Type.INT);
            } else if (right == "FLOAT") {
                temp_var.setType(Type.FLOAT);
            }

            lookupTable.put(temp_var.getName(), temp_var);
        }

    }

    void TYPE_ASSIGN() {
        expect(TokenName.COLON);
        TYPE();
    }

    void TYPE() {
        if (!accept(TokenName.BIT_8_FLOAT_OP) && !accept(TokenName.BIT_8_INT_OP) && !accept(TokenName.BIT_16_FLOAT_OP)
                && !accept(TokenName.BIT_16_INT_OP) && !accept(TokenName.BIT_32_FLOAT_OP)
                && !accept(TokenName.BIT_32_INT_OP) && !accept(TokenName.BIT_64_FLOAT_OP)
                && !accept(TokenName.BIT_64_INT_OP) && !accept(TokenName.BIT_128_FLOAT_OP))
            expect(TokenName.BIT_128_INT_OP);
    }

    /**
     * logic may be wrong 
     * 
     * Can generate: JMP, LBL, MOV atoms
     */
    void IF_EXPR() {
        // atom already added.
        String label = COMPARISON_EXPR();
        //add label, runs if true. jmps to end  
        atomList.lblAtom(label); 
        expect(TokenName.OPEN_BRACKET);
        // added? should've been if everything else has been added
        STATEMENTS();

        //prematurely add jmp to end label, pull back 
        label = generateLabel();
        atomList.jmpAtom(label);
        lblCounter--; 

        expect(TokenName.CLOSE_BRACKET);
        //point of processing: if num > 5 { x = y;} (we are here)

        // Generate LBL atom with value from COMPARISON_EXPR. will increment + 1
        ELSE_CLAUSE();

        //end label, has to jump to this
        atomList.lblAtom(generateLabel());
    }

    /**
     * process
     */
    void ELSE_CLAUSE() {
        if (accept(TokenName.ELSE_KW)){
            // Generate a JMP atom to a temporary label, pass this label name to nested
            String label = generateLabel(); 
            ELSE_NESTED(label);
        }
            
    }

    
    /** 
     * @param label
     * 
     * process else stuff
     */
    void ELSE_NESTED(String label) {
        //nested
        if (accept(TokenName.IF_KW)){
            IF_EXPR();
            /* // Generate LBL atom with passed in parameter
            atomList.lblAtom(label); */
        }
        
        //1 level 
        else {
            expect(TokenName.OPEN_BRACKET);
            atomList.lblAtom(label);
            STATEMENTS();
            expect(TokenName.CLOSE_BRACKET);
        }
    }

    // Can generate: JMP, LBL, MOV atoms
    void WHILE_EXPR() {
        COMPARISON_EXPR();
        expect(TokenName.OPEN_BRACKET);
        STATEMENTS();
        expect(TokenName.CLOSE_BRACKET);
    }

    // Can generate: JMP, LBL, MOV atoms
    void FOR_EXPR() {
        expect(TokenName.IDENTIFIER);
        // Ensure Identifier being used is not already in Lookup table; if so, raise
        // exception
        // Generate new Variable with name, yes mutable, type - add to lookup table
        expect(TokenName.IN_KW);
        ARITHMETIC_EXPR();
        // Generate MOV atom placing initial value of first ARITHMETIC_EXPR into
        // identifier
        // Generate LBL atom with String label1 = labelVar + labelNum++;
        // Make temp variable with String labelAfterName = labelVar + labelNum++;
        RANGE();
        ARITHMETIC_EXPR();
        // Generate TST atom using cmp of 5 for “..” and cmp of 3 for “..=” which jumps
        // to labelAfterName (left value is identifier, right is second ARITHMETIC_EXPR
        // return)
        expect(TokenName.OPEN_BRACKET);
        // Go into STATEMENTS() - which generates those associated atoms
        STATEMENTS();
        // Generate ADD atom which adds 1 to identifier and stores result in identifier
        // Generate JMP atom which goes to label1
        expect(TokenName.CLOSE_BRACKET);
        // Generate LBL atom with labelAfterName
        // At end of function, remove Variable from lookup table

    }

    void RANGE() {
        if (!accept(TokenName.RANGE_OP))
            expect(TokenName.INCLUSIVERANGE_OP);
    }

    /**
     * @return Object
     * 
     *         lower levels in progress, there may be something wrong with the line
     *         ordering in here
     */

    Object ARITHMETIC_EXPR() {
        // is this left or right? since it gets processed twice, it should be able to be
        // both
        Object left = TERM();

        // operator and operator in progress
        ARITH_LIST(left);

        if (left != null) {
            return left;
        }

        return null;

    }

    /**
     * @param left
     * 
     *             add/sub atoms
     *             worked on, done?
     */

    void ARITH_LIST(Object left) {
        // buck stops here
        String leftSide = String.valueOf(left);
        if (accept(TokenName.ADD_OP)) {
            // this is the right term
            String right = String.valueOf(ARITHMETIC_EXPR());
            atomList.addAtom(leftSide, right, result);
        } else if (accept(TokenName.SUB_OP)) {
            String right = String.valueOf(ARITHMETIC_EXPR());
            atomList.subAtom(leftSide, right, result);
        }
    }

    /**
     * @return Object
     * 
     *         worked on, done?
     */
    Object TERM() {
        // left HAS to come out of here, for mul operator. can be x + y, or 1 + y
        Object left = VALUE();

        // feed left into here, if not null
        if (left != null) {
            TERM_LIST(left);
        }

        // this returns the right one
        return left;
    }

    // worked on, done?
    /**
     * @param left item
     * @return
     * 
     *         Adds mul atom or div atom depending on operator.
     *         return result back to term()?
     * 
     *         worked on, done?
     */
    String TERM_LIST(Object left) {
        var leftSide = String.valueOf(left);
        if (accept(TokenName.MULT_OP)) {
            String right = String.valueOf(TERM());
            atomList.mulAtom(leftSide, right, result);
            return result;
        } else if (accept(TokenName.DIV_OP)) {
            String right = String.valueOf(TERM());
            atomList.divAtom(leftSide, right, result);
            return result;
        }
        return "";
    }

    /**
     * @return Object
     * 
     *         worked on, done?
     *         can generate: neg atoms
     *         must return numeric or char value to punt up tree
     */

    Object VALUE() {

        // worked on, no return?
        if (accept(TokenName.OPEN_PAREN)) {
            Object result = ARITHMETIC_EXPR();
            expect(TokenName.CLOSE_PAREN);
            return result;
        }

        // worked on
        else if (accept(TokenName.NUMERIC)) {
            Integer num1 = (Integer) destroyed;
            Integer floatResult = FLOAT();

            // witchcraft
            return floatResult == null ? num1 : floatCalculator(num1, floatResult);
        }

        // in progress, returns data type of obj to cast to Integer (?)
        else if (accept(TokenName.SUB_OP)) {
            return NEGATED_VALUE();
        }

        // worked on
        else {
            // terminal
            expect(TokenName.IDENTIFIER);
            // return up the tree
            return destroyed;
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

    String COMPARISON_EXPR() {
        // what can this be? can o' worms
        Object left = ARITHMETIC_EXPR();

        Integer comparison = COMPARISON();
        // same issue, take care of atoms at lower level for arth expr
        Object right = ARITHMETIC_EXPR();

        String label = generateLabel();

        // Generate TST atom using comparison variable value which jumps to a label
        atomList.tstAtom((String) left, (String) right, comparison, label);

        return label;
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
