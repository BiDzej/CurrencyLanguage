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
        parser.program();
        /*Symbol tmp;
        do {
            tmp = myScanner.nextSymbol();
            System.out.print( tmp.getType() + "(" + tmp.getText() + ")  ");
        } while (tmp.getType()!=KeyWords.SymType.ERROR && tmp.getType()!=KeyWords.SymType.EOF && tmp.getType()!=KeyWords.SymType.UNKNOWN); */

        Writer.getInstance().close();
    }


}