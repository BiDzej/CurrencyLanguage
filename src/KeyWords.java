import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KeyWords {
    public static KeyWords instance = null;
    Map<String, SymType> words;
    List<String> symbols;
    long MAX_INT = 2147483647;
    int MAX_IDENT_LENGTH = 20;
    int MAX_STRING_LENGTH = 200;

    public enum SymType{
        //operators
        NOT_OP, OR_OP, AND_OP,
        EOF, CURRENCY_TYPE, IDENT, INT_CONST, FLOAT_CONST, CHAR_CONST, ASSIGNMENT, INT_TYPE, FLOAT_TYPE,
        L_ROUND, R_ROUND, COMMA, COLON, SEMICOLON,
        REL_OP, ADD_OP, MULT_OP,
        BEGIN, END,
        IF_WORD, ELSE_WORD, ELIF_WORD, FOR_WORD, TO_WORD, DO_WORD, WHILE_WORD, FUNCTION_WORD,
        VOID_WORD, SET_WORD, READ_WORD, WRITE_WORD, ERROR, UNKNOWN
    }

    private KeyWords()
    {
        words = new HashMap<>();
        symbols = new LinkedList<>();

        //adding all key words to the list
        words.put("int", SymType.INT_TYPE);       //create int variable
        words.put("float", SymType.FLOAT_TYPE);     //create float variable
        words.put("if", SymType.IF_WORD);        //if statement
        words.put("else", SymType.ELSE_WORD);      //else after if or elif
        words.put("elif", SymType.ELIF_WORD);      //elif = else if
        words.put("for", SymType.FOR_WORD);       //for loop begin
        words.put("to", SymType.TO_WORD);        //for loop condition
        words.put("do", SymType.DO_WORD);        //key word of fo loop
        words.put("while", SymType.WHILE_WORD);     //while loop begin
        words.put("function", SymType.FUNCTION_WORD);  //function create keyword
        words.put("void", SymType.VOID_WORD);      //information that function does not return anything
        words.put("set", SymType.SET_WORD);       //set currency course
        words.put("read", SymType.READ_WORD);      //read from standard input
        words.put("write", SymType.WRITE_WORD);     //write on standard output

        symbols.add("(");   //left bracket
        symbols.add(")");   //right bracket
        symbols.add("!");   //not
        symbols.add("||");  //or with logic expressions
        symbols.add("&&");  //and with logic expressions
        symbols.add("==");  //equal
        symbols.add(">");   //greater
        symbols.add("<");   //lower
        symbols.add(">=");  //greater or equal
        symbols.add("<=");  //lower or equal
        symbols.add("<>");  //different
        symbols.add("+");   //plus
        symbols.add("-");   //minus
        symbols.add("*");   //multiply
        symbols.add("/");   //divide
        symbols.add(".");   //dot for float value
        symbols.add(",");   //semicolon for arguments list
        symbols.add("\n");  //new line, end of command
        symbols.add("{");   //begin brace
        symbols.add("}");   //end brace
        symbols.add("//");  //for comments
        symbols.add(":");   //to set the return type of function
    }

    public static synchronized KeyWords getInstance()
    {
        if(instance == null)
            instance = new KeyWords();
        return instance;
    }

    SymType getByWord(String string)
    {
        return (words.get(string)==null) ? SymType.IDENT : words.get(string);
    }
}
