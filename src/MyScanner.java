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

    public String getLastWord()
    {
        return stringBuilder.toString();
    }

    public KeyWords.SymType nextSymbol()
    {
        do
        {
            //skip all spaces and tabs
            while(isWhiteSign(nextChar))
                nextChar = reader.nextChar();
            //if it's file end, return file end
            if(nextChar==-1)
                return KeyWords.SymType.EOF;

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
                else return KeyWords.SymType.DIVIDE;
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
                    writer.write("//Error in line: " + cp.line + " at char: " + cp.sign + ". Variable should start with a small letter.");
                    return KeyWords.SymType.ERROR;
                }
                stringBuilder.append((char)nextChar);
            }
            //get next sign after currency type
            nextChar = reader.nextChar();
            //if there are more signs after that 3 letters error
            if(!isWhiteSign(nextChar))
            {
                writer.write("//Error in line: " + cp.line + " at char: " + (cp.sign) + ". Currency type should have only 3 great letters.");
                return KeyWords.SymType.ERROR;
            }
            //it's for sure currency name
            return KeyWords.SymType.CURRENCY_TYPE;
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
                writer.write("//Error in line: " + cp.line + " at char: " + cp.sign + ". Variable and function name should contains only letters.");
                return KeyWords.SymType.ERROR;
            }
            //check if it's not longer than 20 letters
            if(stringBuilder.toString().length() > keyWords.MAX_IDENT_LENGTH)
            {
                writer.write("//Error in line: " + cp.line + " at char: " + cp.sign + ". Variable and function name's length should be <=20 signs.");
                return KeyWords.SymType.ERROR;
            }
            //it's correct so check if it's any key word or return IDENT, all that stuff makes simple method from KeyWords class
            return keyWords.getByWord(stringBuilder.toString());
        }
        else if(isDigit(nextChar))
        {
            long tmp = 0;
            do{
                tmp = tmp*10 + (nextChar - '0');
                if(tmp > keyWords.MAX_INT)
                {
                    writer.write("//Error in line: " + cp.line + " at char: " + cp.sign + ". Int should be in range <-2147483647, 2147483647>.");
                    return KeyWords.SymType.ERROR;
                }
                nextChar = reader.nextChar();
            } while (isDigit(nextChar));
            if(nextChar==' ' || nextChar=='\t' || nextChar=='\n')
            {
                stringBuilder.setLength(0);
                stringBuilder.append(tmp);
                return KeyWords.SymType.INT_CONST;
            }
            //it can be float
            if(nextChar=='.')
            {
                nextChar = reader.nextChar();
                if(!isDigit(nextChar))
                {
                    writer.write("//Error in line: " + cp.line + " at char: " + cp.sign + ". After dot there should be another digit.");
                    return KeyWords.SymType.ERROR;
                }
                stringBuilder.append(".");
                tmp = 0;
                do{
                    tmp = tmp*10 + (nextChar - '0');
                    if(tmp > keyWords.MAX_INT)
                    {
                        writer.write("//Error in line: " + cp.line + " at char: " + cp.sign + ". Int should be in range <-2147483647, 2147483647>.");
                        return KeyWords.SymType.ERROR;
                    }
                    nextChar = reader.nextChar();
                } while (isDigit(nextChar));
                if(nextChar==' ' || nextChar=='\t' || nextChar=='\n')
                {
                    stringBuilder.append(tmp);
                    return KeyWords.SymType.FLOAT_CONST;
                }
            }
            writer.write("//Error in line: " + cp.line + " at char: " + cp.sign + ". Number can contain only digits. To end a number add space.");
            return KeyWords.SymType.ERROR;
        }
        KeyWords.SymType result = KeyWords.SymType.UNKNOWN;
        switch (nextChar) {
        //one char operators
            case '+':   result = KeyWords.SymType.PLUS; break;

            case '-':   result = KeyWords.SymType.MINUS; break;

            case '*':   result = KeyWords.SymType.MULTIPLY; break;

            case '(':   result = KeyWords.SymType.L_ROUND; break;

            case ')':   result = KeyWords.SymType.R_ROUND; break;

            case '{':   result = KeyWords.SymType.BEGIN; break;

            case '}':   result = KeyWords.SymType.END; break;

            case ',':   result = KeyWords.SymType.COMMA; break;

            case '!':   result = KeyWords.SymType.NOT_OP; break;

            case ':':   result = KeyWords.SymType.COLON; break;

            case ';':  result = KeyWords.SymType.SEMICOLON; break;

            //operators that can have more than one operator
            case '|':   nextChar = reader.nextChar();
                        if(nextChar=='|')
                            result = KeyWords.SymType.OR_OP;
                        break;

            case '&':   nextChar = reader.nextChar();
                        if(nextChar=='&')
                            result = KeyWords.SymType.AND_OP;
                        break;

            case '=':   nextChar = reader.nextChar();
                        if(nextChar=='=')
                            result = KeyWords.SymType.EQUAL;
                        else
                            result = KeyWords.SymType.ASSIGNMENT;
                        break;

            case '>':   nextChar = reader.nextChar();
                        if(nextChar=='=')
                            result = KeyWords.SymType.GREATER_EQUAL;
                        else
                            result = KeyWords.SymType.GREATER;
                        break;

            case '<':   nextChar = reader.nextChar();
                        if(nextChar=='=')
                            result = KeyWords.SymType.LOWER_EQUAL;
                        else if(nextChar=='>')
                            result = KeyWords.SymType.DIFFERENT;
                        else
                            result = KeyWords.SymType.LOWER;
                        break;

            //now string const
            case '"':   stringBuilder.setLength(0);
                        nextChar = reader.nextChar();
                        while (nextChar!='"')
                        {
                            if(stringBuilder.toString().length() >= keyWords.MAX_STRING_LENGTH)
                            {
                                writer.write("Error in line: " + cp.line + " at char: " + cp.sign + ". Char const can have max " + keyWords.MAX_STRING_LENGTH + " signs.");
                                return KeyWords.SymType.ERROR;
                            }
                            stringBuilder.append((char)nextChar);
                            nextChar = reader.nextChar();
                        }
                        result = KeyWords.SymType.CHAR_CONST;
                        break;

        }
        if(result == KeyWords.SymType.UNKNOWN)
            writer.write("Error in line: " + cp.line + " at char: " + cp.sign + ". Unknown symbol.");
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
        return (sign==' ' || sign=='\t' || sign=='\n');
    }
}
