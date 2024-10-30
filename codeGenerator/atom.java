package codeGenerator;


//reference
// 0 - always
// 1 - equal
// 2 - lesser
// 3 - greater
// 4 - lesser or equal
// 5 - greater or equal
// 6 - unequal


//data holder 
public class atom {
    //based on big boy: (name, left, right, result, cmp, dest). 
    
    //data
    String name, left, right, result, dest;
    int cmp;
    
    //constructor
    public atom(String name, String left, String right, String result, int cmp, String dest){
        this.name = name; 
        this.left = left; 
        this.right = right; 
        this.result = result; 
        this.cmp = cmp; 
        this.dest = dest; 
    }

    //print
    public void print(){
        System.out.println(name + "," + left+ "," + right+ "," + result+ "," + cmp+ "," + dest); 
    }

    //nothing else? 

}    

