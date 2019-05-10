package ProgramStructures;

import Compilator.Symbol;
import javafx.util.Pair;

import java.util.LinkedList;

public class Function {
    private LinkedList<Instruction> instructions;
    private String name;
    private Symbol returnedType;
    private LinkedList<Pair<Symbol, String>> params;

    public Function(String name) {
        this.name = name;
        instructions = new LinkedList<>();
        params = new LinkedList<>();
    }

    //set returned type
    public void setReturnedType(Symbol returnedType) {
        this.returnedType = returnedType;
    }

    //add parameter to parameter list
    public void addParams(Pair<Symbol, String> param) {
        params.add(param);
    }

    //get param
    public Pair<Symbol, String> getNextParam() {
        if(params.isEmpty())
            return null;
        return params.removeFirst();
    }

    //add instruction to the list
    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public Instruction getNextInstruction() {
        //if list is empty return null
        if(instructions.isEmpty())
            return null;
        //else return first instruction and remove it from the list
        return instructions.removeFirst();
    }

    public String getName() {
        return name;
    }
}
