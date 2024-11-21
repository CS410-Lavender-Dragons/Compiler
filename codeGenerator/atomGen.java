package codeGenerator;

import Parser.Parser;

import Core.Token;
import Core.TokenName;

import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

import java.util.Arrays; 

@SuppressWarnings("unused")

public class atomGen {    
    private Queue<atom> atomList; 
    public atomGen(){
        atomList = new LinkedList<>(); 
    }

    // add add atom: (ADD, left, right, result)
    public  void addAtom(String left, String right, String result) {
        atomList.add(new atom("ADD", left, right, result, -1, null));
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

    public void negAtom(String left, String result){
        atomList.add(new atom("NEG", left, null, result, -1, null));
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

    public Queue<atom> getAtomList() {
        return this.atomList;
    }
}
