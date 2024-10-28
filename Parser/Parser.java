package Parser;

import Core.Token;
import Core.TokenName;

import java.util.Queue;

public class Parser {
    Queue<Token> tokenQueue;

    public void parse(Queue<Token> tokenQueue){
        this.tokenQueue = tokenQueue;
        STATEMENTS();
    }

    boolean accept(TokenName tokenName){
        if (tokenQueue.peek().getName() == tokenName){
            tokenQueue.remove();
            return true;
        }
        return false;
    }

    void expect(TokenName tokenName){
        if (tokenQueue.peek().getName() != tokenName)
            throw new RuntimeException("Expected: " + tokenName + ", but found: " + tokenQueue.peek().getName());
        tokenQueue.remove();
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
        else
            ASSIGNMENT_EXPR();
    }

    void ASSIGNMENT_EXPR(){
        if (accept(TokenName.IDENTIFIER)){
            expect(TokenName.ASSIGN_OP);
            ARITHMETIC_EXPR();
            expect(TokenName.SEMICOLON);
        }
        else if (accept(TokenName.LET_KW)){
            if (accept(TokenName.MUT_KW)){
                expect(TokenName.IDENTIFIER);
                if (accept(TokenName.COLON))
                    TYPE();
                expect(TokenName.ASSIGN_OP);
                ARITHMETIC_EXPR();
                expect(TokenName.SEMICOLON);
            }
            else {
                expect(TokenName.IDENTIFIER);
                if (accept(TokenName.COLON))
                    TYPE();
                expect(TokenName.ASSIGN_OP);
                ARITHMETIC_EXPR();
                expect(TokenName.SEMICOLON);
            }
        }

    }

    void TYPE(){
        if (!accept(TokenName.BIT_8_FLOAT_OP) || !accept(TokenName.BIT_8_INT_OP) || !accept(TokenName.BIT_16_FLOAT_OP) || !accept(TokenName.BIT_16_INT_OP) || !accept(TokenName.BIT_32_FLOAT_OP) || !accept(TokenName.BIT_32_INT_OP) || !accept(TokenName.BIT_64_FLOAT_OP) || !accept(TokenName.BIT_64_INT_OP) || !accept(TokenName.BIT_128_FLOAT_OP))
            expect(TokenName.BIT_128_INT_OP);
    }

    void IF_EXPR(){
        COMPARISON_EXPR();
        expect(TokenName.OPEN_BRACKET);
        STATEMENTS();
        expect(TokenName.CLOSE_BRACKET);
        if (accept(TokenName.ELSE_KW)){
            if (accept(TokenName.IF_KW)){
                IF_EXPR();
            }
            else{
                expect(TokenName.OPEN_BRACKET);
                STATEMENTS();
                expect(TokenName.CLOSE_BRACKET);
            }
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
        RANGE();
        expect(TokenName.OPEN_BRACKET);
        STATEMENTS();
        expect(TokenName.CLOSE_BRACKET);
    }

    void RANGE(){
        ARITHMETIC_EXPR();
        if (!accept(TokenName.RANGE_OP))
            expect(TokenName.INCLUSIVERANGE_OP);
        ARITHMETIC_EXPR();
    }

    void ARITHMETIC_EXPR(){

 //       else {
            TERM();
            if (accept(TokenName.ADD_OP)){
                ARITHMETIC_EXPR();
            }
            else if (accept(TokenName.SUB_OP)){
                ARITHMETIC_EXPR();
            }
   //     }
    }

    void TERM(){
        VALUE();
        if (accept(TokenName.MULT_OP)){
            TERM();
        }
        else if (accept(TokenName.DIV_OP)){
            TERM();
        }
    }

    void VALUE(){
        if (accept(TokenName.OPEN_PAREN)){
            ARITHMETIC_EXPR();
            expect(TokenName.CLOSE_PAREN);
        }
        else if (accept(TokenName.NUMERIC)){
            if (accept(TokenName.DECIMAL))
                expect(TokenName.NUMERIC);
        }
        else {
            expect(TokenName.IDENTIFIER);
        }
    }

    void COMPARISON_EXPR(){
        ARITHMETIC_EXPR();
        COMPARISON();
        ARITHMETIC_EXPR();
    }

    //Returns int representing complement operation of comparison token
    int COMPARISON(){
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
