package Lexer;

public class Lexer {
    public static void main(String[] args) {
        StateTransitionTable table = new StateTransitionTable();
        int nextState = table.getNextState(0, "a");
        System.out.println(nextState);
    }
}
