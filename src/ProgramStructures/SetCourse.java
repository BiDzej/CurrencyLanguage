package ProgramStructures;

import Compilator.Symbol;

public class SetCourse extends Instruction {
    Symbol firstCurrency;
    Symbol secondCurrency;
    Symbol course;

    public SetCourse(Symbol firstCurrency, Symbol secondCurrency, Symbol course) {
        type = InstructionType.SET_COURSE;
        this.firstCurrency = firstCurrency;
        this.secondCurrency = secondCurrency;
        this.course = course;
    }

    public Symbol getFirstCurrency() {
        return firstCurrency;
    }

    public Symbol getSecondCurrency() {
        return secondCurrency;
    }

    public Symbol getCourse() {
        return course;
    }
}
