// Authors: Hunter Oxley, Branden Purdum, Skyler Putney, Emma Kupec
// Reviewers: William Valentine, Alyssa Mesinere

package Core;

//Token class which identifies a token's name and corresponding value
public class Token {

    private final TokenName name;
    private final Object value;

    //Constructor for tokens without a value
    public Token(TokenName tokenName){
        this(tokenName, null);
    }

    //Constructor for tokens which store a value (identifier, numeric)
    public Token(TokenName tokenName, Object value){
        this.name = tokenName;
        this.value = value;
    }

    public TokenName getName(){
        return name;
    }

    public Object getValue(){
        return value;
    }
    
    public String toString() {
    	return name + " " + "{" + (value == null ? "" : value.toString()) + "}"; 
    }
}
