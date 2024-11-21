package phase3;

public class machineCode {

    Integer opcode, cmp, r, a;

    //constructor
    public machineCode(int opcode, int cmp, int r, int a) {
        this.opcode = opcode;
        this.cmp = cmp;
        this.r = r;
        this.a = a;
    }

    public String toString(){
        String opString = opcode.toString();
        String cmpString = cmp.toString();
        String rString = r.toString();
        String aString = a.toString();

        String machCode = (opString + cmpString + rString + aString);

        return machCode;
    }
}   