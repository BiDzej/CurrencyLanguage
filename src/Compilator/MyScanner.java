package Compilator;

public class MyScanner {
    //private object reader to read from source file
    private Reader reader;
    private Writer writer;
    private KeyWords keyWords;
    private int nextChar;
    private StringBuilder stringBuilder;

    //public constructor
    public MyScanner (String sourcePath)
    {
        reader = new Reader(sourcePath);
        writer = Writer.getInstance();
        keyWords = KeyWords.getInstance();
        stringBuilder = new StringBuilder();
        nextChar = reader.nextChar();
    }

    public Symbol nextSymbol()
    {
        do
        {
            //skip all spaces and tabs
            while(nextChar==' ' || nextChar=='\t' || nextChar=='\n')
                nextChar = reader.nextChar();
            //if it's file end, return file end
            if(nextChar==-1)
                return new Symbol(KeyWords.SymType.EOF);

            //checking if comments or divide sign
            if(nextChar=='/')
            {
                nextChar = reader.nextChar();
                if(nextChar=='/') {
                    do
                    {
                        nextChar = reader.nextChar();
                    }
                    while (nextChar != '\n');
                }
                else return new Symbol(KeyWords.SymType.MULT_OP, "/");
            }
        } while (isWhiteSign(nextChar) || nextChar=='/');

        //now it will be something important or errors
        CursorPosition cp = reader.getPosition();

        if(isGreatLetter(nextChar))
        {
            //clean the string
            stringBuilder.setLength(0);
            stringBuilder.append((char)nextChar);
            //we have one great letter, should be 3 great letters
            for(int i = 0; i < 2; ++i)
            {
                nextChar = reader.nextChar();
                //if second and third letter are not both great letters
                if(!isGreatLetter(nextChar))
                {
                    writer.error("//[S] Error in line: " + cp.line + " at char: " + cp.sign + ". Variable should start with a small letter.");
                    return new Symbol (KeyWords.SymType.ERROR);
                }
                stringBuilder.append((char)nextChar);
            }
            //get next sign after currency type
            nextChar = reader.nextChar();
            //if there are more signs after that 3 letters error
            if(!isWhiteSign(nextChar))
            {
                writer.error("//[S] Error in line: " + cp.line + " at char: " + (cp.sign) + ". Currency type should have only 3 great letters.");
                return new Symbol(KeyWords.SymType.ERROR);
            }
            //it's for sure currency name
            return new Symbol(KeyWords.SymType.CURRENCY_TYPE, stringBuilder.toString());
        }
        else if(isSmallLetter(nextChar))
        {
            //clear string builder
            stringBuilder.setLength(0);
            do{
                stringBuilder.append((char)nextChar);
                nextChar = reader.nextChar();
            } while(isLetter(nextChar));
            //check if the char is space enter or tab
            if(!isWhiteSign(nextChar))
            {
                writer.error("//[S] Error in line: " + cp.line + " at char: " + cp.sign + ". Variable and function name should contains only letters.");
                return new Symbol(KeyWords.SymType.ERROR);
            }
            //check if it's not longer than 20 letters
            if(stringBuilder.toString().length() > keyWords.MAX_IDENT_LENGTH)
            {
                writer.error("//[S] Error in line: " + cp.line + " at char: " + cp.sign + ". Variable and function name's length should be <=20 signs.");
                return new Symbol(KeyWords.SymType.ERROR);
            }
            //it's correct so check if it's any key word or return IDENT, all that stuff makes simple method from Compilator.KeyWords class
            return new Symbol(keyWords.getByWord(stringBuilder.toString()), stringBuilder.toString());
        }
        else if(nextChar=='-')
        {
            nextChar = reader.nextChar();
            if(isWhiteSign(nextChar))
                return new Symbol(KeyWords.SymType.ADD_OP, "-");

            if(!isDigit(nextChar))
            {
                writer.error("//[S] Error in line: " + cp.line + " at char: " + cp.sign + ". After - should be digit or white sign.");
                return new Symbol(KeyWords.SymType.ERROR);
            }
            //It's number for sure
            stringBuilder.setLength(0);
            stringBuilder.append('-');
            return floatValue();
        }
        else if(isDigit(nextChar))
        {
            stringBuilder.setLength(0);
            return floatValue();
        }
        Symbol result = new Symbol(KeyWords.SymType.UNKNOWN);
        switch (nextChar) {
        //one char operators
            case '+':   result = new Symbol(KeyWords.SymType.ADD_OP,"+");
                        break;

            case '-':   result = new Symbol(KeyWords.SymType.ADD_OP,"-");
                        break;

            case '*':   result = new Symbol(KeyWords.SymType.MULT_OP,"*");
                        break;

            case '(':   result = new Symbol(KeyWords.SymType.L_ROUND,"(");
                        break;

            case ')':   result = new Symbol(KeyWords.SymType.R_ROUND,")");
                        break;

            case '{':   result = new Symbol(KeyWords.SymType.BEGIN,"{");
                        break;

            case '}':   result = new Symbol(KeyWords.SymType.END,"}");
                        break;

            case ',':   result = new Symbol(KeyWords.SymType.COMMA,",");
                        break;

            case '!':   result = new Symbol(KeyWords.SymType.NOT_OP,"!");
                        break;

            case ':':   result = new Symbol(KeyWords.SymType.COLON,":");
                        break;

            case ';':   result = new Symbol(KeyWords.SymType.SEMICOLON,";");
                        break;

            //operators that can have more than one operator
            case '|':   nextChar = reader.nextChar();
                        if(nextChar=='|')
                            result = new Symbol(KeyWords.SymType.OR_OP,"||");
                        break;

            case '&':   nextChar = reader.nextChar();
                        if(nextChar=='&')
                            result = new Symbol(KeyWords.SymType.AND_OP,"&&");
                            break;

            case '=':   nextChar = reader.nextChar();
                        if(nextChar=='=')
                            result = new Symbol(KeyWords.SymType.REL_OP, "==");
                        else
                            result = new Symbol(KeyWords.SymType.ASSIGNMENT,"=");
                        break;

            case '>':   nextChar = reader.nextChar();
                        if(nextChar=='=')
                            result = new Symbol(KeyWords.SymType.REL_OP, ">=");
                        result = new Symbol(KeyWords.SymType.REL_OP, ">");
                        break;

            case '<':   nextChar = reader.nextChar();
                        if(nextChar=='=')
                            result = new Symbol(KeyWords.SymType.REL_OP,"<=");
                        else if(nextChar=='>')
                            result = new Symbol(KeyWords.SymType.REL_OP,"!=");
                        else result = new Symbol(KeyWords.SymType.REL_OP,"<");
                        break;

            //now string const
            case '"':   stringBuilder.setLength(0);
                        nextChar = reader.nextChar();
                        while (nextChar!='"')
                        {
                            if(stringBuilder.toString().length() >= keyWords.MAX_STRING_LENGTH)
                            {
                                writer.error("//[S] Error in line: " + cp.line + " at char: " + cp.sign + ". Char const can have max " + keyWords.MAX_STRING_LENGTH + " signs.");
                                return new Symbol(KeyWords.SymType.ERROR);
                            }
                            stringBuilder.append((char)nextChar);
                            nextChar = reader.nextChar();
                        }
                        nextChar = reader.nextChar();
                        return new Symbol(KeyWords.SymType.CHAR_CONST, stringBuilder.toString());

        }
        if(result.getType() == KeyWords.SymType.UNKNOWN)
            writer.error("//[S] Error in line: " + cp.line + " at char: " + cp.sign + ". Unknown symbol.");
        else
            nextChar = reader.nextChar();
        return result;
    }

