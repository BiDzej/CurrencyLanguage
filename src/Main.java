import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        //Prepare a path of the source file
        String path;
        if(args.length == 0)
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please write path to the source file: ");
            path = scanner.nextLine();
        }
        else path = args[0];

        //we can run compilation with correct path
        Compiler compiler = new Compiler();
        compiler.compile(preparePath(path));
    }

    public static String preparePath(String path)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i <path.length(); ++i)
        {
            char tmp = path.charAt(i);
            if(tmp=='\\')
                stringBuilder.append('\\');
            stringBuilder.append(tmp);
        }

        return stringBuilder.toString();
    }



}
