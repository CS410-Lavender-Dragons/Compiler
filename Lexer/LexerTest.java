package Lexer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class LexerTest {

	public static void main(String[] args) {
		Queue<Token> generatedTokens = generateTokenStream();
		String toLex = generateInputFromTokens(generatedTokens);
		Lexer lex = new Lexer();
		Queue<Token> lexedTokens = lex.tokenize(toLex);
		System.out.println(generatedTokens);
		System.out.println(lexedTokens);
		System.out.println(compareTokens(generatedTokens, lexedTokens));
	}
	
    private static LinkedList<Integer> idList = new LinkedList<>(Arrays.asList(1, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 17, 18, 20, 21, 23, 24, 25, 27, 28, 30, 31, 54, 56, 58, 60, 63, 65, 67, 69));
    
	private static Queue<Token> generateTokenStream(){
		Queue<Token> tokens = new LinkedList<Token>();
        final Random RANDOM = new Random();
    	final int length = RANDOM.nextInt(64) + 1;
    	
    	for(int i = 0; i < length; i++) {
    		int tokenType = RANDOM.nextInt(71) + 1;
    		
    		if (tokenType == TokenName.IDENTIFIER.getValue())
    			tokens.add(new Token(TokenName.IDENTIFIER, getRandomIdentifier()));
    		else if (tokenType == TokenName.NUMERIC.getValue())
    			tokens.add(new Token(TokenName.NUMERIC, Integer.valueOf(RANDOM.nextInt(9999))));
    		else if (idList.contains(tokenType))
    			tokens.add(new Token(TokenName.IDENTIFIER, getRandomIdentifier()));
    		else
    			tokens.add(new Token(TokenName.valToToken(tokenType)));
    	}
    	
    	tokens.add(new Token(TokenName.EOI));
    	
    	return tokens;
	}
	
	private static String generateInputFromTokens(Queue<Token> tokens) {
        StringBuilder sb = new StringBuilder();

        for (Token token : tokens) {
            TokenName tokenName = token.getName();
            Object tokenValue = token.getValue();
            int tokenNum = 0;
            switch (tokenName) {
                case IN_KW:
                    sb.append("in ");
                    break;
                case IF_KW:
                    sb.append("if ");
                    break;
                case LOOP_KW:
                    sb.append("loop ");
                    break;
                case LET_KW:
                    sb.append("let ");
                    break;
                case ELSE_KW:
                    sb.append("else ");
                    break;
                case FOR_KW:
                    sb.append("for ");
                    break;
                case MUT_KW:
                    sb.append("mut ");
                    break;
                case BREAK_KW:
                    sb.append("break ");
                    break;
                case CONTINUE_KW:
                    sb.append("continue ");
                    break;
                case NUMERIC:
                    if (tokenValue instanceof Integer) {
                        sb.append(tokenValue.toString()).append(" ");
                    }
                    break;
                case DECIMAL:
                    sb.append("."); // Example decimal value
                    break;
                case RANGE_OP:
                    sb.append("..");
                    break;
                case INCLUSIVERANGE_OP:
                    sb.append("..=");
                    break;
                case OPEN_BRACKET:
                    sb.append("{");
                    break;
                case CLOSE_BRACKET:
                    sb.append("}");
                    break;
                case OPEN_PAREN:
                    sb.append("(");
                    break;
                case CLOSE_PAREN:
                    sb.append(")");
                    break;
                case ADD_OP:
                    sb.append("+");
                    break;
                case SUB_OP:
                    sb.append("-");
                    break;
                case MULT_OP:
                    sb.append("*");
                    break;
                case DIV_OP:
                    sb.append("/");
                    break;
                case GREATER_OP:
                    sb.append(">");
                    break;
                case GREATER_EQ_OP:
                    sb.append(">=");
                    break;
                case LESS_OP:
                    sb.append("<");
                    break;
                case LESS_EQ_OP:
                    sb.append("<=");
                    break;
                case ASSIGN_OP:
                    sb.append("=");
                    break;
                case EQ_OP:
                    sb.append("==");
                    break;
                case SEMICOLON:
                    sb.append(";");
                    break;
                case COLON:
                    sb.append(":");
                    break;
                case BIT_8_INT_OP:
                    sb.append("i8 ");
                    break;
                case BIT_16_INT_OP:
                    sb.append("i16 ");
                    break;
                case BIT_32_INT_OP:
                    sb.append("i32 ");
                    break;
                case BIT_64_INT_OP:
                    sb.append("i64 ");
                    break;
                case BIT_128_INT_OP:
                    sb.append("i128 ");
                    break;
                case BIT_8_FLOAT_OP:
                    sb.append("f8 ");
                    break;
                case BIT_16_FLOAT_OP:
                    sb.append("f16 ");
                    break;
                case BIT_32_FLOAT_OP:
                    sb.append("f32 ");
                    break;
                case BIT_64_FLOAT_OP:
                    sb.append("f64 ");
                    break;
                case BIT_128_FLOAT_OP:
                    sb.append("f128 ");
                    break;
                case IDENTIFIER:
                    if (tokenValue instanceof String) {
                        sb.append(tokenValue.toString()).append(" ");
                    }
                    break;
                default:
                    switch(tokenNum) {
                    	case 1:
                    		sb.append("i").append(" ");
                    		break;
                    	case 4:
                    		sb.append("c").append(" ");
                    		break;
                    	case 5:
                    		sb.append("co").append(" ");
                    		break;
                    	case 6:
                    		sb.append("con").append(" ");
                    		break;
                    	case 7:
                    		sb.append("cont").append(" ");
                    		break;
                    	case 8:
                    		sb.append("conti").append(" ");
                    		break;
                    	case 9:
                    		sb.append("contin").append(" ");
                    		break;
                    	case 10:
                    		sb.append("continu").append(" ");
                    		break;
                    	case 12:
                    		sb.append("b").append(" ");
                    		break;
                    	case 13:
                    		sb.append("br").append(" ");
                    		break;
                    	case 14:
                    		sb.append("bre").append(" ");
                    		break;
                    	case 15:
                    		sb.append("brea").append(" ");
                    		break;
                    	case 17:
                    		sb.append("m").append(" ");
                    		break;
                    	case 18:
                    		sb.append("mu").append(" ");
                    		break;
                    	case 20:
                    		sb.append("f").append(" ");
                    		break;
                    	case 21:
                    		sb.append("fo").append(" ");
                    		break;
                    	case 23:
                    		sb.append("e").append(" ");
                    		break;
                    	case 24:
                    		sb.append("el").append(" ");
                    		break;
                    	case 25:
                    		sb.append("els").append(" ");
                    		break;
                    	case 27:
                    		sb.append("l").append(" ");
                    		break;
                    	case 28:
                    		sb.append("le").append(" ");
                    		break;
                    	case 30:
                    		sb.append("lo").append(" ");
                    		break;
                    	case 31:
                    		sb.append("loo").append(" ");
                    		break;
                    	case 54:
                    		sb.append("i1").append(" ");
                    		break;
                    	case 56:
                    		sb.append("i12").append(" ");
                    		break;
                    	case 58:
                    		sb.append("i3").append(" ");
                    		break;
                    	case 60:
                    		sb.append("i6").append(" ");
                    		break;
                    	case 63:
                    		sb.append("f1").append(" ");
                    		break;
                    	case 65:
                    		sb.append("f12").append(" ");
                    		break;
                    	case 67:
                    		sb.append("f3").append(" ");
                    		break;
                    	case 69:
                    		sb.append("f6").append(" ");
                    		break;
                    		
                    }
                    break;
            }
        }
        return sb.toString().trim();
    }
    
    private static String getRandomIdentifier() {
        final String STARTING_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
        final String ALPHANUMERIC_CHARACTERS = STARTING_CHARACTERS + "0123456789";
        final Random RANDOM = new Random();
    	final int length = RANDOM.nextInt(64);
        
        StringBuilder sb = new StringBuilder();
        sb.append(STARTING_CHARACTERS.charAt(RANDOM.nextInt(STARTING_CHARACTERS.length())));
        for (int i = 1; i < length; i++) {
            sb.append(ALPHANUMERIC_CHARACTERS.charAt(RANDOM.nextInt(ALPHANUMERIC_CHARACTERS.length())));
        }
       
        
        return sb.toString();
    }
    
    private static boolean compareTokens(Queue<Token> originalTokens, Queue<Token> lexedTokens) {
        if (originalTokens.size() != lexedTokens.size()) {
            return false;
        }

        while (!originalTokens.isEmpty() && !lexedTokens.isEmpty()) {
            Token original = originalTokens.poll();
            Token lexed = lexedTokens.poll();

            if (!original.getName().equals(lexed.getName())) {
                return false;
            }

            if (original.getValue() instanceof String && !original.getValue().toString().toLowerCase().equals(lexed.getValue().toString()))
            	return false;
            if (original.getValue() instanceof Integer && !original.getValue().equals(lexed.getValue()))
            	return false;
        }

        return true;
    }
}
