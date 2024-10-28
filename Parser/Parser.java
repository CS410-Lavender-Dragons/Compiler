package Parser;

import Core.Token;
import Core.TokenName;

import java.util.Queue;

public class Parser {
    Queue<Token> tokenQueue;
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

    }

    void STATEMENT(){

    }

    void ASSIGNMENT_EXPR(){

    }

    void TYPE(){

    }

    void IF_EXPR(){

    }

    void WHILE_EXPR(){

    }

    void FOR_EXPR(){

    }

    void RANGE(){

    }

    void ARITHMETIC_EXPR(){

    }

    void TERM(){

    }

    void VALUE(){

    }

    void COMPARISON_EXPR(){

    }

    //Returns int representing complement operation of comparison token
    int COMPARISON(){
        return 0;
    }
}
