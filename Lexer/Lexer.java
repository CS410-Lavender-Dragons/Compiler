    package Lexer;

    
    import java.util.Arrays;
    import java.util.LinkedList;
    import java.util.Queue;
    import Lexer.Token;
    import Lexer.TokenName;


    @SuppressWarnings("unused")
    public class Lexer {
        public static void main(String[] args) {
            Lexer lex = new Lexer();

//            var tokens1 = lex.tokenize("x = 89 + a");
//            while (!tokens1.isEmpty()) {
//                System.out.println(tokens1.remove().getName());
//            }

            var tokens2 = lex.tokenize("variable_name = 39");
            while (!tokens2.isEmpty()) {
                System.out.println(tokens2.remove().getName());
            }
        }

        private final StateTransitionTable table = new StateTransitionTable();

        // List of states that imply an Identifier Token
        LinkedList<Integer> idList = new LinkedList<>(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 17, 18, 20, 21, 23, 24, 25, 27, 28, 30, 31));

        public Queue<Token> tokenize(String input) {
            @SuppressWarnings("unused")
            Queue<Token> tokens = new LinkedList<Token>();
            char inputChar;
            int inputIndexMapping;
            String value = "";
            int curr_state = 0;

            // Loop through the input
            for(int i = 0; i < input.length(); i++){
                inputChar = input.charAt(i);

                // Character is not a valid character in the language
                if (table.columnIndex(inputChar) == -1){
                    tokens.add(createToken(TokenName.INVALID_INPUT));
                    //return tokens; //TODO should this be here? -> yes we should stop processing input upon encountering a character that's not valid
                }

                if (table.getNextState(curr_state, inputChar) == -1) { // No further transitions
                    if (accepting(curr_state)) { // In an accepting state
                        // If the current state implies an Identifier Token
                        if(idList.contains(curr_state) || curr_state == TokenName.IDENTIFIER.getValue()){ //Tokens which have a value (Identifier, String)
                            tokens.add(createToken(TokenName.IDENTIFIER, value));
                        }
                        else if (curr_state == TokenName.NUMERIC.getValue()){ //Token with a value (Numeric, Integer)
                            tokens.add(createToken(TokenName.NUMERIC, Integer.valueOf(value).toString()));
                        }
                        else if (curr_state != 0){ //Tokens that don't have an associated value, excluding all whitespace
                            tokens.add(createToken(TokenName.valToToken(curr_state)));
                        } //TODO might need a catch all

                        //Reset to start state, clear value String
                        curr_state = 0;
                        value = "";
                    } else { //I'm not sure about this
                        tokens.add(createToken(TokenName.INVALID_INPUT));
                    }

                    curr_state = 0;
                } else {
                    // Update state
                    curr_state = table.getNextState(curr_state, inputChar);

                    // Add input character to value String
                    value += inputChar;
                }
            }

            tokens.add(createToken(TokenName.EOI));
            return tokens;
        }

        // Determine if state is an accepting state
        private boolean accepting(int curr_state) { //TODO implement
            return true;
        }

        private Token createToken(TokenName name, String value){
            return new Token(name, value);
        }

        private Token createToken(TokenName name){
            return new Token(name);
        }

    }
