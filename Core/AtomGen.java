package Core;

import Core.Atom;

import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings("unused")

public class AtomGen {
    private Queue<Atom> atomList;
    public AtomGen(){
        atomList = new LinkedList<>(); 
    }

    // add add atom: (ADD, left, right, result)
    public  void addAtom(String left, String right, String result) {
        atomList.add(new Atom("ADD", left, right, result, -1, null));
    }

    // add sub atom: (SUB, left, right, result)
    public  void subAtom(String left, String right, String result) {
        atomList.add(new Atom("SUB", left, right, result, -1, null));
    }

    //add mul atom: (MUL, left, right, result)
    public void mulAtom(String left, String right, String result){
        atomList.add(new Atom("MUL", left, right, result, -1, null));
    }

    //add div atom: (div, left, right, result)
    public void divAtom(String left, String right, String result){
        atomList.add(new Atom("DIV", left, right, result, -1, null));
    }

    public void jmpAtom(String dest){
        atomList.add(new Atom("JMP", null, null, null, -1, dest));
    }

    public void negAtom(String left, String result){
        atomList.add(new Atom("NEG", left, null, result, -1, null));
    }

    public void lblAtom(String dest){
        atomList.add(new Atom("LBL", null, null, null, -1, dest));
    }

    public void tstAtom(String left, String right, int cmp, String dest){
        atomList.add(new Atom("TST", left, right, null, cmp, dest));

    }

    public void movAtom(String left, String dest){
        atomList.add(new Atom("MOV", left, null, null, -1, dest));
    }

    //problem of identifying stuff, counter as well

    //done
    public void end(){
        for(var atom : atomList){
            System.out.println(atom);
        }
    }

    public Queue<Atom> getAtomList() {
        return this.atomList;
    }
}
