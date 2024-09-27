    //Authors: Emma Kupec, Skyler Putney
    //Reviewers: Hunter Oxley, Branden Purdum, William Valentine
    
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

            var tokens1 = lex.tokenize("x = 89 + a");
            while (!tokens1.isEmpty()) {
                System.out.println(tokens1.remove().toString());
            }
            System.out.println("\n");
            var tokens2 = lex.tokenize("variable_name = 39");
            while (!tokens2.isEmpty()) {
                System.out.println(tokens2.remove().toString());
            }
            System.out.println("\n");
            var tokens3 = lex.tokenize("for n in 1..100{x=x+y-100*20/20 }");
            while (!tokens3.isEmpty()) {
            	System.out.println(tokens3.remove().toString());
            }
            System.out.println("\n");
            var tokens4 = lex.tokenize("for x in 23..=  4912 { x - 99} & { y + 2} ");
            while (!tokens4.isEmpty()) {
            	System.out.println(tokens4.remove().toString());
            }
            System.out.println("\n");
            var tokens5 = lex.tokenize("for i in 1..= 99 { x = ( 62.0 + 9.0) < > : ;}");
            while (!tokens5.isEmpty()) {
                System.out.println(tokens5.remove().toString());
            }
        }

        private final StateTransitionTable table = new StateTransitionTable();

        // List of states that imply an Identifier Token
        LinkedList<Integer> idList = new LinkedList<>(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 17, 18, 20, 21, 23, 24, 25, 27, 28, 30, 31, 54, 56, 58, 60, 63, 65, 67, 69));

        public Queue<Token> tokenize(String input) {
            @SuppressWarnings("unused")
            Queue<Token> tokens = new LinkedList<Token>();
            char inputChar;
            String value = "";
            int currState = 0;

            // Loop through the input
            for(int i = 0; i < input.length(); i++){
            	
            	//Skip whitespace
            	while(input.charAt(i) == ' ') {
            		i++;
            	}
            	
            	//Grab character to process
                inputChar = Character.toLowerCase(input.charAt(i));

                // Character is not a valid character in the language
                if (table.columnIndex(inputChar) == -1){
                    tokens.add(createToken(TokenName.INVALID_INPUT));
                    return tokens;
                }
                
                // Update state
                currState = table.getNextState(currState, inputChar);

                // Add input character to value String
                value += inputChar;

                if (i + 1 == input.length() || accepting(currState, Character.toLowerCase(input.charAt(i + 1)))) { // In an accepting state
                    // If the current state implies an Identifier Token
                	if(idList.contains(currState) || currState == TokenName.IDENTIFIER.getValue()){ //Tokens which have a value (Identifier, String)
                        tokens.add(createToken(TokenName.IDENTIFIER, value));
                    }
                	else if (currState == TokenName.NUMERIC.getValue()){ //Token with a value (Numeric, Integer)
                		tokens.add(createToken(TokenName.NUMERIC, Integer.valueOf(value)));
                    }
                    else if (currState != 0){ //Tokens that don't have an associated value, excluding all whitespace
                        tokens.add(createToken(TokenName.valToToken(currState)));
                    }

                    //Reset to start state, clear value String
                    currState = 0;
                    value = "";
                }
            }

            tokens.add(createToken(TokenName.EOI));
            return tokens;
        }

        // Determine if state is an accepting state
        private boolean accepting(int currState, char nextChar) {
        	int nextState = table.getNextState(currState, nextChar);
            return nextState == -1 || nextState == 0;
        }

        private Token createToken(TokenName name, Object value) {
            return new Token(name, value);
        }

        private Token createToken(TokenName name) {
            return new Token(name);
        }
        
        private void processToken(int currState, String lexeme) {
        	
        }

    }
