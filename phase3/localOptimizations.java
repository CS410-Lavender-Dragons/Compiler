package phase3;

import java.util.*;

public class localOptimizations {
    public Queue<machineCode> loadStoreOptimization(Queue<machineCode> machineQueue) {
        for (machineCode code : machineQueue) {
            if (code.opcode == 8 && machineQueue.peek().opcode == 7) { //TODO determine how we will look ahead
                if (code.a == machineQueue.peek().a) {
                    machineQueue.remove();
                }

                //pc +=4;
            }
        }

        //adjustMemAddr(); //replace it’s pc with new pc
        //newMemAddr();
        
        return machineQueue;
    }
}
