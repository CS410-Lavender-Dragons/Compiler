package Lexer;

public class StateTransitionTable {
    public int rows = 72;
    public int columns = 51;

    public StateTransitionTable() { // TODO determine how to index using the characters AND how to handle null (Integer type?)
        int[][] table = {
                {71,12,4,71,23,20,71,71,71,71,71,27,17,71,71,71,71,71,71,71,71,71,71,71,71,34,34,34,34,34,34,34,34,34,34,71,50,36,49,48,47,46,45,44,43,42,40,38,33,35,0}, //0
                {71,71,71,71,71,3,71,71,71,71,71,71,71,2,71,71,71,71,71,71,71,71,71,71,71,71,54,71,58,71,71,60,71,53,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //1
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //2
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //3
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,5,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //4
                {71,71,71,71,71,71,71,71,71,71,71,71,71,6,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //5
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,7,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //6
                {71,71,71,71,71,71,71,71,8,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //7
                {71,71,71,71,71,71,71,71,71,71,71,71,71,9,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //8
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,10,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //9
                {71,71,71,71,11,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //10
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //11
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,13,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //12
                {71,71,71,71,14,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //13
                {15,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //14
                {71,71,71,71,71,71,71,71,71,71,16,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //15
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //16
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,18,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //17
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,19,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //18
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //19
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,21,71,71,71,71,71,71,71,71,71,71,71,63,71,67,71,71,69,71,62,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //20
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,22,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //21
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //22
                {71,71,71,71,71,71,71,71,71,71,71,24,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //23
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,25,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //24
                {71,71,71,71,26,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //25
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //26
                {71,71,71,71,28,71,71,71,71,71,71,71,71,71,30,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //27
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,29,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //28
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //29
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,31,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //30
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,32,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //31
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //32
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //33
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,34,34,34,34,34,34,34,34,34,34,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //34
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //35
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,37,null,null,null,null,null,null,null,null,null,null,null,null,0}, //36
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //37
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,39,null,null,null,null,null,null,null,null,null,null,null,null,0}, //38
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //39
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,41,null,null,null,null,null,null,null,null,null,null,null,null,0}, //40
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //41
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //42
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //43
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //44
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //45
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //46
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //47
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //48
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //49
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,51,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //50
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,52,null,null,null,null,null,null,null,null,null,null,null,null,0}, //51
                {null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //52
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //53
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,56,71,71,71,55,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //54
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //55
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,57,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //56
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //57
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,59,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //58
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //59
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,61,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //60
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //61
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //62
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,65,71,71,71,64,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //63
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //64
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,66,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //65
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //66
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,68,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //67
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //68
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,70,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //69
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //70
                {71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,71,null,null,null,null,null,null,null,null,null,null,null,null,null,null,0}, //71

        };
    }
}
