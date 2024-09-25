package Lexer;

//Valeu will be ITS FINAL STATE
public enum TokenName {
    IN_KW, //in keyword 
    IF_KW, //if keyword
    LOOP_KW, //loop keyword
    LET_KW, //let keyword
    ELSE_KW, //else keyword
    FOR_KW, //for keyword
    MUT_KW, //mut keyword
    BREAK_KW,
    CONTINUE_KW,
    NUMERIC,
    DECIMAL,
    RANGE_OP,
    INCRANGE_OP, //NAME IS TRASH ILL THINK OF A BETTER ONE LATER
    OPEN_BRACKET_KW,
    CLOSE_BRACKET_KW,
    OPEN_PARENTHESIS_KW,
    CLOSE_PARENTHESIS_KW,
    PLUS_OP,
    MINUS_OP,
    MULTIPLY_OP,
    DIVIDE_OP,
    GT_OP,
    GE_OP,
    LT_OP,
    LE_OP,
    ASSIGNMENT_OP,
    EQUALITY_COMPARISON_OP,
    STATEMENT_TERMINATOR,
    SIGNED8BIT_INT, //128-bit signed integer point data type
    SIGNED16BIT_INT, //128-bit signed integer point data type
    SIGNED32BIT_INT, //128-bit signed integer point data type
    SIGNED64BIT_INT, //128-bit signed integer point data type
    SIGNED128BIT_INT, //128-bit signed integer data type
    SIGNED8BIT_FLOAT, //8-bit signed floating point data type
    SIGNED16BIT_FLOAT, //16-bit signed floating point data type
    SIGNED32BIT_FLOAT, //32-bit signed floating point data type
    SIGNED64BIT_FLOAT, //64-bit signed floating point data type
    SIGNED128BIT_FLOAT, // 128-bit signed floating point data type

}
