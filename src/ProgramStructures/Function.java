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

    //get returned type
    public Compilator.Symbol getReturnedType() {
        return returnedType;
    }

    //Add full list of params
    public void addParams(LinkedList<Pair<Symbol,String>> list) {
        params = (LinkedList) list.clone();
    }

    public void addInstructions(LinkedList<Instruction> list) {
        instructions = (LinkedList) list.clone();
    }

    //add parameter to parameter list
    public void addParam(Pair<Symbol, String> param) {
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

    public LinkedList<Instruction> getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }
}
