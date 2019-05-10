package ProgramStructures;

import Compilator.Symbol;

import java.util.LinkedList;

public class LogicExpression {
    LinkedList<Symbol> symbols;

    public LogicExpression() {
        symbols = new LinkedList<>();
    }

    public void addSymbol(Symbol symbol) {
        symbols.add(symbol);
    }

    public Symbol getNextSymbol() {
        if(symbols.isEmpty())
            return null;
        return symbols.removeFirst();
    }
}
