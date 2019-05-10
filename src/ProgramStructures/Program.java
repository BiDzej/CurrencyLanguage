package ProgramStructures;

import java.util.LinkedList;

public class Program {
    //program is an ArrayList of Functions
    LinkedList<Function> functions;

    public Program() {
        functions = new LinkedList<>();
    }

    public void addFunction(Function function) {
        functions.add(function);
    }

    public Function getNextFunction() {
        //if list is empty, no more funcitons to checl
        if(functions.isEmpty())
            return null;

        //if not return first element and remove it from functions list
        return functions.removeFirst();
    }
}
