package Compilator;

public class Compiler {
    String sourcePath;
    String destinationPath;
    MyScanner myScanner;

    private void preparePaths(String sourcePath)
    {
        this.sourcePath = sourcePath;
        //prepare destination filename with localisation
        destinationPath = sourcePath.substring(0, sourcePath.lastIndexOf("."));
        StringBuilder stringBuilder = new StringBuilder(destinationPath);
        stringBuilder.append(".cpp");
        destinationPath = stringBuilder.toString();
    }

    public void compile(String path) throws Exception {
        //Prepare paths and add them to singleton object
        preparePaths(path);
        Writer.createWriter(destinationPath);
        myScanner = new MyScanner(sourcePath);
        Parser parser = new Parser(myScanner);
        Generator generator = new Generator(Writer.getInstance());
        //here the generators start the parser and it's own work, parser is starting lexer
        generator.generate(parser.program());
        Writer.getInstance().close();
    }
}