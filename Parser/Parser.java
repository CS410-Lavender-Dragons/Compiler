package Parser;

import Core.Token;
import Core.TokenName;
import Core.Variable;
import Core.Variable.Type;
import codeGenerator.atom;
import codeGenerator.atomGen;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;


//casting needs to be done right before use to avoid data types issues 

public class Parser {

    Queue<Token> tokenQueue;
    Dictionary<String, Variable> lookupTable;
    Object destroyed = null;
    atomGen atomList;
    int lblCounter;
    int tRegCount;
    private boolean globalFlag, localFlag;

    //for capturing thing 
    public Parser(boolean global, boolean local){
        globalFlag = global;
        localFlag = local; 
    }

    public Queue<atom> parse(Queue<Token> tokenQueue) {
        this.tokenQueue = tokenQueue;
        this.lookupTable = new Hashtable<>();
        this.atomList = new atomGen();
        lblCounter = 0;
        tRegCount = 0;
        STATEMENTS();
        expect(TokenName.EOI);
        atomList.end();
        //constant folding
        if(globalFlag){
            constantFolding(atomList.getAtomList());
        }
        //what
        if(localFlag){
            //loadStoreOptimization(); 
        }

        return atomList.getAtomList();
    }

    boolean accept(TokenName tokenName) {
        if (tokenQueue.peek().getName() == tokenName) {
            destroyed = tokenQueue.remove().getValue();

            return true;
        }
        return false;
    }

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
        else if (accept(TokenName.WHILE_KW))//Temporary until Lexer corrected to lex "while"
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