    private boolean isGreatLetter(int sign)
    {
        return (sign <= 90 && sign >= 65);
    }

    private boolean isSmallLetter(int sign)
    {
        return (sign <= 122 && sign >= 97);
    }

    private boolean isDigit(int sign)
    {
        return (sign <= 57 && sign >= 48);
    }

    private boolean isLetter(int sign) {return (isGreatLetter(sign) || isSmallLetter(sign));}

    private boolean isAlphaNumeric(int sign)
    {
        return (isLetter(sign) || isDigit(sign));
    }

    private boolean isWhiteSign(int sign)
    {
        return (sign==' ' || sign=='\t' || sign=='\n' || sign == -1);
    }

    public CursorPosition getPosition()
    {
        return reader.getPosition();
    }

    //it checks if it's float or int const value and detects errors
    private Symbol floatValue()
    {
        CursorPosition cp = reader.getPosition();
        Symbol tmpRes = intValue();
        if(isWhiteSign(nextChar))
            return tmpRes;

        if(nextChar!='.')
        {
            writer.error("//[S] Error in line: " + cp.line + " at char: " + cp.sign + ". After int value should be white sign (tab, space or enter).");
            return new Symbol(KeyWords.SymType.ERROR);
        }

        stringBuilder.append('.');
        nextChar = reader.nextChar();

        if(!isDigit(nextChar))
        {
            writer.error("//[S] Error in line: " + cp.line + " at char: " + cp.sign + ". After dot there should be a digit.");
            return new Symbol(KeyWords.SymType.ERROR);
        }

        intValue();

        if(!isWhiteSign(nextChar))
        {
            writer.error("//[S] Error in line: " + cp.line + " at char: " + cp.sign + ". Number can contain only digits. To end a number add space.");
            return new Symbol(KeyWords.SymType.ERROR);
        }
        return new Symbol(KeyWords.SymType.FLOAT_CONST, stringBuilder.toString());
    }

    //reads all digits in a row
    private Symbol intValue()
    {
        CursorPosition cp = reader.getPosition();
        long tmp = 0;
        do{
            tmp = tmp*10 + (nextChar - '0');
            if(tmp > keyWords.MAX_INT)
            {
                writer.error("//[S] Error in line: " + cp.line + " at char: " + cp.sign + ". Int should be in range <-2147483647, 2147483647>.");
                return new Symbol(KeyWords.SymType.ERROR);
            }
            nextChar = reader.nextChar();
        } while (isDigit(nextChar));

        stringBuilder.append(tmp);
        return new Symbol(KeyWords.SymType.INT_CONST, stringBuilder.toString());

    }
}
