import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    //file reader to read from file
    BufferedReader bufferedReader;
    //current line buffor
    String line;
    //path to source file
    String sourceFile;
    //Cursor position while reading from file
    CursorPosition cp;

    public Reader(String sourceFile)
    {
        this.sourceFile = sourceFile;
        try {
            bufferedReader = new BufferedReader( new FileReader(sourceFile));
            cp = new CursorPosition(0, 0);
            nextLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //read next line from the file and skip spaces nad tabs
    private boolean nextLine()
    {
        try {
            //if there are no more lines in the source file
            if((line = bufferedReader.readLine()) == null)
                return false;
            //else
            line = prepareLine(line);
            cp.line++;
            cp.sign = 0;
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int nextChar()
    {
        boolean isNextLine = true;
        if(cp.sign == line.length())
            isNextLine = nextLine();
        if(isNextLine)
        {
            //Line for debugging
            //System.out.print("[" + line.charAt(cp.sign) + "]");
            return line.charAt(cp.sign++);
        }
        else return -1;
    }

    private String prepareLine(String tmp)
    {
        StringBuilder stringBuilder = new StringBuilder(tmp);
        stringBuilder.append('\n');
        return stringBuilder.toString();
    }

    public CursorPosition getPosition()
    {
        return cp;
    }
}
