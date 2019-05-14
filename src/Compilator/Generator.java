package Compilator;

import ProgramStructures.*;
import javafx.util.Pair;

import java.util.LinkedList;


public class Generator {

    private Writer writer;
    private Function main;
    private int incision;

    public Generator(Writer writer) {
        this.writer = writer;
        incision = 0;
    }

    public void generate(Program program) {
        //first of all, generate all includes
        generateIncludes();

        //take one function after another and generate code from every single function
        //after all functions, generate main function
        Function function = program.getNextFunction();
        while(function != null) {
            if (function.getName().equals("main")) {
                main = function;
            }
            else generate(function);

            function = program.getNextFunction();
        }

        //now generate main
        //TODO when there is only main, null pointer exception ? :c
        generateMain();
    }

    //TODO add all of needed libraries
    //write all includes which are necessary to let the code work properly
    private void generateIncludes() {
        writer.write("#include <iostream>\n", incision);
        writer.write("#include \"Currencies.h\"\n", incision);
    }

    //generate in loop all instructions
    private void generate(LinkedList<Instruction> list) {
        if(list.isEmpty())
            return;
        do {
            Instruction tmp = list.removeFirst();
            switch (tmp.getInstructionType()) {
                case FOR_LOOP:      generate((ForLoop)tmp);
                                    break;
                case WHILE_LOOP:    generate((WhileLoop)tmp);
                                    break;
                case IF:            generate((IfExpression)tmp);
                                    break;
                case EXPRESSION:    generate((Expression)tmp);
                                    break;
                case SET_COURSE:    generate((SetCourse)tmp);
                                    break;
            }
        } while (!list.isEmpty());
    }

    //TODO generate for loop
    //generate for loop
    private void generate(ForLoop loop) {

    }

    //TODO generate while loop
    //generate while loop
    private void generate(WhileLoop loop) {

    }

    //generate logic expression
    private void generate(LogicExpression log) {
        Symbol simpleLog = log.getNextSymbol();
        //simply rewrite the symbols of log expression
        while(simpleLog!=null) {
            writer.write(simpleLog.getText(), 0);
            simpleLog = log.getNextSymbol();
        }
    }

    //generate if statement
    private void generate(IfExpression ifExpr) {
        writer.write("if ", incision);
        //generate condition
        generate(ifExpr.getCondition());

        writer.write(" {\n", 0);
        ++incision;

        //generate all instructions
        generate(ifExpr.getInstructions());
        writer.write("}\n", incision);

        --incision;

        //get first else if
        Pair<LogicExpression, LinkedList<Instruction>> tmp = ifExpr.getNextElseIf();
        //generate all else ifs
        while (tmp != null) {
            //if it's empty do not generate it (for what to do it)?
            if(tmp.getValue().isEmpty()) {
                tmp = ifExpr.getNextElseIf();
                continue;
            }
            writer.write("else if", incision);
            //generate condition
            generate(tmp.getKey());
            writer.write(") {\n", 0);
            ++incision;
            generate(tmp.getValue());
            --incision;
            writer.write("}\n", incision);
            tmp = ifExpr.getNextElseIf();
        }

        //generate else
        if(!ifExpr.getElseInstructions().isEmpty()) {
            writer.write("else {\n", incision);
            ++incision;
            generate(ifExpr.getElseInstructions());
            --incision;
            writer.write("}\n", incision);
        }
    }

    //generate expression
    private void generate(Expression exp) {
        Symbol tmp = exp.getNextSymbol();
        //just to move a cursor in new line, new expression is new line
        writer.write("", incision);
        while(tmp!=null) {
            writer.write(tmp.getText() + " ", 0);
            tmp = exp.getNextSymbol();
        }
        writer.write(";\n", 0);
    }

    //TODO generate set course
    //TODO write before it all classes in C++
    //generate set course
    private void generate(SetCourse course) {

    }

    //generate main function
    private void generateMain() {
        //generate function header
        writer.write("int main( ) {\n", incision);
        ++incision;

        //generate all instructions in main
        generate(main.getInstructions());

        //generate function end
        writer.write("return 0;\n", incision);
        --incision;
        writer.write("}", incision);
    }

    private void generate(Function function) {
        //write returned type
        writer.write(function.getReturnedType().getText() + " ", incision);
        //write function name
        writer.write(function.getName() + " (", incision);
        //write all params list
        Pair<Symbol, String> tmp = function.getNextParam();
        while(tmp!=null) {
            writer.write(tmp.getKey().getText() + " " + tmp.getValue(), incision);
            tmp = function.getNextParam();
            if(tmp!=null)
                writer.write(", ", incision);
        }
        //end params list and write bracet for function body
        writer.write(") {\n", incision);
        ++incision;

        //write the return value
        if(function.getReturnedType().getType()!= KeyWords.SymType.VOID_WORD)
            writer.write(function.getReturnedType().getText() + " " + function.getName() + ";\n", incision);

        //now generate all instructions
        generate(function.getInstructions());

        //end the function
        if(function.getReturnedType().getType()!= KeyWords.SymType.VOID_WORD)
            writer.write("return " + function.getName() + ";\n", incision);
        --incision;
        writer.write("}\n\n\n", incision);

    }
}
