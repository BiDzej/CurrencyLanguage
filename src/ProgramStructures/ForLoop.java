package ProgramStructures;

import java.util.LinkedList;

//for loop extends instruction
public class ForLoop extends Instruction {

    //initialized variable in loop
    //variable type is always integer
    private String variableName;
    //start value of the variable
    private int startValue;
    //condition to reach
    private int endValue;
    //linked list of instructions beetween begin-end bracets
    private LinkedList<Instruction> instructions;

    public ForLoop(String variableName, int startValue, int endValue)
    {
        //set type as a for loop
        type = InstructionType.FOR_LOOP;
        instructions = new LinkedList<>();
        this.variableName = variableName;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public String getVariableName() {
        return variableName;
    }

    public int getEndValue() {
        return endValue;
    }

    public int getStartValue() {
        return startValue;
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public Instruction getNextInstruction() {
        if(instructions.isEmpty())
            return null;
        return instructions.removeFirst();
    }
}
