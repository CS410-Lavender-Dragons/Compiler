package Lexer;

import java.util.LinkedList;
import java.util.Queue;

public class Lexer {
    public static void main(String[] args) {
        StateTransitionTable table = new StateTransitionTable();
        int nextState = table.getNextState(0, "a");
        System.out.println(nextState);
    }

    private final StateTransitionTable table = new StateTransitionTable();

    public Queue<Token> tokenize(String input) {
        Queue<Token> tokens = new LinkedList<Token>();
        char inputChar;
        int inputIndexMapping;
        String value = "";
        int curr_state = 0;

        for(int i = 0; i < input.length(); i++){
            inputChar = input.charAt(i);

            //Character is not valid
            if (table.columnIndex(inputChar) == -1){
                tokens.add(createToken(TokenName.INVALID_INPUT));
                return tokens;
            }

            //Update current state
            curr_state = table.getNextState(curr_state, inputChar);

            //Add input character to value String
            value += inputChar;

            if(accepting(curr_state)){

                //Need IDENTIFIER to also encompass states 1, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 17, 18, 20, 21, 23, 24, 25, 27, 28, 30, 31
                if(curr_state == TokenName.IDENTIFIER){ //Token with a value (string identifier)
                    tokens.add(createToken(curr_state, value));
                }
                else if (curr_state == TokenName.NUMERIC.getValue()){ //Token with a value (Integer numeric)
                    tokens.add(createToken(curr_state, Integer.valueOf(value)));
                }
                else if (curr_state != 0){ //Tokens that don't have an associated value, excluding all whitespace
                    tokens.add(createToken(curr_state));
                }

                //Reset to start state, clear value String
                curr_state = 0;
                value = "";
            }
        }

        tokens.add(TokenName.EOI);
        return tokens;
    }

    private Token createToken(TokenName name, String value){
        return new Token(name, value);
    }

    private Token createToken(TokenName name){
        return new Token(name);
    }
}
