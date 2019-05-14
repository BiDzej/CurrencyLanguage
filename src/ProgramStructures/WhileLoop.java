package ProgramStructures;

import java.util.LinkedList;

public class WhileLoop extends Instruction{
    //logic expression as an end condition of the loop
    LogicExpression logicExpression;
    //linked list of instructions beetween begin-end bracets
    private LinkedList<Instruction> instructions;

    public WhileLoop(LogicExpression logicExpression) {
        this.logicExpression = logicExpression;
        instructions = new LinkedList<>();
        type = InstructionType.WHILE_LOOP;
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public void addInstructions(LinkedList<Instruction> list) {
        instructions = (LinkedList) list.clone();
    }

    public Instruction getNextInstruction() {
        if(instructions.isEmpty())
            return null;
        return instructions.removeFirst();
    }

    public LinkedList<Instruction> getInstructions() {
        return instructions;
    }

    public LogicExpression getLogicExpression() {
        return logicExpression;
    }
}
