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

    public void compile(String path) {
        //Prepare paths and add them to singleton object
        preparePaths(path);
        Writer.createWriter(destinationPath);
        myScanner = new MyScanner(sourcePath);
        KeyWords.SymType tmp;
        do {
            tmp = myScanner.nextSymbol();
            System.out.print( tmp + " ");
        } while (tmp!=KeyWords.SymType.ERROR && tmp!=KeyWords.SymType.EOF && tmp!=KeyWords.SymType.UNKNOWN);

        Writer.getInstance().close();
    }


}