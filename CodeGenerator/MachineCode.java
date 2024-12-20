package codeGenerator;

public class MachineCode {

    Integer opcode, cmp, r, a;

    //constructor
    public MachineCode(int opcode, int cmp, int r, int a) {
        this.opcode = opcode;
        this.cmp = cmp;
        this.r = r;
        this.a = a;
    }

    public String toString(){
        
        //4 bits
        String opString = Integer.toBinaryString(opcode);

        //4 bits
        String cmpString = Integer.toBinaryString(cmp);

        //4 bits
        String rString = Integer.toBinaryString(r);

        //20 bits
        String aString = Integer.toBinaryString(a);


        while(opString.length() < 4){
            opString = '0' + opString;
        }

        while (cmpString.length() < 4){
            cmpString = '0' + cmpString;
        }

        while (rString.length() < 4){
            rString = '0' + rString;
        }

        while(aString.length() < 20){
            aString = '0' + aString;
        }


        String machCode = (opString + cmpString + rString + aString);

        return machCode;
    }

    public int combineParameters(){
        return ((this.opcode & 0xF) << 28) | ((this.cmp & 0xF) << 24) | ((this.r & 0xF) << 20) | (a & 0xFFFFF);
    }
}   