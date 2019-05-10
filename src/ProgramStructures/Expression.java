package ProgramStructures;

import Compilator.Symbol;

import java.util.LinkedList;

public class Expression extends Instruction {
    LinkedList<Symbol> expression;

    public Expression() {
        type = InstructionType.EXPRESSION;
        expression = new LinkedList<>();
    }

    public void addSymbol(Symbol symbol) {
        expression.add(symbol);
    }

    public Symbol getNextSymbol() {
        if(expression.isEmpty())
            return null;
        return expression.removeFirst();
    }
}
