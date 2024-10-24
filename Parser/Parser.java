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
}
