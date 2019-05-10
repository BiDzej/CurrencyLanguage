package ProgramStructures;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;

public class IfExpression extends Instruction {
    //if condition
    Condition condition;
    //if body (instructions)
    private LinkedList<Instruction> instructions;
    //else if list
    private LinkedList<Pair<Condition, LinkedList<Instruction>>> elif;
    //else body (instructions)
    private LinkedList<Instruction> elseBody;

    public IfExpression(Condition condition) {
        this.condition = condition;
        type = InstructionType.IF;
        elif = new LinkedList<>();
        elseBody = new LinkedList<>();
        instructions = new LinkedList<>();
    }

    public Condition getCondition() {
        return condition;
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public Instruction getNextInstruction() {
        if(instructions.isEmpty())
            return null;
        return instructions.removeFirst();
    }

    public void addElseIf(Pair<Condition, LinkedList<Instruction>> elif) {
        this.elif.add(elif);
    }

    public Pair<Condition, LinkedList<Instruction>> getNextElseIf() {
        if(elif.isEmpty())
            return null;
        return elif.removeFirst();
    }

    public void addElseInstruction(Instruction instruction) {
        elseBody.add(instruction);
    }

    public Instruction getNextElseInstruction() {
        if(elseBody.isEmpty())
            return null;
        return elseBody.removeFirst();
    }


}