    void ELSE_NESTED(String afterIfLabel) {
        if (accept(TokenName.IF_KW)) {
            //JMP to label after "else if" if "if" executes
            String afterElseIfLabel = generateLabel();
            atomList.jmpAtom(afterElseIfLabel);
            //LBL atom for afterIf
            atomList.lblAtom(afterIfLabel);
            IF_EXPR();
            atomList.lblAtom(afterElseIfLabel);
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

        //Generate beforeLabel name
        var beforeLabel = generateLabel();

        // Make temp variable with String labelAfterName = labelVar + labelNum++;
        var labelAfterName = generateLabel();

        int cmp = RANGE();
        
        String upperLoopVal = ARITHMETIC_EXPR();

        //Generate LBL for before loop atoms
        atomList.lblAtom(beforeLabel);
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
        atomList.jmpAtom(beforeLabel);

        expect(TokenName.CLOSE_BRACKET);

        // Generate LBL atom with labelAfterName
        atomList.lblAtom(labelAfterName);

        // At end of function, remove Variable from lookup table
        lookupTable.remove(loopIdent);
    }

    //Returns complement of range operator for comparison
    int RANGE() {
        if (accept(TokenName.RANGE_OP))
            return 5;
        else
            expect(TokenName.INCLUSIVERANGE_OP);
        return 3;
    }

    //Returns register holding result of expression
    String ARITHMETIC_EXPR() {
        String left = TERM();
        String resultReg = ARITH_LIST(left);
        return resultReg;
    }

    //Adds add atom or sub atom depending on operator, returns register containing result
    String ARITH_LIST(String tReg) {
        if (accept(TokenName.ADD_OP)) {
            String resultReg = newTReg();
            String right = ARITHMETIC_EXPR();
            atomList.addAtom(tReg, right, resultReg);
            return resultReg;
        } else if (accept(TokenName.SUB_OP)) {
            String resultReg = newTReg();
            String right = ARITHMETIC_EXPR();
            atomList.subAtom(tReg, right, resultReg);
            return resultReg;
        }
        return tReg;
    }

    //Returns register holding result
    String TERM() {
        String left = VALUE();
        String resultReg = TERM_LIST(left);
        return resultReg;
    }

    //Adds mul atom or div atom depending on operator, returns register containing result
    String TERM_LIST(String tReg) {
        if (accept(TokenName.MULT_OP)) {
            String resultReg = newTReg();
            String right = TERM();
            atomList.mulAtom(tReg, right, resultReg);
            return resultReg;
        } else if (accept(TokenName.DIV_OP)) {
            String resultReg = newTReg();
            String right = TERM();
            atomList.divAtom(tReg, right, resultReg);
            return resultReg;
        }
        return tReg;
    }

    //Returns temp register/identifier holding value
    String VALUE() {
        if (accept(TokenName.OPEN_PAREN)) {
            String tReg = ARITHMETIC_EXPR();
            expect(TokenName.CLOSE_PAREN);
            return tReg;
        }
        else if (accept(TokenName.NUMERIC)) {
            Integer num1 = (Integer) destroyed;
            Integer floatResult = FLOAT();
            String value = String.valueOf(floatResult == null ? num1 : floatCalculator(num1, floatResult));
            String result = newTReg();
            atomList.movAtom(value, result);
            return result;
        }
        else if (accept(TokenName.SUB_OP)) {
            return NEGATED_VALUE();
        }
        else {
            expect(TokenName.IDENTIFIER);
            String identifier = String.valueOf(destroyed);
            if (lookupTable.get(identifier) == null)
                throw new RuntimeException(identifier + " used before initialized!");
            return identifier;
        }
    }

    //Returns extracted value after decimal, null if DNE
    Integer FLOAT() {
        if (accept(TokenName.DECIMAL)) {
            expect(TokenName.NUMERIC);
            return (Integer) destroyed;
        }
        return null;
    }

    //Returns Temp register holding negated value
    String NEGATED_VALUE() {
        String result = newTReg();
        String value;
        if (accept(TokenName.NUMERIC)) {
            Integer num1 = (Integer) destroyed;
            Integer floatResult = FLOAT();
            value = String.valueOf(floatResult == null ? num1 : floatCalculator(num1, floatResult));
        }
        else {
            expect(TokenName.IDENTIFIER);
            value = String.valueOf(destroyed);
            if (lookupTable.get(value) == null)
                throw new RuntimeException("Cannot negate " + value + " before initialization!");
        }
        atomList.negAtom(value, result);
        return result;
    }

    //Generates TST atom using value from two arithmetic expressions and comparison (complement) from COMPARISON()
    void COMPARISON_EXPR(String label) {
        String left = ARITHMETIC_EXPR();
        Integer comparison = COMPARISON();
        String right = ARITHMETIC_EXPR();

        // Generate TST atom using comparison variable value which jumps to a label
        atomList.tstAtom(left, right, comparison, label);

    }

     //Returns int representing complement operation of comparison token
    int COMPARISON() {
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

    //get new temp register, increment count
    public String newTReg() {
        return "T" + tRegCount++;
    }

    //returns value of Double
    double floatCalculator(Integer left, Integer right) {
        double answer = (left) + (right / Math.pow(10, right.toString().length()));
        return answer;
    }

   //get new label, increment count
    public String generateLabel() {
        return "L" + lblCounter++;
    }

    //constant folding optimization
    //TODO: Add a flag option. 
    public static Queue<atom> constantFolding(Queue<atom> atoms) {
        Queue<atom> optimizedAtoms = new LinkedList<>();

        boolean firstIter = true;
        int resultValue = 0;
        //atom lastDest = null; 

        while (!atoms.isEmpty()) {
            atom currentAtom = atoms.poll();
            //check for operators
            if (currentAtom.name.equals("ADD") || currentAtom.name.equals("SUB") ||  currentAtom.name.equals("MUL") || currentAtom.name.equals("DIV")) {

                //check if both left and right operands are constants, or if one of them is a temp reg 
                if ((isNumeric(currentAtom.left) && isNumeric(currentAtom.right)) || (currentAtom.left.contains("T") || currentAtom.right.contains("T"))) {
                    //preform the constant folding
                    
                    int leftValue = 0; 
                    int rightValue = 0; 

                    //has to start somewhere 
                    if(firstIter){
                        leftValue = Integer.parseInt(currentAtom.left);
                        rightValue = Integer.parseInt(currentAtom.right);
                    }

                    //if this is not the first iteration 
                    if(!firstIter){
                        //if the t register is on the left 
                        if(currentAtom.left.contains("T")){
                            //from the last iteration 
                            leftValue = resultValue;
                        }
                        else{
                            leftValue = Integer.parseInt(currentAtom.left);
                        }
                        //if the t register is on the right 
                        if(currentAtom.right.contains("T")){
                            rightValue = resultValue; 
                        }
                        else{
                            rightValue = Integer.parseInt(currentAtom.right);
                        }
                    }

                   //does the  actual operation
                   //probably will have to  calculate div as floats??
                    switch (currentAtom.name) {
                        case "ADD":
                            resultValue = leftValue + rightValue;
                            break;
                        case "SUB":
                            resultValue = leftValue - rightValue;
                            break;
                        case "MUL":
                            resultValue = leftValue * rightValue;
                            break;
                        case "DIV":
                        //make sure we dont divide by 0
                            if (rightValue != 0) {
                                resultValue = leftValue / rightValue;
                            } else {
                                System.err.println("Division by zero detected.");
                                resultValue = 0; 
                            }
                            break;
                    }

                    /* //replace with MOV atom with the result
                    atom movedAtom = new atom("MOV", String.valueOf(resultValue), "", "", 0, currentAtom.dest);

                    //add the new MOV atom to the optimized atoms list
                    optimizedAtoms.add(movedAtom); */
                    
                    
                } 

                //this is the end of the line, no more t registers, nothing else 
                else {
                    
                    // if operands are not constants, add the atom as-is
                    optimizedAtoms.add(currentAtom);
                    System.out.println("here"); 
                }
            } 
            
            else {
                atom movAtom = new atom("MOV", String.valueOf(resultValue), "","", 0, currentAtom.dest);    
                optimizedAtoms.add(movAtom); 
                //if this is not the end of a cont. sequence of stuff that can be folded
                if(firstIter == true){
                    optimizedAtoms.add(currentAtom); 
                }
                /* //for others, just add them to the optimized list without modification
                optimizedAtoms.add(currentAtom); */
            }
            

            
            //add the final result to the 
            firstIter = false; 
            System.out.println(resultValue);
        }
        
        return optimizedAtoms; 
    }
   //helper to check for constants
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str); 
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
  //just a helper function to print the expressions for testing, will remove once complete 
     public static void printExpression(Queue<atom> atoms) {
        for (atom a : atoms) {
            System.out.println(a);
        }
    }

    //main method for testing purposes only, will remove once complete.
    public static void main(String[] args) {
       
        Queue<atom> test = new LinkedList<>();
        
      
       /* test.add(new atom("ADD", "3", "4", "", 0, "R1")); 
        test.add(new atom("SUB", "10", "5", "", 0, "R2")); 
        test.add(new atom("MUL", "2", "3", "", 0, "R3")); 
        test.add(new atom("DIV", "6", "2", "", 0, "R4"));  */
        
        test.add(new atom("MUL", "3", "10", "", 0, "T1")); 
        test.add(new atom("DIV", "T1", "2", "", 0, "T2")); 
        test.add(new atom("ADD", "1", "T2", "", 0, "T3")); 
        test.add(new atom("MOV", "T3", "", "", 0, "R1"));
        
       
        System.out.println("Original Expressions:");
        printExpression(test);

   
        Queue<atom> optimizedAtoms = constantFolding(test);

    
        System.out.println("\nOptimzed Expression");
        printExpression(optimizedAtoms);
    }

}