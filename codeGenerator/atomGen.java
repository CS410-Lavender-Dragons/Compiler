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
    Queue<atom> atomList = new LinkedList<>(); 

    public void generateAtoms(Queue<Token> stream){

        //t regs start at 1 
        int tempRegCount = 1; 
        //counter stuff. 0 is slot 0, 1 is slot 1, etc. 
        ArrayList<Integer> tempRegs = new ArrayList<>(); 
        
        //to store identifiers to plug into things 
        ArrayList<String> idList = new ArrayList<>();
        String[] identifiers = new String[4];  
        int curIdentifier = 0; 

        //other register stuff 
        //int 
            
        //scan through stream
        for(Token token : stream){
            //dogwater code
            
            //if the first one is a var and not a kw, capture.
            if(token.getName().equals(TokenName.IDENTIFIER)){
                //TODO: double check to make sure getValue works 
                identifiers[curIdentifier] = token.getValue().toString(); 
                curIdentifier++; 
            }
            
            //load in 
            if(token.getName().equals(TokenName.ADD_OP)){
                //increment temp reg count 
                String result = "T" + tempRegCount; 
                addAtom(idList.get(0), idList.get(1), result);
                tempRegCount++; 
            }
            else if(token.getName().equals(TokenName.SUB_OP)){
                String result = "T" + tempRegCount; 
                subAtom(idList.get(0), idList.get(1), result);
                tempRegCount++; 
            }
            else if(token.getName().equals(TokenName.MULT_OP)){
                String result = "T" + tempRegCount; 
                mulAtom(idList.get(0), idList.get(1), result);
                tempRegCount++; 
            }
            else if(token.getName().equals(TokenName.DIV_OP)){
                String result = "T" + tempRegCount; 
                divAtom(idList.get(0), idList.get(1), result);
                tempRegCount++; 
            }
            //do jump, lbl, test, and counter magic 
            else if(token.getName().equals(TokenName.FOR_KW)){
                
            }
            //add mov atom  
            else if(token.getName().equals(TokenName.EQ_OP)){

            }
            
        } 
            

        //determine which one it is? 
        
        //return finalized list of atoms 
        //return atomList;
    }

    // add add atom: (ADD, left, right, result)
    public void addAtom(String left, String right, String result) {
        atomList.add(new atom("ADD", left, right, result, -1, null));
    }

    // add sub atom: (SUB, left, right, result)
    public void subAtom(String left, String right, String result) {
        atomList.add(new atom("SUB", left, right, result, -1, null));
    }

    //add mul atom: (MUL, left, right, result)
    public void mulAtom(String left, String right, String result){
        atomList.add(new atom("SUB", left, right, result, -1, null));
    }

    //problem of identifying stuff, counter as well





}
