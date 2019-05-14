package ProgramStructures;

import javafx.util.Pair;

import java.util.LinkedList;

public class IfExpression extends Instruction {
    //if condition
    LogicExpression condition;
    //if body (instructions)
    private LinkedList<Instruction> instructions;
    //else if list
    private LinkedList<Pair<LogicExpression, LinkedList<Instruction>>> elif;
    //else body (instructions)
    private LinkedList<Instruction> elseBody;

    public IfExpression(LogicExpression condition) {
        this.condition = condition;
        type = InstructionType.IF;
        elif = new LinkedList<>();
        elseBody = new LinkedList<>();
        instructions = new LinkedList<>();
    }

    public LogicExpression getCondition() {
        return condition;
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

    public LinkedList<Instruction> getElseInstructions() {
        return elseBody;
    }


    public void addElseIf(Pair<LogicExpression, LinkedList<Instruction>> elif) {
        this.elif.add(elif);
    }

    public Pair<LogicExpression, LinkedList<Instruction>> getNextElseIf() {
        if(elif.isEmpty())
            return null;
        return elif.removeFirst();
    }

    public void addElseInstructions(LinkedList<Instruction> instructions) {
        this.instructions = (LinkedList) instructions.clone();
    }

    public Instruction getNextElseInstruction() {
        if(elseBody.isEmpty())
            return null;
        return elseBody.removeFirst();
    }


}
