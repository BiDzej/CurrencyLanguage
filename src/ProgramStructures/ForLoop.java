package ProgramStructures;

import Compilator.Symbol;

import java.util.LinkedList;

//for loop extends instruction
public class ForLoop extends Instruction {

    //initialized variable in loop
    //variable type is always integer
    private LinkedList<Symbol> initialization;
    //condition to reach
    private Symbol endValue;
    //linked list of instructions beetween begin-end bracets
    private LinkedList<Instruction> instructions;

    public ForLoop()
    {
        //set type as a for loop
        type = InstructionType.FOR_LOOP;
    }

    public LinkedList<Symbol> getInitialization() {
        return initialization;
    }

    public void addInitialization(LinkedList<Symbol> initialization) {
        this.initialization = (LinkedList) initialization.clone();
    }

    public Symbol getEndValue() {
        return endValue;
    }

    public void addEndValue(Symbol endValue) {
        this.endValue = endValue;
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
}
