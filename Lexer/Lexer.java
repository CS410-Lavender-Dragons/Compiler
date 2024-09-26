    package Lexer;

    
    import java.util.Arrays;
    import java.util.LinkedList;
    import java.util.Queue;
    import Lexer.Token;
    import Lexer.TokenName;


    @SuppressWarnings("unused")
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
            @SuppressWarnings("unused")
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

                //update state; ooh ooh aah aah 
                curr_state = table.getNextState(curr_state, inputChar);

                //Add input character to value String
                value += inputChar;

                if(accepting(curr_state)){
                    LinkedList<Integer> idList = new LinkedList<>(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 17, 18, 20, 21, 23, 24, 25, 27, 28, 30, 31));

                    if(idList.contains(curr_state) || curr_state == TokenName.IDENTIFIER.getValue()){ //Tokens which have a value (Identifier, String)
                        tokens.add(createToken(TokenName.valToToken(curr_state), value));
                    }
                    else if (curr_state == TokenName.NUMERIC.getValue()){ //Token with a value (Numeric, Integer)
                        tokens.add(createToken(TokenName.valToToken(curr_state), Integer.valueOf(value).toString()));
                    }
                    else if (curr_state != 0){ //Tokens that don't have an associated value, excluding all whitespace
                        tokens.add(createToken(TokenName.valToToken(curr_state)));
                    }

                    //Reset to start state, clear value String
                    curr_state = 0;
                    value = "";
                }
            }

            //https://tinyurl.com/ycyya2pm 
            tokens.add(createToken(TokenName.EOI));
            return tokens;
        }

        //determination if it's all done
        private boolean accepting(int curr_state){
            return true;
        }

        private Token createToken(TokenName name, String value){
            return new Token(name, value);
        }

        private Token createToken(TokenName name){
            return new Token(name);
        }

    }
