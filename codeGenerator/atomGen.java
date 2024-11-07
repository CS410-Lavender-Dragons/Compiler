package codeGenerator;

import Parser.Parser;

import Core.Token;
import Core.TokenName;

import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

import java.util.Arrays; 

//annoying
@SuppressWarnings("unused")




public class atomGen {    

    //dogwater code at its finest 

    private Queue<atom> atomList; 
    public atomGen(){
        atomList = new LinkedList<>(); 
    }

    //TODO: what is the logic that'll tie everything together, pemdas? according to prof, recursion will, but there is the possiblity that this MAY not be the case
    //depends on if previous step is correct   

    //reg trackers
    static int tempCounter = 0; 
    //static int lblCounter = 0; 

    /* //logic & 2d arrays to track data....maybe needed? 
    ArrayList<ArrayList<Character>> regArr = new ArrayList<>();   */

    //for everything below, -1 means null. just helper stuff 

    //helper
    public String newTReg(){
        return "T" + tempCounter++; 
    }

    // add add atom: (ADD, left, right, result)
    public  void addAtom(String left, String right, String result) {
        atomList.add(new atom("ADD", left, right, result, -1, null));
        tempCounter++; 
    }

    // add sub atom: (SUB, left, right, result)
    public  void subAtom(String left, String right, String result) {
        atomList.add(new atom("SUB", left, right, result, -1, null));
    }

    //add mul atom: (MUL, left, right, result)
    public void mulAtom(String left, String right, String result){
        atomList.add(new atom("MUL", left, right, result, -1, null));
    }

    //add div atom: (div, left, right, result)
    public void divAtom(String left, String right, String result){
        atomList.add(new atom("DIV", left, right, result, -1, null));
    }

    public void jmpAtom(String dest){
        atomList.add(new atom("JMP", null, null, null, -1, dest));
    }

    public void negAtom(String left, String dest){
        atomList.add(new atom("NEG", left, null, null, -1, dest));
    }

    public void lblAtom(String dest){
        atomList.add(new atom("LBL", null, null, null, -1, dest));
    }

    public void tstAtom(String left, String right, int cmp, String dest){
        atomList.add(new atom("TST", left, right, null, cmp, dest));

    }

    public void movAtom(String left, String dest){
        atomList.add(new atom("MOV", left, null, null, -1, dest));
    }

    //problem of identifying stuff, counter as well

    //done
    public void end(){
        for(var atom : atomList){
            System.out.println(atom);
        }
    }

}
