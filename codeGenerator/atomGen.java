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

    //..
    static Queue<atom> atomList = new LinkedList<>(); 

    //TODO: what is the logic that'll tie everything together, pemdas? 

    static int tempCounter = 0; 

    //for everything below, -1 means null. just helper stuff 

    // add add atom: (ADD, left, right, result)
    public static void addAtom(String left, String right, String result) {
        atomList.add(new atom("ADD", left, right, result, -1, null));
    }

    // add sub atom: (SUB, left, right, result)
    public static void subAtom(String left, String right, String result) {
        atomList.add(new atom("SUB", left, right, result, -1, null));
    }

    //add mul atom: (MUL, left, right, result)
    public static void mulAtom(String left, String right, String result){
        atomList.add(new atom("SUB", left, right, result, -1, null));
    }

    //add mul atom: (MUL, left, right, result)
    public static void divAtom(String left, String right, String result){
        atomList.add(new atom("SUB", left, right, result, -1, null));
    }

    public static void jmpAtom(String dest){
        atomList.add(new atom("JMP", null, null, null, -1, dest));
    }

    public static void negAtom(String dest){
        atomList.add(new atom("NEG", null, null, null, -1, dest));
    }

    public static void lblAtom(String dest){
        atomList.add(new atom("LBL", null, null, null, -1, dest));
    }

    public static void tstAtom(String left, String right, int cmp, String dest){
        atomList.add(new atom("TST", left, right, null, cmp, dest));
    }

    public static void movAtom(String left, String dest){
        atomList.add(new atom("MOV", left, null, null, -1, dest));
    }

    //problem of identifying stuff, counter as well





}
