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
        generateMain();
    }

    //write all includes which are necessary to let the code work properly
    private void generateIncludes() {
        writer.write("#include <iostream>\n", incision);
        writer.write("#include \"Wallet.h\"\n\n", incision);
    }

    //generate in loop all instructions
    private void generate(LinkedList<Instruction> list, String funcName) {
        if(list.isEmpty())
            return;
        do {
            Instruction tmp = list.removeFirst();
            switch (tmp.getInstructionType()) {
                case FOR_LOOP:      generate((ForLoop)tmp, funcName);
                                    break;
                case WHILE_LOOP:    generate((WhileLoop)tmp, funcName);
                                    break;
                case IF:            generate((IfExpression)tmp, funcName);
                                    break;
                case EXPRESSION:    generate((Expression)tmp, funcName);
                                    break;
                case SET_COURSE:    generate((SetCourse)tmp);
                                    break;
            }
        } while (!list.isEmpty());
    }

    //generate for loop
    private void generate(ForLoop loop, String funcName) {
        //for word
        writer.write("for ( ", incision);
        LinkedList<Symbol> initialization = loop.getInitialization();
        //it's the symbol type
        writer.write(initialization.removeFirst().getText() + " ", 0);
        //it's the variable name
        Symbol variableName = initialization.removeFirst();
        writer.write(variableName.getText(), 0);
        while(!initialization.isEmpty()) {
            Symbol tmp = initialization.removeFirst();
            if(!tmp.getText().equals(funcName))
                writer.write(tmp.getText(), 0);
            else {
                Symbol tmp2 = initialization.removeFirst();
                if(tmp2==null || tmp2.getType()!= KeyWords.SymType.L_ROUND)
                    writer.write(tmp.getText() + "Res", 0);
                else
                    writer.write(tmp.getText(), 0);
                if(tmp2!=null)
                    writer.write(" " + tmp2.getText(), 0);
            }
            writer.write(" ", 0);
        }
        //end of for loop
        writer.write("; " + variableName.getText() + " <= " + loop.getEndValue().getText() + "; ++" + variableName.getText() + " ) { \n", 0);
        ++incision;
        //generate instructions inside the loop
        generate(loop.getInstructions(), funcName);
        --incision;
        writer.write("}\n", incision);
    }

    //generate while loop
    private void generate(WhileLoop loop, String funcName) {
        //while word
        writer.write("while", incision);
        //generate condition
        generate(loop.getLogicExpression(), funcName);
        writer.write(" {\n", 0);
        ++incision;
        //generate instructions
        generate(loop.getInstructions(), funcName);
        //end the loop
        --incision;
        writer.write("}\n", incision);
    }

    //generate logic expression
    private void generate(LogicExpression log, String funcName) {
        Symbol simpleLog = log.getNextSymbol();
        //simply rewrite the symbols of log expression
        while(simpleLog!=null) {
            if(simpleLog.getText().equals(funcName))
            {
                Symbol tmp = log.getNextSymbol();
                if(tmp==null || tmp.getType()!= KeyWords.SymType.L_ROUND) {
                    writer.write(simpleLog.getText()+"Res", 0);
                }
                else
                    writer.write(tmp.getText(), 0);
                if(tmp!=null)
                    writer.write(" " + tmp.getText(), 0);
            }
            else
                writer.write(simpleLog.getText(), 0);
            simpleLog = log.getNextSymbol();
            if(simpleLog!=null)
                writer.write(" ", 0);
        }
    }

    //generate if statement
    private void generate(IfExpression ifExpr, String funcName) {
        writer.write("if ", incision);
        //generate condition
        generate(ifExpr.getCondition(), funcName);

        writer.write(" {\n", 0);
        ++incision;

        //generate all instructions
        generate(ifExpr.getInstructions(), funcName);
        writer.write("}\n", --incision);

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
            generate(tmp.getKey(), funcName);
            writer.write(" {\n", 0);
            ++incision;
            generate(tmp.getValue(), funcName);
            --incision;
            writer.write("}\n", incision);
            tmp = ifExpr.getNextElseIf();
        }

        //generate else
        if(!ifExpr.getElseInstructions().isEmpty()) {
            writer.write("else {\n", incision);
            ++incision;
            generate(ifExpr.getElseInstructions(), funcName);
            --incision;
            writer.write("}\n", incision);
        }
    }

    //generate expression
    private void generate(Expression exp, String funcName) {
        Symbol tmp = exp.getNextSymbol();
        //if it's write word
        if(tmp.getType()== KeyWords.SymType.WRITE_WORD) {
            tmp = exp.getNextSymbol();
            if(tmp.getText().equals(funcName))
                tmp.setText(tmp.getText()+"Res");
            writer.write("std::cout << " + tmp.getText() + " << std::endl;\n", incision);
            return;
        }
        if(tmp.getType()== KeyWords.SymType.READ_WORD) {
            tmp = exp.getNextSymbol();
            if(tmp.getText().equals(funcName))
                tmp.setText(tmp.getText()+"Res");
            writer.write("std::cin >> " + tmp.getText() + " ;\n", incision);
            return;
        }

        if(tmp.getType()== KeyWords.SymType.CURRENCY_TYPE) {
            Symbol type = tmp;
            Symbol name = exp.getNextSymbol();
            writer.write("Wallet " + name.getText() + "(\"" + type.getText() + "\", ", incision);
            tmp = exp.getNextSymbol();
            if(tmp==null)
                writer.write("0); \n", 0);
            else if(tmp.getType()== KeyWords.SymType.ASSIGNMENT) {
                tmp = exp.getNextSymbol();
                generateRestExpression(exp, funcName, tmp);
                writer.write(");\n", 0);
            }
        } else {
            //just to move a cursor in new line, new expression is new line
            writer.write("", incision);
            generateRestExpression(exp, funcName, tmp);
            writer.write(";\n", 0);
        }
    }

    private void generateRestExpression(Expression exp, String funcName, Symbol tmp) {
        while(tmp!=null) {
            if(tmp.getType()== KeyWords.SymType.FLOAT_CONST) {
                writer.write(tmp.getText() + "f ", 0);
                tmp = exp.getNextSymbol();
                continue;
            }
            if(tmp.getText().equals(funcName)){
                Symbol tmp1 = exp.getNextSymbol();
                if(tmp1.getType()== KeyWords.SymType.L_ROUND)
                    writer.write(tmp.getText() + " " + tmp1.getText(), 0);
                else writer.write(tmp.getText() + "Res " + tmp1.getText(), 0);
            }
            else
                writer.write(tmp.getText(), 0);
            tmp = exp.getNextSymbol();
            if(tmp!=null)
                writer.write(" ", 0);
        }
    }

    //generate set course
    private void generate(SetCourse course) {
        writer.write("CurrenciesLibrary::getInstance().setCourse(\"", incision);
        writer.write(course.getFirstCurrency().getText() + "\", \"", 0);
        writer.write(course.getSecondCurrency().getText() + "\", ", 0);
        writer.write(course.getCourse().getText() + ");\n", 0);
    }

    //generate main function
    private void generateMain() {
        //generate function header
        writer.write("int main( ) {\n", incision);
        ++incision;

        //generate all instructions in main
        generate(main.getInstructions(), "");

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
            writer.write(function.getReturnedType().getText() + " " + function.getName() + "Res;\n", incision);

        //now generate all instructions
        generate(function.getInstructions(), function.getName());

        //end the function
        if(function.getReturnedType().getType()!= KeyWords.SymType.VOID_WORD)
            writer.write("return " + function.getName() + "Res;\n", incision);
        --incision;
        writer.write("}\n\n\n", incision);

    }
}
