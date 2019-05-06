import java.util.ArrayList;
import java.util.List;

public class Parser {
    List<Symbol> tokens;
    Symbol lookAhead;
    MyScanner scanner;
    Writer writer;
    int currentLevel = 0;       //Lvl = 0 means global variables.
    VariablesAndFunctions collection;
    ArrayList<String> functionParams;

    public Parser(MyScanner myScanner) throws Exception {
        scanner = myScanner;
        writer = Writer.getInstance();
        tokens = new ArrayList<>();
        collection = new VariablesAndFunctions();
        collection.createVariablesLevel(currentLevel);
        functionParams = new ArrayList<>();
    }

    private void getNextSymbol() throws Exception {
        Symbol tmp = scanner.nextSymbol();

        if(tmp.getType()==KeyWords.SymType.ERROR ||
                tmp.getType()==KeyWords.SymType.UNKNOWN )
            throw new Exception("Some errors found in the code.");
        if(tmp.getType()==KeyWords.SymType.EOF && currentLevel != 0)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Unexpected end of source file.");
            throw new Exception("Unexpected end of file.");
        }

        tokens.add(lookAhead);
        lookAhead = tmp;
        writer.write(tmp.getText()+"  ");
        System.out.print(tmp.getType() + "  ");
    }


    //main loop of the parser, checks all the symbols received from scanner
    public void program() throws Exception {
        getNextSymbol();

        //while it's not a program end, check if it's instruction block
        //or functionDef
        while(lookAhead.getType() != KeyWords.SymType.EOF)
        {
            //if it's a key word function we kno it's a function definition
            if(lookAhead.getType()==KeyWords.SymType.FUNCTION_WORD)
                functionDef();
            //else it can be instruction block or error occurs
            else
                instructionBlock();
        }
    }

    private void instructionBlock() throws Exception {
        //checks by first word is it statement if block, while block or for loop
        switch (lookAhead.getType())
        {
            //case if it's if statement
            case IF_WORD:       getNextSymbol();
                                ifStatement();
                                break;
            //case for it's for loop
            case FOR_WORD:      getNextSymbol();
                                forExpression();
                                break;
            //case while word it's while loop
            case WHILE_WORD:    getNextSymbol();
                                whileExpression();
                                break;
            //if it's not any of those it can be statement or default
            default:            statement();
                                if(lookAhead.getType()!=KeyWords.SymType.SEMICOLON)
                                {
                                    writer.error("//Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". ';' expected.");
                                    throw new Exception("';' expected.");
                                }
                                getNextSymbol();
                                break;
        }
    }

    private void functionCall(Symbol name) throws Exception {
        //first should be function name
        if(!collection.functionExist(name.getText()))
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Function does not exist.");
            throw new Exception("Function does not exist.");
        }
        //now there should be '('
        if(lookAhead.getType()!=KeyWords.SymType.L_ROUND) {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". '(' expected.");
            throw new Exception("')' expected.");
        }
        //get next symbol
        getNextSymbol();

        //loop
        ArrayList<String> params = new ArrayList<>();
        while(lookAhead.getType() != KeyWords.SymType.R_ROUND)
        {
            switch (lookAhead.getType()) {
                case INT_CONST:     params.add("int");
                    getNextSymbol();
                    break;
                case FLOAT_CONST:   params.add("float");
                    getNextSymbol();
                    break;
                case IDENT:         if(!variableName())
                                    {
                                        writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Variable does not exist.");
                                        throw new Exception("Variable does not exist.");
                                    }
                                    params.add(collection.getVariableType(lookAhead.getText(), currentLevel));
                                    getNextSymbol();
                                    break;
                default:            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Unexpected symbol: " + lookAhead.getType() + ".");
                    throw new Exception("Unexpected symbol.");
            }
            //now it has to be coma or ')'
            if(lookAhead.getType()!=KeyWords.SymType.R_ROUND && lookAhead.getType()!=KeyWords.SymType.COMMA) {
                writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Expected ')' or ','.");
                throw new Exception("Expected ')' or ','.");
            }
            //if it's comma, next cannot be ')'
            if(lookAhead.getType()==KeyWords.SymType.COMMA) {
                getNextSymbol();
                if(lookAhead.getType()==KeyWords.SymType.R_ROUND) {
                    writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Unexpected ')' after ','.");
                    throw new Exception("')' expected after ','.");
                }
            }
        }
        if(!collection.functionWithParamsExist(name.getText(), params))
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". There is no function with this params list.");
            throw new Exception("Function does not exist.");
        }
        //get next symbol
        getNextSymbol();
    }

    //checks if function definition is correct
    private void functionDef() throws Exception {
        //we know for sure that first word is function so we need to check rest of definition
        getNextSymbol();
        if(lookAhead.getType()!=KeyWords.SymType.IDENT) {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Expected function name (ident).");
            throw new Exception("Function name expected after 'function' key word.");
        }
        //check if this function name is not in use
        if(collection.functionExist(lookAhead.getText()))
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". This function name already exists.");
            throw new Exception("Function name already exists.");
        }

        //save the function name
        Symbol name = lookAhead;

        //now we check if there is '('
        getNextSymbol();
        if(lookAhead.getType() != KeyWords.SymType.L_ROUND) {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Expected '(' after function name.");
            throw new Exception("Symbol '(' expected after function name.");
        }

        //create new level of variables
        collection.createVariablesLevel(++currentLevel);

        //check if we have any list of attributes
        getNextSymbol();
        if(lookAhead.getType()!= KeyWords.SymType.R_ROUND)
            //check attribute list
            attributeList();

        //now we have attribute list so we can add function
        collection.addFunction(name.getText(), functionParams);
        functionParams.clear();

        //here we should for sure have ')', if not there'a an error
        if(lookAhead.getType()!=KeyWords.SymType.R_ROUND) {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Expected ')' after parameters list.");
            throw new Exception("Symbol ')' expected after parameters list.");
        }

        //next step is :
        getNextSymbol();
        if(lookAhead.getType()!=KeyWords.SymType.COLON) {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Expected ':'.");
            throw new Exception("Symbol ':' expected.");
        }

        //now we need the return type
        getNextSymbol();
        //if it's not a void word, check is it a correct variable type
        if(lookAhead.getType()!=KeyWords.SymType.VOID_WORD)
            //if it's not a void name we should check if it's any
            if(!variableType())
            {
                //if it's not any of known variable types throw exception
                writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". " + lookAhead.getText() + " is not any of known variable types.");
                throw new Exception("Unknown variable type.");
            }
            else
            {
                //we should add variable of this type and name like function
                collection.addVariable(name.getText(), lookAhead.getText(), currentLevel);
            }

        //check if it has correct begin and end braces
        getNextSymbol();

        //beginEndBraces
        if(lookAhead.getType()!=KeyWords.SymType.BEGIN)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". '{' expected.");
            throw new Exception("'{' expected.");
        }

        //get next symbol
        getNextSymbol();
        //while next symbol is not } check if it's instruction block
        while(lookAhead.getType()!=KeyWords.SymType.END)
            instructionBlock();

        //decrement current level and destroy it
        collection.deleteVariablesLevel(currentLevel--);

        //we are sure that next symbol is '}' so we can get next symbol
        getNextSymbol();

        //now it's the end of function body so we finished checking it
    }

    private boolean variableType() throws Exception {
        //if it's any of known
        if( lookAhead.getType()==KeyWords.SymType.INT_TYPE ||
            lookAhead.getType()==KeyWords.SymType.FLOAT_TYPE)
            return true;
        //if it's not a int or float, check if it's any known currency type
        if(lookAhead.getType()!=KeyWords.SymType.CURRENCY_TYPE)
            return false;
        //if it's currency type, check if it's in database
        currencyType();
        return true;
    }

    private void currencyType() throws Exception {
        //checks if the currency type exists in database
        if(!collection.doesCurrencyTypeExist(lookAhead.getText()))
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". " + lookAhead.getText() + " is not a known currency type.");
            throw new Exception("Unknown currency.");
        }
    }

    private void attributeList() throws Exception {
        //while we have coma check one more time if it's attribute
        attribute();
        while (lookAhead.getType()==KeyWords.SymType.COMMA)
        {
            //this symbol is a comma, we need next one
            getNextSymbol();
            //check attribute if it's correct
            attribute();
        }
    }

    private void attribute() throws Exception {
        //first should be variable type
        Symbol type = lookAhead;
        if(!variableType())
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Variable type expected.");
            throw new Exception("Variable type expected.");
        }
        //next should be variable name
        getNextSymbol();
        if(lookAhead.getType()!=KeyWords.SymType.IDENT)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Variable name expected.");
            throw new Exception("Variable name expected.");
        }
        //check the variable name, if it's exist throw exception
        if(variableName())
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". This variable nam already exist.");
            throw new Exception("Variable name already exist.");
        }
        //if it does not exist add to database
        collection.addVariable(lookAhead.getText(), type.getText(), currentLevel);
        functionParams.add(type.getText());
        //get next symbol
        getNextSymbol();
    }

    private boolean variableName()
    {
        //checks if variable is visible from that point of code
        //if it's visible returns true else returns false
        if(collection.getVariableType(lookAhead.getText(), currentLevel)==null)
            return false;
        return true;
    }

    private void statement() throws Exception {
        switch (lookAhead.getType())
        {
            case READ_WORD:     getNextSymbol();
                                readValue();
                                break;
            case WRITE_WORD:    getNextSymbol();
                                writeValue();
                                break;
            case SET_WORD:      getNextSymbol();
                                setCourse();
                                break;
            //here it can be assignment/functionCall/
            default:            if(variableType())
                                    assingmentOrAttribute();
                                //else if it's not an ident throw exception
                                else if(lookAhead.getType()!=KeyWords.SymType.IDENT) {
                                    writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Unexpected token.");
                                    throw new Exception("Unexpected token.");
                                }
                                //else it can be function call or assingment
                                else {
                                    CursorPosition cp = scanner.getPosition();
                                    Symbol tmp = lookAhead;
                                    getNextSymbol();
                                    if(lookAhead.getType()== KeyWords.SymType.L_ROUND)
                                        functionCall(tmp);
                                    //else it can be only assingment
                                    else {
                                        if(collection.getVariableType(tmp.getText(), currentLevel)==null)
                                        {
                                            writer.error("//[P] Error in line: " + cp.line + " at char: " + cp.sign + ". Unknown variable name: " + tmp.getText() + ".");
                                            throw new Exception("Unknown variable name.");
                                        }
                                        assignment(tmp);
                                    }
                                }
        }
    }

    private void assingmentOrAttribute() throws Exception {
        Symbol type = lookAhead;
        //get next symbol
        getNextSymbol();
        //it has to be an ident
        if(lookAhead.getType()!=KeyWords.SymType.IDENT)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Variable name expected.");
            throw new Exception("Variable name already exist.");
        }
        if(variableName())
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". This variable name already exist.");
            throw new Exception("Variable name already exist.");
        }
        //if does not exist add to collection
        Symbol name = lookAhead;
        collection.addVariable(name.getText(), type.getText(), currentLevel);

        //get next symbol
        getNextSymbol();
        //it can be assignment
        if(lookAhead.getType() == KeyWords.SymType.ASSIGNMENT)
            assignment(type, name);
        //else it can only be an attribute so everything different than ';' is an error
        else if(lookAhead.getType() != KeyWords.SymType.SEMICOLON)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". ';' expected.");
            throw new Exception("';' expected.");
        }
    }

    private void readValue() throws Exception {
        //now we should have variable name
        if(!variableName())
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Variable name expected.");
            throw new Exception("Variable name expected.");
        }
        getNextSymbol();
    }

    private void writeValue() throws Exception {
        //now we should have variable name or string const
        if(lookAhead.getType()!=KeyWords.SymType.CHAR_CONST)
            if(!variableName())
            {
                writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Variable name or char const expected.");
                throw new Exception("Variable name expected.");
            }
        getNextSymbol();
    }

    private void setCourse() throws Exception {
        //check if it's currency type one of known
        //if it does not exist create it
        if(lookAhead.getType()!=KeyWords.SymType.CURRENCY_TYPE)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Currency type expected.");
            throw new Exception("Currency type expected.");
        }
        //if does not exist, add to collection
        if(!collection.doesCurrencyTypeExist(lookAhead.getText()))
            collection.addCurrencyType(lookAhead.getText());

        Symbol tmp = lookAhead;

        //get next symbol
        getNextSymbol();
        //check if it's ':'
        if(lookAhead.getType()!=KeyWords.SymType.COLON)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". ':' expected.");
            throw new Exception("':' expected.");
        }
        //get next symbol
        getNextSymbol();
        //check if it's currency type
        if(lookAhead.getType()!=KeyWords.SymType.CURRENCY_TYPE)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Currency type expected.");
            throw new Exception("Currency type expected.");
        }
        currencyType();
        //check if both currency types are different
        if(lookAhead.getText().equals(tmp.getText()))
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Currencys types has to be different.");
            throw new Exception("The same currency type.");
        }
        //get next symbol
        getNextSymbol();
        //check if it's '=' symbol
        if(lookAhead.getType()!=KeyWords.SymType.ASSIGNMENT)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". '=' expected.");
            throw new Exception("'=' expected.");
        }
        //get next symbol
        getNextSymbol();
        if(lookAhead.getType()!=KeyWords.SymType.FLOAT_CONST)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Float const expected.");
            throw new Exception("Float const expected.");
        }
        //get next symbol to allow further checking
        getNextSymbol();

    }

    private void ifStatement() throws Exception {
        ifExpression();
        //now we have first symbol after if expression
        while(lookAhead.getType()==KeyWords.SymType.ELIF_WORD)
        {
            //get next symbol (first after elif key word)
            getNextSymbol();
            //check elif expression which is the same as if expression
            ifExpression();
        }
        //now we have first sign after all elif expressions
        if(lookAhead.getType()==KeyWords.SymType.ELSE_WORD) {
            //get first word after else key word
            getNextSymbol();
            //check else expression
            elseExpression();
        }
    }

    private void elseExpression() throws Exception {
        //else expression is simple a begin and end braces
        beginEndBraces();
    }

    private void ifExpression() throws Exception {
        //logic expression
        logicExpression();
        //then if body
        beginEndBraces();
    }

    private void beginEndBraces() throws Exception {
        if(lookAhead.getType()!=KeyWords.SymType.BEGIN)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". '{' expected.");
            throw new Exception("'{' expected.");
        }
        //increment current level and create it in collection
        collection.createVariablesLevel(++currentLevel);

        //get next symbol
        getNextSymbol();
        //while next symbol is not } check if it's instruction block
        while(lookAhead.getType()!=KeyWords.SymType.END)
            instructionBlock();

        //decrement current level and destroy it
        collection.deleteVariablesLevel(currentLevel--);

        //we are sure that next symbol is '}' so we can get next symbol
        getNextSymbol();
    }

    private void logicExpression() throws Exception {
        //first has to be '('
        if(lookAhead.getType()!=KeyWords.SymType.L_ROUND) {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". '=' expected.");
            throw new Exception("'=' expected.");
        }
        //get next symbol
        getNextSymbol();
        //now it has to be at least one simpleLogExpression
        simpleLogExpression();
        //while there are more simple expressions check them
        while(lookAhead.getType()==KeyWords.SymType.OR_OP) {
            getNextSymbol();
            simpleLogExpression();
        }
        //at the end there should be ')'
        if(lookAhead.getType()!=KeyWords.SymType.R_ROUND) {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". ')' expected.");
            throw new Exception("')' expected.");
        }
        //get next symbol after that
        getNextSymbol();
    }

    private void simpleLogExpression() throws Exception {
        if(lookAhead.getType()==KeyWords.SymType.L_ROUND) {
            //get next symbol
            getNextSymbol();
            //check termlog
            termLog();
            while(lookAhead.getType()==KeyWords.SymType.AND_OP) {
                //get next symbol
                getNextSymbol();
                //check termLog
                termLog();
            }
            //now there should be ')'
            if(lookAhead.getType()!=KeyWords.SymType.R_ROUND) {
                writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". ')' expected.");
                throw new Exception("')' expected.");
            }
            //get next symbol and end function
            getNextSymbol();
        }
        else
            //else there can be only one termlog nothing more
            termLog();
    }

    private void termLog() throws Exception {
        //it can be not expression
        if(lookAhead.getType()==KeyWords.SymType.NOT_OP) {
            //get next symbol
            getNextSymbol();
            //it has to be '('
            if(lookAhead.getType()!=KeyWords.SymType.L_ROUND) {
                writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". '(' expected after '!'.");
                throw new Exception("'(' expected after '!'.");
            }
            //get next symbol and check expression
            getNextSymbol();
            expression();
            //now it has to be ')'
            if(lookAhead.getType()!=KeyWords.SymType.R_ROUND) {
                writer.error("//Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". ')' expected.");
                throw new Exception("')' expected.");
            }
            //get next symbol and return
            getNextSymbol();
        }
        else
            //else it can only be an expression
            expression();
    }

    private void forExpression() throws Exception {
        //we are already after key word for
        //it has to be int value!!!
        if(lookAhead.getType()!=KeyWords.SymType.INT_TYPE)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". int type variable required.");
            throw new Exception("int type variable required.");
        }
        //check assignment
        assignment();

        //now we are on the first symbol after assignment
        //it has to be 'to' keyword
        if(lookAhead.getType()!=KeyWords.SymType.TO_WORD)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". 'to' keyword expected.");
            throw new Exception("'to' keyword expected.");
        }

        //get next symbol, first after 'to' keyword
        getNextSymbol();

        //now it should be int const value
        if(lookAhead.getType()!=KeyWords.SymType.INT_CONST)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Int const value expected.");
            throw new Exception("int const value expected.");
        }

        //get next symbol
        getNextSymbol();

        //do keyword expected
        if(lookAhead.getType()!=KeyWords.SymType.DO_WORD)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". 'do' keyword expected.");
            throw new Exception("'do' keyword expected.");
        }
        //get next symbol
        getNextSymbol();
        //now we should have begin end braces
        beginEndBraces();
    }

    private void newVariable() throws Exception {
        //we are on the first symbol now, check if it's a known variable type
        if( variableType())
        {
            //we need to take next symbol
            getNextSymbol();
            //it it's a variable name check in database if it's exist
            if(variableName())
            {
                writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Variable: " + lookAhead.getText() + " is in use.");
                throw new Exception("Variable name in use.");
            }
            else
            {
                //else add this variable to
                collection.addVariable(lookAhead.getText(), tokens.get(tokens.size()-1).getText(), currentLevel);
            }
        }
    }

    private void assignment(Symbol type, Symbol name) throws Exception {
        //we have now next symbol after variable name, check if it's '='
        if(lookAhead.getType()!=KeyWords.SymType.ASSIGNMENT)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". '=' expected.");
            throw new Exception("'=' expected.");
        }

        //get next symbol
        getNextSymbol();
        //now it should be simple expression
        simpleExpression();
        //we are on the last symbol after expression so do nothing
    }

    private void assignment(Symbol name) throws Exception {
        //we have now next symbol after variable name, check if it's '='
        if(lookAhead.getType()!=KeyWords.SymType.ASSIGNMENT)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". '=' expected.");
            throw new Exception("'=' expected.");
        }

        //get next symbol
        getNextSymbol();
        //now it should be expression
        expression();
        //we are on the last symbol after expression so do nothing
    }

    private void assignment() throws Exception {
        if(variableType())
            newVariable();
        else
        {
            if(!variableName())
            {
                writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Unknown variable name: " + lookAhead.getText() + ".");
                throw new Exception("Unknown variable name.");
            }
        }

        //get next symbol and check if it's '='
        getNextSymbol();
        if(lookAhead.getType()!=KeyWords.SymType.ASSIGNMENT)
        {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". '=' expected.");
            throw new Exception("'=' expected.");
        }

        //get next symbol
        getNextSymbol();
        //now it should be simple expression
        simpleExpression();
        //we are on the last symbol after expression so do nothing
    }

    private void expression() throws Exception {
        simpleExpression();
        while(lookAhead.getType()==KeyWords.SymType.REL_OP) {
            getNextSymbol();
            simpleExpression();
        }
    }

    private void simpleExpression() throws Exception {
        term();
        while(lookAhead.getType()==KeyWords.SymType.ADD_OP) {
            getNextSymbol();
            term();
        }
    }

    private void term() throws Exception {
        factor();
        while(lookAhead.getType()==KeyWords.SymType.MULT_OP)
        {
            getNextSymbol();
            factor();
        }
    }

    private void factor() throws Exception {
        if(lookAhead.getType()==KeyWords.SymType.L_ROUND) {
            //get next symbol and check expression
            getNextSymbol();
            //TODO chyba tak nie powinno być ze expression
            expression();
            if(lookAhead.getType()!=KeyWords.SymType.R_ROUND) {
                writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". ')' expected.");
                throw new Exception("')' expected.");
            }
            //after checking get next symbol
            getNextSymbol();
        }
        else if(lookAhead.getType()==KeyWords.SymType.FLOAT_CONST) {
            //it's const
            //after checking get next symbol
            getNextSymbol();
        }
        else if(lookAhead.getType()==KeyWords.SymType.INT_CONST) {
            //it's const
            //after checking get next symbol
            getNextSymbol();
        }
        else if(lookAhead.getType()==KeyWords.SymType.IDENT) {
            //tmp value
            Symbol tmp = lookAhead;
            //getNext symbol
            getNextSymbol();
            //check if it's a function call
            if(lookAhead.getType()==KeyWords.SymType.L_ROUND)
                //it's function call
                functionCall(tmp);
            else {
                //it's variable name
                if(collection.getVariableType(tmp.getText(), currentLevel)==null)
                {
                    writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Variable name unknown.");
                    throw new Exception("Variable name unknown.");
                }
            }
        }
        else {
            writer.error("//[P] Error in line: " + scanner.getPosition().line + " at char: " + scanner.getPosition().sign + ". Unexpected symbol.");
            throw new Exception("Unexpected symbol.");
        }
    }

    private void whileExpression() throws Exception {
        //we are at first symbol after while keyword
        //so check logic expression
        logicExpression();

        //we are at first char after logic expression
        //so check begin end braces
        beginEndBraces();
    }
}