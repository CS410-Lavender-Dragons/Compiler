package Lexer;

//Token name with corresponding final state
public enum TokenName {
    IN_KW(2), //in keyword 
    IF_KW(3), //if keyword
    LOOP_KW(32), //loop keyword
    LET_KW(29), //let keyword
    ELSE_KW(26), //else keyword
    FOR_KW(22), //for keyword
    MUT_KW(19), //mut keyword
    BREAK_KW(16), //break keyword
    CONTINUE_KW(11), //continue keyword
    NUMERIC(34), //numeric
    DECIMAL(50), //decimal
    RANGE_OP(51), //range operator 
    INCLUSIVERANGE_OP(52), //Inclusive range operator
    OPEN_BRACKET(49), //open bracket
    CLOSE_BRACKET(48), //close bracket
    OPEN_PAREN(47), //open parenthesis
    CLOSE_PAREN(46), //close parenthesis
    ADD_OP(45), //addition operator
    SUB_OP(44), //subtraction operator 
    MULT_OP(43), //multiplication operator
    DIV_OP(42), //division operator
    GREATER_OP(40), //greater than operator
    GREATER_EQ_OP(41), //greater than or equal operator
    LESS_OP(38), //less than operator
    LESS_EQ_OP(39), //less than or equal operator 
    ASSIGN_OP(36), //assignment operator
    EQ_OP(37), //equality comparison operator
    SEMICOLON(35), //statement terminator
    BIT_8_INT_OP(53), 
    BIT_16_INT_OP(55), 
    BIT_32_INT_OP(59), 
    BIT_64_INT_OP(61), 
    BIT_128_INT_OP(57), 
    BIT_8_FLOAT_OP(62), 
    BIT_16_FLOAT_OP(64),
    BIT_32_FLOAT_OP(68),
    BIT_64_FLOAT_OP(70),
    BIT_128_FLOAT_OP(66),
    IDENTIFIER(71),

    INVALID_INPUT(100),
    EOI(101);


    private final int token;

    //Token Constructor
    TokenName(int token) {
        this.token = token;
    }

    //Token Getter
    public int getValue() {
        return token;
    }
}
