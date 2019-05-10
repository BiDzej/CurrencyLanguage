package ProgramStructures;

public class Instruction {

    public enum InstructionType {
        EXPRESSION, FOR_LOOP, WHILE_LOOP, IF, SET_COURSE
    }

    //defines what's the instruction
    protected InstructionType type;

    public InstructionType getInstructionType() {
        return type;
    }
}
